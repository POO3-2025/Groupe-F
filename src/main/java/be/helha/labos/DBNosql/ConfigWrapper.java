package be.helha.labos.DBNosql;

import java.util.Map;

public class ConfigWrapper {

    private Map<String, DBConfig> connections;

    public Map<String, DBConfig> getConnections() {
        return connections;
    }

    public void setConnections(Map<String, DBConfig> connections) {
        this.connections = connections;
    }

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
