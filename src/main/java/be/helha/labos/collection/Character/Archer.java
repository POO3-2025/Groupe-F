package be.helha.labos.collection.Character;
import be.helha.labos.collection.Inventaire;

public class Archer extends CharacterType

        {
            public Archer(){
            }

            /**
             * Constructeur pour le character de classe Archer
             *
             * @param name
             */
            public Archer(String name)
            {
                //default archer
                super();
                this.name = name;
                this.title="Archer";
                this.health = 100;
                this.damage = 5;
                money = 100.00;
                level = 1;
                this.dodge = 0.5;
                this.precision = 0.9;
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