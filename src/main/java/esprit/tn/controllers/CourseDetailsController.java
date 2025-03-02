package esprit.tn.controllers;

import esprit.tn.entities.Cours;
import esprit.tn.entities.Progress;
import esprit.tn.entities.StProgress;
import esprit.tn.service.ProgressService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CourseDetailsController implements Initializable {

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

    // List to store MediaPlayer objects for the videos
    private List<MediaPlayer> mediaPlayers = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (course != null) {
            loadVideos(course.getVideoPaths());
        }    }

    public void setCourse(Cours course) {
        this.course = course;
        // Display course details
        titleLabel.setText(course.getTitle());
        descriptionLabel.setText(course.getDescription());

        // Load videos (prepare MediaPlayer objects)
        loadVideos(course.getVideoPaths());
    }

    private void loadVideos(List<String> videoPaths) {
        // Clear any existing MediaPlayer objects
        mediaPlayers.clear();

        for (String videoPath : videoPaths) {
            try {
                // Create a Media object from the video path
                Media media = new Media(new File(videoPath).toURI().toString());

                // Create a MediaPlayer for the video
                MediaPlayer mediaPlayer = new MediaPlayer(media);

                // Add the MediaPlayer to the list
                mediaPlayers.add(mediaPlayer);

                // Optional: Add a "Play" button for each video
                Button playButton = new Button("Play Video");
                playButton.setOnAction(event -> playVideo(mediaPlayer));

                // Add the button to the videoContainer
                videoContainer.getChildren().add(playButton);
            } catch (Exception e) {
                System.err.println("Error loading video: " + videoPath);
                e.printStackTrace();
            }
        }
    }

    private void playVideo(MediaPlayer mediaPlayer) {
        // Play the video
        mediaPlayer.play();
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
}