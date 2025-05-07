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
import org.bson.Document;

import java.util.List;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;


public class main_Characters {
    public static void main(String[] args) {
        Connexion_DB_Nosql connexionDbNosql = Connexion_DB_Nosql.getInstance();
        MongoDatabase mongoDatabase = connexionDbNosql.getDatabase();

        MongoCollection<CharacterType> Charactercollection = mongoDatabase.getCollection("characters", CharacterType.class);


        Authen authen = new Authen();
        User_DAO daoUser = new User_DAO();

        try
        {
            User nouvelUser = new User("PSEUDOTEST", "","USER");
            User nouvelUser2 = new User("PSEUDOTEST2", "","USER");
            User nouvelUser3 = new User("PSEUDOTEST3", "","USER");

            boolean success = daoUser.ajouterUser(nouvelUser);
            if (success)
            {
                System.out.println("Utilisateur ajouté");
            }
            else
            {
                System.out.println("Erreur lors de l'ajout");
            }

            if(daoUser.GetUserById(nouvelUser.getId()) != null)
            {
                System.out.println("Id :" + nouvelUser.getId());
            }
            else
            {
                System.out.println("Erreur de la récupération de ID");
            }



            DAO_NOSQL daoNosql = new DAO_NOSQL();

            Archer archerTest = new Archer("archerX");
            daoNosql.ajouterPersonnagePourUser(nouvelUser.getPseudo(),archerTest);

            Knight knightTest= new Knight("onightX");
            daoNosql.ajouterPersonnagePourUser(nouvelUser2.getPseudo(),knightTest);

            Orc orcTest = new Orc("orcX");
            daoNosql.ajouterPersonnagePourUser(nouvelUser3.getPseudo(),orcTest);

            List<CharacterType> characters = daoNosql.readAllCharacters(mongoDatabase);
            if (characters.isEmpty())
            {
                System.out.println("No characters found");
            }
            else
            {
                StringBuilder builder = new StringBuilder();
                for (CharacterType character : characters)
                {
                    builder.append("- ").append(character.getName())
                            .append(" | HP: ").append(character.getHealth())
                            .append(" | DMG: ").append(character.getDamage())
                            .append("\n");
                }
                System.out.println(builder.toString());
            }


        } catch (Exception e)
        {
            e.printStackTrace();

        }

        daoUser.fermerConnexion();
        connexionDbNosql.closeConnection();

    }
}
