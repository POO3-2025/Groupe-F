package be.helha.labos.DB;

import be.helha.labos.DB.Configuration;
import be.helha.labos.DB.DbSQLFactory;
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
public class Connexion_DB implements DbSQLFactory {


    private final String dbKey;

    /**
     * Le nom de la DB avec laquelle on se connecte
     * @param dbKey
     */
    public Connexion_DB(String dbKey) {
        this.dbKey = dbKey;
    }

    @Override
    /**
     * Modele factory pour la connexion en DB
     */
    public Connection createConnection() {
        try {
            Gson gson = new Gson();
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            ConfigurationRoot root = gson.fromJson(reader, ConfigurationRoot.class);
            Configuration selectedDb = root.Databases.get(dbKey);

            if (selectedDb == null) {
                throw new RuntimeException("La configuration pour '" + dbKey + "' est introuvable !");
            }

            String url = "jdbc:" + selectedDb.DBType + "://"
                    + selectedDb.DBCredentials.HostName + ":"
                    + selectedDb.DBCredentials.port + "/"
                    + selectedDb.DBCredentials.DBName
                    + "?useSSL=false&serverTimezone=UTC";

            return DriverManager.getConnection(
                    url,
                    selectedDb.DBCredentials.UserName,
                    selectedDb.DBCredentials.Password
            );
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la connexion SQL : " + e.getMessage(), e);
        }
    }
}