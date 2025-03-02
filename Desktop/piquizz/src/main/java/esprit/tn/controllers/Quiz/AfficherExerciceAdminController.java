package esprit.tn.controllers.Quiz;

import esprit.tn.entities.Exercice;
import esprit.tn.main.DatabaseConnection;
import esprit.tn.services.ExerciceService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.stage.Stage;


import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class AfficherExerciceAdminController {


        @FXML
        private TableView<Exercice> exercicesTable;

        @FXML
        private TableColumn<Exercice, Integer> exerciceIdColumn;

        @FXML
        private TableColumn<Exercice, String> questionColumn;

        @FXML
        private TableColumn<Exercice, String> optionsColumn;

        @FXML
        private TableColumn<Exercice, String> correctAnswerColumn;

        @FXML
        private TableColumn<Exercice, Integer> scoreColumn;

        @FXML
        TableColumn<Exercice, Boolean> mandatoryColumn = new TableColumn<>("Mandatory");

        @FXML
        private TableColumn<Exercice, String> imageColumn;



        private ExerciceService exerciceService;
        private Connection cnx = DatabaseConnection.getInstance().getCnx();
        private int quizId;

        @FXML
        public void initialize() {
            exerciceIdColumn.setVisible(false);

            exerciceService = new ExerciceService(cnx);
            exerciceIdColumn.setCellValueFactory(new PropertyValueFactory<>("idE"));
            questionColumn.setCellValueFactory(new PropertyValueFactory<>("question"));
            optionsColumn.setCellValueFactory(new PropertyValueFactory<>("options"));

            correctAnswerColumn.setCellValueFactory(new PropertyValueFactory<>("correctAnswer"));
            scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
            mandatoryColumn.setCellValueFactory(new PropertyValueFactory<>("isMandatory"));
            imageColumn.setCellValueFactory(new PropertyValueFactory<>("imagePath"));

        }

        public void setQuizId(int quizId) {
            this.quizId = quizId;
            loadExercises(quizId);
        }

        public void loadExercises(int quizId) {
            try {
                List<Exercice> exercices = exerciceService.getExercicesByQuizId(quizId);
                exercicesTable.getItems().setAll(exercices);
            } catch (SQLException e) {
                e.printStackTrace();
                showMessage("Error loading exercises from the database.");
            }
        }

        private void showMessage(String message) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        }

        public void goBackToQuiz() {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Quiz/AfficherQuizAdmin.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) exercicesTable.getScene().getWindow();

                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showMessage("Error loading the quiz view.");
            }
        }
    }