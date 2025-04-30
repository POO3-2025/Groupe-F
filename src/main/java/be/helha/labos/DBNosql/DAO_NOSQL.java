package be.helha.labos.DBNosql;

import be.helha.labos.collection.Character.CharacterType;
import com.mongodb.client.*;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;


public class DAO_NOSQL {

    public void readAllCollections (MongoDatabase database){
            for (String collectionName : database.listCollectionNames()) {
                MongoCollection<Document> collection = database.getCollection(collectionName);
                System.out.println("\nLecture de la collection " + collectionName + " : ");
                for (Document doc : collection.find()) {
                    System.out.println(doc.toJson());
                }
            }
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
