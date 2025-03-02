package esprit.tn.controllers.Quiz;

import esprit.tn.controllers.Quiz.AfficherExerciceController;
import esprit.tn.entities.Exercice;
import esprit.tn.main.DatabaseConnection;
import esprit.tn.services.ExerciceService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class ModifierExerciceController {

    @FXML
    private TextField questionField;

    @FXML
    private TextField optionsField;

    @FXML
    private TextField correctAnswerField;

    @FXML
    private TextField scoreField;

    @FXML
    private TextField imagePathField;

    @FXML
    private CheckBox mandatoryCheckBox;

    private ExerciceService exerciceService;
    private Exercice currentExercice;
    private AfficherExerciceController afficherExerciceController;
    private int quizId;

    public void initialize() {
        exerciceService = new ExerciceService(DatabaseConnection.getInstance().getCnx());
    }

    public void setExercice(Exercice exercice) {
        this.currentExercice = exercice;
        populateFields(exercice);
    }

    public void setAfficherExerciceController(AfficherExerciceController afficherExerciceController) {
        this.afficherExerciceController = afficherExerciceController;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    private void populateFields(Exercice exercice) {
        questionField.setText(exercice.getQuestion());
        optionsField.setText(String.join(",", exercice.getOptions()));
        correctAnswerField.setText(exercice.getCorrectAnswer());
        scoreField.setText(String.valueOf(exercice.getScore()));
        imagePathField.setText(exercice.getImagePath());
        mandatoryCheckBox.setSelected(exercice.isMandatory());
    }

    @FXML
    private void handleSave() {
        try {
            if (!validateFields()) {
                return;
            }

            currentExercice.setQuestion(questionField.getText());
            currentExercice.setOptions(Arrays.asList(optionsField.getText().split(",")));
            currentExercice.setCorrectAnswer(correctAnswerField.getText());
            currentExercice.setScore(Integer.parseInt(scoreField.getText()));
            currentExercice.setImagePath(imagePathField.getText());
            currentExercice.setMandatory(mandatoryCheckBox.isSelected());

            exerciceService.updateExercice(currentExercice);

            showAlert("Success", "Exercise updated successfully.", Alert.AlertType.INFORMATION);

            if (afficherExerciceController != null) {
                afficherExerciceController.loadExercises(quizId);
            }

            closeWindow();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while updating the exercise.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) questionField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean validateFields() {
        if (questionField.getText().trim().isEmpty()) {
            showAlert("Error", "The question cannot be empty.", Alert.AlertType.ERROR);
            return false;
        }

        List<String> options = Arrays.asList(optionsField.getText().split(","));
        if (options.size() < 2) {
            showAlert("Error", "Please enter at least two options separated by commas.", Alert.AlertType.ERROR);
            return false;
        }

        if (!options.contains(correctAnswerField.getText().trim())) {
            showAlert("Error", "The correct answer must be among the options.", Alert.AlertType.ERROR);
            return false;
        }

        try {
            int score = Integer.parseInt(scoreField.getText().trim());
            if (score < 0) {
                showAlert("Error", "The score must be a positive number.", Alert.AlertType.ERROR);
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid score (integer).", Alert.AlertType.ERROR);
            return false;
        }



        return true;
    }


}
