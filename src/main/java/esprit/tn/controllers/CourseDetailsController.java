package esprit.tn.controllers;

import esprit.tn.entities.Cours;
import esprit.tn.entities.Progress;
import esprit.tn.entities.StProgress;
import esprit.tn.service.ProgressService;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.control.Button;

import java.util.List;

public class CourseDetailsController {

    @FXML
    private Label titleLabel;

    @FXML
    private Label descriptionLabel;

    @FXML
    private VBox videoContainer;

    @FXML
    private CheckBox completedCheckBox;

    @FXML
    private Button submitButton;

    private Cours course;
    private int studentId = 1; // Replace this with actual student ID

    private final ProgressService progressService = new ProgressService();

    public void setCourse(Cours course) {
        this.course = course;
        // Display course details
        titleLabel.setText(course.getTitle());
        descriptionLabel.setText(course.getDescription());

        // Load video(s)
        loadVideos(course.getVideoPaths());
    }

    private void loadVideos(List<String> videoPaths) {
        // Clear any existing video players
        videoContainer.getChildren().clear();

        for (String videoPath : videoPaths) {
            Media media = new Media(videoPath);
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            MediaView mediaView = new MediaView(mediaPlayer);
            mediaPlayer.setAutoPlay(false); // Let the user control the playback

            videoContainer.getChildren().add(mediaView); // Add the video view to the container
        }
    }

    @FXML
    private void handleCompletion() {
        if (completedCheckBox.isSelected()) {
            // Update progress to "completed"
            Progress progress = new Progress(course.getIdCours(), studentId, StProgress.COMPLETED);
            progressService.update(progress);

            // Display a success message (could be a pop-up or console log)
            System.out.println("Course " + course.getTitle() + " marked as completed.");
        }
    }

    public void initialize(Cours course) {
        titleLabel.setText(course.getTitle());
        descriptionLabel.setText(course.getDescription());

    }
}
