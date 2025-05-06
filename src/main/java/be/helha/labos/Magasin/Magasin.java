package be.helha.labos.Magasin;

import be.helha.labos.collection.Inventaire;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Magasin {

    private MongoCollection<Document> itemsCollection;
    private static final String[] NOMS_OBJETS = {"Épée", "Potion", "Arc", "Masse", "Armure"};
    private static final double PRIX_MIN = 10.0;
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



    public static void genererObjets(MongoCollection<Document> itemsCollection) {
        Random random = new Random();
            for (int i = 0; i < 10; i++) {
                String nom = NOMS_OBJETS[random.nextInt(NOMS_OBJETS.length)];
                double prix = PRIX_MIN + (PRIX_MAX - PRIX_MIN) * random.nextDouble();

                Document objet = new Document("nom", nom)
                        .append("prix", prix);

                itemsCollection.insertOne(objet);
                System.out.println("Objet généré : " + nom + " (Prix : " + prix + ")");
            }
    }
}

