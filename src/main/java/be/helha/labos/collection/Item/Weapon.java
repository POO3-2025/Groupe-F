package be.helha.labos.collection.Item;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.bson.types.ObjectId;

/**
 * Classe abstraite représentant une arme dans le jeu.
 * Elle hérite de la classe Item et contient des informations spécifiques à l'arme, telles que les dégâts et le type d'arme.
 */
public abstract class Weapon extends Item {
    /**
     * constructeur de la classe Weapon
     */
    protected int damage;
    protected WeaponType weaponType;
    protected String material;

    /**
     * Constructeur vide
     */
    public Weapon()
    {
        super();
    }

    /**
     * Constructeur de la classe Weapon.
     *
     * @param damage      Dégâts infligés par l'arme.
     * @param weaponType  Type de l'arme.
     */
    public Weapon(int damage, WeaponType weaponType)
    {
        this.damage = damage;
        this.weaponType = weaponType;
    }

    /**
     * Méthode abstraite pour obtenir le nom du matériau de l'arme.
     * @return Nom du matériau de l'arme.
     */
    protected abstract String getMaterialName();
    public void attack() {
        System.out.printf("Vous infligez %d dégâts avec %s en %s !", getDamage(), weaponType.getName(), getMaterialName()); // Méthode abstraite à implémenter
    }

    /**
     * Méthode pour obtenir les dégâts infligés par l'arme.
     * @return Dégâts infligés par l'arme.
     */
    public int getDamage() {
        return damage;
    }
    /**
     * Méthode pour obtenir le type d'arme.
     * @return Type d'arme.
     */
    public WeaponType getWeaponType() {
        return weaponType;
    }

    /**
     * Méthode pour définir les dégâts infligés par l'arme.
     * @param damage Dégâts infligés par l'arme.
     */
    public void setDamage(int damage)
    {
        if(damage < 0) {
            throw new IllegalArgumentException("Dégats négatifs");
        }
        this.damage = damage;
    }

    /**
     * Méthode pour définir le type d'arme.
     * @param weaponType Type d'arme.
     */
    public void setWeaponType(WeaponType weaponType)
    {
        this.weaponType = weaponType;
    }

    /**
     * methode toString retournant une chaine de caractère composée de
     * l'arme, de ses dégats et de son type
     */
    @Override
    public String toString() {
        return weaponType.getName() + " (Dégâts: " + damage + ", Type: " + weaponType.getDamageType() + ")";
    }
}