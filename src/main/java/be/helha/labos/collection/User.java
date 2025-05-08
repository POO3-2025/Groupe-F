package be.helha.labos.collection;

/**
 * Classe représentant un utilisateur dans le système.
 */
public class User {
    /**
     * Constructeur de la classe User.
     */
    private int id;
    private String pseudo;
    private String password;
    private boolean actif;
    private String rôle;

    /**
     * Constructeur de la classe User.
     * @param id Identifiant de l'utilisateur.
     * @param pseudo Nom d'utilisateur.
     * @param password Mot de passe de l'utilisateur.
     * @param role Rôle de l'utilisateur.
     * @param actif Indique si l'utilisateur est actif ou non.
     */
    public User(int id,String pseudo, String password,String role) {
        this.id=id;
        this.pseudo = pseudo;
        this.password = password;
        this.rôle = role;
        this.actif = true;
    }

    /**
     * Methode getteur pour obtenir l'identifiant de l'utilisateur.
     */
    public int getId() {
        return id;
    }

    /**
     * Methode setteur pour définir l'identifiant de l'utilisateur.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Methode getteur pour obtenir le pseudo de l'utilisateur.
     */
    public String getPseudo() {
        return pseudo;
    }
    /**
     * Methode setteur pour définir le pseudo de l'utilisateur.
     */
    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    /**
     * Methode getteur pour obtenir le mot de passe de l'utilisateur.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Methode setteur pour définir le mot de passe de l'utilisateur.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Methode getteur pour obtenir le rôle de l'utilisateur.
     */
    public String getRôle() {
        return rôle;
    }

    /**
     * Methode setteur pour définir le rôle de l'utilisateur.
     */
    public void setRôle(String rôle) {
        this.rôle = rôle;
    }

    /**
     * Methode getteur pour savoir si l'utilisateur est actif.
     */
    public boolean isActif() {
        return actif;
    }

    /**
     * Methode setteur pour définir si l'utilisateur est actif.
     */
    public void setActif(boolean actif) {
        this.actif = actif;
    }

}
