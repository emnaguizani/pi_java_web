package esprit.tn.controllers.backoffice;

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

public class ListResponsesAdminController {

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
            DescriptionForum.setText(forum.getDescription());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy");
            String formattedDate = forum.getDateCreation().format(formatter);
            DateCreation.setText(formattedDate);
        }
    }

    private void populateResponses() {
        if (forum != null) {
            List<Response> responses = forumService.getResponsesForForum(forum.getIdForum());
            ListResponses.getItems().setAll(responses);

            ListResponses.setCellFactory(new Callback<>() {
                @Override
                public ListCell<Response> call(ListView<Response> param) {
                    return new ListCell<>() {

                        private final Button deleteButton = new Button("Delete");
                        private final Text authorText = new Text();
                        private final Text dateText = new Text();
                        private final Text contentText = new Text();
                        private final HBox buttonsBox = new HBox(10,  deleteButton);
                        private final HBox authorDateBox = new HBox(10, authorText, dateText);
                        private final VBox vbox = new VBox(5, authorDateBox, contentText, buttonsBox);

                        {
                            deleteButton.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white;");

                            HBox.setHgrow(buttonsBox, javafx.scene.layout.Priority.ALWAYS);
                            buttonsBox.setAlignment(Pos.CENTER_RIGHT);

                            HBox.setHgrow(dateText, javafx.scene.layout.Priority.ALWAYS);
                            dateText.setStyle("-fx-text-alignment: right;");

                            authorDateBox.setAlignment(Pos.CENTER_LEFT);



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
                                authorText.setText("Auteur: getAuthorNameById");

                                contentText.setText(response.getContent());

                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy, HH:mm");
                                String formattedDate = response.getCreatedAt().format(formatter);
                                dateText.setText(formattedDate);

                                setGraphic(vbox);
                            }
                        }
                    };
                }
            });
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/backoffice/AjouterReponseAdmin.fxml"));
            Parent root = loader.load();

           AjouterResponseAdminController controller = loader.getController();
            controller.setForum(forum);

            ListResponses.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToListForums(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/backoffice/ListForumsAdmin.fxml"));
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