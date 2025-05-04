package be.helha.labos.main;

import be.helha.labos.Authentification.Authen;
import be.helha.labos.DBNosql.Connexion_DB_Nosql;
import be.helha.labos.DB.*;

import be.helha.labos.DBNosql.DAO_NOSQL;
import be.helha.labos.collection.Character.*;
import be.helha.labos.collection.Item.*;
import be.helha.labos.collection.User;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import java.util.List;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;


public class main {
    public static void main(String[] args) {
        Connexion_DB_Nosql connexionDbNosql = Connexion_DB_Nosql.getInstance();
        MongoDatabase mongoDatabase = connexionDbNosql.getDatabase();

        MongoCollection<Item> Itemcollection = mongoDatabase.getCollection("items", Item.class);
        MongoCollection<CharacterType> Charactercollection = mongoDatabase.getCollection("characters", CharacterType.class);


        Authen authen = new Authen();

        User_DAO dao = new User_DAO();

        try{

        //dao.supprimerTableUser();

        /*boolean connex = dao.verifierConnexion("Jo","Jo");

        if(connex)
        {
            System.out.println("Utilisateur connexion avec succes !");
        }
        else {
            System.out.println("Utilisateur connexion échoué !");
        }*/




        //User nouvelUser = new User("nigf", "Jo","USER");
        User userExistant = dao.GetUserByPseudo("Jo");

        //boolean success = dao.ajouterUser(nouvelUser);
        /*if (success) {
            System.out.println("Utilisateur ajouté !");
        } else {
            System.out.println("Erreur lors de l'ajout.");
        }*/

        /*if(dao.GetUserById(nouvelUser.getId()) != null){
            System.out.println("User récupéré ,  Id :" + nouvelUser.getId());
        }
        else {
            System.out.println("Erreur lors de la récupération de l'utilisateur.");
        }*/

        DAO_NOSQL daoNosql = new DAO_NOSQL();

            if (userExistant != null) {
                int idUser = userExistant.getId(); // ✅ c'est l'ID qui vient de MySQL
                Archer archer = new Archer("archerX", 120, 30, 0.4, 0.9, idUser);
                Charactercollection.insertOne(archer);
                System.out.println("Personnage ajouté pour le user ID : " + idUser);
            } else {
                System.out.println("Utilisateur non trouvé.");
            }

            Sword sword = new Sword();
            Sword fireSword = new Sword(Sword.SwordMaterial.FIRE);

            Mace mace = new Mace();
            Mace diamondMace = new Mace(Mace.MaceMaterial.DIAMOND);

            Bow bow = new Bow();
            Bow arablette = new Bow(Bow.BowMaterial.CROSSBOW);

            Potion potion = new Potion (20,15);

            //CharacterType Chara = new CharacterType("Chara", 100, 20, 0.3, 0.8);

            // Insertion du document
            //Itemcollection.insertOne(sword);

            //Itemcollection.insertOne(shield);
            //Itemcollection.insertOne(potion);

            /*putItemsInInventory(new ObjectId("67c4646cc5452e653988b340"),
                    new ObjectId("67d048cb69f5966a18dcef48") , false);*/

            /*putItemsInChest(new ObjectId("67c4687a6085201f7eca9d02"),
                    new ObjectId("67d048cb69f5966a18dcef4a") , true);*/


            /*updateDocument(mongoDatabase, "items", new Document("type", "WoodenSword"),
                    new Document("$set", new Document("type", "Sword")));*/

            System.out.println("\n\n");
            //daoNosql.readAllCollections(mongoDatabase);

            List<CharacterType> characters = daoNosql.readAllCharacters(mongoDatabase); // récupère les persos
            if (characters.isEmpty()) {
               System.out.println("No characters found");
            } else {
                StringBuilder builder = new StringBuilder();
                for (CharacterType character : characters) {
                    builder.append("- ").append(character.getName())
                            .append(" | HP: ").append(character.getHealth())
                            .append(" | DMG: ").append(character.getDamage())
                            .append("\n");
                }
                System.out.println(builder.toString());
            }

            /*List<Document> users = readAllUser(mongoDatabase);
            for (Document User : users) {
                System.out.println(User.toJson());
            }*/

        } catch (Exception e) {
            e.printStackTrace();

        }

        dao.fermerConnexion();
        connexionDbNosql.closeConnection();

    }
}
