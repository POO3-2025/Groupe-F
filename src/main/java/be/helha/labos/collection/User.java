package be.helha.labos.collection;

import jakarta.persistence.*;

@Entity
@Table(name = "users") // nom de la table MySQL
/**
 * Classe User qui correspond aux champs dans la DB SQL
 */
public class User {

    /**
     * Variables de la classes
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String pseudo;

    @Column(nullable = false)

    private String password;
    private boolean actif;
    private String role;

    /**
     * Constructeur par défaut
     */
    public User() {
    }

    /**
     * Contructeur de base
     * @param pseudo // pseudo du user
     * @param password // password du user
     * @param role // role du user
     */
    public User(String pseudo, String password,String role) {
        this.pseudo = pseudo;
        this.password = password;
        this.role = role;
        this.actif = false;
    }

    /**
     * Constructeur pour les méthode GetByPseudo et ID dans le user_DAO
     * @param id // id du user
     * @param pseudo // pseudo du user
     * @param password // password du user
     * @param role // role du user
     * @param actif // Boolean qui permet de vérifier si le user est connecté ou non
     */
    public User(int id ,String pseudo, String password,String role, boolean actif) {
        this.id = id;
        this.pseudo = pseudo;
        this.password = password;
        this.role = role;
        this.actif = actif;
    }


    /**
     * Retourne l'identifiant de l'utilisateur.
     *
     * @return l'identifiant (id)
     */
    public int getId() {
        return id;
    }

    /**
     * Définit l'identifiant de l'utilisateur.
     *
     * @param id l'identifiant à définir
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Retourne le pseudo de l'utilisateur.
     *
     * @return le pseudo
     */
    public String getPseudo() {
        return pseudo;
    }

    /**
     * Définit le pseudo de l'utilisateur.
     *
     * @param pseudo le pseudo à définir
     */
    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    /**
     * Retourne le mot de passe de l'utilisateur.
     *
     * @return le mot de passe
     */
    public String getPassword() {
        return password;
    }

    /**
     * Définit le mot de passe de l'utilisateur.
     *
     * @param password le mot de passe à définir
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Retourne le rôle de l'utilisateur.
     *
     * @return le rôle
     */
    public String getRole() {
        return role;
    }

    /**
     * Définit le rôle de l'utilisateur.
     *
     * @param role le rôle à définir
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Indique si l'utilisateur est actif (connecté).
     *
     * @return true si actif, false sinon
     */
    public boolean isActif() {
        return actif;
    }

    /**
     * Définit l'état actif (connecté ou non) de l'utilisateur.
     *
     * @param actif true si l'utilisateur est actif, false sinon
     */
    public void setActif(boolean actif) {
        this.actif = actif;
    }

    @Override
    public String toString() {
        return
                "\n{id=" + id +
                "\n pseudo='" + pseudo +
                "\n rôle='" + role +
                "\n actif=" + actif + "}";
    }
}
