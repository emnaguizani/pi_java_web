package esprit.tn.controllers;

import esprit.tn.entities.Reclamation;
import esprit.tn.services.ReclamationService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;

import javafx.fxml.Initializable;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ModifierReclamationController {

    @FXML
    private TextField titreIdM;

    @FXML
    private TextArea descriptionIdM;

    @FXML
    private ComboBox<String> statusIdM;

    private Reclamation reclamation;

    @FXML
    private Button nextM;

    @FXML
    private Button annulerModifR;


    public class AjouterReclamationController implements Initializable {

        @FXML
        private ComboBox<String> statusComboBox; // fx:id must match the one in FXML

        @Override
        public void initialize(URL location, ResourceBundle resources) {
            // Add the status options to the ComboBox
            statusComboBox.getItems().addAll(
                    "Soumise",
                    "En cours de traitement",
                    "Résolue",
                    "Fermée"
            );

            // Set a default value (optional)
            statusComboBox.setValue("Soumise");
        }
    }

    public void setReclamation(Reclamation reclamation) {
        this.reclamation = reclamation;

        // Initialiser les champs avec les données de la réclamation
        titreIdM.setText(reclamation.getTitre());
        descriptionIdM.setText(reclamation.getDescription());
        statusIdM.setValue(reclamation.getStatus());
    }

    @FXML
    void enregistrerModifications() {

        // Mettre à jour la réclamation avec les nouvelles valeurs
        reclamation.setTitre(titreIdM.getText());
        reclamation.setDescription(descriptionIdM.getText());
        String selectedStatus = statusIdM.getValue();

        if (selectedStatus != null) {
            System.out.println("Statut sélectionné : " + selectedStatus);
            reclamation.setStatus(selectedStatus);
        } else {
            System.out.println("Aucun statut sélectionné.");
        }

        // Mettre à jour la réclamation dans la base de données
        ReclamationService rs = new ReclamationService();
        rs.modifier(reclamation);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setContentText("Réclamation modifiée avec succès !");
        alert.showAndWait();

        // Fermer la fenêtre de modification
        titreIdM.getScene().getWindow().hide();
    }

    @FXML
    void annulerModifReclamation() {
        // Fermer la fenêtre de modification sans enregistrer les modifications
        titreIdM.getScene().getWindow().hide();
    }
}
