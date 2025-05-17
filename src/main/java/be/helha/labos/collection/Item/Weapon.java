package be.helha.labos.collection.Item;
/**
 * Classe abstraite représentant une arme dans le jeu.
 * La classe est utilisée pour stocker les informations de l'arme dans la base de données.
 */
public abstract class Weapon extends Item {
    /**
     * Attributs de la classe Weapon
     */
    protected int damage;
    protected WeaponType weaponType;
    protected String material;

    public Weapon()
    {
        super();
    }
    /**
     * Constructeur de la classe Weapon
     *
     * @param damage      Dégâts infligés par l'arme.
     * @param weaponType  Type d'arme (épée, masse, etc.).
     */
    public Weapon(int damage, WeaponType weaponType)
    {
        this.damage = damage;
        this.weaponType = weaponType;
    }

    /**
     * Méthode abstraite pour obtenir le nom du matériau de l'arme.
     * La méthode doit être implémentée dans les sous-classes.
     *
     * @return Nom du matériau de l'arme.
     */
    protected abstract String getMaterialName();

    /**
     * Méthode abstraite pour attaquer avec l'arme.
     * La méthode doit être implémentée dans les sous-classes.
     */
    public void attack() {
        System.out.printf("Vous infligez %d dégâts avec %s en %s !", getDamage(), weaponType.getName(), getMaterialName()); // Méthode abstraite à implémenter
    }


    public int getDamage() {
        return damage;
    }

    public WeaponType getWeaponType() {
        return weaponType;
    }

    public void setDamage(int damage)
    {
        if(damage < 0) {
            throw new IllegalArgumentException("Dégats négatifs");
        }
        this.damage = damage;
    }

    public void setWeaponType(WeaponType weaponType)
    {
        this.weaponType = weaponType;
    }


    @Override
    public String toString() {
        return weaponType.getName() + " (Dégâts: " + damage + ", Type: " + weaponType.getDamageType() + ")";
    }
}