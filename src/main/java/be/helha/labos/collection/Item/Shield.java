package be.helha.labos.collection.Item;

public class Shield extends Item {


    private int defense ;
    private String type;


    public Shield() {
        super();
    }

    public Shield(int Shield , int defense) {
        this();
        this.defense = defense;
        this.type = "Shield";
    }

    public int getDefense() {
        return defense;
    }
    public void setDefense(int defense) {
        this.defense = defense;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
}