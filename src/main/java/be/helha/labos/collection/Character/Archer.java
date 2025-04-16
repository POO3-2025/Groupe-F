package be.helha.labos.collection.Character;

import be.helha.labos.collection.Inventaire;

public class Archer extends characters {

    public Archer()
    {
        super();
        //Archer default
        this.name = "Archer Name";
        this.health = 70;
        this.damage = 35;
        this.dodge = 0.8;
        this.precision = 0.667;
        this.inventaire = getInventaire();

    }
    @Override
    public String toString() {
        return "Archer{" +
                "name='" + name + '\'' +
                ", health=" + health +
                ", damage=" + damage +
                ", dodge=" + dodge +
                ", precision=" + precision +
                '}';
    }
}
