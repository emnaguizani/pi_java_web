package esprit.tn.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.CheckBox;
import esprit.tn.entities.Exercice;
import esprit.tn.services.ExerciceService;
import esprit.tn.main.DatabaseConnection;
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
            String question = questionId.getText();
            String[] optionsArray = optionsId.getText().split(",");
            List<String> options = new ArrayList<>();
            for (String option : optionsArray) {
                options.add(option.trim());
            }
            String correctAnswer = correctAnswerId.getText();
            int score = Integer.parseInt(scoreId.getText());
            String imagePath = imagePathId.getText();
            boolean isMandatory = mandatoryCheckBox.isSelected();

            Exercice exercice = new Exercice(question, options, correctAnswer, score, imagePath, isMandatory);

            ExerciceService exerciceService = new ExerciceService(cnx);
            exerciceService.ajouterExercice(exercice,quizId);

            error.setText("Exercice ajouté avec succès!");
        } catch (SQLException e) {
            error.setText("Erreur lors de l'ajout de l'exercice: " + e.getMessage());
            e.printStackTrace();
        } catch (NumberFormatException e) {
            error.setText("Veuillez entrer des valeurs valides pour le score.");
        } catch (Exception e) {
            error.setText("Erreur lors de l'ajout de l'exercice.");
            e.printStackTrace();
        }
    }
}
