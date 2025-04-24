package be.helha.labos.collection.character;

import be.helha.labos.collection.Inventaire;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Random;

public class CharacterType {
    @JsonProperty("_id")
    protected ObjectId id;
    protected String name;
    protected int health;
    protected int damage;
    protected Inventaire inventaire;
    protected double dodge;
    protected double precision;

    public CharacterType(String name, int health, int damage, double dodge, double precision) {
        this.id = new ObjectId();
        this.name = name;
        this.health = health;
        this.damage = damage;
        this.dodge = dodge;
        this.precision = precision;
        this.inventaire = new Inventaire();

        // Création du document MongoDB
        Document characterDocument = new Document("_id", id)
                .append("name", name)
                .append("health", health)
                .append("damage", damage)
                .append("dodge", dodge)
                .append("precision", precision)
                .append("inventory_id", inventaire.getId());

        System.out.println("Personnage créé : " + characterDocument.toJson());
    }
    public CharacterType(){
    }

    public boolean attackHits()
    {
        Random random = new Random();
        double roll = random.nextDouble(); // Génère un nombre aléatoire entre 0.0 et 1.0
        return roll < (precision * (1 - dodge));
    }

    // Getters et setters
    public ObjectId getId()
    {
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

    public void setDamage(int damage) {
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