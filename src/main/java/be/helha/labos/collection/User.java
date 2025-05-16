package be.helha.labos.collection;

import jakarta.persistence.*;

@Entity
@Table(name = "users") // nom de la table MySQL
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String pseudo;

    @Column(nullable = false)

    private String password;
    private boolean actif;
    private String role;

    public User() {
    }

    public User(String pseudo, String password,String role) {
        this.pseudo = pseudo;
        this.password = password;
        this.role = role;
        this.actif = true;
    }

    public User(int id ,String pseudo, String password,String role) {
        this.id = id;
        this.pseudo = pseudo;
        this.password = password;
        this.role = role;
        this.actif = true;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isActif() {
        return actif;
    }

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
