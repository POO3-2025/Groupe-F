package be.helha.labos.collection.Item;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.bson.types.ObjectId;

public class Potion extends Item {

    private int maxContent ;
    private int actualContent ;
    private String type;


    public Potion() {
        super();
    }
    public Potion(int maxContent, int actualContent) {
        this.maxContent = maxContent;
        this.actualContent = actualContent;
        this.type = "Potion";
    }

    public int getMaxContent() {
        return maxContent;
    }

    public void setMaxContent(int maxContent) {
        this.maxContent = maxContent;
    }

    public int getActualContent() {
        return actualContent;
    }

    public void setActualContent(int actualContent) {
        this.actualContent = actualContent;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
