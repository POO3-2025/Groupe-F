package be.helha.labos.Lanterna;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.*;
import com.googlecode.lanterna.terminal.*;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame;
import java.io.IOException;

public class Inscription {

    public static void main(String[] args) {
        try {
            // Utilisation de DefaultTerminalFactory pour créer un terminal Swing
            DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
            // Spécifiez les dimensions ici
            terminalFactory.setInitialTerminalSize(new TerminalSize(80, 24));
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
            BasicWindow window = new BasicWindow("Connexion");
            // Création du contenu de la fenêtre (panel avec un bouton)
            Panel panel = new Panel();
            panel.addComponent(new Label("Bienvenue dans le jeu!"));
            panel.addComponent(new Button("S'inscrire", () -> {
                BasicWindow nouvelleFenetre = new BasicWindow("Inscription");
                Panel nouveauPanel = new Panel();
                nouveauPanel.addComponent(new Label("Hello POO3 !"));
                nouveauPanel.addComponent(new Button("Fermer", nouvelleFenetre::close));
                nouveauPanel.addComponent(new Button("Accepter", nouvelleFenetre::close));
                nouvelleFenetre.setComponent(nouveauPanel);
                textGUI.addWindowAndWait(nouvelleFenetre);
            }));
            panel.addComponent(new Button("Se connecter", () -> {
                try {
                    screen.stopScreen();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }));
            panel.addComponent(new Button("Quitter", () -> {
                try {
                    screen.stopScreen();
                    System.exit(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }));
            // Ajout du panel à la fenêtre principale
            window.setComponent(panel);
            // Affichage de la fenêtre et attente d'interaction
            textGUI.addWindowAndWait(window);
            // Fermeture propre du terminal après l'utilisation
            screen.stopScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}