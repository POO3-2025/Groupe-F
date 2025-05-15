package be.helha.labos.Bot;

import be.helha.labos.collection.Character.CharacterType;

import java.util.HashMap;
import java.util.Map;

/**
 * Classe Bot qui gère les interactions avec le bot dans le jeu.
 * Elle contient des méthodes pour simuler des actions du bot.
 */
public class Bot {

    private final Map<String, Integer> baseDamageByTitle = new HashMap<>();

    public Bot() {
        // Initialisation des dégâts de base par type de bot
        baseDamageByTitle.put("Orc", 40);
        baseDamageByTitle.put("Knight", 25);
    }

    /**
     * Méthode pour simuler une action du bot.
     * @param bot Le personnage du bot.
     * @return Les dégâts infligés par le bot.
     */
    public int jouerContreBot(CharacterType bot) {


        int baseDamage = baseDamageByTitle.getOrDefault(bot.getTitle(), 5);

        // Dégats selon le niveau
        int levelBonus = switch (bot.getLevel()) {
            case 1 -> 0;
            case 2 -> 10;
            case 3 -> 20;
            default -> 30;
        };

        return baseDamage + levelBonus;
    }
}