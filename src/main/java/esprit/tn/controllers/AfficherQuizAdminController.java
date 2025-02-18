package esprit.tn.controllers;

import esprit.tn.entities.Quiz;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import esprit.tn.entities.Quiz;
import esprit.tn.services.QuizService;
import esprit.tn.main.DatabaseConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


public class AfficherQuizAdminController {


        @FXML
        private TableView<Quiz> quizTableView;

        @FXML
        private TableColumn<Quiz, Integer> quizId;

        @FXML
        private TableColumn<Quiz, String> title;

        @FXML
        private TableColumn<Quiz, String> description;

        @FXML
        private TableColumn<Quiz, Integer> duration;

        @FXML
        private TableColumn<Quiz, Integer> totalScore;

        @FXML
        private TableColumn<Quiz, String> author;

        @FXML
        private TableColumn<Quiz, String> exercices;

        @FXML

        private Connection cnx = DatabaseConnection.getInstance().getCnx();
        private QuizService quizService;

        @FXML
        public void initialize() {
            quizService = new QuizService(cnx);

            quizId.setCellValueFactory(new PropertyValueFactory<>("quiz_id"));
            title.setCellValueFactory(new PropertyValueFactory<>("title"));
            description.setCellValueFactory(new PropertyValueFactory<>("description"));
            duration.setCellValueFactory(new PropertyValueFactory<>("duration"));
            totalScore.setCellValueFactory(new PropertyValueFactory<>("totalScore"));
            author.setCellValueFactory(new PropertyValueFactory<>("author"));

            exercices.setCellFactory(new Callback<TableColumn<Quiz, String>, TableCell<Quiz, String>>() {
                @Override
                public TableCell<Quiz, String> call(TableColumn<Quiz, String> param) {
                    return new TableCell<Quiz, String>() {
                        private final Button showExercisesButton = new Button("Show Exercises");

                        {
                            showExercisesButton.setOnAction(event -> {
                                Quiz quiz = getTableRow().getItem();
                                if (quiz != null) {
                                    navigateToExercisesPage(quiz);
                                }
                            });
                        }

                        @Override
                        protected void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty || getTableRow().getItem() == null) {
                                setGraphic(null);
                            } else {
                                setGraphic(showExercisesButton);
                            }
                        }
                    };
                }
            });



            loadQuizzes();
        }

        private void loadQuizzes() {
            try {
                List<Quiz> quizzes = quizService.getAllQuizzes();
                quizTableView.getItems().setAll(quizzes);
            } catch (SQLException e) {
                e.printStackTrace();
                showMessage("Error loading quizzes from the database.");
            }
        }

        private void navigateToExercisesPage(Quiz quiz) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/esprit/tn/views/exercises.fxml"));
                Parent root = loader.load();


                AfficherExerciceController exercisesController = loader.getController();



                Stage stage = (Stage) quizTableView.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
                showMessage("Error loading exercises page.");
            }
        }

        private void showMessage(String message) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        }




    }


