
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * Constructeur de la classe DAO_NOSQL.
     * Il initialise la connexion à la base de données et les collections nécessaires.
     */
    public DAO_NOSQL(String dbkey) {
        connexionDbNosql = new Connexion_DB_Nosql(dbkey);
        mongoDatabase = connexionDbNosql.createDatabase();
        Itemcollection = mongoDatabase.getCollection("items", Item.class);
        Charactercollection = mongoDatabase.getCollection("characters", CharacterType.class);
    }

    /**
     * Méthode pour afficher le contenue de chaque collections de la DB Nosql
     */
    public void readAllCollections() {
        for (String collectionName : mongoDatabase.listCollectionNames()) {
            MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName);
            System.out.println("\nLecture de la collection : " + collectionName);
            for (Document doc : collection.find()) {
                System.out.println(doc.toJson());
            }
        }
    }

    /**
     * Méthode pour afficher le contenue de chaque collections de la DB Nosql en map
     *
     * @return la liste en map
     */
    public Map<String, List<Document>> readAllCollectionsAsMap() {
        Map<String, List<Document>> result = new HashMap<>();
        for (String collectionName : mongoDatabase.listCollectionNames()) {
            MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName);
            List<Document> docs = new ArrayList<>();
            for (Document doc : collection.find()) {
                docs.add(doc);
            }
            result.put(collectionName, docs);
        }
        return result;
    }

    /**
     * Méthode pour ajouter un personnage pour un utilisateur donné.
     *
     * @param pseudo Le pseudo de l'utilisateur.
     * @param perso  Le personnage à ajouter.
     */
    public void ajouterPersonnagePourUser(String dbkey,String pseudo, CharacterType perso) {
        User_DAO userDao = new User_DAO(dbkey);
        User user = userDao.getUserByPseudo(pseudo);
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
    public void DeleteCharacters (ObjectId id){
        CharacterType characterType = new CharacterType();
        characterType.removeCharacter(mongoDatabase,id);
    }

    /**
     * liste tous les personnages de la base de données.
     * @return Une liste de tous les personnages.
     */
    public List<CharacterType> readAllCharacters() {
        List<CharacterType> characters = new ArrayList<>();
        for (CharacterType character : Charactercollection.find()) {
            characters.add(character);
        }
        return characters;
    }

    /**
     * Méthode pour récupérer tous les personnages d'un utilisateur par son identifiant.
     *
     * @param idUser   L'identifiant de l'utilisateur.
     * @return Une liste de personnages associés à l'utilisateur.
     */
    public List<CharacterType> readAllCharactersByUserId(int idUser) {
        List<CharacterType> characters = new ArrayList<>();

        for (CharacterType character : Charactercollection.find(Filters.eq("idUser", idUser))) {
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