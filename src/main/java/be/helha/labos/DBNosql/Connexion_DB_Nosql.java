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
public class Connexion_DB_Nosql implements Mongo{

    /**
     * Clé de configuration pour identifier la connexion dans le fichier de configuration.
     */
    private final String configKey;

    public Connexion_DB_Nosql(String configKey) {
        this.configKey = configKey;
    }

    /**
     * Crée une connexion à la base de données MongoDB.
     * Utilise le codec POJO pour la sérialisation et désérialisation des objets.
     *
     * @return Une instance de MongoDatabase.
     */
    @Override
    public MongoDatabase createDatabase() {
        ConfigWrapper.DBConfig config = chargerConfiguration(configKey);

        String uri = "mongodb://" + config.getDBCredentials().getHost() + ":27017";

        CodecRegistry pojoCodecRegistry = fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build())
        );

        MongoClient client = MongoClients.create(uri);
        return client.getDatabase(config.getDBCredentials().getDatabase())
                .withCodecRegistry(pojoCodecRegistry);
    }

    /**
     * Crée une connexion à la base de données SQL.
     * Utilise la classe Connexion_DB pour établir la connexion.
     *
     * @return Une instance de Connection.
     */
    private ConfigWrapper.DBConfig chargerConfiguration(String key) {
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream("config.json");
            InputStreamReader reader = new InputStreamReader(is);
            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
            JsonObject databases = json.getAsJsonObject("Databases");
            JsonObject conf = databases.getAsJsonObject(key);

            if (conf == null) {
                throw new IllegalArgumentException("La configuration pour '" + key + "' est introuvable.");
            }

            return new Gson().fromJson(conf, ConfigWrapper.DBConfig.class);
        } catch (Exception e) {
            throw new RuntimeException("Erreur de chargement de la configuration MongoDB", e);
        }
    }
}