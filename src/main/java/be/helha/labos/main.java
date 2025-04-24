package be.helha.labos;

import be.helha.labos.Authentification.AuthController;
import be.helha.labos.Authentification.Authen;
import be.helha.labos.DBNosql.Connexion_DB_Nosql;
import be.helha.labos.DB.*;

import be.helha.labos.Lanterna.Inscription;
import be.helha.labos.collection.Character.*;
import be.helha.labos.collection.Item.*;
import be.helha.labos.collection.User;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import java.io.ObjectInputFilter;
import java.sql.Connection;

import static be.helha.labos.DBNosql.MongoDB.readAllCollections;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;


public class main {
    public static void main(String[] args) {
        Connexion_DB_Nosql connexionDbNosql = Connexion_DB_Nosql.getInstance();
        MongoDatabase mongoDatabase = connexionDbNosql.getDatabase();

        try {

            MongoCollection<Item> Itemcollection = mongoDatabase.getCollection("items", Item.class);
            MongoCollection<CharacterType> Charactercollection = mongoDatabase.getCollection("characters", CharacterType.class);


            Sword sword = new Sword();
            Sword fireSword = new Sword(Sword.SwordMaterial.FIRE);

            Mace mace = new Mace();
            Mace diamondMace = new Mace(Mace.MaceMaterial.DIAMOND);

            Bow bow = new Bow();
            Bow arablette = new Bow(Bow.BowMaterial.CROSSBOW);

            Potion potion = new Potion (20,15);

            //Archer archerTest = new Archer("archerTest", 101, 20, 0.3, 0.8);

            //CharacterType Chara = new CharacterType("Chara", 100, 20, 0.3, 0.8);

            // Insertion du document
            //Itemcollection.insertOne(sword);

            //Itemcollection.insertOne(shield);
            //Itemcollection.insertOne(potion);

            //Charactercollection.insertOne(archerTest);

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

            /*List<Document> users = readAllUser(mongoDatabase);
            for (Document User : users) {
                System.out.println(User.toJson());
            }*/

        } catch (Exception e) {
            e.printStackTrace();

        }

        connexionDbNosql.closeConnection();

        Authen authen = new Authen();

        User_DAO dao = new User_DAO();

        //dao.supprimerTableUser();

        boolean connex = dao.verifierConnexion("Jo","Jo");

        if(connex)
        {
            System.out.println("Utilisateur connexion avec succes !");
        }
        else {
            System.out.println("Utilisateur connexion échoué !");
        }

        /*User nouvelUser = new User("Jo", "Jo");
        boolean success = dao.ajouterUser(nouvelUser);

        if (success) {
            System.out.println("Utilisateur ajouté !");
        } else {
            System.out.println("Erreur lors de l'ajout.");
        }*/
    }
}
