package be.helha.labos.Lanterna;

import be.helha.labos.collection.User;
import be.helha.labos.collection.Character.CharacterType;
import be.helha.labos.collection.Item.Item;
import be.helha.labos.collection.Inventaire;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.IOException;

public class Menu {

    public void Affichage(String pseudo) throws IOException {
        MenuCréerPersonnage menuCréerPersonnage = new MenuCréerPersonnage();

        try {
            // Connexion avec l'API MongoDB version 5.x
            MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase database = mongoClient.getDatabase("TestDB");

            // Terminal Lanterna avec dimensions personnalisées
            DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
            terminalFactory.setInitialTerminalSize(new TerminalSize(120, 24));
            SwingTerminalFrame terminal = terminalFactory.createSwingTerminal();
            terminal.setVisible(true);
            terminal.setResizable(false);

            // Initialisation de l'écran Lanterna
            Screen screen = new TerminalScreen(terminal);
            screen.startScreen();
            WindowBasedTextGUI textGUI = new MultiWindowTextGUI(screen);
            BasicWindow window = new BasicWindow("Bienvenue " + pseudo);

            // Interface utilisateur
            Panel panel = new Panel();

            panel.addComponent(new Button("Comment jouer ?", () -> {
                MessageDialog.showMessageDialog(textGUI, "Les règles du jeu", "Voici comment jouer :\n" +
                        "Taper l'adversaire (genre fort) !\n" +
                        "C'est tout !");
            }));

            panel.addComponent(new Button("Jouer", () -> {
                // À compléter
            }));

            panel.addComponent(new Button("Gérer personnage personnage", () -> {
                menuCréerPersonnage.afficherCréationPersonnage(pseudo);
            }));

            panel.addComponent(new Button("Boutique", () -> {
                MongoCollection<Document> collection = database.getCollection("Magasin");
                Boutique boutique = new Boutique(collection);
                boutique.afficherBoutique(textGUI);
            }));

            panel.addComponent(new Button("Déconnexion", window::close));

            panel.addComponent(new Button("Quitter le jeu", () -> {
                System.exit(0);
            }));

            window.setComponent(panel);
            textGUI.addWindowAndWait(window);

            screen.stopScreen();
            terminal.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
