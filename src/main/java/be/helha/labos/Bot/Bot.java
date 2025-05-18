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

        switch (bot.getTitle()){
            case "Knight":
                // Dégats selon le niveau
                int levelBonus = switch (bot.getLevel()) {
                    case 1 -> 0;
                    case 2 -> 15;
                    case 3 -> 25;
                    case 4 -> 40;
                    case 5 -> 60;
                    case 6 -> 75;
                    case 7 -> 85;
                    case 8 -> 100;
                    case 9 -> 120;
                    case 10 -> 150;
                    default -> 180;
                };
               baseDamage += levelBonus;
        }
        switch (bot.getTitle()){
            case "Orc":
                // Dégats selon le niveau
                int levelBonus = switch (bot.getLevel()) {
                    case 1 -> 0;
                    case 2 -> 20;
                    case 3 -> 45;
                    case 4 -> 55;
                    case 5 -> 70;
                    case 6 -> 90;
                    case 7 -> 100;
                    case 8 -> 120;
                    case 9 -> 140;
                    case 10 -> 170;
                    default -> 200;
                };
                baseDamage += levelBonus;
        }
        switch (bot.getTitle()){
            case "Archer":
                // Dégats selon le niveau
                int levelBonus = switch (bot.getLevel()) {
                    case 1 -> 0;
                    case 2 -> 10;
                    case 3 -> 15;
                    case 4 -> 25;
                    case 5 -> 35;
                    case 6 -> 44;
                    case 7 -> 52;
                    case 8 -> 60;
                    case 9 -> 70;
                    case 10 -> 82;
                    default -> 100;
                };
                baseDamage += levelBonus;
        }
        return baseDamage;
    }
}