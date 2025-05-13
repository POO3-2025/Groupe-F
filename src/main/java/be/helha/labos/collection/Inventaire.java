package be.helha.labos.collection;

import be.helha.labos.DBNosql.Connexion_DB_Nosql;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class Inventaire {
    @JsonProperty("_id")
    protected ObjectId id;

    private MongoDatabase mongoDatabase;
    private static MongoCollection<Document> collection;
    private List<Document> inventorySlots;

    public Inventaire() {
        this.id = new ObjectId();
        this.inventorySlots = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Document slot = new Document("slot_number", i + 1).append("item", null);
            inventorySlots.add(slot);
        }
    }

    public Inventaire(MongoDatabase mongoDatabase) {
        this.id = new ObjectId();
        this.mongoDatabase = mongoDatabase;
        this.inventorySlots = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Document slot = new Document("slot_number", i + 1).append("item", null);
            inventorySlots.add(slot);
        }
    }

    public void insererDansLaBase() {
        MongoCollection<Document> collection = mongoDatabase.getCollection("inventory");
        Document inventory = new Document("_id", this.id)
                .append("maxSize", 10)
                .append("slots", inventorySlots);
        collection.insertOne(inventory);
        System.out.println("Inventaire inséré avec ID : " + id);
    }

    public ObjectId getId() {
        return id;
    }

    public boolean retirerObjetDeInventaire(MongoDatabase mongoDatabase,ObjectId inventaireId, ObjectId objetId) {
        MongoCollection<Document> inventaires = mongoDatabase.getCollection("inventory");

        Document update = new Document("$pull", new Document("objets", new Document("_id", objetId)));
        return inventaires.updateOne(new Document("_id", inventaireId), update).getModifiedCount() > 0;
    }

    public Document getInventaireFromMongoDB(MongoDatabase mongoDatabase,ObjectId inventaireId) {
        try {
            collection = mongoDatabase.getCollection("inventory");

            Document inventaire = collection.find(new Document("_id", inventaireId)).first();

            if (inventaire != null) {
                return inventaire;
            } else {
                System.out.println("Aucun inventaire trouvé avec l'ID : " + inventaireId);
                return null;
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la récupération de l'inventaire.");
            e.printStackTrace();
            return null;
        }
    }

    public static boolean ajouterObjetDansInventaire(MongoDatabase mongoDatabase, ObjectId inventaireId, ObjectId objetId) {
        try {
            MongoCollection<Document> collection = mongoDatabase.getCollection("inventory");
            MongoCollection<Document> magasinCollection = mongoDatabase.getCollection("Magasin");
            Document inventaire = collection.find(new Document("_id", inventaireId)).first();

            // Récupérer le document complet de l'objet à partir de son ID
            Document objet = magasinCollection.find(new Document("_id", objetId)).first();
            if (objet == null) {
                System.out.println("Objet non trouvé");
                return false;
            }

            if (inventaire != null) {
                List<Document> slots = (List<Document>) inventaire.get("slots");

                for (Document slot : slots) {
                    if (slot.get("item") instanceof Document) {
                        Document item = (Document) slot.get("item");
                        if (objetId.equals(item.getObjectId("_id"))) {
                            System.out.println("L'objet est déjà dans l'inventaire");
                            return false;
                        }
                    }
                }

                // Ajouter l'objet dans le premier slot vide
                for (Document slot : slots) {
                    if (!slot.containsKey("item") || slot.get("item") == null) {
                        slot.put("item", objet);
                        Document updateQuery = new Document("$set", new Document("slots", slots));
                        collection.updateOne(new Document("_id", inventaireId), updateQuery);
                        System.out.println("Objet ajouté avec succès");
                        return true;
                    }
                }

                System.out.println("Inventaire plein");
                return false;
            }

            System.out.println("Inventaire non trouvé");
            return false;

        } catch (Exception e) {
            System.out.println("Erreur lors de l'ajout de l'objet");
            e.printStackTrace();
            return false;
        }
    }

}