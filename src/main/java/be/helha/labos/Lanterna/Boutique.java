package be.helha.labos.Lanterna;

import be.helha.labos.DBNosql.Connexion_DB_Nosql;
import be.helha.labos.collection.Character.CharacterType;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.googlecode.lanterna.gui2.table.Table;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import be.helha.labos.Magasin.Magasin;

import java.util.ArrayList;
import java.util.List;

/**
 * La classe Boutique permet d'afficher une interface de boutique textuelle
 * où un personnage peut acheter ou vendre des objets.
 * Les données sont gérées via MongoDB.
 */
public class Boutique {
    /** Collection MongoDB contenant les objets disponibles dans la boutique */
    private final MongoCollection<Document> collection;

    /** Quantité d'argent (or) du personnage */
    private double argent;

    /** Base de données MongoDB contenant les données du personnage et de l'inventaire */
    private final MongoDatabase mongoDatabase;

    /** Instance de Magasin pour gérer les achats/ventes */
    private final Magasin magasin;

    /**
     * Constructeur de la boutique.
     * Initialise la base de données et la collection d'objets.
     *
     * @param collection Collection MongoDB des objets disponibles à l'achat.
     */
    public Boutique(MongoCollection<Document> collection) {
        this.collection = collection;
        Connexion_DB_Nosql connexionDbNosql = new Connexion_DB_Nosql("nosqlTest");
        this.mongoDatabase = connexionDbNosql.createDatabase();
        this.magasin = new Magasin(mongoDatabase);
    }

    /**
     * Affiche le menu principal de la boutique.
     * Permet au joueur de choisir entre acheter ou vendre des objets, ou quitter.
     *
     * @param gui        Interface Lanterna.
     * @param personnage Personnage actuellement connecté.
     */
    public void afficherBoutique(WindowBasedTextGUI gui, CharacterType personnage) {
        BasicWindow menuBoutique = new BasicWindow("Menu Boutique");
        Panel menuPanel = new Panel(new LinearLayout(Direction.VERTICAL));

        Button acheterButton = new Button("Acheter des objets", () -> {
            afficherMenuAchat(gui, personnage);
        });

        Button vendreButton = new Button("Vendre des objets", () -> {
            afficherMenuVente(gui, personnage, null);
        });

        Button fermerButton = new Button("Fermer", menuBoutique::close);

        menuPanel.addComponent(new Label("Que souhaitez-vous faire ?"));
        menuPanel.addComponent(acheterButton);
        menuPanel.addComponent(vendreButton);
        menuPanel.addComponent(fermerButton);

        menuBoutique.setComponent(menuPanel);
        gui.addWindowAndWait(menuBoutique);
    }

    /**
     * Affiche le menu d'achat des objets disponibles dans la boutique.
     *
     * @param gui        Interface Lanterna.
     * @param personnage Personnage effectuant l'achat.
     */
    private void afficherMenuAchat(WindowBasedTextGUI gui, CharacterType personnage) {
        BasicWindow fenetreAchat = new BasicWindow("Acheter des objets");
        Panel boutiquePanel = new Panel(new LinearLayout(Direction.VERTICAL));
        List<Document> objetsDisponibles = collection.find().into(new ArrayList<>());

        Table<String> objetsTable = new Table<>("Nom", "Prix");
        for (Document item : objetsDisponibles) {
            objetsTable.getTableModel().addRow(
                    item.getString("nom"),
                    String.format("%.2f", item.getDouble("prix"))
            );
        }

        this.argent = personnage.getMoney();
        Label orRestantLabel = new Label("Or restant : " + argent + " pièces");
        boutiquePanel.addComponent(orRestantLabel);
        boutiquePanel.addComponent(objetsTable);

        objetsTable.setSelectAction(() -> {
            int selectedRow = objetsTable.getSelectedRow();
            if (selectedRow >= 0) {
                Document objetSelectionne = objetsDisponibles.get(selectedRow);
                traiterAchat(gui, personnage, objetSelectionne, objetsTable,
                        objetsDisponibles, selectedRow, orRestantLabel);
            }
        });

        Button fermerButton = new Button("Fermer", fenetreAchat::close);
        boutiquePanel.addComponent(fermerButton);

        fenetreAchat.setComponent(boutiquePanel);
        gui.addWindowAndWait(fenetreAchat);
    }

