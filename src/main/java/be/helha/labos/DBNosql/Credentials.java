package be.helha.labos.DBNosql;

/**
 * Classe représentant les informations d'identification nécessaires pour se connecter à la base de données.
 */
public class Credentials {

    /**
     * Nom d'hôte du serveur de la base de données.
     */
    public String HostName;

    /**
     * Nom d'utilisateur pour la connexion à la base de données.
     */
    public String UserName;

    /**
     * Mot de passe pour la connexion à la base de données.
     */
    public String Password;

    /**
     * Nom de la base de données.
     */
    public String DBName;

    /**
     * Port utilisé pour la connexion à la base de données.
     */
    public int port;

    // Getters
    public String getHost()
    { return HostName; }

    public int getPort()
    { return port; }

    public String getDatabase()
    { return DBName; }

    public String getUser()
    { return UserName; }

    public String getPassword()
    { return Password; }
}
