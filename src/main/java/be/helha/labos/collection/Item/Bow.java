package be.helha.labos.collection.Item;

/**
 * Classe représentant un arc dans le jeu.
 * La classe hérite de la classe Weapon.
 */
public class Bow extends Weapon {
    /**
     * Enumération des différents matériaux de l'arc.
     */
    public enum BowMaterial {
        WOOD(20f, "Bois",1,
                "Archer"),
        CROSSBOW(55f, "Arbalète",5,
                "Archer"),
        ICE(100f, "Glace",15,"Archer"),;

        /**
         * attributs de l'énumération BowMaterial.
         */
        private String material;
        private float damage;
        private int requiredLevel;
        private String AllowedCharacterType;

        /**
         * Constructeur de l'énumération BowMaterial.
         *
         * @param damage          Dégâts infligés par l'arc.
         * @param material        Matériau de l'arc.
         * @param requiredLevel   Niveau requis pour utiliser l'arc.
         * @param allowedCharacterType Type de personnage autorisé à utiliser l'arc.
         */
        BowMaterial(float damage, String material,int requiredLevel,String allowedCharacterType) {
            this.damage = damage;
            this.material = material;
            this.requiredLevel=requiredLevel;
            this.AllowedCharacterType=allowedCharacterType;
        }

        /**
         * Méthode pour obtenir le matériau de l'arc.
         *
         * @return Matériau de l'arc.
         */
        public String getMaterial() {
            return material;
        }

        /**
         * Méthode pour obtenir les dégâts infligés par l'arc.
         *
         * @return Dégâts infligés par l'arc.
         */
        public float getDamage() {
            return damage;
        }
    }



    private BowMaterial material;

    /**
     * Constructeur de la classe Bow.
     *
     * @param material Matériau de l'arc.
     */
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
    /**
     * Méthode pour obtenir le nom du matériau de l'arc.
     *
     * @return Nom du matériau de l'arc.
     */
    protected String getMaterialName() {
        return material.getMaterial();
    }

    /**
     * Méthode pour obtenir le matériau de l'arc.
     *
     * @return Matériau de l'arc.
     */
    public BowMaterial getMaterial() {
        return material;
    }

    /**
     * Méthode pour définir le matériau de l'arc.
     *
     * @param material Matériau de l'arc.
     */
    public void setMaterial(BowMaterial material) {
        this.material = material;
        setDamage((int) material.getDamage());
    }

    /**
     * Méthode pour obtenir les dégâts infligés par l'arc.
     *
     * @return Dégâts infligés par l'arc.
     */
    public int getRequiredLevel() {
        return material.requiredLevel;
    }

    /**
     * Méthode pour définir le niveau requis pour utiliser l'arc.
     *
     * @param requiredLevel Niveau requis pour utiliser l'arc.
     */
    public void setRequiredLevel(int requiredLevel) {
        this.material.requiredLevel = requiredLevel;
    }

    /**
     * Méthode pour obtenir le type de personnage autorisé à utiliser l'arc.
     *
     * @return Type de personnage autorisé à utiliser l'arc.
     */
    public String getAllowedCharacterType() {
        return material.AllowedCharacterType;
    }

    /**
     * Méthode pour définir le type de personnage autorisé à utiliser l'arc.
     *
     * @param allowedCharacterType Type de personnage autorisé à utiliser l'arc.
     */
    public void setAllowedCharacterType(String allowedCharacterType) {
        this.material.AllowedCharacterType = allowedCharacterType;
    }

    /**
     * Méthode toString pour afficher les informations de l'arc.
     *
     * @return Une chaîne de caractères contenant les informations de l'arc.
     */
    @Override
    public String toString() {
        return String.format("%s (Matériau: %s, Dégâts: %d)",
                getName(),
                getMaterial(),
                getAllowedCharacterType(),
                getRequiredLevel(),
                getDamage());
    }
}