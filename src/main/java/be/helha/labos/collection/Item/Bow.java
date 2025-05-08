package be.helha.labos.collection.Item;

import org.bson.codecs.pojo.annotations.BsonProperty;

/**
 * Classe étendue de Weapon représentant un arc.
 */
public class Bow extends Weapon {
    /**
     * Enumération des différents matériaux des arcs.
     */
    public enum BowMaterial {
        WOOD(12f, "Bois", 1),
        CROSSBOW(18f, "Arbalète", 10),
        ICE(15f, "Glace", 15);

        private final String material;
        private final float damage;
        private final int level_Required;

        /**
         * Constructeur de l'énumération BowMaterial.
         *
         * @param damage          Dégâts infligés par l'arc.
         * @param material        Matériau de l'arc.
         * @param level_Required  Niveau requis pour utiliser l'arc.
         */
        BowMaterial(float damage, String material, int level_Required) {
            this.damage = damage;
            this.material = material;
            this.level_Required = level_Required;
        }
        /**
         * Méthode pour obtenir le matériau de l'arc.
         *
         * @return Matériau de l'arc.
         */
        public String getMaterial() {
            return this.material;
        }
        /**
         * Méthode pour obtenir les dégâts infligés par l'arc.
         *
         * @return Dégâts infligés par l'arc.
         */
        public float getDamage() {
            return this.damage;
        }
        /**
         * Méthode pour obtenir le niveau requis pour utiliser l'arc.
         *
         * @return Niveau requis pour utiliser l'arc.
         */
        public int getLevel_Required() {
            return this.level_Required;
        }
    }

    private BowMaterial material;

    /**
     * Constructeur vide
     */
    public Bow() {
        super((int) BowMaterial.WOOD.getDamage(), WeaponType.BOW);
        this.material = BowMaterial.WOOD;
    }

    /**
     * Constructeur de la classe Bow.
     *
     * @param material Matériau de l'arc.
     */
    public Bow(BowMaterial material) {
        super((int) material.getDamage(), WeaponType.BOW);
        this.material = material;
    }

    /**
     * Méthode pour obtenir le nom du matériau de l'arc.
     * @return
     */
    @Override
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
     * Méthode toString pour afficher les informations de l'arc.
     *
     * @return Une chaîne de caractères contenant les informations de l'arc.
     */
    @Override
    public String toString() {
        return String.format("%s (Matériel: %s, Dégâts: %d, Niveau requis: %d)",
                weaponType.getName(),
                material.getMaterial(),
                getDamage(),
                material.getLevel_Required());
    }
}