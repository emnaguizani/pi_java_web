package esprit.tn.controllers;

import esprit.tn.entities.Cours;
import esprit.tn.entities.Niveau;
import esprit.tn.entities.Status;
import esprit.tn.services.CourService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ModifierCoursController implements Initializable {

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
    private Button updateButton;

    @FXML
    private Label videoErrorLabel;

    private static final CourService service = new CourService();
    private Cours currentCourse;

    public void setCourse(Cours course) {
        this.currentCourse = course;
        getFields();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        StatusBox.setItems(FXCollections.observableArrayList("PUBLIC", "HIDDEN"));
        LevelBox.setItems(FXCollections.observableArrayList("BEGINNER", "INTERMEDIATE", "ADVANCED"));

        titleErrorLabel.setVisible(false);
        descriptionErrorLabel.setVisible(false);
        videoErrorLabel.setVisible(false);

        importVideoButton.setOnAction(event -> importVideo());
        updateButton.setOnAction(event -> updateCourse());
    }

    private void getFields() {
        if (currentCourse != null) {
            titleField.setText(currentCourse.getTitle());
            descriptionField.setText(currentCourse.getDescription());
            StatusBox.setValue(currentCourse.getStatus().toString());
            LevelBox.setValue(currentCourse.getNiveau().toString());
            videoPathField.setText(String.join(",", currentCourse.getVideoPaths()));
        }
    }

    private void importVideo() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Video Files");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Video Files", "*.mp4", "*.avi", "*.mov", "*.mkv")
        );
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(new Stage());

        if (selectedFiles != null && !selectedFiles.isEmpty()) {
            String videoPaths = selectedFiles.stream()
                    .map(File::getAbsolutePath)
                    .collect(Collectors.joining(","));
            videoPathField.setText(videoPaths);
        }
    }

    private void updateCourse() {
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

        String status = StatusBox.getValue();
        String niveau = LevelBox.getValue();
        String videoPaths = videoPathField.getText();

        if (videoPaths.isEmpty()) {
            videoErrorLabel.setVisible(true);
            return;
        }
        videoErrorLabel.setVisible(false);

        currentCourse.setTitle(title);
        currentCourse.setDescription(description);
        currentCourse.setStatus(Status.valueOf(status));
        currentCourse.setNiveau(Niveau.valueOf(niveau));
        currentCourse.setVideoPaths(List.of(videoPaths.split(",")));

        service.update(currentCourse);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Course Updated");
        alert.setContentText("The course has been successfully updated.");
        alert.showAndWait();
    }
}
