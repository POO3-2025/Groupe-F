package be.helha.labos.Spring;

import be.helha.labos.DB.Connexion_DB;
import be.helha.labos.DBNosql.Connexion_DB_Nosql;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import be.helha.labos.collection.User;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;

@Service
public class jeuService {

    private  MongoCollection<Document> userCollection;

    Connexion_DB_Nosql connexionDbNosql = Connexion_DB_Nosql.getInstance();
    MongoDatabase mongoDatabase = connexionDbNosql.getDatabase();

    public List<User> getAllUsers() {
        // Récupère l'instance de connexion
        Connexion_DB connexion = Connexion_DB.getInstance("mysql");

        // Vérifie si la connexion est bien ouverte
        Connection conn = connexion.getConnection();
        return List.of();
    }

    /*public Optional<User> getUserById(ObjectId id) {
        Document doc = userCollection.find(eq("_id", id)).first();
        return doc != null ? Optional.of(documentToUser(doc)) : Optional.empty();
    }

    public User saveUser(User user) {
        Document doc = userToDocument(user);
        userCollection.insertOne(doc);
        return user;
    }*/

    public void deleteUser(ObjectId id) {
        userCollection.deleteOne(eq("_id", id));
    }

    /*private User documentToUser(Document doc) {
        return new User(
                doc.getString("name"),
                doc.getString("prenom")
        );
    }*/

    /*private Document userToDocument(User user) {
        return new Document("_id", user.getId())
                .append("name", user.getNom())
                .append("prenom", user.getPrenom());
    }*/
}
