package be.helha.labos.Spring;

import be.helha.labos.DBNosql.Connexion_DB_Nosql;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import be.helha.labos.collection.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import be.helha.labos.collection.*;

import static com.mongodb.client.model.Filters.eq;

@Service
public class jeuService {

    private  MongoCollection<Document> userCollection;

    Connexion_DB_Nosql connexionDbNosql = Connexion_DB_Nosql.getInstance();
    MongoDatabase mongoDatabase = connexionDbNosql.getDatabase();

    public List<User> getAllUsers() {
        Connexion_DB_Nosql connexionDbNosql = Connexion_DB_Nosql.getInstance();
        MongoDatabase mongoDatabase = connexionDbNosql.getDatabase();

        // Utiliser la méthode readAllUser() pour récupérer la liste de documents
        List<Document> documents = User.readAllUser(mongoDatabase);

        // Créer une liste de User à partir des documents
        List<User> users = new ArrayList<>();
        for (Document doc : documents) {
            // Extraire les informations du document et les convertir en objet User
            String nom = doc.getString("nom");
            String prenom = doc.getString("prenom");
            ObjectId id = doc.getObjectId("inventory_id");

            // Créer un utilisateur avec les informations extraites
            User user = new User(nom, prenom);
            user.setId(doc.getObjectId("_id"));  // Assurer que l'ID est récupéré correctement

            users.add(user);
        }

        // Afficher chaque utilisateur pour le débogage
        for (User user : users) {
            System.out.println(user.toString());
        }

        return users;
    }

    public Optional<User> getUserById(ObjectId id) {
        Document doc = userCollection.find(eq("_id", id)).first();
        return doc != null ? Optional.of(documentToUser(doc)) : Optional.empty();
    }

    public User saveUser(User user) {
        Document doc = userToDocument(user);
        userCollection.insertOne(doc);
        return user;
    }

    public void deleteUser(ObjectId id) {
        userCollection.deleteOne(eq("_id", id));
    }

    private User documentToUser(Document doc) {
        return new User(
                doc.getString("name"),
                doc.getString("prenom")
        );
    }

    private Document userToDocument(User user) {
        return new Document("_id", user.getId())
                .append("name", user.getNom())
                .append("prenom", user.getPrenom());
    }
}
