package be.helha.labos.collection.Character;

import be.helha.labos.collection.Inventaire;

public class Knight extends CharacterType {

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