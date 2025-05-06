package be.helha.labos.collection.Character;

import be.helha.labos.collection.Inventaire;

public class Knight extends CharacterType {

    public Knight() {
    }

    public Knight(String name, int health, int damage, double dodge, double precision) {
        super();
        this.name = name;
        this.health = health;
        this.title="Knight";
        this.damage = damage;
        this.dodge = dodge;
        this.precision = precision;
        this.inventaire = new Inventaire();
        inventaire.insererDansLaBase();
    }

    @Override
    public String toString() {
        return "Knight{" +
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