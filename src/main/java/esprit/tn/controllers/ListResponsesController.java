package esprit.tn.controllers;

import esprit.tn.entities.Forum;
import esprit.tn.entities.Response;
import esprit.tn.services.ForumService;
import esprit.tn.services.ResponseService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ListResponsesController {

    @FXML
    private Text DateCreation;

    @FXML
    private TextArea DescriptionForum;

    @FXML
    private ListView<Response> ListResponses;

    @FXML
    private Text NomCreateur;

    @FXML
    private Text TitreForum;

    @FXML
    private Button backButton;

    @FXML
    private Button AjouterReponse;

    @FXML
    private Text DescriptionContainer;

    @FXML
    private ImageView ImageForum;

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

            NomCreateur.setText("Author: getAuthorNameById");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy");
            String formattedDate = forum.getDateCreation().format(formatter);
            DateCreation.setText(formattedDate);
            DescriptionContainer.setText(forum.getDescription());
            System.out.println("Image Path: " + forum.getImagePath());
            if (forum.getImagePath() != null && !forum.getImagePath().isEmpty()) {
                try {
                    File imageFile = new File(forum.getImagePath());
                    if (imageFile.exists()) {
                        Image image = new Image(imageFile.toURI().toString());
                        ImageForum.setImage(image);
                        ImageForum.setFitWidth(700);
                        ImageForum.setPreserveRatio(true);
                    } else {
                        System.err.println("Image file does not exist: " + forum.getImagePath());
                    }
                } catch (Exception e) {
                    System.err.println("Error loading image: " + e.getMessage());
                }
            } else {
                System.err.println("No image path provided for the forum.");
            }
        }
    }

    private void populateResponses() {
        if (forum != null) {

            List<Response> responses = responseService.getAllResponses(forum.getIdForum());


            Map<Integer, List<Response>> responseMap = new HashMap<>();
            for (Response response : responses) {
                int parentId = response.getParentResponseId();
                responseMap.computeIfAbsent(parentId, k -> new ArrayList<>()).add(response);
            }


            List<Response> orderedResponses = new ArrayList<>();
            buildResponseHierarchy(responseMap, 0, orderedResponses);


            ListResponses.getItems().setAll(orderedResponses);


            ListResponses.setCellFactory(param -> new ListCell<>() {
                private final Button respondButton = new Button("Respond");
                private final Button updateButton = new Button("Update");
                private final Button deleteButton = new Button("Delete");
                private final Text authorText = new Text();
                private final Text dateText = new Text();
                private final Text contentText = new Text();
                private final HBox buttonsBox = new HBox(10, respondButton, updateButton, deleteButton);
                private final HBox authorDateBox = new HBox(10, authorText, dateText);
                private final VBox vbox = new VBox(5, authorDateBox, contentText, buttonsBox);

                {
                    respondButton.setStyle("-fx-background-color: #28a745; -fx-text-fill: white;");
                    updateButton.setStyle("-fx-background-color: #ffc107; -fx-text-fill: black;");
                    deleteButton.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white;");

                    respondButton.setOnAction(event -> {
                        Response response = getItem();
                        if (response != null) {
                            goToAjouterReponse(response);
                        }
                    });

                    updateButton.setOnAction(event -> {
                        Response response = getItem();
                        if (response != null) {
                            handleUpdateResponse(response);
                        }
                    });

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
                        authorText.setText("Author: " + response.getAuthor());
                        contentText.setText(response.getContent());

                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy, HH:mm");
                        dateText.setText(response.getCreatedAt().format(formatter));


                        double indent = response.getParentResponseId() == 0 ? 0 : 20;
                        vbox.setPadding(new Insets(5, 5, 5, indent));

                        setGraphic(vbox);
                    }
                }
            });
        }
    }


    private void buildResponseHierarchy(Map<Integer, List<Response>> responseMap, int parentId, List<Response> orderedResponses) {
        List<Response> children = responseMap.get(parentId);
        if (children != null) {
            for (Response child : children) {
                orderedResponses.add(child);
                buildResponseHierarchy(responseMap, child.getIdResponse(), orderedResponses);
            }
        }
    }

    private void goToAjouterReponse(Response parentResponse) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterReponse.fxml"));
            Parent root = loader.load();

            AjouterResponseController controller = loader.getController();
            controller.setForum(forum);
            controller.setParentResponse(parentResponse);

            ListResponses.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load AjouterReponse page: " + e.getMessage());
        }
    }

    private void handleUpdateResponse(Response response) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateReponse.fxml"));
            Parent root = loader.load();

            UpdateResponseController controller = loader.getController();
            controller.setResponse(response);
            controller.setForum(forum);

            ListResponses.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load UpdateResponse page: " + e.getMessage());
        }
    }

    private void handleDeleteResponse(Response response) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Delete Response");
        alert.setContentText("Are you sure you want to delete this response?");

        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == ButtonType.OK) {
                responseService.deleteResponse(response.getIdResponse());

                ListResponses.getItems().remove(response);

                showAlert("Success", "Response deleted successfully!");
            }
        });
    }

    @FXML
    private void handleAjouterReponse(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterReponse.fxml"));
            Parent root = loader.load();

            AjouterResponseController controller = loader.getController();
            controller.setForum(forum);

            ListResponses.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToListForums(ActionEvent event) {
        try {
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
