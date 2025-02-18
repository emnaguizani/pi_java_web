package esprit.tn.controllers;

import esprit.tn.entities.Feedback;
import esprit.tn.entities.Reclamation;
import esprit.tn.services.FeedbackService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.time.LocalDateTime;

public class AjouterFeedbackController {
    @FXML
    private Button quizbutton;
    @FXML
    private Button forumbutton;
    @FXML
    private Button coursbutton;
    @FXML
    private Button reclamationbutton;




    @FXML
    private Button AjouterFeed;

    @FXML
    private TextArea MessageF;

    @FXML
    private TextField NoteF;

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
    }
@FXML
    void AjouterFeedback() {
        Feedback feedback = new Feedback();

        // Récupérer les données du formulaire
        String message = MessageF.getText();
        String typeFeedback = TypeFeedbackF.getValue();
        LocalDateTime dateFeedback = LocalDateTime.now();

        if (MessageF.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Champ obligatoire");
            alert.setContentText("Le champ 'Message' est obligatoire. Veuillez le remplir.");
            alert.showAndWait();
            return; // Arrêter l'exécution de la méthode
        }
        else if(NoteF.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Champ obligatoire");
            alert.setContentText("Le champ 'Note' est obligatoire. Veuillez le remplir.");
            alert.showAndWait();
            return; // Arrêter l'exécution de la méthode
        }
        //else if(feedback.getNote() < 1 || feedback.getNote() > 5) {
        //    Alert alert = new Alert(Alert.AlertType.WARNING);
        //    alert.setTitle("Champ obligatoire");
        //    alert.setContentText("La note doit être comprise entre 1 et 5. Veuillez la réctifier.");
        //    alert.showAndWait();
        //    return; // Arrêter l'exécution de la méthode
        //}

        int note = Integer.parseInt(NoteF.getText());

        // Créer un nouveau feedback
        //Feedback feedback = new Feedback();
        feedback.setMessage(message);
        feedback.setNote(note);
        feedback.setTypeFeedback(typeFeedback);
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

    public void gotocours(javafx.event.ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherCours.fxml"));
            coursbutton.getScene().setRoot(root); // Change root without creating a new scene
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void afficherReclamation(javafx.event.ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherReclamation.fxml"));
            reclamationbutton.getScene().setRoot(root); // Change root without creating a new scene
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void goToQuiz(javafx.event.ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherQuiz.fxml"));
            quizbutton.getScene().setRoot(root); // Change root without creating a new scene
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void goToForum(javafx.event.ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherQuiz.fxml"));
            forumbutton.getScene().setRoot(root); // Change root without creating a new scene
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}