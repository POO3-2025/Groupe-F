package be.helha.labos.Bot;

import be.helha.labos.collection.Character.CharacterType;

public class Bot {

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
