package be.helha.labos.Lanterna;

import be.helha.labos.Authentification.Authen;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.screen.*;
import com.googlecode.lanterna.terminal.*;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame;
import java.io.IOException;

public class Inscription {

   public void Lancer () {
       Authen authen = new Authen();
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
                BasicWindow fenetreConnexion = new BasicWindow("Connexion");

                Panel panelConnexion = new Panel();

                TextBox pseudoInput = new TextBox();
                TextBox passwordInput = new TextBox().setMask('*'); // cache les caractères

                panelConnexion.addComponent(new Label("Pseudo :"));
                panelConnexion.addComponent(pseudoInput);

                panelConnexion.addComponent(new Label("Mot de passe :"));
                panelConnexion.addComponent(passwordInput);

                panelConnexion.addComponent(new Button("Se connecter", () -> {
                    String pseudo = pseudoInput.getText();
                    String password = passwordInput.getText();

                    try {
                        String token = authen.login(pseudo, password);
                        MessageDialog.showMessageDialog(textGUI, "Succès", "Connexion validé !");
                    } catch (Exception e) {
                        MessageDialog.showMessageDialog(textGUI, "Erreur", "Échec de la connexion : " + e.getMessage());
                    }

                    fenetreConnexion.close(); // Ferme la fenêtre après traitement
                }));

                panelConnexion.addComponent(new Button("Annuler", fenetreConnexion::close));

                fenetreConnexion.setComponent(panelConnexion);
                textGUI.addWindowAndWait(fenetreConnexion);
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