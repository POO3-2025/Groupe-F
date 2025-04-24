package be.helha.labos.collection.character;

import be.helha.labos.collection.Inventaire;

public class Knight extends CharacterType {

    public Knight(String name, int health, int damage, double dodge, double precision) {
        super();
        this.name = name;
        this.health = health;
        this.damage = damage;
        this.dodge = dodge;
        this.precision = precision;
        this.inventaire = new Inventaire();
    }

    @Override
    public String toString() {
        return "Knight{" +
                "name='" + name +
                ", health=" + health +
                ", damage=" + damage +
                ", dodge=" + dodge +
                ", precision=" + precision +
                '}';
    }
}