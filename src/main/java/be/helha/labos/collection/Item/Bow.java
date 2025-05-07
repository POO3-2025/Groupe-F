package be.helha.labos.collection.Item;

import be.helha.labos.collection.Item.Weapon;
import be.helha.labos.collection.Item.WeaponType;
import org.bson.codecs.pojo.annotations.BsonProperty;


public class Bow extends Weapon {
    public enum BowMaterial {
        WOOD(12f, "Bois"),
        CROSSBOW(18f, "Arbalète"),
        ICE(15f, "Glace");

        private final String material;
        private final float damage;

        BowMaterial(float damage, String material) {
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


    private BowMaterial material;

    // Default constructor required for MongoDB
    public Bow() {
        super((int) BowMaterial.WOOD.getDamage(), WeaponType.BOW);
        this.material = BowMaterial.WOOD;
    }


    public Bow(BowMaterial material) {
        super((int) material.getDamage(), WeaponType.BOW);
        this.material = material;
    }

    @Override
    protected String getMaterialName() {
        return material.getMaterial();
    }

    public BowMaterial getMaterial() {
        return material;
    }

    public void setMaterial(BowMaterial material) {
        this.material = material;
        setDamage((int) material.getDamage());
    }

    @Override
    public String toString() {
        return String.format("%s (Matériau: %s, Dégâts: %d)",
                weaponType.getName(),
                material.getMaterial(),
                getDamage());
    }
}