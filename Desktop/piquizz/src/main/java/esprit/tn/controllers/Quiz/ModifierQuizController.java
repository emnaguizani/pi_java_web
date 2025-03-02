package esprit.tn.controllers.Quiz;

import esprit.tn.entities.Quiz;
import esprit.tn.main.DatabaseConnection;
import esprit.tn.services.QuizService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.sql.SQLException;

public class ModifierQuizController {

    @FXML
    private TextField titleField;

    @FXML
    private TextArea descriptionField;

    @FXML
    private TextField durationField;

    @FXML
    private TextField totalScoreField;

    @FXML
    private TextField authorField;

    private Quiz quizToModify;

    public void setQuizData(Quiz quiz) {
        if (quiz != null) {
            quizToModify = quiz;
            titleField.setText(quiz.getTitle());
            descriptionField.setText(quiz.getDescription());
            durationField.setText(String.valueOf(quiz.getDuration()));
            totalScoreField.setText(String.valueOf(quiz.getTotalScore()));
            authorField.setText(quiz.getAuthor());
        }
    }

    @FXML
    private void saveQuiz() {
        try {
            int duration = Integer.parseInt(durationField.getText());
            int totalScore = Integer.parseInt(totalScoreField.getText());

            if (titleField.getText().trim().isEmpty()) {
                showErrorMessage("Title cannot be empty.");
                return;
            }

            if (descriptionField.getText().trim().isEmpty()) {
                showErrorMessage("Description cannot be empty.");
                return;
            }

            if (authorField.getText().trim().isEmpty()) {
                showErrorMessage("Author cannot be empty.");
                return;
            }

            quizToModify.setTitle(titleField.getText());
            quizToModify.setDescription(descriptionField.getText());
            quizToModify.setDuration(duration);
            quizToModify.setTotalScore(totalScore);
            quizToModify.setAuthor(authorField.getText());

            QuizService quizService = new QuizService(DatabaseConnection.getInstance().getCnx());
            quizService.updateQuiz(quizToModify);

            showMessage("Quiz updated successfully.");
            navigateToQuizList();
        } catch (NumberFormatException e) {
            showErrorMessage("Invalid input. Please enter valid numbers for duration and total score.");
        } catch (SQLException e) {
            showErrorMessage("Error updating the quiz: " + e.getMessage());
        }
    }

    @FXML
    private void cancelQuiz() {
        navigateToQuizList();
    }

    private void navigateToQuizList() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Quiz/AfficherQuiz.fxml"));
            Parent root = loader.load();

            AfficherQuizController afficherQuizController = loader.getController();
            afficherQuizController.loadQuizzes();

            Stage stage = (Stage) titleField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showErrorMessage("Error navigating back to quiz list: " + e.getMessage());
        }
    }

    private void showMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
