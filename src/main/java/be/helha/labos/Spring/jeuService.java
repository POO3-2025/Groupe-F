package be.helha.labos.Spring;

import be.helha.labos.DB.User_DAO;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import be.helha.labos.collection.User;

import java.util.List;

import static com.mongodb.client.model.Filters.eq;

/**
 * Classe de service pour gérer les opérations liées aux utilisateurs dans le jeu.
 */
@Service
public class jeuService {
    /**
     * Instance de User_DAO pour interagir avec la base de données.
     */
    User_DAO dao = new User_DAO("mysql");
    /**
     * Méthode pour récupérer tous les utilisateurs.
     *
     * @return liste des utilisateurs
     */
    public List <User> getAllUsers() {
        return dao.getAllUser();
    }
    /**
     * Méthode pour récupérer un utilisateur par son ID.
     *
     * @param id l'ID de l'utilisateur
     * @return l'utilisateur correspondant à l'ID
     */
    public User getUserById(int id) {
        return dao.getUserById(id);
    }
    /**
     * Méthode pour créer un nouvel utilisateur.
     *
     * @param user l'utilisateur à créer
     * @return l'utilisateur créé
     */
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