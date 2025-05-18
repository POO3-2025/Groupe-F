package be.helha.labos.Authentification.Http;

public class UserHttp {

    private int id;
    private String username;
    private String role;

    /**
     * Constructeur par défaut
     */
    public UserHttp() {
    }

    /**
     * Constructeur de base
     * @param id ID du user
     * @param username Username du user
     * @param role role du user
     */
    public UserHttp(int id, String username, String role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }

    /**
     * Getter pour récupérer l'ID
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * Setter pour l'id
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter pour récupérer le username
     * @return
     */
    public String getUsername() {
        return username;
    }

    /**
     * Setter pour le username
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }
}
