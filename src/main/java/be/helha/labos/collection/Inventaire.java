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
 * Classe représentant un inventaire dans le jeu.
 * Elle contient des informations sur l'inventaire, telles que son ID et les slots qu'il contient.
 */
public class Inventaire {


    @JsonProperty("_id")
    protected ObjectId id;

    private static Connexion_DB_Nosql connexionDbNosql = new Connexion_DB_Nosql("nosqlTest");;
    private static MongoDatabase mongoDatabase = connexionDbNosql.createDatabase();
    private static MongoCollection<Document> collection;
    private List<Document> inventorySlots;

    /**
     * Constructeur vide
     */
    public Inventaire() {
        this.id = new ObjectId();
        this.inventorySlots = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Document slot = new Document("slot_number", i + 1).append("item", null);
            inventorySlots.add(slot);
        }
    }

    /**
     * méthode explicite pour insérer dans la DB
     * @throws Exception
     */
    public void insererDansLaBase() {

        collection = mongoDatabase.getCollection("inventory");

        Document inventory = new Document("_id", this.id)
                .append("maxSize", 10)
                .append("slots", inventorySlots);

        collection.insertOne(inventory);
        System.out.println("Inventaire inséré avec ID : " + id);
    }

    /**
     * méthode pour obtenir l'identifiant de l'inventaire
     */
    public ObjectId getId() {
        return id;
    }

    /**
     *Methodde rajouter un item dans l'inventaire d'un user
     */
    public static void putItemsInInventory(ObjectId inventoryId, ObjectId itemsId, boolean remove) {
        try {
                // Initialisation de la connexion
                connexionDbNosql = new Connexion_DB_Nosql("nosqlTest");
                mongoDatabase = connexionDbNosql.createDatabase();
                collection = mongoDatabase.getCollection("inventory");

                Document inventory = collection.find(new Document("_id", inventoryId)).first();

                if (inventory != null) {
                    // Récupérer les slots de l'inventaire
                    List<Document> slots = (List<Document>) inventory.get("slots");

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
                            // Mettre à jour l'inventaire dans la base de données
                            System.out.println("Item ajouté à l'inventaire avec succès.");
                        } else {
                            System.out.println("Aucun slot disponible dans l'inventaire.");
                        }
                    } else if (remove) {
                        for (Document slot : slots) {
                            if ( itemsId.equals(slot.get("item"))) {
                                slot.put("item",null);
                                System.out.println("Item retiré de l'inventaire avec succès.");
                                break;
                            }
                            else {
                                System.out.println("L'item n'est pas présent dans l'inventaire.");
                            }
                        }
                    }
                    Document updateQuery = new Document("$set", new Document("slots", slots));
                    collection.updateOne(new Document("_id", inventoryId), updateQuery);
                } else {
                    System.out.println("Inventaire introuvable.");
                }
        } catch (Exception e) {
            System.out.println("Erreur lors de l'ajout de l'item à l'inventaire.");
            e.printStackTrace();
        }
    }
}
