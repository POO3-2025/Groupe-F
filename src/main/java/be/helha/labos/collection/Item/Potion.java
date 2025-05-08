package be.helha.labos.collection.Item;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.bson.types.ObjectId;
/**
 * Classe représentant une potion dans le jeu.
 * Elle hérite de la classe Item et contient des informations spécifiques à la potion, telles que sa capacité maximale et son contenu actuel.
 */
public class Potion extends Item {

    private int maxContent ;
    private int actualContent ;
    private String type;

    /**
     * Enumération des différents types de potions. (potion de poison à rajouter)
     */
    public Potion() {
        super();
    }
    /**
     * Constructeur de la classe Potion.
     *
     * @param maxContent    Capacité maximale de la potion.
     * @param actualContent Contenu actuel de la potion.
     */
    public Potion(int maxContent, int actualContent) {
        this.maxContent = maxContent;
        this.actualContent = actualContent;
        this.type = "Potion";
    }

    /**
     * Méthode pour obtenir la capacité maximale de la potion.
     * @return
     */
    public int getMaxContent() {
        return maxContent;
    }
    /**
     * Méthode pour définir la capacité maximale de la potion.
     * @param maxContent Capacité maximale de la potion.
     */
    public void setMaxContent(int maxContent) {
        this.maxContent = maxContent;
    }
    /**
     * Méthode pour obtenir le contenu actuel de la potion.
     * @return
     */
    public int getActualContent() {
        return actualContent;
    }

    /**
     * Méthode pour définir le contenu actuel de la potion.
     * @param actualContent Contenu actuel de la potion.
     */
    public void setActualContent(int actualContent) {
        this.actualContent = actualContent;
    }

    /**
     * Méthode pour obtenir le type de la potion.
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     * Méthode pour définir le type de la potion.
     * @param type Type de la potion.
     */
    public void setType(String type) {
        this.type = type;
    }

}
