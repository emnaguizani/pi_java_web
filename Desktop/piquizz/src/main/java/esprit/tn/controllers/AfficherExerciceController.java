package esprit.tn.controllers;

import esprit.tn.entities.Exercice;
import esprit.tn.services.ExerciceService;
import esprit.tn.main.DatabaseConnection;
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

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class AfficherExerciceController {

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

    @FXML
    private TableColumn<Exercice, String> actions;

    private ExerciceService exerciceService;
    private Connection cnx = DatabaseConnection.getInstance().getCnx();

    @FXML
    public void initialize() {
        exerciceService = new ExerciceService(cnx);
        exerciceIdColumn.setCellValueFactory(new PropertyValueFactory<>("idE"));
        questionColumn.setCellValueFactory(new PropertyValueFactory<>("question"));
        optionsColumn.setCellValueFactory(new PropertyValueFactory<>("options"));

        correctAnswerColumn.setCellValueFactory(new PropertyValueFactory<>("correctAnswer"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        mandatoryColumn.setCellValueFactory(new PropertyValueFactory<>("isMandatory"));
        imageColumn.setCellValueFactory(new PropertyValueFactory<>("imagePath"));

        actions.setCellFactory(new Callback<TableColumn<Exercice, String>, TableCell<Exercice, String>>() {
            @Override
            public TableCell<Exercice, String> call(TableColumn<Exercice, String> param) {
                return new TableCell<Exercice, String>() {
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
    }

    public void setQuizId(int quizId) {
        loadExercises(quizId);
    }

    private void loadExercises(int quizId) {
        try {
            List<Exercice> exercices = exerciceService.getExercicesByQuizId(quizId);
            exercicesTable.getItems().setAll(exercices);
        } catch (SQLException e) {
            e.printStackTrace();
            showMessage("Error loading exercises from the database.");
        }
    }


    private void handleDelete(Exercice exercice) {
        try {
            exerciceService.deleteExerciceById(exercice.getIdE());
            exercicesTable.getItems().remove(exercice);
            showMessage("Exercice deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            showMessage("Error deleting the exercice.");
        }
    }

    private void handleModify(Exercice exercice) {

        showMessage("Modify action triggered for Exercice: " + exercice.getIdE());
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherQuiz.fxml"));
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
