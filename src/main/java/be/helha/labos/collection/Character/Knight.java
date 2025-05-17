package be.helha.labos.collection.Character;

import be.helha.labos.collection.Item.WeaponType;
import be.helha.labos.collection.Inventaire;
import com.mongodb.client.MongoDatabase;

/**
 * Classe étendue de Charactertype représentant un personnage de type Knight
 */
public class Knight extends CharacterType {

    /**
     * Constructeur par défaut
     */
    public Knight() {
    }

    /**
     * Constructeur de la classe Knight
     *
     * @param name     le nom du personnage
     */
    public Knight(String name, MongoDatabase db) {
        super();
        this.allowedWeaponType= WeaponType.SWORD;
        this.name = name;
        this.health = 150;
        this.maxHealth = 150;
        this.experience = 0;
        this.experienceToNextLevel = 100; // Ex. : 100 XP pour passer du niveau 1 au 2
        this.title="Knight";
        this.damage = 25;
        money = 100.00;
        level = 1;
        this.dodge = 0.3;
        this.precision = 0.7;
        this.inventaire = new Inventaire(db);
        inventaire.insererDansLaBase();

    }

    /**
     * Méthode toString pour afficher les informations du chevalier
     * @return une chaîne de caractères contenant les informations du chevalier
     */
    @Override
    public String toString() {
        return "Knight{" +
                "name='" + name + '\'' +
                ", health=" + health +
                ", title='" + title + '\'' +
                ", damage=" + damage +
                ", money=" + money +
                ", user=" + idUser +
                ", dodge=" + dodge +
                ", precision=" + precision +
                '}';
    }
}