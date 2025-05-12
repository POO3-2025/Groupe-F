package be.helha.labos.DBNosql;

import java.util.Map;

/**
 * Classe de configuration pour les connexions à la base de données.
 */
public class ConfigWrapper {

    /**
     * Map contenant les configurations de connexion à la base de données.
     * La clé est le nom de la connexion et la valeur est un objet DBConfig contenant les détails de la connexion.
     */
    private Map<String, DBConfig> connections;
    /**
     * Constructeur de la classe ConfigWrapper.
     *
     * @return  connections Map contenant les configurations de connexion à la base de données.
     */
    public Map<String, DBConfig> getConnections() {
        return connections;
    }

    /**
     * Méthode pour définir les configurations de connexion à la base de données.
     *
     * @param connections Map contenant les configurations de connexion à la base de données.
     */
    public void setConnections(Map<String, DBConfig> connections) {
        this.connections = connections;
    }
    /**
     * Classe interne représentant la configuration de connexion à la base de données.
     */
    public static class DBConfig {
        private String ConnectionType;
        private String DBType;
        private Credentials DBCredentials;

        // Getters & Setters

        public String getConnectionType() {
            return ConnectionType;
        }

        public void setConnectionType(String connectionType) {
            ConnectionType = connectionType;
        }

        public String getDBType() {
            return DBType;
        }

        public void setDBType(String DBType) {
            this.DBType = DBType;
        }

        public Credentials getDBCredentials() {
            return DBCredentials;
        }

        public void setDBCredentials(Credentials DBCredentials) {
            this.DBCredentials = DBCredentials;
        }
    }
}