package be.helha.labos.collection.Item;

import org.bson.codecs.pojo.annotations.BsonProperty;

public class Bow extends Weapon {
    public enum BowMaterial {
        WOOD(12f, "Bois", 1),
        CROSSBOW(18f, "Arbalète", 10),
        ICE(15f, "Glace", 15);

        private final String material;
        private final float damage;
        private final int level_Required;

        BowMaterial(float damage, String material, int level_Required) {
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

    private BowMaterial material;

    // Constructeur par défaut requis pour MongoDB
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
        return String.format("%s (Matériel: %s, Dégâts: %d, Niveau requis: %d)",
                weaponType.getName(),
                material.getMaterial(),
                getDamage(),
                material.getLevel_Required());
    }
}