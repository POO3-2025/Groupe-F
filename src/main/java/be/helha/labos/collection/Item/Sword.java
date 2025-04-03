package be.helha.labos.collection.Item;


import be.helha.labos.DBNosql.MongoDB;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.bson.types.ObjectId;


public class Sword extends Item {

    private int damage ;
    private String type;

    public Sword() {
        super();
    }

    public Sword(int damage) {
        this.damage = damage;
        this.type = "Sword";
    }

    public int getDamage() {
        return damage;
    }
    public void setDamage(int damage) {
        this.damage = damage;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
}
