package be.helha.labos.collection.Item;

/**
 * Enumération des différents types d'armes dans le jeu.
 * Chaque type d'arme a un nom et un type de dégâts associés.
 */
public enum WeaponType {
    SWORD("Épée", "Tranchant"),
    BOW("Arc", "Perforant"),
    MACE("Masse", "Contondant");
    //AXE("Hache", "Tranchant");

    private final String name;
    private final String damageType;

    /**
     * Constructeur de l'énumération WeaponType.
     *
     * @param name       Nom de l'arme.
     * @param damageType Type de dégâts infligés par l'arme.
     */
    WeaponType(String name, String damageType) {
        this.name = name;
        this.damageType = damageType;
    }

    /**
     * Méthode pour obtenir le nom de l'arme.
     * @return Nom de l'arme.
     */
    public String getName() {
        return name;
    }
    /**
     * Méthode pour obtenir le type de dégâts infligés par l'arme.
     * @return Type de dégâts infligés par l'arme.
     */
    public String getDamageType() {
        return damageType;
    }
}