package be.helha.labos.Lanterna;

import be.helha.labos.DBNosql.Connexion_DB_Nosql;
import be.helha.labos.collection.Character.CharacterType;
import be.helha.labos.collection.Inventaire;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.googlecode.lanterna.gui2.table.Table;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;


public class Boutique {
    private final MongoCollection<Document> collection;
    private double argent;
    private final MongoDatabase mongoDatabase;

    public Boutique(MongoCollection<Document> collection) {
        this.collection = collection;
        Connexion_DB_Nosql connexionDbNosql = new Connexion_DB_Nosql("nosqlTest");
        this.mongoDatabase = connexionDbNosql.createDatabase();
    }

    public double afficherBoutique(WindowBasedTextGUI gui, CharacterType personnage) {

        this.argent = personnage.getMoney();

        BasicWindow boutiqueMenu = new BasicWindow("Boutique");
        Panel boutiquePanel = new Panel();
        boutiquePanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));

        // Label pour afficher l'or restant
        Label orRestantLabel = new Label("Or restant : " + argent + " pièces");
        boutiquePanel.addComponent(orRestantLabel);

        // Table pour afficher les objets disponibles
        Table<String> objetsTable = new Table<>("Nom", "Prix");
        List<Document> objetsDisponibles = collection.find().into(new ArrayList<>());
        for (Document doc : objetsDisponibles) {
            objetsTable.getTableModel().addRow(
                    doc.getString("nom"),
                    String.valueOf(doc.getDouble("prix"))
            );
        }
        boutiquePanel.addComponent(objetsTable);

        // Ajout d'un listener pour détecter la touche "Entrée"
        objetsTable.setSelectAction(() -> {
            int selectedRow = objetsTable.getSelectedRow();
            if (selectedRow == -1) {
                MessageDialog.showMessageDialog(gui, "Erreur", "Veuillez sélectionner un objet.");
                return;
            }

            Document objetSelectionne = objetsDisponibles.get(selectedRow);
            double prix = objetSelectionne.getDouble("prix");

            // Vérification de l'or restant
            if (prix > argent) {
                MessageDialog.showMessageDialog(gui, "Erreur", "Vous n'avez pas assez de pièces pour acheter cet objet.");
            } else {
                // Afficher une boîte de dialogue de confirmation
                MessageDialogButton confirmation = MessageDialog.showMessageDialog(
                        gui,
                        "Confirmation",
                        "Voulez-vous acheter : " + objetSelectionne.getString("nom") + " pour " + prix + " pièces ?",
                        MessageDialogButton.Yes,
                        MessageDialogButton.No
                );

                if (confirmation == MessageDialogButton.Yes) {
                    ObjectId objetId = objetSelectionne.getObjectId("_id");

                    // Récupération du document du personnage pour obtenir l'ID de son inventaire
                    Document characterDoc = mongoDatabase.getCollection("characters")
                            .find(new Document("_id", personnage.getId()))
                            .first();

                    if (characterDoc != null) {
                        // Récupération de l'ID de l'inventaire du personnage
                        Document inventaireDoc = characterDoc.get("inventaire", Document.class);
                        ObjectId inventaireId = inventaireDoc.getObjectId("_id");

                        // Tentative d'ajout de l'objet dans l'inventaire
                        if (Inventaire.ajouterObjetDansInventaire(inventaireId, objetId)) {
                            // Mise à jour du magasin et de l'interface
                            collection.deleteOne(new Document("_id", objetId));
                            MessageDialog.showMessageDialog(gui, "Achat", "Vous avez acheté : " + objetSelectionne.getString("nom"));

                            // Mise à jour de l'or
                            argent -= prix;
                            personnage.setMoney(argent);
                            personnage.updateMoneyInDB();
                            orRestantLabel.setText("Or restant : " + argent + " pièces");

                            // Mise à jour de la table
                            objetsDisponibles.remove(objetSelectionne);
                            objetsTable.getTableModel().removeRow(selectedRow);
                        } else {
                            MessageDialog.showMessageDialog(gui, "Erreur", "Impossible d'ajouter l'objet dans l'inventaire");
                        }
                    } else {
                        MessageDialog.showMessageDialog(gui, "Erreur", "Impossible de trouver le personnage");
                    }
                }
            }
        });

        // Bouton pour afficher l'inventaire du personnage (bouton Vente)
        Button venteButton = new Button("Vendre", () -> {
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

            List<Document> objetsInventaire = inventaireDoc.getList("objets", Document.class, new ArrayList<>());

            BasicWindow inventaireWindow = new BasicWindow("Inventaire de " + personnage.getName());
            Panel inventairePanel = new Panel(new LinearLayout(Direction.VERTICAL));

            Table<String> inventaireTable = new Table<>("Nom", "Type");

            for (Document item : objetsInventaire) {
                inventaireTable.getTableModel().addRow(
                        item.getString("nom"),
                        item.getString("type")
                );
            }

            inventairePanel.addComponent(inventaireTable);
            inventairePanel.addComponent(new Button("Fermer", inventaireWindow::close));

            inventaireWindow.setComponent(inventairePanel);
            gui.addWindowAndWait(inventaireWindow);
        });

        // Bouton pour retourner au menu précédent
        Button retourButton = new Button("Retour", boutiqueMenu::close);

        // Ajout des composants au panel
        boutiquePanel.addComponent(retourButton);

        // Configuration de la fenêtre
        boutiqueMenu.setComponent(boutiquePanel);

        // Affichage de la fenêtre
        gui.addWindowAndWait(boutiqueMenu);

        return argent;
    }
}