package esprit.tn.controllers;

import esprit.tn.entities.Forum;
import esprit.tn.services.ForumService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class AjouterForumController {

    @FXML
    private TextField forumAuthorId;

    @FXML
    private TextArea forumDescription;

    @FXML
    private TextField forumTitle;

    @FXML
    private TextField ChosenID;

    private String imagePath;

    @FXML
    void AjouteForum(ActionEvent event) {
        if (!validateInput()) {
            return;
        }

        Forum f = new Forum();
        f.setIdAuthor(Integer.parseInt(forumAuthorId.getText()));
        f.setTitle(forumTitle.getText().trim());
        f.setDescription(forumDescription.getText().trim());
        f.setDateCreation(LocalDateTime.now());
        f.setImagePath(imagePath);

        ForumService fs = new ForumService();
        try {
            fs.ajouterForum(f);
        } catch (SQLException e) {
            showErrorAlert("Database Error", e.getMessage());
            return;
        }

        showSuccessAlert("Forum Added", "Forum added successfully!");

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ListForums.fxml"));
            forumAuthorId.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void uploadImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            imagePath = selectedFile.getAbsolutePath();
            ChosenID.setText(imagePath);
        }
    }

    private boolean validateInput() {
        String title = forumTitle.getText().trim();
        String description = forumDescription.getText().trim();
        String authorIdText = forumAuthorId.getText().trim();


        if (containsProfanity(title)) {
            showErrorAlert("Profanity Detected", "The title contains inappropriate language.");
            return false;
        }
        if (containsProfanity(description)) {
            showErrorAlert("Profanity Detected", "The description contains inappropriate language.");
            return false;
        }

        ForumService fs = new ForumService();
        if (fs.forumExists(title)) {
            showErrorAlert("Duplicate Title", "A forum with this title already exists.");
            return false;
        }

        if (title.isEmpty()) {
            showErrorAlert("Invalid Title", "Title cannot be empty.");
            return false;
        }
        if (!title.matches("[a-zA-Z0-9 ]+")) {
            showErrorAlert("Invalid Title", "Title can only contain letters, numbers, and spaces.");
            return false;
        }

        if (description.isEmpty()) {
            showErrorAlert("Invalid Description", "Description cannot be empty.");
            return false;
        }
        if (description.length() < 20) {
            showErrorAlert("Invalid Description", "Description must be at least 20 characters long.");
            return false;
        }

        if (!authorIdText.matches("\\d+")) {
            showErrorAlert("Invalid Author ID", "Author ID must be a positive number.");
            return false;
        }
        if (SelfHarmDetector.containsSelfHarm(title)) {
            showErrorAlert("Self-Harm Detected", "The title contains self-harm-related content.");
            return false;
        }
        if (SelfHarmDetector.containsSelfHarm(description)) {
            showErrorAlert("Self-Harm Detected", "The description contains self-harm-related content.");
            return false;
        }

        return true;
    }

    private boolean containsProfanity(String text) {
        try {

            String encodedText = URLEncoder.encode(text, StandardCharsets.UTF_8.toString());


            String apiUrl = "https://www.purgomalum.com/service/containsprofanity?text=" + encodedText;


            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");


            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {

                String response = new String(connection.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
                return Boolean.parseBoolean(response);
            } else {
                System.err.println("Failed to check profanity: HTTP " + responseCode);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccessAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void resetFields(ActionEvent event) {
        forumAuthorId.clear();
        forumTitle.clear();
        forumDescription.clear();
        ChosenID.clear();
    }

    @FXML
    private void goToListForums(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ListForums.fxml"));
            forumAuthorId.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class SelfHarmDetector {


        private static final List<String> SELF_HARM_KEYWORDS = Arrays.asList(
                "self harm", "suicide", "cutting", "self injury", "end my life", "kill myself", "harm myself","kill yourself"
        );

        public static boolean containsSelfHarm(String text) {
            String lowerCaseText = text.toLowerCase();
            for (String keyword : SELF_HARM_KEYWORDS) {
                if (lowerCaseText.contains(keyword)) {
                    return true;
                }
            }
            return false;
        }
    }
}