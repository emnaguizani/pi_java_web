package esprit.tn.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.CheckBox;
import esprit.tn.entities.Exercice;
import esprit.tn.services.ExerciceService;
import esprit.tn.main.DatabaseConnection;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AjouterExerciceController {

    @FXML
    private Button ajouterButton;

    @FXML
    private TextField questionId;

    @FXML
    private TextField optionsId;

    @FXML
    private TextField correctAnswerId;

    @FXML
    private TextField scoreId;

    @FXML
    private TextField imagePathId;

    @FXML
    private CheckBox mandatoryCheckBox;

    @FXML
    private Label error;

    private Connection cnx = DatabaseConnection.getInstance().getCnx();
    private int quizId;

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    @FXML
    private void handleAjouterExercice() {
        try {
            if (questionId.getText().isEmpty() || optionsId.getText().isEmpty() ||
                    correctAnswerId.getText().isEmpty() || scoreId.getText().isEmpty()) {
                error.setText("Tous les champs obligatoires doivent être remplis !");
                return;
            }

            String question = questionId.getText().trim();
            String[] optionsArray = optionsId.getText().split(",");
            List<String> options = new ArrayList<>();

            for (String option : optionsArray) {
                String trimmedOption = option.trim();
                if (!trimmedOption.isEmpty() && !options.contains(trimmedOption)) {
                    options.add(trimmedOption);
                }
            }

            String correctAnswer = correctAnswerId.getText().trim();
            String imagePath = imagePathId.getText().trim();
            boolean isMandatory = mandatoryCheckBox.isSelected();

            if (question.length() < 5) {
                error.setText("The question must contain at least 5 characters.");
                return;
            }

            if (options.size() < 2) {
                error.setText("There must be at least two distinct options.");
                return;
            }

            if (!options.contains(correctAnswer)) {
                error.setText("The correct answer must be one of the options given.");
                return;
            }

            int score;
            try {
                score = Integer.parseInt(scoreId.getText().trim());
                if (score <= 0) {
                    error.setText("The score must be a positive number.");
                    return;
                }
            } catch (NumberFormatException e) {
                error.setText("Please enter a valid number for the score.");
                return;
            }

            Exercice exercice = new Exercice(question, options, correctAnswer, score, imagePath, isMandatory);
            ExerciceService exerciceService = new ExerciceService(cnx);
            exerciceService.ajouterExercice(exercice, quizId);

            error.setText("Exercise added successfully!");

            clearFields();

        } catch (SQLException e) {
            error.setText("Error adding exercise: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            error.setText("Unexpected error adding exercise.");
            e.printStackTrace();
        }
    }

    private void clearFields() {
        questionId.clear();
        optionsId.clear();
        correctAnswerId.clear();
        scoreId.clear();
        imagePathId.clear();
        mandatoryCheckBox.setSelected(false);
    }
    @FXML
    private void handleretourAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/path/to/ajouterquiz.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
