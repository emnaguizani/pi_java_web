package esprit.tn.controllers;

import esprit.tn.entities.Forum;
import esprit.tn.entities.Response;
import esprit.tn.services.ResponseService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class AjouterResponseController {

    @FXML
    private Text DateCreation;

    @FXML
    private TextArea DescriptionForum;

    @FXML
    private Text NomCreateur;

    @FXML
    private Button ResetForum;

    @FXML
    private TextField ResponseAuthorId;

    @FXML
    private TextArea ResponseContent;

    @FXML
    private Text TitreForum;

    @FXML
    private Button backButton;

    private Response parentResponse;

    private Forum forum;
    private final ResponseService responseService = new ResponseService();

    public void setForum(Forum forum) {
        this.forum = forum;
        populateFields();
    }

    public void setParentResponse(Response parentResponse) {
        this.parentResponse = parentResponse;
    }

    private void populateFields() {
        if (forum != null) {
            TitreForum.setText(forum.getTitle());
            NomCreateur.setText("Author: getAuthorNameById");
            DescriptionForum.setText(forum.getDescription());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy");
            String formattedDate = forum.getDateCreation().format(formatter);
            DateCreation.setText(formattedDate);
        }
    }

    @FXML
    void AjouteResponse(ActionEvent event) {
        try {
            int authorId;
            try {
                authorId = Integer.parseInt(ResponseAuthorId.getText().trim());
            } catch (NumberFormatException e) {
                showAlert("Invalid Input", "Author ID must be a valid number.");
                return;
            }

            String content = ResponseContent.getText().trim();
            if (content.isEmpty()) {
                showAlert("Invalid Input", "Content cannot be empty.");
                return;
            }

            if (containsProfanity(content)) {
                showAlert("Profanity Detected", "The response contains inappropriate language.");
                return;
            }

            if (SelfHarmDetector.containsSelfHarm(content)) {
                showAlert("Self-Harm Detected", "The response contains self-harm-related content.");
                return;
            }

            if (responseService.responseExists(content, forum.getIdForum())) {
                showAlert("Duplicate Response", "A response with the same content already exists in this forum.");
                return;
            }

            int parentResponseId = (parentResponse != null) ? parentResponse.getIdResponse() : 0;

            Response response = new Response(content, authorId, LocalDateTime.now(), parentResponseId);
            responseService.ajouter(response, forum.getIdForum());

            resetFields(null);
            showAlert("Success", "Response created successfully!");

            redirectToListResponses();
        } catch (Exception e) {
            showAlert("Error", "Error creating response: " + e.getMessage());
        }
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

    @FXML
    void goToListForums(ActionEvent event) {
        redirectToListForums();
    }

    @FXML
    void resetFields(ActionEvent event) {
        ResponseAuthorId.clear();
        ResponseContent.clear();
    }

    @FXML
    private void redirectToListResponses() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListResponses.fxml"));
            Parent root = loader.load();
            ListResponsesController controller = loader.getController();
            controller.setForum(forum);
            TitreForum.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void redirectToListForums() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ListForums.fxml"));
            TitreForum.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static class SelfHarmDetector {
        private static final List<String> SELF_HARM_KEYWORDS = Arrays.asList(
                "self harm", "suicide", "cutting", "self injury", "end my life", "kill myself", "harm myself", "kill yourself"
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
