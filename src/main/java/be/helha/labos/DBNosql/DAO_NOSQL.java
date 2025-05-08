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


public class DAO_NOSQL {

    private static Connexion_DB_Nosql connexionDbNosql;
    private static MongoDatabase mongoDatabase;

    MongoCollection<Item> Itemcollection;
    MongoCollection<CharacterType> Charactercollection;

    User_DAO userDao = new User_DAO("mysql");

    public DAO_NOSQL() {
        connexionDbNosql = Connexion_DB_Nosql.getInstance();
        mongoDatabase = connexionDbNosql.getDatabase();

        Itemcollection = mongoDatabase.getCollection("items", Item.class);
        Charactercollection = mongoDatabase.getCollection("characters", CharacterType.class);
    }

    public void readAllCollections (MongoDatabase database){
            for (String collectionName : database.listCollectionNames()) {
                MongoCollection<Document> collection = database.getCollection(collectionName);
                System.out.println("\nLecture de la collection " + collectionName + " : ");
                for (Document doc : collection.find()) {
                    System.out.println(doc.toJson());
                }
            }
    }

    public void ajouterPersonnagePourUser(String pseudo, CharacterType perso) {
        User user = userDao.GetUserByPseudo(pseudo);
        if (user != null) {
            perso.setIdUser(user.getId());
            Charactercollection.insertOne(perso);
        } else {
            throw new IllegalArgumentException("User avec pseudo " + pseudo + " introuvable.");
        }
    }

    public void DeleteCharacters (MongoDatabase database,ObjectId id){
        CharacterType characterType = new CharacterType();
        characterType.removeCharacter(id);
    }

    public List<CharacterType> readAllCharacters(MongoDatabase database) {
        MongoCollection<CharacterType> collection = database.getCollection("characters", CharacterType.class);
        List<CharacterType> characters = new ArrayList<>();

        for (CharacterType character : collection.find()) {
            characters.add(character);
        }

        return characters;
    }

    public List<CharacterType> readAllCharactersByUserId(MongoDatabase database, int idUser) {
        MongoCollection<CharacterType> collection = database.getCollection("characters", CharacterType.class);
        List<CharacterType> characters = new ArrayList<>();

        for (CharacterType character : collection.find(Filters.eq("idUser", idUser))) {
            characters.add(character);
        }
        return characters;
    }

    public static void updateDocument (MongoDatabase database, String collectionName, Document filter, Document
        update){
            MongoCollection<Document> collection = database.getCollection(collectionName);
            collection.updateOne(filter, update);
            System.out.println("Mise à jour effectuée dans la collection " + collectionName + " : ");
        }
    }
