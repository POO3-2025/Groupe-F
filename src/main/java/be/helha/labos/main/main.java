package be.helha.labos.main;

import be.helha.labos.Authentification.Authen;
import be.helha.labos.DBNosql.Connexion_DB_Nosql;
import be.helha.labos.DB.*;

import be.helha.labos.DBNosql.DAO_NOSQL;
import be.helha.labos.collection.Character.*;
import be.helha.labos.collection.Item.*;
import be.helha.labos.collection.User;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import java.util.List;


import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

/**
 * Classe de test pour tester les opérations CRUD de la classe User et characters et test de la DB
 */

public class main {
    public static void main(String[] args) {

        Authen authen = new Authen();

        Connexion_DB_Nosql connexionDbNosql = new Connexion_DB_Nosql("nosql");
        MongoDatabase mongoDatabase = connexionDbNosql.createDatabase();

        User_DAO daoUser = new User_DAO("mysql");
        DAO_NOSQL daoNosql = new DAO_NOSQL("nosql");

        try{

        //daoUser.supprimerTableUser();

        /*boolean connex = dao.verifierConnexion("Jo","Jo");

        if(connex)
        {
            System.out.println("Utilisateur connexion avec succes !");
        }
        else {
            System.out.println("Utilisateur connexion échoué !");
        }*/

        User nouvelUser = new User("Bill", "bill","USER");

        /*boolean success = daoUser.ajouterUser(nouvelUser);
        if (success) {
            System.out.println("Utilisateur ajouté !");
        } else {
            System.out.println("Erreur lors de l'ajout.");
        }*/

        if(daoUser.getUserByPseudo(nouvelUser.getPseudo()) != null){
            System.out.println("User récupéré ,  pseudo :" + nouvelUser.getPseudo());
        }
        else {
            System.out.println("Erreur lors de la récupération de l'utilisateur.");
        }

        List<User> lesUsers;
        System.out.println("\nListe des users :");
        lesUsers = daoUser.getAllUser();
        for (User u : lesUsers) {
                System.out.println(u);
        }


            Archer archer = new Archer("archer",mongoDatabase);
            daoNosql.ajouterPersonnagePourUser("mysql",nouvelUser.getPseudo(), archer);

            Sword sword = new Sword();
            Sword fireSword = new Sword(Sword.SwordMaterial.FIRE);

            Mace mace = new Mace();
            Mace diamondMace = new Mace(Mace.MaceMaterial.DIAMOND);

            Bow bow = new Bow();
            Bow arablette = new Bow(Bow.BowMaterial.CROSSBOW);

            Potion potion = new Potion (20,15);


            // Insertion du document
            //Itemcollection.insertOne(sword);

            //Itemcollection.insertOne(shield);
            //Itemcollection.insertOne(potion);


            /*putItemsInChest(new ObjectId("67c4687a6085201f7eca9d02"),
                    new ObjectId("67d048cb69f5966a18dcef4a") , true);*/

            System.out.println("\n\n");
            daoNosql.readAllCollections();

            List<CharacterType> characters = daoNosql.readAllCharacters(); // récupère les persos
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

        } catch (Exception e) {
            e.printStackTrace();

        }

        daoUser.fermerConnexion();
    }
}
