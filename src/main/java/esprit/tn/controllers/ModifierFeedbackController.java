package esprit.tn.controllers;

import esprit.tn.entities.Feedback;
import esprit.tn.services.FeedbackService;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ModifierFeedbackController {

    @FXML
    private TextArea MessageFM; // Champ pour le message du feedback

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

        if (MessageFM.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Champ obligatoire");
            alert.setContentText("Le champ 'Message' est obligatoire. Veuillez le remplir.");
            alert.showAndWait();
            return; // Arrêter l'exécution de la méthode
        }
        else if(NoteFM.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Champ obligatoire");
            alert.setContentText("La note est obligatoire. Veuillez la remplir.");
            alert.showAndWait();
            return; // Arrêter l'exécution de la méthode
        }
        else if(feedback.getNote() < 1 || feedback.getNote() > 5) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Champ obligatoire");
            alert.setContentText("La note doit être comprise entre 1 et 5. Veuillez la réctifier.");
            alert.showAndWait();
            return; // Arrêter l'exécution de la méthode
        }

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