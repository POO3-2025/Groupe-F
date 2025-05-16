package be.helha.labos.Bot;

import be.helha.labos.collection.Character.CharacterType;

import java.util.HashMap;
import java.util.Map;

/**
 * Classe Bot qui gère les interactions avec le bot dans le jeu.
 * Elle contient des méthodes pour simuler des actions du bot.
 */
public class Bot {

    /**
     * Map qui contient le title du perso en clé et les dégâts associé à cette clé
     */
    private final Map<String, Integer> baseDamageByTitle = new HashMap<>();

    /**
     * Contructeur du Bot
     */
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


        int baseDamage = baseDamageByTitle.getOrDefault(bot.getTitle(), 10);

        // Dégats selon le niveau
        int levelBonus = switch (bot.getLevel()) {
            case 1 -> 0;
            case 2 -> 20;
            case 3 -> 35;
            case 4 -> 45;
            case 5 -> 60;
            case 6 -> 70;
            case 7 -> 90;
            case 8 -> 100;
            case 9 -> 115;
            case 10 -> 130;
            default -> 180;
        };

        return baseDamage + levelBonus;
    }
}