package be.helha.labos;

import be.helha.labos.DBNosql.Connexion_DB_Nosql;

import be.helha.labos.DBNosql.MongoDB;
import be.helha.labos.collection.Inventaire;
import be.helha.labos.collection.Item.*;
import be.helha.labos.collection.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import javax.crypto.Mac;

import static be.helha.labos.DBNosql.MongoDB.readAllCollections;
import static be.helha.labos.DBNosql.MongoDB.updateDocument;
import static be.helha.labos.collection.Inventaire.putItemsInInventory;
import static be.helha.labos.collection.chests.putItemsInChest;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;


public class main {
    public static void main(String[] args) {
        Connexion_DB_Nosql connexionDbNosql = Connexion_DB_Nosql.getInstance();
        MongoDatabase mongoDatabase = connexionDbNosql.getDatabase();
        try {

            MongoCollection <Item>Itemcollection = mongoDatabase.getCollection("items", Item.class);

            Sword sword = new Sword();
            Sword fireSword = new Sword(Sword.SwordMaterial.FIRE);

            Mace mace = new Mace();
            Mace diamondMace = new Mace(Mace.MaceMaterial.DIAMOND);

            Bow bow = new Bow();
            Bow arablette = new Bow(Bow.BowMaterial.CROSSBOW);

            Shield shield = new Shield(1,50);

            Potion potion = new Potion (20,15);


            // Insertion du document
            Itemcollection.insertOne(arablette);
            Itemcollection.insertOne(sword);
            Itemcollection.insertOne(fireSword);
            Itemcollection.insertOne(diamondMace);

            //user user = new user("Doe","John");
            //chests chests = new chests(new ObjectId("67c4646cc5452e653988b340"));

            /*putItemsInInventory(new ObjectId("67c4646cc5452e653988b340"),
                    new ObjectId("67d048cb69f5966a18dcef48") , false);*/

            /*putItemsInChest(new ObjectId("67c4687a6085201f7eca9d02"),
                    new ObjectId("67d048cb69f5966a18dcef4a") , true);*/


            /*updateDocument(mongoDatabase, "items", new Document("type", "WoodenSword"),
                    new Document("$set", new Document("type", "Sword")));*/

            System.out.println("\n\n");
            readAllCollections(mongoDatabase);

        }catch (Exception e){
            e.printStackTrace();

        }

        connexionDbNosql.closeConnection();
    }
}
