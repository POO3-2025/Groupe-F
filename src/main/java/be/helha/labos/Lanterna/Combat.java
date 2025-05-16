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
import com.googlecode.lanterna.gui2.table.Table;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class Combat {
    private MongoDatabase mongoDatabase;

    private void afficherInventaire(WindowBasedTextGUI gui, CharacterType personnage, Label pvLabel) {
        BasicWindow inventaireWindow = new BasicWindow("Sélection d'objet");
        Panel inventairePanel = new Panel(new LinearLayout(Direction.VERTICAL));
        Table<String> inventaireTable = new Table<>("Nom");

        // Récupération de l'inventaire
        MongoCollection<Document> inventoryCollection = mongoDatabase.getCollection("inventory");
        Document query = new Document("characterId", personnage.getId());
        Document inventoryDoc = inventoryCollection.find(query).first();

        if (inventoryDoc == null) {
            MessageDialog.showMessageDialog(gui, "Erreur", "Inventaire non trouvé");
            return;
        }

        List<Document> slots = inventoryDoc.getList("slots", Document.class);
        if (slots == null || slots.isEmpty()) {
            MessageDialog.showMessageDialog(gui, "Info", "Inventaire vide");
            return;
        }

        List<Document> objetsUtilisables = new ArrayList<>();
        for (Document slot : slots) {
            Document item = slot.get("item", Document.class);
            if (item != null) {
                inventaireTable.getTableModel().addRow(item.getString("nom"));
                objetsUtilisables.add(item);
            }
        }

        inventaireTable.setSelectAction(() -> {
            int selectedIndex = inventaireTable.getSelectedRow();
            if (selectedIndex >= 0 && selectedIndex < objetsUtilisables.size()) {
                Document objetSelectionne = objetsUtilisables.get(selectedIndex);
                String nomObjet = objetSelectionne.getString("nom");

                // Extraire les PV actuels et mettre à jour le label avec le nouvel objet
                String pvText = pvLabel.getText();
                int indexTiret = pvText.indexOf("-");
                if (indexTiret != -1) {
                    pvText = pvText.substring(0, indexTiret);
                }
                pvLabel.setText(pvText + "- Objet utilisé : " + nomObjet);

                utiliserObjet(gui, personnage, objetSelectionne, pvLabel);
                inventaireWindow.close();
            }
        });

        inventairePanel.addComponent(inventaireTable);
        inventairePanel.addComponent(new Button("Fermer", inventaireWindow::close));
        inventaireWindow.setComponent(inventairePanel);
        gui.addWindowAndWait(inventaireWindow);
    }

    /**
     * Méthode qui gère le combat avec une arme
     * @param armeSelectionnee
     * @param perso
     * @param botVi
     * @param pvBot
     * @param pvLabel
     * @param textGUI
     * @param window
     * @param armeWindow
     * @param bot
     */
    private void gererCombatArme(Document armeSelectionnee, CharacterType perso, AtomicInteger botVi,
                                 Label pvBot, Label pvLabel, WindowBasedTextGUI textGUI,
                                 BasicWindow window, BasicWindow armeWindow, Bot bot) {

        // XP gagné en fonction du niveau du perso
        int xpGagne;

        switch (perso.getLevel()) {
            case 1 -> xpGagne = 20;
            case 2 -> xpGagne = 30;
            case 3 -> xpGagne = 45;
            case 4 -> xpGagne = 70;
            case 5 -> xpGagne = 85;
            case 6 -> xpGagne = 110;
            case 7 -> xpGagne = 130;
            case 8 -> xpGagne = 150;
            case 9 -> xpGagne = 175;
            default -> xpGagne = 200; // valeur par défaut au cas où
        }

        try {
            // Récupérer les dégâts directement depuis le document de l'arme
            int degats = armeSelectionnee.getInteger("degats", 0);
            if (degats == 0) {
                System.out.println("Erreur : Arme sans dégâts");
                degats = 10; // Valeur par défaut
            }

            int vieBot = botVi.get();
            vieBot -= degats;
            botVi.set(vieBot);
            System.out.println("Le bot a pris " + degats + " de dégâts !");

            if (vieBot <= 0) {
                pvBot.setText("PV de l'adversaire : 0");
                int recomp = 15;
                perso.setMoney(perso.getMoney() + recomp);
                perso.updateMoneyInDB(mongoDatabase);
                perso.gainExperience(xpGagne, mongoDatabase);
                MessageDialog.showMessageDialog(textGUI, "Victoire",
                        "Vous avez gagné ! Voici votre récompense : " + recomp + " pièces.");
                perso.recupererVie(mongoDatabase);
                window.close();
                armeWindow.close();
                return;
            }

            pvBot.setText("PV de l'adversaire : " + vieBot);
            int retour = bot.jouerContreBot(perso);
            perso.setHealth(perso.getHealth() - retour);
            System.out.println("Le bot vous a infligé : " + retour);
            pvLabel.setText("Vos PV : " + perso.getHealth());

            if (perso.getHealth() <= 0) {
                MessageDialog.showMessageDialog(textGUI, "Défaite",
                        "Vous avez perdu ! Vous ne gagnez rien !");
                perso.recupererVie(mongoDatabase);
                window.close();
            }
            armeWindow.close();
        } catch (Exception e) {
            System.out.println("Erreur lors du combat : " + e.getMessage());
            MessageDialog.showMessageDialog(textGUI, "Erreur", "Une erreur est survenue pendant le combat");
            armeWindow.close();
        }
    }

    private void utiliserObjet(WindowBasedTextGUI gui, CharacterType personnage, Document objet, Label pvLabel) {
        if (objet.getString("type").equals("potion")) {
            int soin = objet.getInteger("valeur");
            personnage.setHealth(Math.min(personnage.getHealth() + soin, 100));
            pvLabel.setText("Vos PV : " + personnage.getHealth());

            // Retirer l'objet de l'inventaire
            MongoCollection<Document> inventoryCollection = mongoDatabase.getCollection("inventory");
            List<Document> slots = inventoryCollection.find(
                    new Document("characterId", personnage.getId())
            ).first().getList("slots", Document.class);

            for (int i = 0; i < slots.size(); i++) {
                Document slot = slots.get(i);
                Document item = slot.get("item", Document.class);
                if (item != null && item.get("_id").equals(objet.get("_id"))) {
                    inventoryCollection.updateOne(
                            new Document("characterId", personnage.getId()),
                            new Document("$set", new Document("slots." + i + ".item", null))
                    );
                    break;
                }
            }

            MessageDialog.showMessageDialog(gui, "Potion utilisée",
                    String.format("Vous avez récupéré %d points de vie !", soin));
        }
    }

    public void AfficherCombat(CharacterType perso, boolean type) {
        Bot bot = new Bot();
        Connexion_DB_Nosql connexionDbNosql = new Connexion_DB_Nosql("nosql");
        mongoDatabase = connexionDbNosql.createDatabase();

        try {
            DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
            terminalFactory.setInitialTerminalSize(new TerminalSize(150, 32));
            SwingTerminalFrame terminal = terminalFactory.createSwingTerminal();
            terminal.setVisible(true);
            terminal.setResizable(false);

            Screen screen = new TerminalScreen(terminal);
            screen.startScreen();
            WindowBasedTextGUI textGUI = new MultiWindowTextGUI(screen);

            if (type) {
                BasicWindow window = new BasicWindow("Partie en solo");
                Panel panel = new Panel();
                panel.setLayoutManager(new LinearLayout(Direction.VERTICAL));

                Label pvLabel = new Label("Vos PV : " + perso.getHealth());
                panel.addComponent(pvLabel);

                int pvInitialBot;
                // PV du bot en fonction du niveau du perso
                switch (perso.getLevel()) {
                    case 1 -> pvInitialBot = 70;
                    case 2 -> pvInitialBot = 90;
                    case 3 -> pvInitialBot = 115;
                    case 4 -> pvInitialBot = 140;
                    case 5 -> pvInitialBot = 160;
                    case 6 -> pvInitialBot = 180;
                    case 7 -> pvInitialBot = 220;
                    case 8 -> pvInitialBot = 270;
                    case 9 -> pvInitialBot = 340;
                    default -> pvInitialBot = 400; // valeur par défaut au cas où
                }

                // XP gagné en fonction du niveau du perso
                int xpGagne;
                switch (perso.getLevel()) {
                    case 1 -> xpGagne = 20;
                    case 2 -> xpGagne = 30;
                    case 3 -> xpGagne = 45;
                    case 4 -> xpGagne = 70;
                    case 5 -> xpGagne = 85;
                    case 6 -> xpGagne = 110;
                    case 7 -> xpGagne = 130;
                    case 8 -> xpGagne = 150;
                    case 9 -> xpGagne = 175;
                    default -> xpGagne = 200; // valeur par défaut au cas où
                }

                AtomicInteger botVi = new AtomicInteger(pvInitialBot);
                Label pvBot = new Label("PV de l'adversaire : " + botVi);
                panel.addComponent(pvBot);

                panel.addComponent(new Button("Coup à main nue", () -> {
                    System.out.println("Vous attaquez le bot !");
                    int degats = perso.attackHitsMainNu(perso);
                    botVi.set(botVi.get() - degats);
                    System.out.println("Le bot a pris " + degats + " de dégâts !");

                    if (botVi.get() <= 0) {
                        pvBot.setText("PV de l'adversaire : 0");
                        int recomp = 15;
                        perso.setMoney(perso.getMoney() + recomp);
                        perso.updateMoneyInDB(mongoDatabase);
                        perso.gainExperience(xpGagne, mongoDatabase);
                        MessageDialog.showMessageDialog(textGUI, "Victoire",
                                "Vous avez gagné ! Voici votre récompense : " + recomp + " pièces.");
                        perso.recupererVie(mongoDatabase);
                        window.close();
                        return;
                    }

                    pvBot.setText("PV de l'adversaire : " + botVi.get());
                    int retour = bot.jouerContreBot(perso);
                    perso.setHealth(perso.getHealth() - retour);
                    System.out.println("Le bot vous a infligé :" + retour);
                    pvLabel.setText("Vos PV : " + perso.getHealth());

                    if (perso.getHealth() <= 0) {
                        MessageDialog.showMessageDialog(textGUI, "Défaite",
                                "Vous avez perdu ! Vous ne gagnez rien !");
                        perso.recupererVie(mongoDatabase);
                        window.close();
                    }
                }));

                panel.addComponent(new Button("Coup avec l'arme", () -> {
                    BasicWindow armeWindow = new BasicWindow("Sélection d'arme");
                    Panel armePanel = new Panel(new LinearLayout(Direction.VERTICAL));
                    Table<String> armeTable = new Table<>("Nom");

                    // Récupération des armes de l'inventaire
                    MongoCollection<Document> inventoryCollection = mongoDatabase.getCollection("inventory");
                    Document query = new Document("characterId", perso.getId());
                    Document inventoryDoc = inventoryCollection.find(query).first();
                    List<Document> armesDisponibles = new ArrayList<>();

                    if (inventoryDoc != null) {
                        List<Document> slots = inventoryDoc.getList("slots", Document.class);
                        if (slots != null) {
                            for (Document slot : slots) {
                                Document item = slot.get("item", Document.class);
                                if (item != null) {
                                    String itemType = item.getString("type");
                                    if ("Sword".equals(itemType) || "Mace".equals(itemType) || "Bow".equals(itemType)) {
                                        armeTable.getTableModel().addRow(item.getString("nom"));
                                        armesDisponibles.add(item);
                                    }
                                }
                            }
                        }
                    }

                    if (armesDisponibles.isEmpty()) {
                        MessageDialog.showMessageDialog(textGUI, "Information", "Vous n'avez pas d'armes dans votre inventaire!");
                        armeWindow.close();
                        return;
                    }

                    // Modification ici : utilisation de setSelectAction pour déclencher l'attaque directement
                    armeTable.setSelectAction(() -> {
                        int selectedIndex = armeTable.getSelectedRow();
                        if (selectedIndex >= 0 && selectedIndex < armesDisponibles.size()) {
                            Document armeSelectionnee = armesDisponibles.get(selectedIndex);
                            gererCombatArme(armeSelectionnee, perso, botVi, pvBot, pvLabel, textGUI, window, armeWindow, bot);
                        }
                    });

                    armePanel.addComponent(armeTable);
                    armePanel.addComponent(new Button("Annuler", armeWindow::close));
                    armeWindow.setComponent(armePanel);
                    textGUI.addWindowAndWait(armeWindow);
                }));

                panel.addComponent(new Button("Utiliser une potion", () -> {
                    BasicWindow potionWindow = new BasicWindow("Sélection de potion");
                    Panel potionPanel = new Panel(new LinearLayout(Direction.VERTICAL));
                    Table<String> potionTable = new Table<>("Nom");

                    // Récupération des potions de l'inventaire
                    MongoCollection<Document> inventoryCollection = mongoDatabase.getCollection("inventory");
                    Document query = new Document("characterId", perso.getId());
                    Document inventoryDoc = inventoryCollection.find(query).first();
                    List<Document> potionsDisponibles = new ArrayList<>();

                    if (inventoryDoc != null) {
                        List<Document> slots = inventoryDoc.getList("slots", Document.class);
                        System.out.println("Nombre de slots trouvés : " + (slots != null ? slots.size() : 0));

                        if (slots != null) {
                            for (Document slot : slots) {
                                Document item = slot.get("item", Document.class);
                                System.out.println("Item examiné : " + (item != null ? item.toJson() : "null"));
                                if (item != null && "Potion".equals(item.getString("type"))) {
                                    String nomPotion = item.getString("nom");
                                    System.out.println("Potion trouvée : " + nomPotion);
                                    potionTable.getTableModel().addRow(nomPotion);
                                    potionsDisponibles.add(item);
                                }
                            }
                        }
                    }

                    if (potionsDisponibles.isEmpty()) {
                        MessageDialog.showMessageDialog(textGUI, "Information", "Vous n'avez pas de potions dans votre inventaire!");
                        potionWindow.close();
                        return;
                    }

                    potionTable.setSelectAction(() -> {
                        int selectedIndex = potionTable.getSelectedRow();
                        if (selectedIndex >= 0 && selectedIndex < potionsDisponibles.size()) {
                            Document potionSelectionnee = potionsDisponibles.get(selectedIndex);
                            int soin = potionSelectionnee.getInteger("valeur", 0);
                            perso.setHealth(perso.getHealth() + soin);
                            pvLabel.setText("Vos PV : " + perso.getHealth());
                            MessageDialog.showMessageDialog(textGUI, "Soin",
                                    "Vous avez utilisé une potion et récupéré " + soin + " points de vie!");
                            potionWindow.close();
                        }
                    });

                    potionPanel.addComponent(potionTable);
                    potionPanel.addComponent(new Button("Annuler", potionWindow::close));
                    potionWindow.setComponent(potionPanel);
                    textGUI.addWindowAndWait(potionWindow);
                }));

                panel.addComponent(new Button("Abandonner", window::close));

                window.setComponent(panel);
                textGUI.addWindowAndWait(window);
                screen.stopScreen();
                terminal.close();
            } else {
                BasicWindow window = new BasicWindow("Partie Multijoueur");
                Panel panel = new Panel();
                panel.addComponent(new Button("Retour", window::close));
                window.setComponent(panel);
                textGUI.addWindowAndWait(window);
                screen.stopScreen();
                terminal.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}