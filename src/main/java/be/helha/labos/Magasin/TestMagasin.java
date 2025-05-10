package be.helha.labos.Magasin;

import com.mongodb.client.MongoDatabase;
import be.helha.labos.DBNosql.Connexion_DB_Nosql;

import java.io.IOException;

public class TestMagasin {
    public static void main(String[] args){
        // Connexion à la base de données
        Connexion_DB_Nosql connexionDbNosql = Connexion_DB_Nosql.getInstance();
        MongoDatabase database = connexionDbNosql.getDatabase();

        try {
            // Générer des objets pour le magasin
            System.out.println("\nGénération de 10 objets pour le magasin :");
            Magasin.genererObjets(database.getCollection("Magasin"));

            // Afficher les objets disponibles dans le magasin
            System.out.println("\nAffichage des objets disponibles dans le magasin :");
            Magasin magasin = new Magasin(database);
            magasin.afficherObjetsDisponibles();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Fermer la connexion à la base de données
            connexionDbNosql.closeConnection();
        }
    }
}