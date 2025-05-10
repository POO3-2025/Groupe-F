package be.helha.labos.Lanterna;

import be.helha.labos.collection.Character.CharacterType;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.googlecode.lanterna.gui2.table.Table;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class Boutique {

    private final MongoCollection<Document> collection;
    private double argent;

    public Boutique(MongoCollection<Document> collection) {
        this.collection = collection; // Injection de la collection MongoDB
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
                    collection.deleteOne(new Document("_id", objetSelectionne.getObjectId("_id")));
                    MessageDialog.showMessageDialog(gui, "Achat", "Vous avez acheté : " + objetSelectionne.getString("nom"));

                    // Mise à jour de l'or restant
                    argent -= prix;

                    personnage.setMoney(argent);  // mise à jour de l'objet

                    personnage.updateMoneyInDB();

                    orRestantLabel.setText("Or restant : " + argent + " pièces");

                    // Mise à jour de la table
                    objetsDisponibles.remove(objetSelectionne);
                    objetsTable.getTableModel().removeRow(selectedRow);
                }
            }
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