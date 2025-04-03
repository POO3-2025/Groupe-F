package be.helha.labos.DBNosql;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;


public class MongoDB {

    public static void readAllCollections (MongoDatabase database){
            for (String collectionName : database.listCollectionNames()) {
                MongoCollection<Document> collection = database.getCollection(collectionName);
                System.out.println("\nLecture de la collection " + collectionName + " : ");
                for (Document doc : collection.find()) {
                    System.out.println(doc.toJson());
                }
            }
    }

    public static void updateDocument (MongoDatabase database, String collectionName, Document filter, Document
        update){
            MongoCollection<Document> collection = database.getCollection(collectionName);
            collection.updateOne(filter, update);
            System.out.println("Mise à jour effectuée dans la collection " + collectionName + " : ");
        }
    }
