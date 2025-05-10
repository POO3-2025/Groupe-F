package be.helha.labos.collection;

import be.helha.labos.DBNosql.Connexion_DB_Nosql;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Classe Factory pour gérer les inventaires dans la base de données.
 * Elle permet de créer, insérer et mettre à jour les inventaires.
 */
public class InventaireFactory {

    private static Connexion_DB_Nosql connexionDbNosql = new Connexion_DB_Nosql("nosqlTest");
    private static MongoDatabase mongoDatabase = connexionDbNosql.createDatabase();
    private static MongoCollection<Document> collection;

    /**
     * Crée un nouvel inventaire dans la base de données.
     * @param inventaire L'inventaire à insérer.
     * @return Le nouvel inventaire inséré avec son ID.
     */
    public Inventaire createInventaire(Inventaire inventaire) {
        try {
            collection = mongoDatabase.getCollection("inventory");

            // Créer un document représentant l'inventaire
            Document inventoryDoc = new Document("_id", inventaire.getId())
                    .append("slots", inventaire.getInventorySlots());

            // Insérer le document dans la base de données
            collection.insertOne(inventoryDoc);

            System.out.println("Inventaire créé avec ID : " + inventaire.getId());

            return inventaire;

        } catch (Exception e) {
            System.out.println("Erreur lors de la création de l'inventaire.");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Récupère un inventaire à partir de son ID.
     * @param inventoryId L'ID de l'inventaire à récupérer.
     * @return L'inventaire correspondant à l'ID.
     */
    public  Inventaire getInventaireById(ObjectId inventoryId) {
        try {
            collection = mongoDatabase.getCollection("inventory");

            // Trouver l'inventaire dans la base de données
            Document inventoryDoc = collection.find(new Document("_id", inventoryId)).first();

            if (inventoryDoc != null) {
                // Récupérer les slots
                List<Document> slots = (List<Document>) inventoryDoc.get("slots");

                // Créer un objet Inventaire à partir du document
                Inventaire inventaire = new Inventaire(inventoryId, slots);
                return inventaire;
            } else {
                System.out.println("Inventaire introuvable avec l'ID : " + inventoryId);
                return null;
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la récupération de l'inventaire.");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Ajoute ou retire un item dans l'inventaire d'un personnage.
     * @param inventoryId L'ID de l'inventaire.
     * @param itemsId L'ID de l'item à ajouter ou retirer.
     * @param remove Si true, l'item sera retiré ; sinon, ajouté.
     */
    public static void putItemsInInventory(ObjectId inventoryId, ObjectId itemsId, boolean remove) {
        try {
            collection = mongoDatabase.getCollection("inventory");

            // Récupérer l'inventaire existant
            Document inventory = collection.find(new Document("_id", inventoryId)).first();

            if (inventory != null) {
                // Récupérer les slots de l'inventaire
                List<Document> slots = (List<Document>) inventory.get("slots");

                // Trouver un slot vide ou un slot contenant déjà l'item
                if (!remove) {
                    boolean itemPlaced = false;
                    for (Document slot : slots) {
                        if (itemsId.equals(slot.get("item"))) {
                            itemPlaced = true;
                            System.out.println("L'objet est déjà présent dans un slot.");
                            break;
                        }
                        if (slot.get("item") == null) {
                            slot.put("item", itemsId);
                            itemPlaced = true;
                            System.out.println("Item ajouté à l'inventaire avec succès.");
                            break;
                        }
                    }

                    if (!itemPlaced) {
                        System.out.println("Aucun slot disponible pour ajouter l'item.");
                    }
                } else {
                    boolean itemRemoved = false;
                    for (Document slot : slots) {
                        if (itemsId.equals(slot.get("item"))) {
                            slot.put("item", null);
                            itemRemoved = true;
                            System.out.println("Item retiré de l'inventaire avec succès.");
                            break;
                        }
                    }

                    if (!itemRemoved) {
                        System.out.println("L'item n'est pas présent dans l'inventaire.");
                    }
                }

                // Mettre à jour les slots dans la base de données
                Document updateQuery = new Document("$set", new Document("slots", slots));
                collection.updateOne(new Document("_id", inventoryId), updateQuery);

            } else {
                System.out.println("Inventaire introuvable avec l'ID : " + inventoryId);
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de l'ajout ou du retrait de l'item dans l'inventaire.");
            e.printStackTrace();
        }
    }
}
