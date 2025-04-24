package be.helha.labos.DBNosql;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class Connexion_DB_Nosql {

    private static Connexion_DB_Nosql instance;  // Singleton
    private MongoClient mongoClient;
    private MongoDatabase database;

    // Constructeur privé pour empêcher l'instanciation directe
    private Connexion_DB_Nosql() {
        try {
            // Création du client MongoDB
            mongoClient = MongoClients.create("mongodb://localhost:27017");

            // Configuration du support POJO
            CodecRegistry pojoCodecRegistry = fromRegistries(
                    MongoClientSettings.getDefaultCodecRegistry(),
                    fromProviders(PojoCodecProvider.builder().automatic(true).build())
            );

            // Connexion à la base de données avec le support des POJOs
            database = mongoClient.getDatabase("TestDB").withCodecRegistry(pojoCodecRegistry);

            System.out.println("Connexion MongoDB établie !");
        } catch (Exception e) {
            System.out.println("Erreur lors de la connexion à MongoDB !");
            e.printStackTrace();
        }
    }

    // Méthode pour obtenir l'instance unique du Singleton
    public static synchronized Connexion_DB_Nosql getInstance() {
        if (instance == null) {
            instance = new Connexion_DB_Nosql();
        }
        return instance;
    }

    // Méthode pour récupérer la base de données
    public MongoDatabase getDatabase() {
        return database;
    }

    // Méthode pour fermer la connexion proprement
    public void closeConnection() {
        if (mongoClient != null) {
            mongoClient.close();
            System.out.println("Connexion MongoDB fermée.");
        }
    }
}
