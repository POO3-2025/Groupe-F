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
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.IOException;
import java.util.List;
/**
 * Classe Partie qui gère l'affichage de l'écran de jeu principal.
 * Elle utilise la bibliothèque Lanterna pour créer une interface graphique dans le terminal.
 */
public class Partie {

    /**
     * Méthode qui affiche l'écran de jeu principal.
     * @param personnage Le personnage du joueur.
     */
    public void AfficherPartie(CharacterType personnage) {

        Connexion_DB_Nosql mongoFactory = new Connexion_DB_Nosql("nosqlTest");
        MongoDatabase database = mongoFactory.createDatabase();

        DAO_NOSQL dao = new DAO_NOSQL();
        Combat combat = new Combat();

        try {
            // Utilisation de DefaultTerminalFactory pour créer un terminal Swing
            DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
            // Spécifiez les dimensions ici
            terminalFactory.setInitialTerminalSize(new TerminalSize(120, 24));
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
            BasicWindow window = new BasicWindow("Nom du jeu");
            // Création du contenu de la fenêtre (panel avec un bouton)
            Panel panel = new Panel();

            panel.addComponent(new Button("Jouer en solo", () -> {
               combat.AfficherCombat(personnage,true);
            }));


            panel.addComponent(new Button("Boutique", () -> {
                MongoCollection<Document> collection = database.getCollection("Magasin");
                Boutique boutique = new Boutique(collection);
                boutique.afficherBoutique(textGUI, personnage);
            }));

            panel.addComponent(new Button("Retour", window::close)); // Ferme juste la fenêtre de menu

            // Ajout du panel à la fenêtre principale
            window.setComponent(panel);
            // Affichage de la fenêtre et attente d'interaction
            textGUI.addWindowAndWait(window);
            // Fermeture propre du terminal après l'utilisation
            screen.stopScreen();
            terminal.close();
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }

}
