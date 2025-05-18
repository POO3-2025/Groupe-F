package be.helha.labos.collection.Item;

/**
 * Classe représentant une masse (Mace) dans le jeu.
 * La classe hérite de la classe Weapon.
 */
public class Mace extends Weapon {

    /**
     * Enumération des différents matériaux de la masse.
     */
    public enum MaceMaterial {
        WOOD(35f, "Bois", 1,
                "Orc"),
        STONE(85f, "Pierre", 5,
                "Orc"),
        DIAMOND(130f, "Diamant", 15,
                "Orc"),;
        /**
         * Attributs de l'énumération MaceMaterial.
         */
        private float damage;
        private String material;
        private int level_Required;
        private String AllowedCharacterType;

        /**
         * Constructeur de l'énumération MaceMaterial.
         *
         * @param damage          Dégâts infligés par la masse.
         * @param material        Matériau de la masse.
         * @param level_Required  Niveau requis pour utiliser la masse.
         */




        MaceMaterial(float damage, String material, int level_Required, String AllowedCharacterType) {
            this.damage = damage;
            this.material = material;
            this.level_Required = level_Required;
            this.AllowedCharacterType = AllowedCharacterType;
        }

        /**
         * Méthode pour obtenir le matériau de la masse.
         *
         * @return Matériau de la masse.
         */
        public String getMaterial() {
            return this.material;
        }
        /**
         * Méthode pour obtenir les dégâts infligés par la masse.
         *
         * @return Dégâts infligés par la masse.
         */
        public float getDamage() {
            return this.damage;
        }
        /**
         * Méthode pour obtenir le niveau requis pour utiliser la masse.
         *
         * @return Niveau requis pour utiliser la masse.
         */


        public int getLevel_Required() {
            return this.level_Required;
        }
        /**
         * Méthode pour obtenir le type de personnage autorisé à utiliser la masse.
         *
         * @return Type de personnage autorisé à utiliser la masse.
         */
        public String getAllowedCharacterType() {
            return AllowedCharacterType;
        }
    }

    private MaceMaterial material;

    /**
     * Constructeur vide
     */
    public Mace() {
        super((int) MaceMaterial.WOOD.getDamage(), WeaponType.MACE);
        this.material = MaceMaterial.WOOD;
    }
    /**
     * Constructeur de la classe Mace
     *
     * @param material le matériau de la masse
     */
    public Mace(MaceMaterial material) {
        super((int) material.getDamage(), WeaponType.MACE);
        this.material = material;
    }
    /**
     * Constructeur de la classe Mace
     *
     */
    @Override
    protected String getMaterialName() {
        return material.getMaterial();
    }
    /**
     * Méthode pour obtenir le matériau de la masse.
     *
     * @return Matériau de la masse.
     */
    public MaceMaterial getMaterial() {
        return material;
    }

    /**
     * Méthode pour définir le matériau de la masse.
     *
     * @param material Matériau de la masse.
     */
    public void setMaterial(MaceMaterial material) {
        this.material = material;
        setDamage((int) material.getDamage());
    }
    public String getAllowedCharacterType() {
        return material.AllowedCharacterType;
    }
    /**
     * Méthode toString pour afficher les informations de la masse
     *
     * @return une chaîne de caractères contenant les informations de la masse
     */
    @Override
    public String toString() {
        return String.format("%s (Matériau: %s, Dégâts: %d, Niveau requis: %d)",
                weaponType.getName(),
                material.getMaterial(),
                getDamage(),
                material.getAllowedCharacterType(),
                material.getLevel_Required());
    }
}