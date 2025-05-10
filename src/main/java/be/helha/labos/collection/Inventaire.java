package be.helha.labos.collection;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.bson.types.ObjectId;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe représentant un inventaire dans le jeu.
 * Elle contient des informations sur l'inventaire, telles que son ID et les slots qu'il contient.
 */
public class Inventaire {

    @JsonProperty("_id")
    protected ObjectId id;
    private List<Document> inventorySlots;

    /**
     * Constructeur vide pour un nouvel inventaire (10 slots vides)
     */
    public Inventaire() {
        this.id = new ObjectId();
        this.inventorySlots = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Document slot = new Document("slot_number", i + 1).append("item", null);
            inventorySlots.add(slot);
        }
    }

    public Inventaire(ObjectId id, List<Document> slots) {
        this.id = id;
        this.inventorySlots = slots;
    }

    public ObjectId getId() {
        return id;
    }

    public List<Document> getInventorySlots() {
        return inventorySlots;
    }

    public void setInventorySlots(List<Document> inventorySlots) {
        this.inventorySlots = inventorySlots;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Inventaire{" +
                "id=" + id +
                ", inventorySlots=" + inventorySlots +
                '}';
    }
}
