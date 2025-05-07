package be.helha.labos.Lanterna;

import be.helha.labos.Authentification.Authen;
import be.helha.labos.DB.User_DAO;
import be.helha.labos.DBNosql.Connexion_DB_Nosql;
import be.helha.labos.DBNosql.DAO_NOSQL;
import be.helha.labos.collection.Character.CharacterType;
import be.helha.labos.collection.Inventaire;
import be.helha.labos.collection.Item.Item;
import be.helha.labos.collection.User;
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

public class Menu {

    public void Affichage(String pseudo) throws IOException {
        MenuCréerPersonnage menuCréerPersonnage = new MenuCréerPersonnage();

        Connexion_DB_Nosql connexionDbNosql = Connexion_DB_Nosql.getInstance();
        MongoDatabase mongoDatabase = connexionDbNosql.getDatabase();

        DAO_NOSQL dao = new DAO_NOSQL();
        User_DAO userDao = new User_DAO("mysql");
        Partie partie = new Partie();

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
            BasicWindow window = new BasicWindow("Bienvenue " + pseudo);
            // Création du contenu de la fenêtre (panel avec un bouton)
            Panel panel = new Panel();
            panel.addComponent(new Button("Comment jouer ?", () -> {
                MessageDialog.showMessageDialog(textGUI, "Les règles du jeu", "Voici comment jouer : \n" +
                        "Taper l'adversaire (genre fort) ! \n" +   // les //n permettent de sauter une ligne
                        "C'est tous ! ");
            }));

            panel.addComponent(new Button("Jouer", () -> {
                BasicWindow jouerWindow = new BasicWindow("Lancement de la Partie");
                Panel jouerPanel = new Panel(new LinearLayout(Direction.VERTICAL));

                jouerPanel.addComponent(new Button("Sélectionner un personnage", () -> {
                    int idUser = userDao.GetUserByPseudo(pseudo).getId();
                    List<CharacterType> characters = dao.readAllCharactersByUserId(mongoDatabase,idUser);

                    if (characters.isEmpty()) {
                        MessageDialog.showMessageDialog(textGUI, "Info", "Aucun personnage disponible.");
                        return;
                    }

                    ComboBox<CharacterType> comboBox = new ComboBox<>();
                    for (CharacterType character : characters) {
                        comboBox.addItem(character);
                    }

                    Panel selectionPanel = new Panel();
                    selectionPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));
                    selectionPanel.addComponent(new Label("Sélectionnez un personnage :"));
                    selectionPanel.addComponent(comboBox);

                    BasicWindow selectionWindow = new BasicWindow("Choisir un personnage");

                    selectionPanel.addComponent(new Button("Confirmer", () -> {
                        CharacterType selected = comboBox.getSelectedItem();
                        if (selected != null) {
                            MessageDialog.showMessageDialog(textGUI, "Succès", "Personnage sélectionné : " + selected.getName());
                            partie.AfficherPartie(selected);
                            selectionWindow.close();
                        }
                    }));

                    selectionPanel.addComponent(new Button("Retour", selectionWindow::close));
                    selectionWindow.setComponent(selectionPanel);
                    textGUI.addWindowAndWait(selectionWindow);
                }));

                jouerPanel.addComponent(new Button("Retour", jouerWindow::close));
                jouerWindow.setComponent(jouerPanel);
                textGUI.addWindowAndWait(jouerWindow);
            }));

            panel.addComponent(new Button("Gérer mes personnages", () -> {
                menuCréerPersonnage.afficherCréationPersonnage(pseudo);
            }));

            panel.addComponent(new Button("Déconnexion", window::close)); // Ferme juste la fenêtre de menu

            panel.addComponent(new Button("Quitter le jeu", () -> {
                System.exit(0);
            }));
            // Ajout du panel à la fenêtre principale
            window.setComponent(panel);
            // Affichage de la fenêtre et attente d'interaction
            textGUI.addWindowAndWait(window);
            // Fermeture propre du terminal après l'utilisation
            screen.stopScreen();
            terminal.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}