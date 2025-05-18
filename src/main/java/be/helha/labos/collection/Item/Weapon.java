package be.helha.labos.collection.Item;

public abstract class Weapon extends Item {
    protected int damage;
    protected WeaponType weaponType;
    protected String material;

    public Weapon()
    {
        super();
    }

    /**
     * Constructeur de base
     * @param damage degâts de l'arme
     * @param weaponType Type de l'arme
     */
    public Weapon(int damage, WeaponType weaponType)
    {
        this.damage = damage;
        this.weaponType = weaponType;
    }

    protected abstract String getMaterialName();

    /**
     * Méthode d'attaque
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