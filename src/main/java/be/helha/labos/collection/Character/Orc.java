package be.helha.labos.collection.Character;

import be.helha.labos.collection.Inventaire;

public class Orc extends CharacterType {

    public Orc(String name) {
        super();
        this.name = name;
        this.health = 250;
        this.title="Orc";
        this.damage = 80;
        money = 100.00;
        level = 1;
        this.dodge = 0.0;
        this.precision = 0.5;
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