    /**
     * Gère l'achat d'un objet : vérifie l'espace dans l'inventaire,
     * le solde d'or et effectue l'achat si possible.
     *
     * @param gui               Interface Lanterna.
     * @param personnage        Personnage acheteur.
     * @param objetSelectionne  Objet sélectionné dans la table.
     * @param objetsTable       Table des objets affichés.
     * @param objetsDisponibles Liste des objets disponibles.
     * @param selectedRow       Ligne sélectionnée dans la table.
     * @param orRestantLabel    Label affichant l'or restant.
     */
    private void traiterAchat(WindowBasedTextGUI gui, CharacterType personnage, Document objetSelectionne,
                              Table<String> objetsTable, List<Document> objetsDisponibles, int selectedRow,
                              Label orRestantLabel) {
        if (objetSelectionne == null) return;

        Document characterDoc = mongoDatabase.getCollection("characters")
                .find(new Document("_id", personnage.getId()))
                .first();

        if (characterDoc == null) {
            MessageDialog.showMessageDialog(gui, "Erreur", "Personnage introuvable.");
            return;
        }

        Document inventaireDoc = characterDoc.get("inventaire", Document.class);
        if (inventaireDoc == null) {
            MessageDialog.showMessageDialog(gui, "Erreur", "Inventaire non trouvé.");
            return;
        }

        Document fullInventaireDoc = mongoDatabase.getCollection("inventory")
                .find(new Document("_id", inventaireDoc.getObjectId("_id")))
                .first();

        List<Document> slots = fullInventaireDoc.getList("slots", Document.class);
        boolean inventairePlein = true;
        for (Document slot : slots) {
            if (slot.get("item") == null) {
                inventairePlein = false;
                break;
            }
        }

        if (inventairePlein) {
            MessageDialog.showMessageDialog(gui, "Erreur", "Votre inventaire est plein.");
            return;
        }

        double prix = objetSelectionne.getDouble("prix");
        String nom = objetSelectionne.getString("nom");

        if (prix > personnage.getMoney()) {
            MessageDialog.showMessageDialog(gui, "Erreur", "Fonds insuffisants.");
            return;
        }

        MessageDialogButton confirmation = MessageDialog.showMessageDialog(
                gui,
                "Confirmation d'achat",
                String.format("Acheter %s pour %.2f pièces ?", nom, prix),
                MessageDialogButton.Yes,
                MessageDialogButton.No
        );

        if (confirmation == MessageDialogButton.Yes) {
            if (magasin.acheterObjet(objetSelectionne, personnage)) {
                objetsDisponibles.remove(selectedRow);
                objetsTable.getTableModel().removeRow(selectedRow);
                orRestantLabel.setText("Or restant : " + personnage.getMoney() + " pièces");
                MessageDialog.showMessageDialog(gui, "Achat réussi",
                        String.format("Vous avez acheté %s pour %.2f pièces", nom, prix));
            } else {
                MessageDialog.showMessageDialog(gui, "Erreur", "L'achat a échoué.");
            }
        }
    }

