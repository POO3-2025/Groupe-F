package be.helha.labos.collection.Character;

import be.helha.labos.collection.Inventaire;

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
    public Knight(String name) {
        super();
        this.name = name;
        this.health = 150;
        this.title="Knight";
        this.damage = 25;
        money = 100.00;
        level = 1;
        this.dodge = 0.3;
        this.precision = 0.7;

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