package esprit.tn.controllers;

import esprit.tn.entities.Feedback;
import esprit.tn.services.FeedbackService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.awt.event.ActionEvent;

public class ModifierFeedbackController {

    @FXML
    private TextArea MessageFM; // Champ pour le message du feedback

    @FXML
    private Button ModifierFeed; // Bouton pour enregistrer les modifications

    @FXML
    private TextField NoteFM; // Champ pour la note du feedback

    @FXML
    private ComboBox<String> TypeFeedbackFM; // ComboBox pour le type de feedback

    private Feedback feedback; // Objet Feedback à modifier

    // Méthode pour initialiser les données du feedback dans les champs
    public void setFeedback(Feedback feedback) {
        this.feedback = feedback;

        // Remplir les champs avec les données du feedback
        MessageFM.setText(feedback.getMessage());
        NoteFM.setText(String.valueOf(feedback.getNote()));
        TypeFeedbackFM.setValue(feedback.getTypeFeedback());
    }

    // Méthode pour initialiser les options du ComboBox
    @FXML
    public void initialize() {
        // Ajouter les types de feedback possibles dans le ComboBox
        TypeFeedbackFM.getItems().addAll("Positif", "Correctif", "Négatif");
    }

    // Méthode pour enregistrer les modifications du feedback
    @FXML
    void ModifierFeedbackC() {
        // Mettre à jour le feedback avec les nouvelles valeurs
        feedback.setMessage(MessageFM.getText());
        feedback.setNote(Integer.parseInt(NoteFM.getText()));
        feedback.setTypeFeedback(TypeFeedbackFM.getValue());

        // Mettre à jour le feedback dans la base de données
        FeedbackService fs = new FeedbackService();
        fs.modifierF(feedback);

        // Afficher un message de succès
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setContentText("Feedback modifié avec succès !");
        alert.showAndWait();

        // Fermer la fenêtre de modification
        MessageFM.getScene().getWindow().hide();
    }

    // Méthode pour annuler la modification
    @FXML
    void AnnulerModificationFeedback() {
        // Fermer la fenêtre de modification sans enregistrer les modifications
        MessageFM.getScene().getWindow().hide();
    }
}