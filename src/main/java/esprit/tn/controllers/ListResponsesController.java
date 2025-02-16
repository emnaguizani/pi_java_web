package esprit.tn.controllers;

import esprit.tn.entities.Forum;
import esprit.tn.entities.Response;
import esprit.tn.services.ForumService;
import esprit.tn.services.ResponseService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
    private final ForumService forumService = new ForumService();

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

            // Format the date
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy");
            String formattedDate = forum.getDateCreation().format(formatter);
            DateCreation.setText(formattedDate);
        }
    }

    private void populateResponses() {
        if (forum != null) {
            // Fetch responses for the forum using ForumService
            List<Response> responses = forumService.getResponsesForForum(forum.getIdForum());
            ListResponses.getItems().setAll(responses);

            // Set a custom cell factory to display author info, buttons, and date
            ListResponses.setCellFactory(new Callback<>() {
                @Override
                public ListCell<Response> call(ListView<Response> param) {
                    return new ListCell<>() {
                        private final Button updateButton = new Button("Update");
                        private final Button deleteButton = new Button("Delete");
                        private final Text authorText = new Text();
                        private final Text dateText = new Text();
                        private final Text contentText = new Text();
                        private final HBox buttonsBox = new HBox(10, updateButton, deleteButton);
                        private final HBox authorDateBox = new HBox(10, authorText, dateText); // Author + Date on same row
                        private final VBox vbox = new VBox(5, authorDateBox, contentText, buttonsBox);

                        {
                            // Style buttons
                            updateButton.setStyle("-fx-background-color: #ffc107; -fx-text-fill: black;");
                            deleteButton.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white;");

                            // Align the buttons box to the right
                            HBox.setHgrow(buttonsBox, javafx.scene.layout.Priority.ALWAYS);
                            buttonsBox.setAlignment(Pos.CENTER_RIGHT);

                            // Align date to the right
                            HBox.setHgrow(dateText, javafx.scene.layout.Priority.ALWAYS);
                            dateText.setStyle("-fx-text-alignment: right;");

                            // Ensure the author and date are spaced correctly
                            authorDateBox.setAlignment(Pos.CENTER_LEFT);

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
                                // Set author info
                                authorText.setText("Auteur: " + response.getAuthor());

                                // Set response content
                                contentText.setText(response.getContent());

                                // Format creation date
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy, HH:mm");
                                String formattedDate = response.getCreatedAt().format(formatter);
                                dateText.setText(formattedDate);

                                // Display VBox with author + date (same row), content, and buttons
                                setGraphic(vbox);
                            }
                        }
                    };
                }
            });

        }
    }

    private void handleUpdateResponse(Response response) {
        try {
            // Load the UpdateResponse.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateReponse.fxml"));
            Parent root = loader.load();

            // Get the controller and set the response and forum data
            UpdateResponseController controller = loader.getController();
            controller.setResponse(response); // Pass the selected response
            controller.setForum(forum); // Pass the forum

            // Set the new scene
            ListResponses.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load UpdateResponse page: " + e.getMessage());
        }
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterReponse.fxml"));
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