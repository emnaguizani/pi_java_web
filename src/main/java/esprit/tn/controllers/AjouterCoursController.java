package esprit.tn.controllers;

import esprit.tn.entities.Cours;
import esprit.tn.entities.Niveau;
import esprit.tn.entities.Status;
import esprit.tn.service.CourService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

    private static final CourService service = new CourService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        StatusBox.setItems(FXCollections.observableArrayList("PUBLIC", "HIDDEN"));
        LevelBox.setItems(FXCollections.observableArrayList("BEGINNER", "INTERMEDIATE", "ADVANCED"));

        titleErrorLabel.setVisible(false);
        descriptionErrorLabel.setVisible(false);

        StatusBox.setValue("PUBLIC");
        LevelBox.setValue("BEGINNER");
        importVideoButton.setOnAction(event -> importVideo());
        createButton.setOnAction(event -> addCourse());


    }

    private void importVideo() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Video Files");

        // video types illi ynajjem y7otthom

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Video Files", "*.mp4", "*.avi", "*.mov", "*.mkv")
        );

        // List bech ynajjem y7ott akther min video
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(new Stage());

        if (selectedFiles != null && !selectedFiles.isEmpty()) {
            // Convertion mta3 les file paths to a comma-separated string
            String videoPaths = selectedFiles.stream()
                    .map(File::getAbsolutePath)
                    .collect(Collectors.joining(","));
            videoPathField.setText(videoPaths);
        }


    }

    private void addCourse(){
        String title = titleField.getText();
        String description = descriptionField.getText();

        boolean isValid = true;

        if (title.isEmpty()) {
            titleErrorLabel.setVisible(true);
            return;
        }

        if (description.isEmpty()) {
            descriptionErrorLabel.setVisible(true);
            return;
        }

        if (!isValid) {
            titleErrorLabel.setVisible(false);
            descriptionErrorLabel.setVisible(false);
            return;
        }

        String creationDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        int formateurId = 1;

        String status = StatusBox.getValue();
        String niveau = LevelBox.getValue();

        String videoPaths = videoPathField.getText();

        Cours newCourse = new Cours();
        newCourse.setTitle(title);
        newCourse.setDescription(description);
        newCourse.setDateCreation(creationDate);
        newCourse.setIdFormateur(formateurId);
        newCourse.setStatus(Status.valueOf(status));
        newCourse.setNiveau(Niveau.valueOf(niveau));
        newCourse.setVideoPaths(List.of(videoPaths.split(","))); // Assuming video paths are separated by commas

        service.add(newCourse);

    }


}
