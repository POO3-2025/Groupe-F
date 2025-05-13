package be.helha.labos.collection.Item;

public class Mace extends Weapon {
    public enum MaceMaterial {
        WOOD(8f, "Bois"),
        STONE(12f, "Pierre"),
        DIAMOND(18f, "Diamant");

        private final float damage;
        private final String material;

        MaceMaterial(float damage, String material) {
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

    private MaceMaterial material;

    // Default constructor needed for MongoDB deserialization
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

    // Getter required for MongoDB serialization
    public MaceMaterial getMaterial() {
        return material;
    }

    public void setMaterial(MaceMaterial material) {
        this.material = material;
        setDamage((int) material.getDamage());
    }

    @Override
    public String toString() {
        return String.format("%s (Matériau: %s, Dégâts: %d)",
                weaponType.getName(), material.getMaterial(), getDamage());
    }
}