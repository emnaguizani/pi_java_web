package esprit.tn.controllers;

import esprit.tn.entities.Cours;
import esprit.tn.entities.Progress;
import esprit.tn.service.CourService;
import esprit.tn.service.ProgressService;
import esprit.tn.entities.StProgress;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AfficherCoursEleveController implements Initializable {

    @FXML
    private TableView<Cours> courseTable;

    @FXML
    private TableColumn<Cours, String> titleColumn;

    @FXML
    private TableColumn<Cours, String> descriptionColumn;

    @FXML
    private TableColumn<Cours, String> dateColumn;

    @FXML
    private TableColumn<Cours, String> statusColumn;

    @FXML
    private TableColumn<Cours, String> levelColumn;

    @FXML
    private TableColumn<Cours, Void> enrollColumn;

    private final CourService service = new CourService();
    private final ProgressService progressService = new ProgressService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        levelColumn.setCellValueFactory(new PropertyValueFactory<>("niveau"));

        addEnrollButton();

        loadCourses();
    }

    private void loadCourses() {
        List<Cours> courses = service.findAll();
        ObservableList<Cours> courseList = FXCollections.observableArrayList(courses);
        courseTable.setItems(courseList);
    }

    // l'ajout mta3 l button dynamiquement
    private void addEnrollButton() {
        enrollColumn.setCellFactory(param -> new TableCell<>() {
            private final Button enrollButton = new Button("Enroll");

            {
                enrollButton.setOnAction(event -> {
                    Cours course = getTableView().getItems().get(getIndex());
                    handleEnroll(course);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox hbox = new HBox(enrollButton);
                    setGraphic(hbox);
                }
            }
        });
    }

    // fn mta3 l enroll
    private void handleEnroll(Cours course) {
        int studentId = 1; // bech yetbaddell fl integration bl eleve.getIdeleve()


        Progress progress = new Progress(course.getIdCours(), studentId, StProgress.INPROGRESS);
        progressService.create(progress);

        // Show an alert for successful enrollment
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Enrollment Successful");
        alert.setHeaderText("You have been enrolled in the course: " + course.getTitle());
        alert.showAndWait();

        // Now, load the course details screen
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CoursDetails.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Course Details");

            // Pass the course details to the CourseDetailsController
            CourseDetailsController controller = loader.getController();
            controller.initialize(course);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
