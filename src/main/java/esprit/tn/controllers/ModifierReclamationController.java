package esprit.tn.controllers;

import esprit.tn.entities.Reclamation;
import esprit.tn.services.ReclamationService;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ModifierReclamationController {

    @FXML
    private TextField titreIdM;

    @FXML
    private TextArea descriptionIdM;

    @FXML
    private ComboBox<String> statusIdM;

    private Reclamation reclamation;

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
        reclamation.setStatus(statusIdM.getValue());

        // Mettre à jour la réclamation dans la base de données
        ReclamationService rs = new ReclamationService();
        rs.modifier(reclamation);

        // Fermer la fenêtre de modification
        titreIdM.getScene().getWindow().hide();
    }
}
