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
import javafx.scene.text.Text;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class UpdateResponseController {

    @FXML
    private Text TitreForum;

    @FXML
    private Text NomCreateur;

    @FXML
    private Text DateCreation;

    @FXML
    private TextArea DescriptionForum;

    @FXML
    private TextArea ResponseContent;

    @FXML
    private Button backButton;

    @FXML
    private Button ResetForum;

    private Response response;
    private Forum forum;
    private final ResponseService responseService = new ResponseService();

    public void setResponse(Response response) {
        this.response = response;
        populateFields();
    }

    public void setForum(Forum forum) {
        this.forum = forum;
        populateFields();
    }

    private void populateFields() {
        if (forum != null) {
            // Populate forum details
            TitreForum.setText(forum.getTitle());
            NomCreateur.setText("Author ID: " + forum.getIdAuthor());
            DescriptionForum.setText(forum.getDescription());

            // Format the date
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy");
            String formattedDate = forum.getDateCreation().format(formatter);
            DateCreation.setText(formattedDate);
        }

        if (response != null) {
            // Populate the old response content
            ResponseContent.setText(response.getContent());
        }
    }

    @FXML
    private void AjouteResponse(ActionEvent event) {
        try {
            // Get the updated content
            String updatedContent = ResponseContent.getText().trim();

            // Validate the content
            if (updatedContent.isEmpty()) {
                showAlert("Error", "Content cannot be empty.");
                return;
            }

            if (updatedContent.length() < 10) {
                showAlert("Error", "Content must be at least 10 characters long.");
                return;
            }

            // Update the response
            response.setContent(updatedContent);
            responseService.updateResponse(response);

            // Show a success message
            showAlert("Success", "Response updated successfully!");

            // Navigate back to the ListResponses page
            goToListResponses();
        } catch (Exception e) {
            showAlert("Error", "Failed to update response: " + e.getMessage());
        }
    }

    @FXML
    private void resetFields(ActionEvent event) {
        // Reset the response content field
        ResponseContent.setText(response.getContent());
    }

    @FXML
    private void goToListResponses() {
        try {
            // Load the ListResponses.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListResponses.fxml"));
            Parent root = loader.load();

            // Get the controller and set the forum data
            ListResponsesController controller = loader.getController();
            controller.setForum(forum);

            // Set the new scene
            ResponseContent.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToListForums(ActionEvent event) {
        try {
            // Load the ListForums.fxml file
            Parent root = FXMLLoader.load(getClass().getResource("/ListForums.fxml"));
            ResponseContent.getScene().setRoot(root);
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
}