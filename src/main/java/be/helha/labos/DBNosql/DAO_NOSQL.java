package be.helha.labos.DBNosql;

import be.helha.labos.DB.User_DAO;
import be.helha.labos.collection.Character.*;
import be.helha.labos.collection.Item.Item;
import be.helha.labos.collection.User;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

/**
 * Classe DAO_NOSQL pour gérer les opérations de base de données NoSQL (MongoDB).
 * Elle utilise le design pattern Singleton pour garantir qu'une seule instance de la connexion existe.
 * La configuration est chargée depuis un fichier JSON.
 */
public class DAO_NOSQL {

    /**
     * Instance unique de la classe DAO_NOSQL (Singleton).
     */
    private static Connexion_DB_Nosql connexionDbNosql;
    private static MongoDatabase mongoDatabase;

    MongoCollection<Item> Itemcollection;
    MongoCollection<CharacterType> Charactercollection;

    User_DAO userDao = new User_DAO("mysql");

    /**
     * Constructeur de la classe DAO_NOSQL.
     * Il initialise la connexion à la base de données et les collections nécessaires.
     */
    public DAO_NOSQL() {
        connexionDbNosql = Connexion_DB_Nosql.getInstance();
        mongoDatabase = connexionDbNosql.getDatabase();

        Itemcollection = mongoDatabase.getCollection("items", Item.class);
        Charactercollection = mongoDatabase.getCollection("characters", CharacterType.class);
    }

    /**
     * Méthode pour obtenir l'instance de la connexion à la base de données.
     *
     * @return L'instance de la connexion à la base de données.
     */
    public void readAllCollections (MongoDatabase database){
            for (String collectionName : database.listCollectionNames()) {
                MongoCollection<Document> collection = database.getCollection(collectionName);
                System.out.println("\nLecture de la collection " + collectionName + " : ");
                for (Document doc : collection.find()) {
                    System.out.println(doc.toJson());
                }
            }
    }

    /**
     * Méthode pour ajouter un personnage pour un utilisateur donné.
     *
     * @param pseudo Le pseudo de l'utilisateur.
     * @param perso  Le personnage à ajouter.
     */
    public void ajouterPersonnagePourUser(String pseudo, CharacterType perso) {
        User user = userDao.GetUserByPseudo(pseudo);
        if (user != null) {
            perso.setIdUser(user.getId());
            Charactercollection.insertOne(perso);
        } else {
            throw new IllegalArgumentException("User avec pseudo " + pseudo + " introuvable.");
        }
    }

    /**
     * Méthode pour supprimer un personnage par son identifiant.
     *
     */
    public void DeleteCharacters (MongoDatabase database,ObjectId id){
        CharacterType characterType = new CharacterType();
        characterType.removeCharacter(id);
    }

    /**
     * liste tous les personnages de la base de données.
     * @param database La base de données MongoDB.
     * @return Une liste de tous les personnages.
     */
    public List<CharacterType> readAllCharacters(MongoDatabase database) {
        MongoCollection<CharacterType> collection = database.getCollection("characters", CharacterType.class);
        List<CharacterType> characters = new ArrayList<>();

        for (CharacterType character : collection.find()) {
            characters.add(character);
        }

        return characters;
    }

    /**
     * Méthode pour récupérer tous les personnages d'un utilisateur par son identifiant.
     *
     * @param database La base de données MongoDB.
     * @param idUser   L'identifiant de l'utilisateur.
     * @return Une liste de personnages associés à l'utilisateur.
     */
    public List<CharacterType> readAllCharactersByUserId(MongoDatabase database, int idUser) {
        MongoCollection<CharacterType> collection = database.getCollection("characters", CharacterType.class);
        List<CharacterType> characters = new ArrayList<>();

        for (CharacterType character : collection.find(Filters.eq("idUser", idUser))) {
            characters.add(character);
        }
        return characters;
    }

    /**
     * Méthode pour mettre à jour un document dans une collection.
     *
     * @param database      La base de données MongoDB.
     * @param collectionName Le nom de la collection.
     * @param filter        Le filtre pour trouver le document à mettre à jour.
     * @param update        Le document de mise à jour.
     */
    public static void updateDocument (MongoDatabase database, String collectionName, Document filter, Document
        update){
            MongoCollection<Document> collection = database.getCollection(collectionName);
            collection.updateOne(filter, update);
            System.out.println("Mise à jour effectuée dans la collection " + collectionName + " : ");
        }
    }
