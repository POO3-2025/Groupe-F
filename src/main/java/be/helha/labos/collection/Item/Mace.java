package be.helha.labos.collection.Item;

import org.bson.codecs.pojo.annotations.BsonProperty;

public class Mace extends Weapon {
    public enum MaceMaterial {
        WOOD(8f, "Bois", 1),
        STONE(12f, "Pierre", 5),
        DIAMOND(18f, "Diamant", 15);

        private final float damage;
        private final String material;
        private final int level_Required;

        MaceMaterial(float damage, String material, int level_Required) {
            this.damage = damage;
            this.material = material;
            this.level_Required = level_Required;
        }

        public String getMaterial() {
            return this.material;
        }

        public float getDamage() {
            return this.damage;
        }

        public int getLevel_Required() {
            return this.level_Required;
        }
    }

    private MaceMaterial material;

    // Constructeur par défaut requis pour MongoDB
    public Mace() {
        super((int) MaceMaterial.WOOD.getDamage(), WeaponType.MACE);
        this.material = MaceMaterial.WOOD;
    }

    public Mace(MaceMaterial material) {
        super((int) material.getDamage(), WeaponType.MACE);
        this.material = material;
    }

    @Override
    protected String getMaterialName() {
        return material.getMaterial();
    }

    public MaceMaterial getMaterial() {
        return material;
    }

    public void setMaterial(MaceMaterial material) {
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