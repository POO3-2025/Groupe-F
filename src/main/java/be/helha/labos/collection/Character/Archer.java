package be.helha.labos.collection.Character;
import be.helha.labos.collection.Inventaire;

public class Archer extends CharacterType
        {
            public Archer(){

            }

            public Archer(String name, int health, int damage, double dodge, double precision)
            {
                //default archer
                super();
                this.name = name;
                this.title="Archer";
                this.health = health;
                this.damage = damage;
                this.dodge = dodge;
                this.precision = precision;
                this.inventaire = new Inventaire();
                inventaire.insererDansLaBase();
            }

            @Override
            public String toString() 
            {
                return "Archer{" +
                        "name='" + name  +
                        ", health=" + health +
                        ", damage=" + damage +
                        ", dodge=" + dodge +
                        ", precision=" + precision +
                        '}';
            }
        }