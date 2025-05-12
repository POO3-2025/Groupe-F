package be.helha.labos.DB;


import java.util.Map;

/**
 * Classe représentant la configuration nécessaire pour établir une connexion à la base de données.
 */
public class Configuration {

    /**
     * Type de connexion à utiliser.
     */
    public String ConnectionType;

    /**
     * Type de base de données.
     */
    public String DBType;

    /**
     * Informations d'identification pour la connexion à la base de données.
     */
    public Credentials DBCredentials;


    public String getConnectionType() {
        return ConnectionType;
    }

    public String getDBType() {
        return DBType;
    }

    public Credentials getDBCredentials() {
        return DBCredentials;
    }
}