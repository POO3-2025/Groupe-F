/**
 * La classe {@code Magasin} représente un magasin permettant de gérer l'achat,
 * la vente et la génération d'objets pour les personnages dans une base de données MongoDB.
 *
 * Elle interagit avec une collection MongoDB pour afficher les objets disponibles,
 * effectuer des achats et des ventes, et générer aléatoirement des objets.
 *
 * @author
 * @version 1.0
 * @since 2025-05-12
 */
package be.helha.labos.Magasin;

import be.helha.labos.collection.Character.CharacterType;
import be.helha.labos.collection.Inventaire;
import be.helha.labos.collection.Item.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Magasin {

    private MongoCollection<Document> itemsCollection;
    private final MongoCollection<Document> charactersCollection;
    private static final double PRIX_MIN = 10.0;
    private static final double PRIX_MAX = 500.0;
    private MongoDatabase mongoDatabase;

    /**
     * Construit un nouveau magasin avec une base de données MongoDB donnée.
     *
     * @param database La base de données MongoDB à utiliser.
     */
    public Magasin(MongoDatabase database) {
        this.mongoDatabase = database;
        this.itemsCollection = database.getCollection("Magasin");
        this.charactersCollection = database.getCollection("characters");
    }

    /**
     * Récupère et affiche les objets actuellement disponibles dans le magasin.
     *
     * @return Une liste de documents représentant les objets en stock.
     */
    public List<Document> afficherObjetsDisponibles() {
        List<Document> objets = itemsCollection.find().into(new ArrayList<>());
        System.out.println("Objets disponibles dans le magasin :");
        for (Document objet : objets) {
            System.out.println("- " + objet.getString("nom") + " (Prix : " + objet.getDouble("prix") + ")");
        }
        return objets;
    }

    /**
     * Tente de faire acheter un objet par un personnage.
     *
     * @param item        Le document représentant l'objet à acheter.
     * @param personnage  Le personnage effectuant l'achat.
     * @return true si l'achat est réussi, false sinon.
     */
    public boolean acheterObjet(Document item, CharacterType personnage) {
        try {
            if (item == null || personnage == null) {
                System.out.println("Item ou personnage invalide");
                return false;
            }

            //verifie si le personnage peut acheter l'objet
            String typeArme = item.getString("type");
            WeaponType weaponType = WeaponType.valueOf(typeArme.toUpperCase());
            if (!personnage.canEquipWeapon(weaponType)) {
                System.out.println("Cette arme n'est pas compatible avec la classe du personnage !");
                return false;
            }

            // Vérifie le niveau requis
            int requiredLevel = item.getInteger("requiredLevel", 0);
            if (personnage.getLevel() < requiredLevel) {
                System.out.println("Niveau insuffisant ! Niveau requis : " + requiredLevel + ", votre niveau : " + personnage.getLevel());
                return false;
            }

            double prix = item.getDouble("prix");
            double argentActuel = personnage.getMoney();

            if (argentActuel < prix) {
                System.out.println("Pas assez d'argent ! Vous avez " + argentActuel + " pièces et l'objet coûte " + prix);
                return false;
            }

            // Vérifier si l'inventaire existe, sinon le créer avec 5 slots vides
            MongoCollection<Document> inventoryCollection = mongoDatabase.getCollection("inventory");
            Document inventoryDoc = inventoryCollection.find(
                    new Document("characterId", personnage.getId())
            ).first();

            if (inventoryDoc == null) {
                // Créer un nouvel inventaire avec 5 slots vides
                List<Document> emptySlots = new ArrayList<>();
                for (int i = 0; i < 5; i++) {
                    emptySlots.add(new Document("item", null));
                }

                inventoryDoc = new Document()
                        .append("characterId", personnage.getId())
                        .append("slots", emptySlots);

                inventoryCollection.insertOne(inventoryDoc);
            }

            // Chercher un slot vide
            List<Document> slots = inventoryDoc.getList("slots", Document.class);
            boolean slotVideTrouve = false;
            int slotIndex = 0;

            for (int i = 0; i < slots.size(); i++) {
                Document slot = slots.get(i);
                if (slot.get("item") == null) {
                    slotVideTrouve = true;
                    slotIndex = i;
                    break;
                }
            }

            if (!slotVideTrouve) {
                System.out.println("Inventaire plein !");
                return false;
            }

            // Mettre à jour le slot vide avec le nouvel item
            inventoryCollection.updateOne(
                    new Document("characterId", personnage.getId()),
                    new Document("$set", new Document("slots." + slotIndex + ".item", item))
            );

            // Mettre à jour l'argent du personnage
            charactersCollection.updateOne(
                    new Document("_id", personnage.getId()),
                    new Document("$set", new Document("money", argentActuel - prix))
            );

            // Mise à jour locale
            personnage.setMoney(argentActuel - prix);
            System.out.println("Achat réussi ! Objet ajouté à l'inventaire. Argent restant : " + personnage.getMoney());
            return true;

        } catch (Exception e) {
            System.out.println("Erreur lors de l'achat : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Permet à un personnage de vendre un objet et de le replacer dans le magasin.
     *
     * @param item           L'objet à vendre.
     * @param personnage     Le personnage qui vend l'objet.
     * @return true si la vente est réussie, false sinon.
     */
    public boolean vendreObjet(Document item, CharacterType personnage) {
        try {
            if (item == null || personnage == null) {
                System.out.println("Item ou personnage invalide");
                return false;
            }

            // Calculer le prix de vente (80% du prix d'achat)
            double prixVente = item.getDouble("prix") * 0.8;
            double argentActuel = personnage.getMoney();

            // Trouver et retirer l'objet de l'inventaire
            MongoCollection<Document> inventoryCollection = mongoDatabase.getCollection("inventory");
            Document inventoryDoc = inventoryCollection.find(
                    new Document("characterId", personnage.getId())
            ).first();

            if (inventoryDoc == null) {
                System.out.println("Erreur : Inventaire non trouvé");
                return false;
            }

            List<Document> slots = inventoryDoc.getList("slots", Document.class);
            int slotIndex = -1;

            // Trouver le slot contenant l'item
            for (int i = 0; i < slots.size(); i++) {
                Document slot = slots.get(i);
                Document slotItem = slot.get("item", Document.class);
                if (slotItem != null && slotItem.get("_id").equals(item.get("_id"))) {
                    slotIndex = i;
                    break;
                }
            }

            if (slotIndex == -1) {
                System.out.println("Erreur : Item non trouvé dans l'inventaire");
                return false;
            }

            // Vider le slot en mettant l'item à null
            inventoryCollection.updateOne(
                    new Document("characterId", personnage.getId()),
                    new Document("$set", new Document("slots." + slotIndex + ".item", null))
            );

            // Mettre à jour l'argent du personnage
            charactersCollection.updateOne(
                    new Document("_id", personnage.getId()),
                    new Document("$set", new Document("money", argentActuel + prixVente))
            );

            // Mise à jour locale de l'argent
            personnage.setMoney(argentActuel + prixVente);

            System.out.println("Vente réussie ! Vous avez gagné " + prixVente + " pièces");
            return true;

        } catch (Exception e) {
            System.out.println("Erreur lors de la vente : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Calcule le prix d'une arme selon son type et son matériau.
     *
     * @param type      Le type d'arme (ex. : Sword, Bow, etc.).
     * @param materiau  Le matériau de l'arme.
     * @return Le prix calculé.
     */
    private static double calculerPrixArme(String type, String materiau) {
        double base = switch (type) {
            case "Sword" -> 50.0;
            case "Bow"   -> 45.0;
            case "Mace"  -> 55.0;
            default      -> 40.0;
        };

        double multiplicateur = switch (materiau.toLowerCase()) {
            case "bois"     -> 1.0;
            case "pierre"   -> 1.2;
            case "fer"      -> 1.5;
            case "argent"   -> 1.7;
            case "or"       -> 1.8;
            case "acier"    -> 2.0;
            case "feu"      -> 2.2;
            case "glace"    -> 2.3;
            case "diamant"  -> 2.5;
            default         -> 1.0;
        };

        return base * multiplicateur;
    }

    /**
     * Génère aléatoirement une série d'objets (épées, potions, arcs, masses)
     * et les insère dans la collection MongoDB fournie.
     *
     * @param itemsCollection La collection MongoDB où insérer les objets.
     */
    public static void genererObjets(MongoCollection<Document> itemsCollection) {
        Random random = new Random();

        for (int i = 0; i < 10; i++) {
            Item objet;
            String nom = "";
            Document document = new Document();
            double prix = 0;

            int typeAleatoire = random.nextInt(4);

            int requiredLevel = 0;
            switch (typeAleatoire) {
                case 0 -> {
                    Sword.SwordMaterial[] materials = Sword.SwordMaterial.values();
                    Sword.SwordMaterial material = materials[random.nextInt(materials.length)];
                    Sword sword = new Sword(material);
                    objet = sword;
                    requiredLevel = material.getRequiredLevel();
                    nom = "Épée en " + material.getMaterial();
                    prix = calculerPrixArme("Sword", material.getMaterial());

                    document.append("type", "Sword")
                            .append("materiau", material.getMaterial())
                            .append("degats", sword.getDamage());
                }
                case 1 -> {
                    int max = 50 + random.nextInt(51);
                    int contenu = random.nextInt(max + 1);
                    Potion potion = new Potion(max, contenu);
                    objet = potion;
                    nom = "Potion (" + contenu + "/" + max + ")";
                    prix = 10 + ((double) contenu / max) * 40;
                    prix = calculerPrixArme("Potion", "none");

                    document.append("type", "Potion")
                            .append("maxContent", max)
                            .append("actualContent", contenu);
                }
                case 2 -> {
                    Bow.BowMaterial[] materials = Bow.BowMaterial.values();
                    Bow.BowMaterial material = materials[random.nextInt(materials.length)];
                    Bow bow = new Bow(material);
                    objet = bow;
                    nom = "Arc en " + material.getMaterial();
                    prix = calculerPrixArme("Bow", material.getMaterial());

                    document.append("type", "Bow")
                            .append("materiau", material.getMaterial())
                            .append("degats", bow.getDamage());
                }
                case 3 -> {
                    Mace.MaceMaterial[] materials = Mace.MaceMaterial.values();
                    Mace.MaceMaterial material = materials[random.nextInt(materials.length)];
                    Mace mace = new Mace(material);
                    objet = mace;
                    nom = "Masse en " + material.getMaterial();
                    prix = calculerPrixArme("Mace", material.getMaterial());

                    document.append("type", "Mace")
                            .append("materiau", material.getMaterial())
                            .append("degats", mace.getDamage());
                }
            }

            document.append("nom", nom)
                    .append("prix", prix)
                    .append("requiredLevel", requiredLevel)
                    .append("_id", new ObjectId());

            itemsCollection.insertOne(document);
            System.out.println("Objet généré : " + nom + " (Prix : " + prix + ")");
        }
    }
}