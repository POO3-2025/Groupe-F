package be.helha.labos.collection.Character;
import be.helha.labos.collection.Inventaire;

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
             * @param name
             * @param health
             * @param damage
             * @param dodge
             * @param precision
             */
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