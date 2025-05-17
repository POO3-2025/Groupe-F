package be.helha.labos.collection.Item;


public class Bow extends Weapon {
    public enum BowMaterial {
        WOOD(12f, "Bois",1),
        CROSSBOW(80f, "Arbalète",5),
        ICE(36f, "Glace",15);

        private String material;
        private float damage;
        private int requiredLevel;

        BowMaterial(float damage, String material,int requiredLevel) {
            this.damage = damage;
            this.material = material;
            this.requiredLevel=requiredLevel;
        }

        public String getMaterial() {
            return material;
        }

        public float getDamage() {
            return damage;
        }
    }


    private BowMaterial material;

    public Bow(BowMaterial material) {
        super((int) material.getDamage(), WeaponType.BOW);
        this.material = material;
    }

    // Default constructor required for MongoDB
    public Bow() {
        super((int) BowMaterial.WOOD.getDamage(), WeaponType.BOW);
        this.material = BowMaterial.WOOD;
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

    public int getRequiredLevel() {
        return material.requiredLevel;
    }

    public void setRequiredLevel(int requiredLevel) {
        this.material.requiredLevel = requiredLevel;
    }

    @Override
    public String toString() {
        return String.format("%s (Matériau: %s, Dégâts: %d)",
                getName(),
                getMaterial(),
                getRequiredLevel(),
                getDamage());
    }
}