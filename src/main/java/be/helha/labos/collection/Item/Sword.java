package be.helha.labos.collection.Item;

import be.helha.labos.collection.Item.Weapon;
import be.helha.labos.collection.Item.WeaponType;
import org.bson.codecs.pojo.annotations.BsonProperty;


public class Sword extends Weapon {
    public enum SwordMaterial {
        GOLD(15f, "Or"),
        SILVER(13f, "Argent"),
        FIRE(20f, "Feu"),
        STEEL(10f, "Acier");

        private final String material;
        private final float damage;

        SwordMaterial(float damage, String material) {
            this.damage = damage;
            this.material = material;
        }

        public String getMaterial() {
            return material;
        }

        public float getDamage() {
            return damage;
        }
    }

    private SwordMaterial material;

    // Default constructor needed for MongoDB
    public Sword() {
        super((int) SwordMaterial.STEEL.getDamage(), WeaponType.SWORD);
        this.material = SwordMaterial.STEEL;
        super.material = SwordMaterial.STEEL.material;
    }

    public Sword(SwordMaterial material) {
        super((int) material.getDamage(), WeaponType.SWORD);
        this.material = material;
        super.material = material.getMaterial();
    }

    @Override
    protected String getMaterialName() {
        return material.getMaterial();
    }

    public SwordMaterial getMaterial() {
        return material;
    }

    public void setMaterial(SwordMaterial material) {
        this.material = material;
        super.material = material.getMaterial();
        setDamage((int) material.getDamage());
    }

    @Override
    public String toString() {
        return String.format("%s (Matériau: %s, Dégâts: %d)",
                weaponType.getName(), material.getMaterial(), getDamage());
    }
}