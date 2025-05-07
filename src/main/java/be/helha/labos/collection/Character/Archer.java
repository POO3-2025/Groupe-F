package be.helha.labos.collection.Character;
import be.helha.labos.collection.Inventaire;

public class Archer extends CharacterType
        {
            /**
             * Default constructor needed for MongoDB
             */
            public Archer(){
            }

            public Archer(String name, int health, int damage, double dodge, double precision)
            {
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
                        "name='" + name + '\'' +
                        ", health=" + health +
                        ", title='" + title + '\'' +
                        ", damage=" + damage +
                        ", money=" + money +
                        ", user=" + idUser +
                        ", dodge=" + dodge +
                        ", precision=" + precision +
                        '}';
            }
        }