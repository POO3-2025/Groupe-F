package be.helha.labos.collection.Character;

import be.helha.labos.collection.Inventaire;

public class Orc extends CharacterType {
    public Orc() {
    }

    public Orc(String name, int health, int damage, double dodge, double precision) {
        super();
        this.name = name;
        this.health = health;
        this.title="Orc";
        this.damage = damage;
        this.dodge = dodge;
        this.level = 1;
        this.precision = precision;
        this.inventaire = new Inventaire();
        inventaire.insererDansLaBase();
    }

    @Override
    public String toString() {
        return "Orc{" +
                "name='" + name  +
                ", title='" + title +
                ", health=" + health +
                ", damage=" + damage +
                ", dodge=" + dodge +
                ", level=" + level +
                ", precision=" + precision +
                '}';
    }
}