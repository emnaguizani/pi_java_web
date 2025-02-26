package esprit.tn.controllers;

import esprit.tn.entities.Feedback;
import esprit.tn.entities.Reclamation;
import esprit.tn.services.FeedbackService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class AjouterFeedbackController {

    @FXML
    private Button AjouterFeed;

    @FXML
    private TextArea MessageF;

    @FXML
    private TextField NoteF;

    @FXML
    private TextField PieceJointeF;

    @FXML
    private Button importPhotoButton;

    @FXML
    private ComboBox<String> TypeFeedbackF;

    private int reclamationId; // ID de la réclamation sélectionnée

    public void setReclamationId(int reclamationId) {
        this.reclamationId = reclamationId;
    }

    @FXML
    void initialize() {
        // Initialiser le ComboBox avec les types de feedback
        TypeFeedbackF.getItems().addAll("Positif", "Correctif", "Négatif");

        importPhotoButton.setOnAction(event -> importPhoto());
    }

    @FXML
    private void importPhoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Video Files");

        // video types illi ynajjem y7otthom

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Video Files", "*.jpg", "*.png", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            // Mettre le chemin du fichier dans le champ de texte
            PieceJointeF.setText(selectedFile.getAbsolutePath());
        }
    }

    @FXML
    void AjouterFeedback() {
        // Récupérer les données du formulaire
        String message = MessageF.getText();
        int note = Integer.parseInt(NoteF.getText());
        String typeFeedback = TypeFeedbackF.getValue();
        String pieceJointe = PieceJointeF.getText();
        LocalDateTime dateFeedback = LocalDateTime.now();

        // Créer un nouveau feedback
        Feedback feedback = new Feedback();
        feedback.setMessage(message);
        feedback.setNote(note);
        feedback.setTypeFeedback(typeFeedback);
        feedback.setPieceJointeF(pieceJointe);
        feedback.setDateFeedback(dateFeedback);
        feedback.setReclamationId(new Reclamation(reclamationId)); // Lier à la réclamation sélectionnée

        // Enregistrer le feedback dans la base de données
        FeedbackService feedbackService = new FeedbackService();
        feedbackService.ajouterF(feedback);

        // Afficher un message de succès
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setContentText("Le feedback a été ajouté avec succès !");
        alert.showAndWait();

        // Fermer la fenêtre
        AjouterFeed.getScene().getWindow().hide();
    }

    @FXML
    void annulerAjoutFeedback() {
        // Fermer la fenêtre
        AjouterFeed.getScene().getWindow().hide();
    }
}