package be.helha.labos.main;

import be.helha.labos.Authentification.Authen;
import be.helha.labos.DBNosql.Connexion_DB_Nosql;
import be.helha.labos.DB.*;

import be.helha.labos.DBNosql.DAO_NOSQL;
import be.helha.labos.collection.Character.*;
import be.helha.labos.collection.User;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import java.util.List;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;


public class main_Characters {
    public static void main(String[] args) {
        Connexion_DB_Nosql mongoFactory = new Connexion_DB_Nosql("nosql");
        MongoDatabase database = mongoFactory.createDatabase();
        MongoCollection<CharacterType> Charactercollection = database.getCollection("characters", CharacterType.class);

        Authen authen = new Authen();
        User_DAO daoUser = new User_DAO("mysql");
        DAO_NOSQL daoNosql = new DAO_NOSQL("nosql");

        try {
            // Ajouter un timestamp aux pseudos pour les rendre uniques
            String timestamp = String.valueOf(System.currentTimeMillis());
            User nouvelUser = new User("PSEUDOTEST_" + timestamp, "", "USER");
            User nouvelUser2 = new User("PSEUDOTEST2_" + timestamp, "", "USER");
            User nouvelUser3 = new User("PSEUDOTEST3_" + timestamp, "", "USER");

            // Tenter d'ajouter les utilisateurs
            if (daoUser.ajouterUser(nouvelUser)) {
                System.out.println("Utilisateur 1 ajouté avec succès");

                // Créer et ajouter le personnage seulement si l'utilisateur est créé
                Archer archerTest = new Archer("archerX");
                daoNosql.ajouterPersonnagePourUser("mysql", nouvelUser.getPseudo(), archerTest);
            }

            if (daoUser.ajouterUser(nouvelUser2)) {
                System.out.println("Utilisateur 2 ajouté avec succès");

                Knight knightTest = new Knight("knightX");
                daoNosql.ajouterPersonnagePourUser("mysql", nouvelUser2.getPseudo(), knightTest);
            }

            if (daoUser.ajouterUser(nouvelUser3)) {
                System.out.println("Utilisateur 3 ajouté avec succès");

                Orc orcTest = new Orc("orcX");
                daoNosql.ajouterPersonnagePourUser("mysql", nouvelUser3.getPseudo(), orcTest);
            }

            // Afficher les personnages
            List<CharacterType> characters = daoNosql.readAllCharacters();
            if (!characters.isEmpty()) {
                System.out.println("Liste des personnages :");
                characters.forEach(character ->
                        System.out.printf("- %s | HP: %d | DMG: %.1f%n",
                                character.getName(),
                                character.getHealth(),
                                character.getDamage())
                );
            } else {
                System.out.println("Aucun personnage trouvé");
            }

        } catch (Exception e) {
            System.err.println("Erreur : " + e.getMessage());
            e.printStackTrace();
        } finally {
            daoUser.fermerConnexion();
        }
    }
}