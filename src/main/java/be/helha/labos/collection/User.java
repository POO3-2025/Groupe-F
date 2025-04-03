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

public class User {

    @JsonProperty("_id")
    protected ObjectId id;
    String nom;
    String Prenom;

    private Connexion_DB_Nosql connexionDbNosql;
    private MongoDatabase mongoDatabase;
    private MongoCollection<Document> userCollection;

    public User() {} // par défaut

    public User(String nom, String Prenom) {

        connexionDbNosql = Connexion_DB_Nosql.getInstance();
        mongoDatabase = connexionDbNosql.getDatabase();
        userCollection = mongoDatabase.getCollection("users");

        this.nom = nom;
        this.Prenom = Prenom;
    }
    public void save(){
        // Création d'un inventaire
        Inventaire inventaire = new Inventaire();

        // Création de l'utilisateur
        Document user = new Document("nom", nom)
                .append("prenom", Prenom)
                .append("inventory_id", inventaire.getId()); // Référence à l'inventaire par _id

        // Insertion de l'utilisateur dans la collection 'users'
        InsertOneResult userResult = userCollection.insertOne(user);
        System.out.println(userResult);
        }

    public ObjectId getId() {
        return id;
    }
    public void setId(ObjectId id) {
        this.id = id;
    }

    @JsonProperty("nom")
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @JsonProperty("prenom")
    public String getPrenom() {
        return Prenom;
    }

    public void setPrenom(String prenom) {
        Prenom = prenom;
    }

    // Méthode permettant de supprimer un user ainsi que son inventaire
    //(Les objets présents dans l'inventaire ne sont pas supprimés de la collection 'Items')
    public static void removeUserAndDependencies(MongoDatabase database, MongoCollection<Document> userCollection, ObjectId userId) {
        try {
            // Récupérer l'utilisateur
            Document user = userCollection.find(new Document("_id", userId)).first();

            if (user != null) {
                // Récupérer l'ID de l'inventaire de l'utilisateur
                ObjectId inventoryId = user.getObjectId("inventory_id");

                // Récupérer la collection des inventaires
                MongoCollection<Document> inventoryCollection = database.getCollection("inventory");

                // Trouver l'inventaire
                Document inventory = inventoryCollection.find(new Document("_id", inventoryId)).first();

                if (inventory != null) {
                    // Récupérer l'ID du coffre dans l'inventaire (s'il y en a un)
                    ObjectId chestId = inventory.getObjectId("chest_id");

                    // Supprimer l'inventaire
                    inventoryCollection.deleteOne(new Document("_id", inventoryId));
                    System.out.println("Inventaire supprimé avec succès (ID: " + inventoryId + ")");

                    // Supprimer le coffre s'il existe
                    if (chestId != null) {
                        MongoCollection<Document> chestCollection = database.getCollection("chests");
                        chestCollection.deleteOne(new Document("_id", chestId));
                        System.out.println("Coffre supprimé avec succès (ID: " + chestId + ")");
                    }
                }

                // Supprimer l'utilisateur
                userCollection.deleteOne(new Document("_id", userId));
                System.out.println("Utilisateur supprimé avec succès (ID: " + userId + ")");
            } else {
                System.out.println("Utilisateur non trouvé avec l'ID: " + userId);
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la suppression de l'utilisateur et de ses dépendances");
            e.printStackTrace();
        }
    }

    public static List<Document> readAllUser(MongoDatabase database) {
        List<Document> users = new ArrayList<>();
        try {
            MongoCollection<Document> collection = database.getCollection("users");
            users = collection.find().into(new ArrayList<>());
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération des utilisateurs : " + e.getMessage());
        }
        return users;
    }
}
