package be.helha.labos.DBNosql;

import be.helha.labos.DB.Connexion_DB;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
/**
 * Classe de connexion à une base de données NoSQL (MongoDB).
 * Utilise le design pattern Singleton pour garantir qu'une seule instance de la connexion existe.
 * La configuration est chargée depuis un fichier JSON.
 */
public class Connexion_DB_Nosql {
    /**
     * Instance unique de la classe Connexion_DB_Nosql (Singleton).
     */
    private static Connexion_DB_Nosql instance;  // Singleton
    private MongoClient mongoClient;
    private MongoDatabase database;

    CodecRegistry pojoCodecRegistry = fromRegistries(
            MongoClientSettings.getDefaultCodecRegistry(),
            fromProviders(PojoCodecProvider.builder().automatic(true).build())
    );
    /**
     * Chemin du fichier de configuration JSON.
     * Il doit être placé dans le répertoire src/main/resources.
     */
    private static final String CONFIG_FILE = "config.json"; // Chemin relatif dans src/main/resources
    /**
     * Méthode pour charger la configuration de la base de données à partir du fichier JSON.
     * @param key La clé de la configuration à charger.
     * @return Un objet DBConfig contenant les informations de connexion.
     */
    private ConfigWrapper.DBConfig chargerConfiguration(String key) {
        InputStream is = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE);
        InputStreamReader reader = new InputStreamReader(is);
        JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();

        // Accéder à l'objet "Databases" puis à la clé spécifique
        JsonObject databases = json.getAsJsonObject("Databases");
        JsonObject conf = databases.getAsJsonObject(key);

        if (conf == null) {
            throw new IllegalArgumentException("La configuration pour '" + key + "' est introuvable dans 'Databases'.");
        }
        return new Gson().fromJson(conf, ConfigWrapper.DBConfig.class);
    }


    /**
     * Constructeur privé pour empêcher l'instanciation directe
     * Il initialise la connexion à la base de données MongoDB en utilisant les informations de configuration.
     */
    private Connexion_DB_Nosql() {

        ConfigWrapper.DBConfig config = chargerConfiguration("nosqlTest");

        String uri = "mongodb://" + config.getDBCredentials().getHost() + ":27017";

        mongoClient = MongoClients.create(uri);
        database = mongoClient.getDatabase(config.getDBCredentials().getDatabase())
                .withCodecRegistry(pojoCodecRegistry);
    }

    /**
     * Méthode pour obtenir l'instance unique du Singleton
     * @return L'instance unique de Connexion_DB_Nosql.
     */
    public static synchronized Connexion_DB_Nosql getInstance(){
        if (instance == null) {
            instance = new Connexion_DB_Nosql();
        }
        return instance;
    }


    /**
     * Méthode pour récupérer la base de données
     * @return La base de données MongoDB.
     */
    public MongoDatabase getDatabase() {
        return database;
    }

    /**
     * Méthode pour fermer la connexion à la base de données MongoDB proprement.
     */
    public void closeConnection() {
        if (mongoClient != null) {
            mongoClient.close();
            System.out.println("Connexion MongoDB fermée.");
        }
    }
}
