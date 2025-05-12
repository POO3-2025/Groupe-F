package be.helha.labos.Bot;

import be.helha.labos.collection.Character.CharacterType;
/**
 * Classe Bot qui gère les interactions avec le bot dans le jeu.
 * Elle contient des méthodes pour simuler des actions du bot.
 */
public class Bot {
    /**
     * Méthode pour simuler une action du bot.
     * @param perso Le personnage du bot.
     * @param attack Indique si le bot doit attaquer ou non.
     * @return Les dégâts infligés par le bot.
     */
    public int jouerContreBot(CharacterType perso,boolean attack) {
        int degats = 10;
        switch (perso.getLevel()) {
            case 1:
                System.out.println("Bot niveau 1, dégâts infligés : " + perso.getDamage());
                return degats;

            case 2:
                // Implémenter dégâts niveau 2
                return 15;

            default:
                // Implémenter dégâts niveau 3+
                return 20;
        }

    }
}