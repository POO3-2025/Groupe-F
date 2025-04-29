package be.helha.labos.collection.Character;

import be.helha.labos.collection.Inventaire;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.Random;

public class characters {
    protected ObjectId id;
    protected String name;
    protected int health;
    protected int damage;
    protected Inventaire inventaire;
    protected double dodge; //esquive (entre 0.0 et 1.0)
    protected double precision; //esquive (entre 0.0 et 1.0)

    public characters(String name, int health, int damage, double dodge, double precision) throws IOException {
        this.name = name;
        this.health = health;
        this.damage = damage;
        this.dodge = dodge;
        this.precision=precision;
        this.inventaire = new Inventaire();

        // Création du document pour le personnage
        Document characterDocument = new Document("name", name)
                .append("health", health)
                .append("damage", damage)
                .append("dodge", dodge)
                .append("precision", precision)
                .append("inventory_id", inventaire.getId()); //ref à l'inventaire par _id

        System.out.println("Personnage créé : " + characterDocument.toJson());
    }

    public characters()
    {
        //default
        this.name = "Character Name";
        this.health = 100;
        this.damage = 10;
        this.dodge = 0.3;
        this.precision = 0.5;
        this.inventaire = new Inventaire();

    }

    public boolean attackHits()
    {
        Random random = new Random();
        double roll = random.nextDouble(); // Génère un nombre aléatoire entre 0.0 et 1.0
        return roll < (precision * (1 - dodge));
    }

    // Getters et setters
    public ObjectId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamageAmount(int damage) {
        this.damage = damage;
    }

    public double getDodgeChance() {
        return dodge;
    }

    public void setDodgeChance(double dodge) {
        this.dodge = dodge;
    }

    public Inventaire getInventaire() {
        return inventaire;
    }

    public String toString() {
        return "Character{" +
                "name='" + name + '\'' +
                ", health=" + health +
                ", damage=" + damage +
                ", dodge=" + dodge +
                ", precision=" + precision +
                '}';
    }
}