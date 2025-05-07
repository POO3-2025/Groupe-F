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
     * @param health   la santé du personnage
     * @param damage   les dégâts du personnage
     * @param dodge    la capacité d'esquive du personnage
     * @param precision la précision du personnage
     */
    public Knight(String name, int health, int damage, double dodge, double precision) {
        super();
        this.name = name;
        this.health = health;
        this.title="Knight";
        this.damage = damage;
        money = 100.00;
        level = 1;
        this.dodge = dodge;
        this.precision = precision;
        this.inventaire = new Inventaire();
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