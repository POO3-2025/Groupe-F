package be.helha.labos.collection.Item;

import org.bson.codecs.pojo.annotations.BsonProperty;
/**
 * Classe représentant une masse (Mace) dans le jeu.
 * La classe hérite de la classe Weapon.
 */
public class Mace extends Weapon {
    /**
     * Enumération des différents matériaux de la masse.
     */
    public enum MaceMaterial {
        WOOD(8f, "Bois", 1),
        STONE(12f, "Pierre", 5),
        DIAMOND(18f, "Diamant", 15);

        private final float damage;
        private final String material;
        private final int level_Required;

        /**
         * Constructeur de l'énumération MaceMaterial.
         *
         * @param damage          Dégâts infligés par la masse.
         * @param material        Matériau de la masse.
         * @param level_Required  Niveau requis pour utiliser la masse.
         */
        MaceMaterial(float damage, String material, int level_Required) {
            this.damage = damage;
            this.material = material;
            this.level_Required = level_Required;
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
     * @param damage   les dégâts de la masse
     * @param material le matériau de la masse
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
                material.getLevel_Required());
    }
}