package be.helha.labos.collection.Character;

import be.helha.labos.collection.Inventaire;
import com.mongodb.client.MongoDatabase;

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
     */
    public Orc(String name,MongoDatabase db)
    {
        super();
        this.name = name;
        this.title="Orc";
        money = 100.00;
        level = 1;
        this.health = 250;
        this.damage = 50;
        this.dodge = 0.0;
        this.precision = 0.5;
        this.inventaire = new Inventaire(db);
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