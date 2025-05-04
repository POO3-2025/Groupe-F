package be.helha.labos.DBNosql;

import be.helha.labos.collection.Character.*;
import com.mongodb.client.*;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;


public class DAO_NOSQL {


    private static Connexion_DB_Nosql connexionDbNosql;
    private static MongoDatabase mongoDatabase;
    private static MongoCollection<Document> collection;


    public void readAllCollections (MongoDatabase database){
            for (String collectionName : database.listCollectionNames()) {
                MongoCollection<Document> collection = database.getCollection(collectionName);
                System.out.println("\nLecture de la collection " + collectionName + " : ");
                for (Document doc : collection.find()) {
                    System.out.println(doc.toJson());
                }
            }
    }

    public void creerUserDansMongo(int idUser, String pseudo) {
        try {
            connexionDbNosql = Connexion_DB_Nosql.getInstance();
            mongoDatabase = connexionDbNosql.getDatabase();
            MongoCollection<Document> usersCollection = mongoDatabase.getCollection("users_nosql");

            Document userDoc = new Document("_id", idUser)
                    .append("pseudo", pseudo)
                    .append("personnages", new ArrayList<>());
            usersCollection.insertOne(userDoc);

        } catch (Exception e) {
            System.out.println("Erreur lors de la création de l'utilisateur dans MongoDB.");
            e.printStackTrace();
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

    public static void updateDocument (MongoDatabase database, String collectionName, Document filter, Document
        update){
            MongoCollection<Document> collection = database.getCollection(collectionName);
            collection.updateOne(filter, update);
            System.out.println("Mise à jour effectuée dans la collection " + collectionName + " : ");
        }
    }
