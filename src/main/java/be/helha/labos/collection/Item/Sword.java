package be.helha.labos.collection.Item;

import org.bson.codecs.pojo.annotations.BsonProperty;

public class Sword extends Weapon {
    public enum SwordMaterial {
        GOLD(15f, "Or", 5),
        SILVER(13f, "Argent", 3),
        FIRE(20f, "Feu", 10),
        STEEL(10f, "Acier", 1);

        private final String material;
        private final float damage;
        private final int level_Required;

        SwordMaterial(float damage, String material, int level_Required) {
            this.damage = damage;
            this.material = material;
            this.level_Required = level_Required;
        }

        public String getMaterial() {
            return material;
        }

        public float getDamage() {
            return damage;
        }

        public int getLevel_Required() {
            return level_Required;
        }
    }

    private SwordMaterial material;

    // Constructeur par défaut requis pour MongoDB
    public Sword() {
        super((int) SwordMaterial.STEEL.getDamage(), WeaponType.SWORD);
        this.material = SwordMaterial.STEEL;
    }

    public Sword(SwordMaterial material) {
        super((int) material.getDamage(), WeaponType.SWORD);
        this.material = material;
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
        setDamage((int) material.getDamage());
    }

    @Override
    public String toString() {
        return String.format("%s (Matériau: %s, Dégâts: %d, Niveau requis: %d)",
                weaponType.getName(),
                material.getMaterial(),
                getDamage(),
                material.getLevel_Required());
    }
}