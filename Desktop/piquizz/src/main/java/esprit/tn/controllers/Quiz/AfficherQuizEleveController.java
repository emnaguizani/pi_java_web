package esprit.tn.controllers.Quiz;

import esprit.tn.entities.Quiz;
import esprit.tn.services.QuizService;
import esprit.tn.services.NoteService;
import esprit.tn.main.DatabaseConnection;
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
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AfficherQuizEleveController {

    @FXML private TableView<Quiz> quizTableView;
    @FXML private TableColumn<Quiz, Integer> quiz_Id;
    @FXML private TableColumn<Quiz, String> title;
    @FXML private TableColumn<Quiz, String> description;
    @FXML private TableColumn<Quiz, Integer> duration;
    @FXML private TableColumn<Quiz, Double> note;
    @FXML private TableColumn<Quiz, Date> date;


    @FXML private TableColumn<Quiz, String> actions;

    private QuizService quizService;
    private NoteService noteService;
    private Connection cnx = DatabaseConnection.getInstance().getCnx();

    private Map<Integer, Double> quizNotes = new HashMap<>();

    @FXML
    public void initialize() throws SQLException {
        assert quiz_Id != null : "QuizId TableColumn is not injected!";
        assert title != null : "Title TableColumn is not injected!";
        assert description != null : "Description TableColumn is not injected!";

        quizService = new QuizService(cnx);
        noteService = new NoteService(cnx);

        quiz_Id.setVisible(false);
        date.setVisible(false);

        quiz_Id.setCellValueFactory(new PropertyValueFactory<>("quiz_id"));
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        date.setCellValueFactory(new PropertyValueFactory<>("dates"));

        duration.setCellValueFactory(new PropertyValueFactory<>("duration"));


        note.setCellValueFactory(cellData -> {
            int quizId = cellData.getValue().getQuiz_id();
            double noteValue = quizNotes.getOrDefault(quizId, 0.0);
            return new javafx.beans.property.SimpleDoubleProperty(noteValue).asObject();
        });

        actions.setCellFactory(param -> new TableCell<Quiz, String>() {
            private final Button participateButton = new Button("Participer");

            {
                participateButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 5px 10px; -fx-border-radius: 5px;");
                participateButton.setOnMouseEntered(e -> participateButton.setStyle("-fx-background-color: #45a049; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 5px 10px; -fx-border-radius: 5px;"));
                participateButton.setOnMouseExited(e -> participateButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 5px 10px; -fx-border-radius: 5px;"));

                participateButton.setOnAction(event -> navigateToExercicePage(getTableRow().getItem()));
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    setGraphic(participateButton);
                }
            }
        });

        loadQuizzes();
    }

    private void loadQuizzes() {
        try {
            int eleveId = 0; // Replace with the actual student ID
            List<Quiz> quizzes = quizService.getAllQuizzes();

            // Fetch the note for each quiz and store it in the map
            for (Quiz quiz : quizzes) {
                double noteValue = noteService.calculerNoteQuiz(eleveId, quiz.getQuiz_id());
                quizNotes.put(quiz.getQuiz_id(), noteValue);
            }

            quizTableView.getItems().setAll(quizzes);
        } catch (SQLException e) {
            e.printStackTrace();
            showMessage("Error loading quizzes from the database.");
        }
    }

    private void navigateToExercicePage(Quiz quiz) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Quiz/PasserExercice.fxml"));
            Parent root = loader.load();

            AfficherExerciceEleveController controller = loader.getController();
            controller.setQuizId(quiz.getQuiz_id());

            Stage stage = (Stage) quizTableView.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showMessage("Error loading the exercise page.");
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