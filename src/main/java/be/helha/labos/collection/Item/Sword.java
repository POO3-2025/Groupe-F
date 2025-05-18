package be.helha.labos.collection.Item;

/**
 * Classe étendue de Weapon représentant une épée dans le jeu.
 */
public class Sword extends Weapon {
    /**
     * Enumération des différents matériaux de l'épée.
     */
    public enum SwordMaterial {
        STEEL(10f, "Bronze", 1,
                "Knight"),
        SILVER(25f, "Fer", 5,
                "Knight"),
        GOLD(55f, "Or", 10,
                "Knight"),
        FIRE(111f, "Feu", 15,
                "Knight"),;

        /**
         * Attributs de l'énumération SwordMaterial.
         */
        private String material;
        private float damage;
        private int requiredLevel;
        private String AllowedCharacterType;

        /**
         * Constructeur de l'énumération SwordMaterial.
         *
         * @param damage          Dégâts infligés par l'épée.
         * @param material        Matériau de l'épée.
         * @param requiredLevel   Niveau requis pour utiliser l'épée.
         * @param allowedCharacterType Type de personnage autorisé à utiliser l'épée.
         */
        SwordMaterial(float damage, String material, int requiredLevel, String allowedCharacterType) {
            this.damage = damage;
            this.material = material;
            this.requiredLevel = requiredLevel;
            this.AllowedCharacterType = allowedCharacterType;
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

    /**
     * Sous classe de Weapon représentant une épée.
     */
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
    public String getAllowedCharacterType() {
        return material.AllowedCharacterType;
    }

    public int getRequiredLevel() {
        return material.getRequiredLevel();
    }


    public void setRequiredLevel(int requiredLevel) {
        this.material.requiredLevel = requiredLevel;
    }

    @Override
    public String toString() {
        return String.format("%s (Matériau: %s, Dégâts: %d, Niveau requis: %d, Type autorisé: %s)",
                weaponType.getName(),
                material.getMaterial(),
                getDamage(),
                getRequiredLevel(),
                getAllowedCharacterType());
    }
}