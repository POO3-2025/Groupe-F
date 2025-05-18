package be.helha.labos.collection.Item;
/**
 * Classe représentant une potion dans le jeu.
 * La classe hérite de la classe Item.
 */
public class Potion extends Item {
    /**
     * Attributs de la classe Potion
     */
    private int maxContent ;
    private int actualContent ;
    private String type;

    /**
     * Constructeur par défaut
     */
    public Potion() {
        super();
    }
    public Potion(int maxContent, int actualContent) {
        /**
         * Constructeur de la classe Potion
         *
         * @param maxContent     le contenu maximum de la potion
         * @param actualContent  le contenu actuel de la potion
         */
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
