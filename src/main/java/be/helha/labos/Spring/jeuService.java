package be.helha.labos.Spring;

import be.helha.labos.DB.User_DAO;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import be.helha.labos.collection.User;

import java.util.List;

import static com.mongodb.client.model.Filters.eq;

@Service
public class jeuService {

    User_DAO dao = new User_DAO("mysql");

    public List <User> getAllUsers() {
        return dao.getAllUser();
    }

    public User getUserById(int id) {
        return dao.getUserById(id);
    }

    public User saveUser(User user) {
        if(dao.getUserById(user.getId()) == null) {
            return user = null;
        }
        else
            dao.ajouterUser(user);
        return user;

    }

    public void deleteUser(ObjectId id) {
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