package be.helha.labos.collection.Character;

import be.helha.labos.collection.Inventaire;

public class Knight extends CharacterType {

    public Knight(String name) {
        super();
        this.name = name;
        this.health = 150;
        this.title="Knight";
        this.damage = 50;
        money = 100.00;
        level = 1;
        this.dodge = 0.2;
        this.precision = 0.7;
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