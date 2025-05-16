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
    public boolean acheterObjet(Document item, CharacterType character) {
        // Vérifier si le joueur a assez d'argent
        double prix = item.getDouble("prix");
        if (character.getMoney() < prix) {
            return false;
        }

        // Vérifier si l'objet est disponible
        if (!item.getBoolean("disponible", true)) {
            return false;
        }

        try {
            // Récupérer l'inventaire du personnage
            Document inventory = mongoDatabase.getCollection("inventory")
                    .find(new Document("_id", character.getInventoryId()))
                    .first();

            if (inventory == null) {
                return false;
            }

            // Trouver un emplacement vide
            List<Document> slots = inventory.getList("slots", Document.class);
            int emptySlotIndex = -1;
            for (int i = 0; i < slots.size(); i++) {
                if (slots.get(i).get("item") == null) {
                    emptySlotIndex = i;
                    break;
                }
            }

            if (emptySlotIndex == -1) {
                return false; // Inventaire plein
            }

            // Mettre à jour l'inventaire
            mongoDatabase.getCollection("inventory").updateOne(
                    new Document("_id", character.getInventoryId()),
                    new Document("$set",
                            new Document("slots." + emptySlotIndex + ".item", item)
                    )
            );

            // Mettre à jour l'argent du personnage
            character.setMoney(character.getMoney() - prix);
            mongoDatabase.getCollection("characters").updateOne(
                    new Document("_id", character.getId()),
                    new Document("$set", new Document("money", character.getMoney()))
            );

            // Marquer l'objet comme non disponible dans le magasin
            itemsCollection.updateOne(
                    new Document("_id", item.getObjectId("_id")),
                    new Document("$set", new Document("disponible", false))
            );

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Permet à un personnage de vendre un objet et de le replacer dans le magasin.
     *
     * @param item           L'objet à vendre.
     * @param personnage     Le personnage qui vend l'objet.
     * @param shopCollection La collection MongoDB du magasin où insérer l'objet.
     * @return true si la vente est réussie, false sinon.
     */
    public boolean vendreObjet(Document item, CharacterType personnage, MongoCollection<Document> shopCollection) {
        try {
            Document characterDoc = mongoDatabase.getCollection("characters")
                    .find(new Document("_id", personnage.getId()))
                    .first();

            if (characterDoc == null) return false;

            Document inventaireDoc = characterDoc.get("inventaire", Document.class);
            if (inventaireDoc == null) return false;

            double prixVente = item.getDouble("prix") * 0.8;

            Document inventoryDoc = mongoDatabase.getCollection("inventory")
                    .find(new Document("_id", inventaireDoc.getObjectId("_id")))
                    .first();

            if (inventoryDoc == null) return false;

            List<Document> slots = inventoryDoc.getList("slots", Document.class);
            int slotIndex = -1;
            for (int i = 0; i < slots.size(); i++) {
                Document slot = slots.get(i);
                Document slotItem = slot.get("item", Document.class);
                if (slotItem != null && slotItem.getObjectId("_id").equals(item.getObjectId("_id"))) {
                    slotIndex = i;
                    break;
                }
            }

            if (slotIndex == -1) return false;

            mongoDatabase.getCollection("inventory").updateOne(
                    new Document("_id", inventaireDoc.getObjectId("_id")),
                    new Document("$set", new Document("slots." + slotIndex + ".item", null))
            );

            Document nouvelObjet = new Document()
                    .append("nom", item.getString("nom"))
                    .append("prix", item.getDouble("prix"));

            shopCollection.insertOne(nouvelObjet);

            double nouvelArgent = personnage.getMoney() + prixVente;
            personnage.setMoney(nouvelArgent);
            personnage.updateMoneyInDB(mongoDatabase);

            return true;
        } catch (Exception e) {
            System.err.println("Erreur lors de la vente : " + e.getMessage());
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

            switch (typeAleatoire) {
                case 0 -> {
                    Sword.SwordMaterial[] materials = Sword.SwordMaterial.values();
                    Sword.SwordMaterial material = materials[random.nextInt(materials.length)];
                    Sword sword = new Sword(material);
                    objet = sword;
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
                    .append("_id", new ObjectId());

            itemsCollection.insertOne(document);
            System.out.println("Objet généré : " + nom + " (Prix : " + prix + ")");
        }
    }
}