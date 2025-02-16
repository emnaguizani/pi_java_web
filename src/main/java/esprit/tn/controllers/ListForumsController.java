package esprit.tn.controllers;


import esprit.tn.entities.Forum;
import esprit.tn.services.ForumService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ListForumsController {

    @FXML
    private Button CreateButton;

    @FXML
    private TableColumn<Forum, LocalDateTime> ForumDateCreation;

    @FXML
    private TableColumn<Forum, String> ForumDescription;

    @FXML
    private TableColumn<Forum, String> ForumTitle;

    @FXML
    private TableColumn<Forum, Void> ForumActions;

    @FXML
    private TableView<Forum> ForumsTable;

    private final ForumService fs = new ForumService();

    @FXML
    void initialize() {
        ObservableList<Forum> observableForumList = FXCollections.observableList(fs.getAllForums());
        ForumsTable.setItems(observableForumList);

        ForumTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        ForumDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy");

        ForumDateCreation.setCellValueFactory(new PropertyValueFactory<>("dateCreation"));
        ForumDateCreation.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date == null) {
                    setText(null);
                } else {
                    setText(date.format(formatter));
                }
            }
        });

        addActionButtons();
    }

    private void addActionButtons() {
        ForumActions.setCellFactory(param -> new TableCell<>() {
            private final Button reponsesButton = new Button("Responses");
            private final Button editerButton = new Button("Update");
            private final Button supprimerButton = new Button("Delete");
            private final HBox buttonsBox = new HBox(5, reponsesButton, editerButton, supprimerButton);

            {
                reponsesButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white;");
                editerButton.setStyle("-fx-background-color: #ffc107; -fx-text-fill: black;");
                supprimerButton.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white;");

                reponsesButton.setOnAction(event -> {
                    Forum forum = getTableView().getItems().get(getIndex());
                    handleReponses(forum);
                });

                editerButton.setOnAction(event -> {
                    Forum forum = getTableView().getItems().get(getIndex());
                    handleEdit(forum);
                });

                supprimerButton.setOnAction(event -> {
                    Forum forum = getTableView().getItems().get(getIndex());
                    handleDelete(forum);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(buttonsBox);
                }
            }
        });
    }

    private void handleReponses(Forum forum) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListResponses.fxml"));
            Parent root = loader.load();


            ListResponsesController controller = loader.getController();
            controller.setForum(forum);


            ForumsTable.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleEdit(Forum forum) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateForum.fxml"));
            Parent root = loader.load();


            UpdateForumController controller = loader.getController();
            controller.setForum(forum);


            ForumsTable.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleDelete(Forum forum) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("Are you sure you want to delete this forum ?");
        alert.setContentText("Forum: " + forum.getTitle());

        if (alert.showAndWait().get() == ButtonType.OK) {
            fs.deleteForum(forum.getIdForum());
            ForumsTable.getItems().remove(forum);
        }
    }

    @FXML
    public void RedirectToCreateForum(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AjouterForum.fxml"));
            ForumsTable.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }





}
