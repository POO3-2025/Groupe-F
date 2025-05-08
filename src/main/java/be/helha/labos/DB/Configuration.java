package be.helha.labos.DB;


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

    /**
     * Methode getteur pour obtenir le type de connexion.
     */
    public String getConnectionType() {
        return ConnectionType;
    }
    /**
     * Methode getteur pour obtenir le type de base de données.
     */
    public String getDBType() {
        return DBType;
    }

    /**
     * Methode getteur pour obtenir les informations d'identification de la base de données.
     */
    public Credentials getDBCredentials() {
        return DBCredentials;
    }
}
