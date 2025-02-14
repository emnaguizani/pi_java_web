package esprit.tn.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
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

    @FXML
    private TableColumn<Quiz, String> actions;

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


        actions.setCellFactory(new Callback<TableColumn<Quiz, String>, TableCell<Quiz, String>>() {
            @Override
            public TableCell<Quiz, String> call(TableColumn<Quiz, String> param) {
                return new TableCell<Quiz, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            Button deleteButton = new Button("Delete");
                            deleteButton.setOnAction(event -> handleDelete(getTableRow().getItem()));

                            Button modifyButton = new Button("Modify");
                            modifyButton.setOnAction(event -> handleModify(getTableRow().getItem()));

                            HBox buttonBox = new HBox(10, deleteButton, modifyButton);
                            setGraphic(buttonBox);
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

    //
    private void showMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private void handleDelete(Quiz quiz) {
        try {
            quizService.deleteQuiz(quiz.getQuiz_id());
            loadQuizzes();
            showMessage("Quiz deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            showMessage("Error deleting the quiz.");
        }
    }


    private void handleModify(Quiz quiz) {
        System.out.println("Modify button clicked for quiz: " + quiz.getTitle());
    }
}
