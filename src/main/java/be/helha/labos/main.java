package be.helha.labos;

import be.helha.labos.DBNosql.Connexion_DB_Nosql;

import be.helha.labos.collection.Item.Item;
import be.helha.labos.collection.Item.Potion;
import be.helha.labos.collection.Item.Shield;
import be.helha.labos.collection.Item.Sword;
import be.helha.labos.collection.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.List;

import static be.helha.labos.collection.User.*;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;


public class main {
    public static void main(String[] args) {
        Connexion_DB_Nosql connexionDbNosql = Connexion_DB_Nosql.getInstance();
        MongoDatabase mongoDatabase = connexionDbNosql.getDatabase();
        try {

            MongoCollection <Item>Itemcollection = mongoDatabase.getCollection("items", Item.class);

            Sword sword = new Sword(25);

            Shield shield = new Shield(1,50);

            Potion potion = new Potion (20,15);


            // Insertion du document
            /*Itemcollection.insertOne(sword);
            Itemcollection.insertOne(shield);
            Itemcollection.insertOne(potion);*/

            //User user = new User("Doe","John");
            //user.save;
            //chests chests = new chests(new ObjectId("67c4646cc5452e653988b340"));

            /*putItemsInInventory(new ObjectId("67c4646cc5452e653988b340"),
                    new ObjectId("67d048cb69f5966a18dcef48") , false);*/

            /*putItemsInChest(new ObjectId("67c4687a6085201f7eca9d02"),
                    new ObjectId("67d048cb69f5966a18dcef4a") , true);*/


            /*updateDocument(mongoDatabase, "items", new Document("type", "WoodenSword"),
                    new Document("$set", new Document("type", "Sword")));*/

            System.out.println("\n\n");
            //readAllCollections(mongoDatabase);

            List<Document> users = readAllUser(mongoDatabase);
            for (Document User : users) {
                System.out.println(User.toJson());
            }

        }catch (Exception e){
            e.printStackTrace();

        }

        connexionDbNosql.closeConnection();
    }
}
