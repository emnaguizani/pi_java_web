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

            TitreForum.setText(forum.getTitle());
            NomCreateur.setText("Author: getAuthorNameById " );
            DescriptionForum.setText(forum.getDescription());


            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy");
            String formattedDate = forum.getDateCreation().format(formatter);
            DateCreation.setText(formattedDate);
        }

        if (response != null) {

            ResponseContent.setText(response.getContent());
        }
    }

    @FXML
    private void AjouteResponse(ActionEvent event) {
        try {

            String updatedContent = ResponseContent.getText().trim();


            if (updatedContent.isEmpty()) {
                showAlert("Error", "Content cannot be empty.");
                return;
            }

            if (updatedContent.length() < 10) {
                showAlert("Error", "Content must be at least 10 characters long.");
                return;
            }

            if (responseService.responseExists(updatedContent, forum.getIdForum())) {
                showAlert("Duplicate Response", "A response with the same content already exists in this forum.");
                return;
            }


            response.setContent(updatedContent);
            responseService.updateResponse(response);


            showAlert("Success", "Response updated successfully!");


            goToListResponses();
        } catch (Exception e) {
            showAlert("Error", "Failed to update response: " + e.getMessage());
        }
    }

    @FXML
    private void resetFields(ActionEvent event) {

        ResponseContent.setText(response.getContent());
    }

    @FXML
    private void goToListResponses() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListResponses.fxml"));
            Parent root = loader.load();


            ListResponsesController controller = loader.getController();
            controller.setForum(forum);


            ResponseContent.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToListForums(ActionEvent event) {
        try {

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