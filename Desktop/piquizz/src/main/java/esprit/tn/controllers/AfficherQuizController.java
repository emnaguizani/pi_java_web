package esprit.tn.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import esprit.tn.entities.Quiz;
import esprit.tn.services.QuizService;
import esprit.tn.main.DatabaseConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class AfficherQuizController {

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

    private Connection cnx = DatabaseConnection.getInstance().getCnx();
    private QuizService quizService;

    @FXML
    public void initialize() {
        quizService = new QuizService(cnx);

        // Set up columns
        quizId.setCellValueFactory(new PropertyValueFactory<>("quiz_id"));
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        duration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        totalScore.setCellValueFactory(new PropertyValueFactory<>("totalScore"));
        author.setCellValueFactory(new PropertyValueFactory<>("author"));

        loadQuizzes();
    }


    private void loadQuizzes() {
        try {
            List<Quiz> quizzes = quizService.getAllQuizzes();
            quizTableView.getItems().setAll(quizzes);
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Error loading quizzes from the database.");
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
