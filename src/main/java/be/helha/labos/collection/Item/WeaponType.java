package be.helha.labos.collection.Item;

public enum WeaponType {
    /**
     * Enum qui donne le type d'arme
     */
    SWORD("Épée", "Tranchant"),
    BOW("Arc", "Perforant"),
    MACE("Masse", "Contondant"),
    AXE("Hache", "Tranchant"); // Pas utilisé

    private final String name;
    private final String damageType;


    /**
     * Contructeur de base
     * @param name Nom de l'arme
     * @param damageType Type de dégâts
     */
    WeaponType(String name, String damageType) {
        this.name = name;
        this.damageType = damageType;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getDamageType() {
        return damageType;
    }
}