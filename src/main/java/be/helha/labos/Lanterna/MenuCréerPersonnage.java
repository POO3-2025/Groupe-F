package be.helha.labos.Lanterna;

import be.helha.labos.DB.User_DAO;
import be.helha.labos.DBNosql.Connexion_DB_Nosql;
import be.helha.labos.DBNosql.DAO_NOSQL;
import be.helha.labos.collection.Character.Archer;
import be.helha.labos.collection.Character.CharacterType;
import be.helha.labos.collection.Character.Knight;
import be.helha.labos.collection.Character.Orc;
import be.helha.labos.collection.Item.Item;
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
 * Classe MenuCréerPersonnage qui gère l'affichage du menu de création de personnage.
 * Elle utilise la bibliothèque Lanterna pour créer une interface graphique dans le terminal.
 */
public class MenuCréerPersonnage {
    /**
     * Méthode qui affiche le menu de création de personnage.
     * @param pseudo Le pseudo de l'utilisateur connecté.
     */
    public void afficherCréationPersonnage(String pseudo) {


        String dbkey = "mysql";
        /**
         *
         */
        DAO_NOSQL dao = new DAO_NOSQL("nosql");
        User_DAO userDao = new User_DAO(dbkey);

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
            BasicWindow window = new BasicWindow("Menu Création");
            // Création du contenu de la fenêtre (panel avec un bouton)



            Panel panel = new Panel();
            /**
             * Méthode pour nous créer un perso
             */
            panel.addComponent(new Button("Archer", () ->
            {
                // Création d'un panel pour la saisie du nom
                Label label = new Label("Entrez le pseudo pour votre Archer :");
                TextBox textBox = new TextBox();
                panel.addComponent(label);
                panel.addComponent(textBox);
                textBox.takeFocus();
                panel.addComponent(new Button("Confirmer", () ->
                {
                    String name = textBox.getText();
                    if (name != null && !name.isEmpty())
                    {
                        Archer archer = new Archer(name);
                        dao.ajouterPersonnagePourUser(dbkey, pseudo, archer);
                        window.close();
                    }
                }));
            }));

            panel.addComponent(new Button("Knight", () ->
            {
                // Création d'un panel pour la saisie du nom
                Label label = new Label("Entrez le pseudo pour votre Knight :");
                TextBox textBox = new TextBox();
                panel.addComponent(label);
                panel.addComponent(textBox);
                textBox.takeFocus();
                panel.addComponent(new Button("Confirmer", () ->
                {
                    String name = textBox.getText();
                    if (name != null && !name.isEmpty())
                    {
                        Knight knight = new Knight(name);
                        dao.ajouterPersonnagePourUser(dbkey, pseudo, knight);
                        window.close();
                    }
                }));
            }));

            panel.addComponent(new Button("Orc", () ->
            {
                // Création d'un panel pour la saisie du nom
                Label label = new Label("Entrez le pseudo pour votre Orc :");
                TextBox textBox = new TextBox();
                panel.addComponent(label);
                panel.addComponent(textBox);
                textBox.takeFocus();
                panel.addComponent(new Button("Confirmer", () ->
                {
                    String name = textBox.getText();
                    if (name != null && !name.isEmpty())
                    {
                        Orc orc = new Orc(name);
                        dao.ajouterPersonnagePourUser(dbkey, pseudo, orc);
                        window.close();
                    }
                }));
            }));

            /**
             * Supprimer un perso
             */
            panel.addComponent(new Button("Supprimer un personnage", () -> {
                int idUser = userDao.getUserByPseudo(pseudo).getId();
                List<CharacterType> characters = dao.readAllCharactersByUserId(idUser);

                if (characters.isEmpty()) {
                    MessageDialog.showMessageDialog(textGUI, "Suppression", "Aucun personnage disponible.");
                    return;
                }

                // ComboBox avec affichage des noms
                ComboBox<CharacterType> comboBox = new ComboBox<>();
                for (CharacterType character : characters) {
                    comboBox.addItem(character); // on affichera un toString() personnalisé
                }

                Panel selectionPanel = new Panel();
                selectionPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));
                selectionPanel.addComponent(new Label("Sélectionnez un personnage à supprimer :"));
                selectionPanel.addComponent(comboBox);

                BasicWindow selectionWindow = new BasicWindow("Supprimer un personnage");

                // Bouton de confirmation
                selectionPanel.addComponent(new Button("Confirmer", () -> {
                    CharacterType selected = comboBox.getSelectedItem();
                    if (selected != null) {
                        dao.DeleteCharacters(selected.getId());
                        MessageDialog.showMessageDialog(textGUI, "Succès", "Personnage supprimé : " + selected.getName());

                        // Retour en arrière après suppression //
                        /* selectionWindow.close(); */   // A voir si on le laisse...
                    }
                }));

                // Bouton de confirmation
                selectionPanel.addComponent(new Button("Retour", selectionWindow::close));

                // Création d'une nouvelle fenêtre popup

                selectionWindow.setComponent(selectionPanel);
                textGUI.addWindowAndWait(selectionWindow);
            }));

            /**
             * Méthode d'affichages des persos
             */
            panel.addComponent(new Button("Voir mes personnages", () -> {

                int idUser = userDao.getUserByPseudo(pseudo).getId();
                List<CharacterType> characters = dao.readAllCharactersByUserId(idUser);

                if (characters.isEmpty()) {
                    MessageDialog.showMessageDialog(textGUI, "Personnages", "Aucun personnage trouvé.");
                } else {
                    StringBuilder builder = new StringBuilder();
                    // Boucle pour récupérer les champs des persos. A Voir...
                    for (CharacterType character : characters) {
                        builder.append("- ").append(character.getName())
                                .append(" | Type: ").append(character.getTitle())
                                .append(" | Argent: ").append(character.getMoney())
                                .append(" | HP: ").append(character.getHealth())
                                .append(" | DMG: ").append(character.getDamage())
                                .append("\n");
                    }
                    MessageDialog.showMessageDialog(textGUI, "Mes Personnages", builder.toString());
                }
            }));

            panel.addComponent(new Button("Revenir en arrière ", window::close)); // Ferme juste la fenêtre de menu

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