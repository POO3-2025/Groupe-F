package be.helha.labos.Authentification;

import be.helha.labos.DB.User_DAO;
import be.helha.labos.collection.User;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
/**
 * Classe de service pour gérer les opérations liées aux utilisateurs dans le jeu.
 */
public class jeuService {

    User_DAO dao = new User_DAO("mysql");

    public List <User> getAllUsers() {
        return dao.getAllUser();
    }

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

    /**
     * Méthode pour supprimer un utilisateur par son ID.
     *
     * @param id l'ID de l'utilisateur à supprimer
     */
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