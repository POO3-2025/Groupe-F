package be.helha.labos.collection.Character;

import be.helha.labos.collection.Inventaire;

public class Orc extends CharacterType {

    public Orc(String name, int health, int damage, double dodge, double precision) {
        super();
        this.name = name;
        this.health = health;
        this.damage = damage;
        this.dodge = dodge;
        this.precision = precision;
        this.inventaire = new Inventaire();
        inventaire.insererDansLaBase();
    }

    @Override
    public String toString() {
        return "Orc{" +
                "name='" + name +
                ", health=" + health +
                ", damage=" + damage +
                ", dodge=" + dodge +
                ", precision=" + precision +
                '}';
    }
}