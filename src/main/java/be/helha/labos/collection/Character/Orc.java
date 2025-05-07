package be.helha.labos.collection.Character;

import be.helha.labos.collection.Inventaire;

/**
 * Classe étendue de Charactertype représentant un personnage de type Orc
 */
public class Orc extends CharacterType
    {
    /**
     * Constructeur par défaut
     **/
    public Orc()
    {
    }

    /**
     * Constructeur de la classe Orc
     *
     * @param name     le nom du personnage
     * @param health   la santé du personnage
     * @param damage   les dégâts du personnage
     * @param dodge    la capacité d'esquive du personnage
     * @param precision la précision du personnage
     */
    public Orc(String name, int health, int damage, double dodge, double precision)
    {
        super();
        this.name = name;
        this.health = health;
        this.title="Orc";
        this.damage = damage;
        this.dodge = dodge;
        this.precision = precision;
        this.inventaire = new Inventaire();
        inventaire.insererDansLaBase();
    }

    /**
     * Méthode toString pour afficher les informations de l'orc
     * @return une chaîne de caractères contenant les informations de l'orc
     */
    @Override
    public String toString()
    {
        return "Orc{" +
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