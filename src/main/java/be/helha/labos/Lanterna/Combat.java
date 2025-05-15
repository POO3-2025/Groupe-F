package be.helha.labos.Lanterna;

import be.helha.labos.Bot.Bot;
import be.helha.labos.DBNosql.Connexion_DB_Nosql;
import be.helha.labos.DBNosql.DAO_NOSQL;
import be.helha.labos.collection.Character.CharacterType;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame;
import com.mongodb.client.MongoDatabase;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Classe Combat qui gère l'affichage du combat entre le joueur et un bot ou entre deux joueurs.
 * Elle utilise la bibliothèque Lanterna pour créer une interface graphique dans le terminal.
 */
public class Combat {

    /**
     * Méthode qui affiche le combet en fonction du type de combat
     * @param type si true La partie est contre un bot si False alors c'est une partie joueur contre joueur
     */
    public void AfficherCombat(CharacterType perso, boolean type) {
        Bot bot = new Bot();
        Connexion_DB_Nosql connexionDbNosql = new Connexion_DB_Nosql("nosql");
        MongoDatabase mongoDatabase = connexionDbNosql.createDatabase();

        try {
            // Utilisation de DefaultTerminalFactory pour créer un terminal Swing
            DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
            // Spécifiez les dimensions ici
            terminalFactory.setInitialTerminalSize(new TerminalSize(150, 32));
            // Ajout du SwingTerminal dans un SwingTerminalFrame
            SwingTerminalFrame terminal = terminalFactory.createSwingTerminal();
            terminal.setVisible(true);
            // Désactiver la redimension
            terminal.setResizable(false);

            // Création de l'écran à partir du terminal
            Screen screen = new TerminalScreen(terminal);
            screen.startScreen(); // Démarre l'écran du terminal
            // Création de l'interface utilisateur Lanterna (avec screen)
            WindowBasedTextGUI textGUI = new MultiWindowTextGUI(screen);

            if(type) // Si True partie PVE
            {
                BasicWindow window = new BasicWindow("Partie en solo");
                // Création du contenu de la fenêtre (panel avec un bouton)
                Panel panel = new Panel();

                panel.setLayoutManager(new LinearLayout(Direction.VERTICAL));

                // PV du joueur
                Label pvLabel = new Label("Vos PV : " + perso.getHealth());
                panel.addComponent(pvLabel);

                // PV du bot
                int pvInitialBot;
                // PV du bot en fonction du niveau du perso
                switch (perso.getLevel()) {
                    case 1 -> pvInitialBot = 70;
                    case 2 -> pvInitialBot = 90;
                    case 3 -> pvInitialBot = 120;
                    default -> pvInitialBot = 100; // valeur par défaut au cas où
                }

                AtomicInteger botVi = new AtomicInteger(pvInitialBot);


                Label pvBot = new Label("PV de l'adversaire : " + botVi );
                panel.addComponent(pvBot);

                panel.addComponent(new Button("Coup à main nue ",()->{

                    System.out.println("Vous attaquez le bot !");
                    int degats = perso.attackHitsMainNu(perso);
                    botVi.set(botVi.get() - degats); // Le bot subit des dégâts
                    System.out.println("Le bot a pris " + degats + " de dégâts !");

                    if (botVi.get() <= 0) {

                        pvBot.setText("PV de l'adversaire : 0");

                        int recomp = 15;
                        perso.setMoney( perso.getMoney() + recomp); //
                        perso.updateMoneyInDB(mongoDatabase);

                        int xpGagne = 20;
                        perso.gainExperience(xpGagne, mongoDatabase);

                        MessageDialog.showMessageDialog(textGUI, "Victoire", "Vous avez gagné ! , " +
                                "voici votre récompense : " + recomp +" pièces.");

                        perso.recupererVie(mongoDatabase);

                        window.close();
                        return;
                    }
                    else {
                        pvBot.setText("PV de l'adversaire : " + botVi.get());
                    }

                    int retour = bot.jouerContreBot(perso); // Attaque du bot
                    perso.setHealth(perso.getHealth() - retour);
                    System.out.println("Le bot vous a infligé :" + retour);
                    pvLabel.setText("Vos PV : " + perso.getHealth());

                    if (perso.getHealth() <= 0) {

                        MessageDialog.showMessageDialog(textGUI, "Défaite", "Vous avez perdu ! " +
                                "Vous ne gagnez rien !");

                        perso.recupererVie(mongoDatabase);
                        window.close();
                    }
                    }));

                    panel.addComponent(new Button("Coup avec l'arme", () -> {
                    // Simuler une autre attaque
                    int retour = bot.jouerContreBot(perso);
                    perso.setHealth(perso.getHealth() - retour);
                    pvLabel.setText("Vos PV : " + perso.getHealth());
                    }));

                    panel.addComponent(new Label("Utiliser une potion "));

                panel.addComponent(new Button("Abandonner", window::close)); // Ferme juste la fenêtre de menu

                // Ajout du panel à la fenêtre principale
                window.setComponent(panel);
                // Affichage de la fenêtre et attente d'interaction
                textGUI.addWindowAndWait(window);
                // Fermeture propre du terminal après l'utilisation
                screen.stopScreen();
                terminal.close();
            }
            else {
                BasicWindow window = new BasicWindow("Partie Multijoueur");
                // Création du contenu de la fenêtre (panel avec un bouton)
                Panel panel = new Panel();


                panel.addComponent(new Button("Retour", window::close)); // Ferme juste la fenêtre de menu

                // Ajout du panel à la fenêtre principale
                window.setComponent(panel);
                // Affichage de la fenêtre et attente d'interaction
                textGUI.addWindowAndWait(window);
                // Fermeture propre du terminal après l'utilisation
                screen.stopScreen();
                terminal.close();
            }

        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }
}
