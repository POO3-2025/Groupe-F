package be.helha.labos.collection.Item;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.bson.types.ObjectId;

public abstract class Weapon extends Item {
    protected int damage;
    protected WeaponType weaponType;
    protected String material;

    public Weapon()
    {
        super();
    }

    public Weapon(int damage, WeaponType weaponType)
    {
        this.damage = damage;
        this.weaponType = weaponType;
    }

    protected abstract String getMaterialName();
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