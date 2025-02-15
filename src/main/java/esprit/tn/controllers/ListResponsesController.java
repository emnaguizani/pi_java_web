package esprit.tn.controllers;

import esprit.tn.entities.Forum;
import esprit.tn.entities.Response;
import esprit.tn.services.ForumService;
import esprit.tn.services.ResponseService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ListResponsesController {

    @FXML
    private Text DateCreation;

    @FXML
    private TextArea DescriptionForum;

    @FXML
    private ListView<Response> ListResponses; // Change to display Response objects

    @FXML
    private Text NomCreateur;

    @FXML
    private Text TitreForum;

    @FXML
    private Button backButton;

    @FXML
    private Button AjouterReponse;

    private Forum forum;
    private final ResponseService responseService = new ResponseService();


    public void setForum(Forum forum) {
        this.forum = forum;
        populateFields();
        populateResponses();
    }

    private void populateFields() {
        if (forum != null) {
            TitreForum.setText(forum.getTitle());
            NomCreateur.setText("Author ID: " + forum.getIdAuthor());
            DescriptionForum.setText(forum.getDescription());


            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy");
            String formattedDate = forum.getDateCreation().format(formatter);
            DateCreation.setText(formattedDate);
        }
    }

    private void populateResponses() {

        if (forum != null) {
            // Fetch responses for the forum

            // Set a custom cell factory to display buttons
            ListResponses.setCellFactory(new Callback<>() {
                @Override
                public ListCell<Response> call(ListView<Response> param) {
                    return new ListCell<>() {
                        private final Button updateButton = new Button("Update");
                        private final Button deleteButton = new Button("Delete");
                        private final HBox buttonsBox = new HBox(5, updateButton, deleteButton);

                        {
                            // Style the buttons
                            deleteButton.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white;");

                            // Handle update button click
                            updateButton.setOnAction(event -> {
                                Response response = getItem();
                                if (response != null) {
                                    handleUpdateResponse(response);
                                }
                            });

                            // Handle delete button click
                            deleteButton.setOnAction(event -> {
                                Response response = getItem();
                                if (response != null) {
                                    handleDeleteResponse(response);
                                }
                            });
                        }

                        @Override
                        protected void updateItem(Response response, boolean empty) {
                            super.updateItem(response, empty);
                            if (empty || response == null) {
                                setText(null);
                                setGraphic(null);
                            } else {
                                setText(response.getContent()); // Display response content
                                setGraphic(buttonsBox); // Display buttons
                            }
                        }
                    };
                }
            });
        }
    }

    private void handleUpdateResponse(Response response) {
        // Implement logic to update the response
        System.out.println("Update response: " + response.getContent());
        // You can open a new window or dialog for updating the response
    }

    private void handleDeleteResponse(Response response) {
        // Delete the response from the database
        responseService.deleteResponse(response.getIdResponse());

        // Remove the response from the ListView
        ListResponses.getItems().remove(response);

        // Show a success message
        showAlert("Success", "Response deleted successfully!");
    }

    @FXML
    private void handleAjouterReponse(ActionEvent event) {
        try {
            // Load the AjouterResponse.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterResponse.fxml"));
            Parent root = loader.load();

            // Get the controller and set the forum data
            AjouterResponseController controller = loader.getController();
            controller.setForum(forum);

            // Set the new scene
            ListResponses.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToListForums(ActionEvent event) {
        try {
            // Load the ListForums.fxml file
            Parent root = FXMLLoader.load(getClass().getResource("/ListForums.fxml"));
            ListResponses.getScene().setRoot(root);
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