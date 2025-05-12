package be.helha.labos.collection;

import be.helha.labos.DBNosql.Connexion_DB_Nosql;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
/**
 * Classe représentant un coffre dans le jeu.
 * Elle contient des informations sur le coffre, telles que son ID et les slots qu'il contient.
 */
public class chests {

    /**
     * Attributs de la classe chests
     * @JsonProperty est utilisé pour indiquer que cet attribut doit être sérialisé/désérialisé avec ce nom
     * @ObjectId est un identifiant unique généré par MongoDB
     * @param id l'identifiant du coffre
     */
    @JsonProperty("_id")
    protected ObjectId id;

    private static Connexion_DB_Nosql connexionDbNosql = new Connexion_DB_Nosql("nosqlTest");
    private static MongoDatabase mongoDatabase = connexionDbNosql.createDatabase();
    private static MongoCollection<Document> collection = mongoDatabase.getCollection("chests");
    private List<Document> chestsSlots;

    /**
     * Constructeur vide
     */
    public chests( ObjectId inventoryId) {

        /**
         * Création de la liste des slots du coffre
         * Chaque slot est un document contenant le numéro de slot et l'item associé (null au départ)
         */
        chestsSlots = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Document slot = new Document("slot_number", i + 1).append("item", null);
            chestsSlots.add(slot);
        }

        // Création du document Inventaire
        Document chests = new Document("maxSize", 10)
                .append("slots", chestsSlots);

        // Vérification de l'existence de l'inventaire avant d'ajouter le coffre
        MongoCollection<Document> inventoryCollection = mongoDatabase.getCollection("inventory");
        Document inventory = inventoryCollection.find(new Document("_id", inventoryId)).first();

        if (inventory == null) {
            System.out.println("⚠️ Erreur : L'inventaire spécifié n'existe pas !");
            return;
        }

        // Insertion de l'inventaire dans la collection 'inventory'
        InsertOneResult chestsResult = collection.insertOne(chests);
        id = (ObjectId) chests.get("_id"); // Stocke l'ID dans l'objet chests

        System.out.println("coffre créé avec ID : " + id);

        List<Document> slots = (List<Document>) inventory.get("slots");

        // Trouver un slot vide
        boolean itemPlaced = false;
        for (Document slot : slots) {
            if (slot.get("item") == null) { // Slot vide trouvé
                slot.put("item", id);
                itemPlaced = true;
                break;
            }
        }

        if (itemPlaced) {
            // Mettre à jour du coffre dans la base de données
            Document updateQuery = new Document("$set", new Document("slots", slots));
            inventoryCollection.updateOne(new Document("_id", inventoryId), updateQuery);
            System.out.println("Coffre ajouté a l'inventaire avec succès.");
        } else {
            System.out.println("Aucun slot disponible dans l'inventaire.");
        }
    }

    /**
     * Constructeur de la classe chests.
     *
     */
    public ObjectId getId() {
        return id;
    }

    /**
     * Méthode pour obtenir l'ID du coffre.
     * Cette méthode permet de rajouter un item dans un coffre d'un user
     * @return ID du coffre.
     */
    public static void putItemsInChest(ObjectId chestId, ObjectId itemsId, boolean remove) {
        try {

            // Récupérer le coffre par son ID
            Document chest = collection.find(new Document("_id", chestId)).first();

            if (chest != null) {
                // Récupérer les slots du coffre
                List<Document> slots = (List<Document>) chest.get("slots");

                // Trouver un slot vide
                if (!remove) {
                    boolean itemPlaced = false;
                    for (Document slot : slots) {
                        if (itemsId.equals(slot.get("item"))) {
                            itemPlaced = true;
                            System.out.println("L'objet à placer est déjà présent.");
                            break;
                        }
                        if (slot.get("item") == null) { // Slot vide trouvé
                            slot.put("item", itemsId);
                            itemPlaced = true;
                            break;
                        }
                    }
                    if (itemPlaced) {
                        // Mettre à jour du coffre dans la base de données
                        System.out.println("Item ajouté au coffre avec succès.");
                    } else {
                        System.out.println("Aucun slot disponible dans le coffre.");
                    }
                } else if (remove) {
                    for (Document slot : slots) {
                        if ( itemsId.equals(slot.get("item"))) {
                            slot.put("item",null);
                            System.out.println("Item retiré du coffre avec succès.");
                            break;
                        }
                        else {
                            System.out.println("L'item n'est pas présent dans le coffre.");
                        }
                    }
                }
                Document updateQuery = new Document("$set", new Document("slots", slots));
                collection.updateOne(new Document("_id", chestId), updateQuery);
            }else {
                System.out.println("Coffre introuvable.");
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de l'ajout de l'item à l'inventaire.");
            e.printStackTrace();
        }
    }

}