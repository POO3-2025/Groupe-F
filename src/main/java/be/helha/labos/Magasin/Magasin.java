package be.helha.labos.Magasin;

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

    private MongoCollection<Document> itemsCollection;    private static final double PRIX_MIN = 10.0;
    private static final double PRIX_MAX = 500.0;

    public Magasin(MongoDatabase database) {
        // Initialisation de la collection des objets
        this.itemsCollection = database.getCollection("Magasin");
    }

    // Méthode pour afficher les objets disponibles
    public List<Document> afficherObjetsDisponibles() {
        List<Document> objets = itemsCollection.find().into(new ArrayList<>());
        System.out.println("Objets disponibles dans le magasin :");
        for (Document objet : objets) {
            System.out.println("- " + objet.getString("nom") + " (Prix : " + objet.getDouble("prix") + ")");
        }
        return objets;
    }

    // Méthode pour acheter un objet
    public void acheterObjet(ObjectId itemId, Inventaire inventaire) {
        Document item = itemsCollection.find(new Document("_id", itemId)).first();
        if (item != null) {
            // Ajouter l'objet à l'inventaire de l'utilisateur
            inventaire.putItemsInInventory(inventaire.getId(), itemId, false);

            // Supprimer l'objet du magasin
            itemsCollection.deleteOne(new Document("_id", itemId));
            System.out.println("Objet acheté avec succès : " + item.getString("nom"));
        } else {
            System.out.println("Objet introuvable dans le magasin.");
        }
    }

    // Méthode pour vendre un objet
    public void vendreObjet(ObjectId itemId, Inventaire inventaire) {
        // Vérifier si l'objet est présent dans l'inventaire
        Document item = itemsCollection.find(new Document("_id", itemId)).first();
        if (item == null) {
            System.out.println("L'objet à vendre n'existe pas dans le magasin.");
            return;
        }
        // Retirer l'objet de l'inventaire
        inventaire.putItemsInInventory(inventaire.getId(), itemId, true);

        // Ajouter l'objet au magasin
        itemsCollection.insertOne(item);
        System.out.println("Objet vendu avec succès : " + item.getString("nom"));
    }

    // Méthode pour calculer le prix selon le matériau
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
                    prix = 10 + ((double) contenu / max) * 40; // Prix selon remplissage
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