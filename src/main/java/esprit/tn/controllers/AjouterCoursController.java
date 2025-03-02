package esprit.tn.controllers;

import esprit.tn.entities.Cours;
import esprit.tn.entities.Niveau;
import esprit.tn.entities.Status;
import esprit.tn.service.CourService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AjouterCoursController implements Initializable {

    @FXML
    private TextField titleField;

    @FXML
    private Label titleErrorLabel;

    @FXML
    private TextArea descriptionField;

    @FXML
    private Label descriptionErrorLabel;

    @FXML
    private ChoiceBox<String> StatusBox;

    @FXML
    private ChoiceBox<String> LevelBox;
    @FXML
    private TextField videoPathField;

    @FXML
    private Button importVideoButton;

    @FXML
    private Button createButton;

    @FXML
    private Label videoErrorLabel;

    private static final CourService service = new CourService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        StatusBox.setItems(FXCollections.observableArrayList("PUBLIC", "HIDDEN"));
        LevelBox.setItems(FXCollections.observableArrayList("BEGINNER", "INTERMEDIATE", "ADVANCED"));

        titleErrorLabel.setVisible(false);
        descriptionErrorLabel.setVisible(false);
        videoErrorLabel.setVisible(false);

        StatusBox.setValue("PUBLIC");
        LevelBox.setValue("BEGINNER");
        importVideoButton.setOnAction(event -> importVideo());
        createButton.setOnAction(event -> addCourse());


    }

    private void importVideo() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Video Files");

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Video Files", "*.mp4", "*.avi", "*.mov", "*.mkv")
        );

        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(new Stage());

        if (selectedFiles != null && !selectedFiles.isEmpty()) {
            List<String> videoPaths = selectedFiles.stream()
                    .map(File::getAbsolutePath)
                    .collect(Collectors.toList());

            // Store the video paths in a shared object (e.g., a Cours object)
            Cours course = new Cours();
            course.setVideoPaths(videoPaths);

            // Optionally, store the course object in a shared service or data model
            SharedDataModel.setCurrentCourse(course);

            // Update the UI (optional)
            videoPathField.setText(String.join(",", videoPaths));
        }
    }

    private void addCourse(){
        String title = titleField.getText();
        String description = descriptionField.getText();


        if (title.isEmpty()) {
            titleErrorLabel.setVisible(true);
            return;
        }
        titleErrorLabel.setVisible(false);


        if (description.isEmpty()) {
            descriptionErrorLabel.setVisible(true);
            return;
        }
        descriptionErrorLabel.setVisible(false);


        String creationDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        int formateurId = 1; // bech yetbaddell fl integration bl formateur.getIdformateur()

        String status = StatusBox.getValue();
        String niveau = LevelBox.getValue();

        String videoPaths = videoPathField.getText();

        if (videoPaths.isEmpty()) {
            videoErrorLabel.setVisible(true);
            return;
        }
        videoErrorLabel.setVisible(false);

        Cours newCourse = new Cours();
        newCourse.setTitle(title);
        newCourse.setDescription(description);
        newCourse.setDateCreation(creationDate);
        newCourse.setIdFormateur(formateurId);
        newCourse.setStatus(Status.valueOf(status));
        newCourse.setNiveau(Niveau.valueOf(niveau));
        newCourse.setVideoPaths(List.of(videoPaths.split(",")));

        service.add(newCourse);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Course Added");
        alert.setContentText("The course has been successfully added.");
        alert.showAndWait();

    }
    public void back(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }



}
