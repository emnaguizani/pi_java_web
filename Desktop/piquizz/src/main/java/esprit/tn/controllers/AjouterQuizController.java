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
    private TextField Dateid;

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
            if (titleId.getText().isEmpty() || descriptionid.getText().isEmpty() ||
                    durationid.getText().isEmpty() || totalScoreid.getText().isEmpty() ||
                    Dateid.getText().isEmpty() || authorid.getText().isEmpty()) {
                error.setText("All fields must be completed!");
                return;
            }

            String title = titleId.getText().trim();
            String description = descriptionid.getText().trim();
            String author = authorid.getText().trim();

            if (title.length() < 3) {
                error.setText("The title must contain at least 3 characters.");
                return;
            }
            if (description.length() < 10) {
                error.setText("The description must contain at least 10 characters.");
                return;
            }
            if (author.length() < 3) {
                error.setText("The author must contain at least 3 characters.");
                return;
            }

            int duration;
            int totalScore;
            try {
                duration = Integer.parseInt(durationid.getText().trim());
                totalScore = Integer.parseInt(totalScoreid.getText().trim());

                if (duration <= 0 || totalScore <= 0) {
                    error.setText("Duration and total score must be positive numbers.");
                    return;
                }
            } catch (NumberFormatException e) {
                error.setText("Please enter valid numerical values ​​for duration and total score.");
                return;
            }

            LocalDateTime creationDate;
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                creationDate = LocalDateTime.parse(Dateid.getText().trim(), formatter);
            } catch (Exception e) {
                error.setText("Invalid date format! Use 'YYYY-MM-DD HH:mm:ss'.");
                return;
            }

            List<Exercice> exercices = new ArrayList<>();
            Quiz quiz = new Quiz(title, description, duration, totalScore, creationDate, author, exercices);

            QuizService quizService = new QuizService(cnx);
            quizService.ajouterQuiz(quiz);

            int quizId = quiz.getQuiz_id();
            error.setText("Quiz added successfully!");
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
            error.setText("Error adding quiz: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            error.setText("Error loading interface.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleNextAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherQuiz.fxml"));

            Parent root = loader.load();

            Stage stage = (Stage) nextButton.getScene().getWindow();
            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.setTitle("Show Quiz");
            stage.show();

        } catch (IOException e) {
            error.setText("Error loading the ShowQuiz page.");
            e.printStackTrace();
        }
    }


}
