package be.helha.labos.DBNosql;

import java.util.Map;

/**
 * Classe représentant la configuration des connexions à la base de données.
 * Elle contient une carte DBConfig, où chaque clé est le nom de la connexion.
 */
public class ConfigWrapper {

    /**
     * Map contenant les configurations de connexion à la base de données.
     * La clé est le nom de la connexion et la valeur est un objet DBConfig.
     */
    private Map<String, DBConfig> connections;

    /**
     * Constructeur par défaut.
     */
    public Map<String, DBConfig> getConnections() {
        return connections;
    }

    /**
     * Méthode pour définir les connexions à la base de données.
     *
     * @param connections Map contenant les configurations de connexion à la base de données.
     */
    public void setConnections(Map<String, DBConfig> connections) {
        this.connections = connections;
    }

    /**
     * Classe interne représentant la configuration d'une connexion à la base de données.
     * Elle contient le type de connexion, le type de base de données et les informations d'identification.
     */
    public static class DBConfig {
        private String ConnectionType;
        private String DBType;
        private Credentials DBCredentials;

        // Getters & Setters
        /**
         * Méthode pour obtenir le type de connexion.
         *
         * @return Le type de connexion.
         */
        public String getConnectionType() {
            return ConnectionType;
        }
        /**
         * Méthode pour set le type de connexion.
         *
         * @param connectionType Le type de connexion.
         */
        public void setConnectionType(String connectionType) {
            ConnectionType = connectionType;
        }
        /**
         * Méthode pour obtenir le type de base de données.
         *
         * @return Le type de base de données.
         */
        public String getDBType() {
            return DBType;
        }
        /**
         * Méthode pour set le type de base de données.
         *
         * @param DBType Le type de base de données.
         */
        public void setDBType(String DBType) {
            this.DBType = DBType;
        }
        /**
         * Méthode pour obtenir les informations d'identification de la base de données.
         *
         * @return Les informations d'identification de la base de données.
         */
        public Credentials getDBCredentials() {
            return DBCredentials;
        }
        /**
         * Méthode pour set les informations d'identification de la base de données.
         *
         * @param DBCredentials Les informations d'identification de la base de données.
         */
        public void setDBCredentials(Credentials DBCredentials) {
            this.DBCredentials = DBCredentials;
        }
    }
}
