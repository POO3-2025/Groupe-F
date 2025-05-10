package be.helha.labos.collection.Item;

public enum WeaponType {
    SWORD("Épée", "Tranchant"),
    BOW("Arc", "Perforant"),
    MACE("Masse", "Contondant"),
    AXE("Hache", "Tranchant");

    private final String name;
    private final String damageType;


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