package be.helha.labos.collection.Item;

public class Sword extends Weapon {
    public enum SwordMaterial {
        STEEL(10f, "Bronze", 1),
        SILVER(20f, "Fer", 5),
        GOLD(50f, "Or", 10),
        FIRE(100f, "Feu", 15);

        private String material;
        private float damage;
        private int requiredLevel;

        SwordMaterial(float damage, String material, int requiredLevel) {
            this.damage = damage;
            this.material = material;
            this.requiredLevel = requiredLevel;
        }

        public String getMaterial() {
            return material;
        }

        public float getDamage() {
            return damage;
        }

        public int getRequiredLevel() {
            return requiredLevel;
        }
    }

    private SwordMaterial material;

    // Default constructor required for MongoDB
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

    public int getRequiredLevel() {
        return material.getRequiredLevel();
    }

    public void setRequiredLevel(int requiredLevel) {
        this.material.requiredLevel = requiredLevel;
    }

    @Override
    public String toString() {
        return String.format("%s (Matériau: %s, Dégâts: %d, Niveau requis: %d)",
                weaponType.getName(),
                material.getMaterial(),
                getDamage(),
                getRequiredLevel());
    }
}