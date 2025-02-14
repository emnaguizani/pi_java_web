package esprit.tn.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import esprit.tn.entities.Quiz;
import esprit.tn.entities.Exercice;
import esprit.tn.services.QuizService;
import esprit.tn.main.DatabaseConnection;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AjouterQuizController {

    @FXML
    private Button ajouterButton;

    @FXML
    private TextField authorid;

    @FXML
    private TextField creationDateid;

    @FXML
    private TextField descriptionid;

    @FXML
    private TextField durationid;

    @FXML
    private TextField titleId;

    @FXML
    private TextField totalScoreid;

    @FXML
    private Label error;

    @FXML
    private Button nextButton;

    private Connection cnx = DatabaseConnection.getInstance().getCnx();

    @FXML
    private void handleAjouterAction() {
        try {
            String title = titleId.getText();
            String description = descriptionid.getText();
            int duration = Integer.parseInt(durationid.getText());
            int totalScore = Integer.parseInt(totalScoreid.getText());

            String creationDateString = creationDateid.getText();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime creationDate = LocalDateTime.parse(creationDateString, formatter);

            String author = authorid.getText();
            List<Exercice> exercices = new ArrayList<>();

            Quiz quiz = new Quiz(title, description, duration, totalScore, creationDate, author, exercices);

            QuizService quizService = new QuizService(cnx);
            quizService.ajouterQuiz(quiz);

            int quizId = quiz.getQuiz_id();
            error.setText("Quiz ajouté avec succès!");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterExercice.fxml"));
            Pane root = loader.load();

            AjouterExerciceController ajouterExerciceController = loader.getController();

            ajouterExerciceController.setQuizId(quizId);

            Stage stage = (Stage) ajouterButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Ajouter Exercice");
            stage.show();

        } catch (SQLException e) {
            error.setText("Erreur lors de l'ajout du quiz: " + e.getMessage());
            e.printStackTrace();
        } catch (NumberFormatException e) {
            error.setText("Veuillez entrer des valeurs valides pour la durée et le score total.");
        } catch (Exception e) {
            error.setText("Erreur lors de la conversion de la date ou autres erreurs.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleNextAction(ActionEvent event) {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherQuiz.fxml"));

            // Ensure the root is loaded correctly
            Parent root = loader.load();

            // Get the current stage and set the new scene
            Stage stage = (Stage) nextButton.getScene().getWindow();
            Scene scene = new Scene(root);

            // Set the new scene and show the stage
            stage.setScene(scene);
            stage.setTitle("Afficher Quiz");
            stage.show();

        } catch (IOException e) {
            // Handle the error by displaying a message in the error label
            error.setText("Erreur lors du chargement de la page AfficherQuiz.");
            e.printStackTrace();
        }
    }


}