    /**
     * Affiche le menu de vente d'objets présents dans l'inventaire du personnage.
     *
     * @param gui             Interface Lanterna.
     * @param personnage      Personnage vendeur.
     * @param orRestantLabel  Label pour mettre à jour l'or affiché (si applicable).
     */
    private void afficherMenuVente(WindowBasedTextGUI gui, CharacterType personnage, Label orRestantLabel) {
        BasicWindow inventaireWindow = new BasicWindow("Vente d'objets");
        Panel inventairePanel = new Panel(new LinearLayout(Direction.VERTICAL));
        Table<String> inventaireTable = new Table<>("Nom", "Prix de vente");
        Label orActuelLabel = new Label("Or actuel : " + personnage.getMoney() + " pièces");

        Document characterDoc = mongoDatabase.getCollection("characters")
                .find(new Document("_id", personnage.getId()))
                .first();

        if (characterDoc == null) {
            MessageDialog.showMessageDialog(gui, "Erreur", "Personnage introuvable.");
            return;
        }

        Document inventaireDoc = characterDoc.get("inventaire", Document.class);
        Document fullInventaireDoc = mongoDatabase.getCollection("inventory")
                .find(new Document("_id", inventaireDoc.getObjectId("_id")))
                .first();

        List<Document> slots = fullInventaireDoc.getList("slots", Document.class, new ArrayList<>());
        List<Document> objetsVendables = new ArrayList<>();

        for (Document slot : slots) {
            Document item = slot.get("item", Document.class);
            if (item != null) {
                double prixVente = item.getDouble("prix") * 0.8;
                inventaireTable.getTableModel().addRow(
                        item.getString("nom"),
                        String.format("%.2f", prixVente)
                );
                objetsVendables.add(item);
            }
        }

        inventaireTable.setSelectAction(() -> {
            int selectedRow = inventaireTable.getSelectedRow();
            if (selectedRow >= 0 && selectedRow < objetsVendables.size()) {
                traiterVente(gui, personnage, objetsVendables.get(selectedRow),
                        inventaireTable, objetsVendables, selectedRow,
                        orActuelLabel, orRestantLabel);
            }
        });

        inventairePanel.addComponent(orActuelLabel);
        inventairePanel.addComponent(inventaireTable);
        inventairePanel.addComponent(new Button("Fermer", inventaireWindow::close));
        inventaireWindow.setComponent(inventairePanel);
        gui.addWindowAndWait(inventaireWindow);
    }

    /**
     * Traite la vente d'un objet de l'inventaire.
     * Le personnage reçoit 80 % du prix de base.
     *
     * @param gui               Interface Lanterna.
     * @param personnage        Personnage vendeur.
     * @param itemAVendre       Objet à vendre.
     * @param inventaireTable   Table affichant l'inventaire.
     * @param objetsVendables   Liste des objets vendables.
     * @param selectedRow       Index de l'objet sélectionné.
     * @param orActuelLabel     Label affichant l'or actuel.
     * @param orRestantLabel    Label secondaire à mettre à jour (optionnel).
     */
    private void traiterVente(WindowBasedTextGUI gui, CharacterType personnage, Document itemAVendre,
                              Table<String> inventaireTable, List<Document> objetsVendables, int selectedRow,
                              Label orActuelLabel, Label orRestantLabel) {
        double prixVente = itemAVendre.getDouble("prix") * 0.8;
        MessageDialogButton confirmation = MessageDialog.showMessageDialog(
                gui,
                "Confirmation",
                String.format("Vendre %s pour %.2f pièces ?", itemAVendre.getString("nom"), prixVente),
                MessageDialogButton.Yes,
                MessageDialogButton.No
        );

        if (confirmation == MessageDialogButton.Yes) {
            if (magasin.vendreObjet(itemAVendre, personnage, collection)) {
                inventaireTable.getTableModel().removeRow(selectedRow);
                objetsVendables.remove(selectedRow);
                orActuelLabel.setText("Or actuel : " + personnage.getMoney() + " pièces");
                if (orRestantLabel != null) {
                    orRestantLabel.setText("Or actuel : " + personnage.getMoney() + " pièces");
                }
                MessageDialog.showMessageDialog(gui, "Vente", "Objet vendu avec succès !");
            } else {
                MessageDialog.showMessageDialog(gui, "Erreur", "La vente a échoué.");
            }
        }
    }
}
