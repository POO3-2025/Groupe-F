package be.helha.labos.collection;

import be.helha.labos.DBNosql.Connexion_DB_Nosql;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class User {

    private int id;
    private String pseudo;
    private String password;
    private boolean actif;
    private String rôle;

    public User(String pseudo, String password,String role) {
        this.pseudo = pseudo;
        this.password = password;
        this.rôle = role;
        this.actif = true;
    }

    public User(int id ,String pseudo, String password,String role) {
        this.id = id;
        this.pseudo = pseudo;
        this.password = password;
        this.rôle = role;
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

    public String getRôle() {
        return rôle;
    }

    public void setRôle(String rôle) {
        this.rôle = rôle;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

}
