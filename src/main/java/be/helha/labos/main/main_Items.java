package be.helha.labos.main;

import be.helha.labos.DBNosql.Connexion_DB_Nosql;
import be.helha.labos.collection.chests;
import be.helha.labos.DBNosql.DAO_NOSQL;
import be.helha.labos.collection.Item.*;
import be.helha.labos.collection.Character.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import static be.helha.labos.DBNosql.DAO_NOSQL.updateDocument;
import static be.helha.labos.collection.Inventaire.putItemsInInventory;
import static be.helha.labos.collection.chests.putItemsInChest;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class main_Items {
    public static void main(String[] args)
    {
        Connexion_DB_Nosql connexionDbNosql = Connexion_DB_Nosql.getInstance();
        MongoDatabase mongoDatabase = connexionDbNosql.getDatabase();
        DAO_NOSQL daoNosql = new DAO_NOSQL();
        try {

            MongoCollection <Item>Itemcollection = mongoDatabase.getCollection("items", Item.class);
            MongoCollection <CharacterType>CharacterTypecollection = mongoDatabase.getCollection("characters", CharacterType.class);


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

            chests chests = new chests(new ObjectId());

            putItemsInInventory(new ObjectId(),
                    new ObjectId() , false);

            putItemsInChest(new ObjectId(),
                    new ObjectId() , true);

            daoNosql.readAllCollections(mongoDatabase);

        }catch (Exception e){
            e.printStackTrace();

        }

        connexionDbNosql.closeConnection();
    }
}

