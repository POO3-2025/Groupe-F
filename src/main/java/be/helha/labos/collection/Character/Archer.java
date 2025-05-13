package be.helha.labos.collection.Character;

import be.helha.labos.collection.Inventaire;
import com.mongodb.client.MongoDatabase;

/**
 * Classe étendue de Charactertype représentant un personnage de type Archer
 */
public class Archer extends CharacterType
        {
            /**
             * Constructeur par défaut
             */
            public Archer(){
            }

            /**
             * Constructeur de la classe Archer
             */
            public Archer(String name, MongoDatabase db) {
                super();
                this.name = name;
                this.title = "Archer";
                this.health = 100;
                this.money = 100.00;
                this.level = 1;
                this.damage = 5;
                this.dodge = 0.5;
                this.precision = 0.9;
                this.inventaire = new Inventaire(db);
                inventaire.insererDansLaBase();
            }

            /**
             * Méthode toString pour afficher les informations de l'archer
             * @return une chaîne de caractères contenant les informations de l'archer
             */
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