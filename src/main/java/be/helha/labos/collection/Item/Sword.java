package be.helha.labos.collection.Item;

import org.bson.codecs.pojo.annotations.BsonProperty;
/**
 * Classe représentant une épée (Sword) dans le jeu.
 * La classe hérite de la classe Weapon.
 */
public class Sword extends Weapon {
    /**
     * Enumération des différents matériaux de l'épée.
     */
    public enum SwordMaterial {
        GOLD(15f, "Or", 5),
        SILVER(13f, "Argent", 3),
        FIRE(20f, "Feu", 10),
        STEEL(10f, "Acier", 1);

        private final String material;
        private final float damage;
        private final int level_Required;

        /**
         * Constructeur de l'énumération SwordMaterial.
         *
         * @param damage          Dégâts infligés par l'épée.
         * @param material        Matériau de l'épée.
         * @param level_Required  Niveau requis pour utiliser l'épée.
         */
        SwordMaterial(float damage, String material, int level_Required) {
            this.damage = damage;
            this.material = material;
            this.level_Required = level_Required;
        }

        /**
         * Méthode pour obtenir le matériau de l'épée.
         *
         * @return Matériau de l'épée.
         */
        public String getMaterial() {
            return material;
        }
        /**
         * Méthode pour obtenir les dégâts infligés par l'épée.
         *
         * @return Dégâts infligés par l'épée.
         */
        public float getDamage() {
            return damage;
        }
        /**
         * Méthode pour obtenir le niveau requis pour utiliser l'épée.
         *
         * @return Niveau requis pour utiliser l'épée.
         */
        public int getLevel_Required() {
            return level_Required;
        }
    }

    private SwordMaterial material;

    /**
     * Constructeur vide
     */
    public Sword() {
        super((int) SwordMaterial.STEEL.getDamage(), WeaponType.SWORD);
        this.material = SwordMaterial.STEEL;
    }

    /**
     * Constructeur de la classe Sword.
     *
     * @param material Matériau de l'épée.
     */
    public Sword(SwordMaterial material) {
        super((int) material.getDamage(), WeaponType.SWORD);
        this.material = material;
    }

    /**
     * Méthode pour obtenir le nom du matériau de l'épée.
     * return @material.getMaterial()
     */
    @Override
    protected String getMaterialName() {
        return material.getMaterial();
    }

    /**
     * Méthode pour obtenir le matériau de l'épée.
     *
     * @return Matériau de l'épée.
     */
    public SwordMaterial getMaterial() {
        return material;
    }

    /**
     * Méthode pour définir le matériau de l'épée.
     *
     * @param material Matériau de l'épée.
     */
    public void setMaterial(SwordMaterial material) {
        this.material = material;
        setDamage((int) material.getDamage());
    }

    /**
     * Méthode pour obtenir une représentation sous forme de chaîne de caractères de l'épée.
     *
     * @return Représentation sous forme de chaîne de caractères de l'épée.
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