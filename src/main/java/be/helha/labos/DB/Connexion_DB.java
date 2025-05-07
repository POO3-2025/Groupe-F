package be.helha.labos.DB;

import be.helha.labos.DB.Configuration;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe responsable de la gestion de la connexion à la base de données.
 * Utilise un fichier de configuration JSON pour initialiser les paramètres de connexion.
 * Implémente le design pattern Singleton pour garantir une instance unique.
 */
public class Connexion_DB {

    private static Connexion_DB instance;
    private Connection connection;
    private static final String CONFIG_FILE = "config.json"; // Chemin relatif dans src/main/resources

    /**
     * Constructeur privé qui initialise la connexion à la base de données.
     * Lit les paramètres de connexion depuis un fichier de configuration JSON.
     *
     * @throws RuntimeException Si le fichier de configuration est introuvable ou si une erreur survient lors de la connexion.
     */
    protected Connexion_DB(String dbKey) {
        try {
            Gson gson = new Gson();

            // Chargement du fichier depuis le classpath
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE);
            if (inputStream == null) {
                throw new RuntimeException("Le fichier de configuration " + CONFIG_FILE + " est introuvable dans le classpath.");
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            ConfigurationRoot root = gson.fromJson(reader, ConfigurationRoot.class);
            Configuration selectedDb = root.Databases.get(dbKey);

            System.out.println(new Gson().toJson(selectedDb)); // Pour afficher ce que Gson a bien lu
            System.out.println("Clé demandée : " + dbKey);
            System.out.println("Clés disponibles : " + root.Databases.keySet());

            reader.close();
            if (selectedDb == null) {
                throw new RuntimeException("La configuration pour '" + dbKey + "' est introuvable dans config.json !");
            }

            String url = "jdbc:" + selectedDb.DBType + "://"
                    + selectedDb.DBCredentials.HostName + ":"
                    + selectedDb.DBCredentials.port + "/"
                    + selectedDb.DBCredentials.DBName
                    + "?useSSL=false&serverTimezone=UTC";

            // Connexion à la base de données MySQL
            connection = DriverManager.getConnection(
                    url,
                    selectedDb.DBCredentials.UserName,
                    selectedDb.DBCredentials.Password
            );

            System.out.println("Connexion OK");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de l'initialisation de la connexion à la base de données", e);
        }
    }

    /**
     * Retourne l'instance unique de la classe (Singleton).
     * Si l'instance n'existe pas, elle est créée.
     *
     * @return L'instance unique de ConnexionDB_DAO.
     */
    public static synchronized Connexion_DB getInstance(String dbKey) {
        if (instance == null) {
            instance = new Connexion_DB(dbKey);
        }
        return instance;
    }


    /**
     * Retourne l'objet Connection représentant la connexion à la base de données.
     *
     * @return L'objet Connection actuel.
     */
    public Connection getConnection() {
        return connection;
    }
}