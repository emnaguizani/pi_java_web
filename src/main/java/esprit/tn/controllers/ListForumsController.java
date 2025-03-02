package esprit.tn.controllers;

import esprit.tn.entities.Forum;
import esprit.tn.entities.Users;
import esprit.tn.services.ForumService;
import esprit.tn.utils.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

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
    private TableColumn<Forum, String> ForumLikesDislikes;

    @FXML
    private TableColumn<Forum, Void> ForumActions;

    @FXML
    private TableView<Forum> ForumsTable;

    private final ForumService fs = new ForumService();

    @FXML
    void initialize() {

        reloadForums();


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


        ForumLikesDislikes.setCellFactory(column -> new TableCell<>() {
            private final Text likesText = new Text("👍 0");
            private final Text dislikesText = new Text("👎 0");
            private final HBox hbox = new HBox(10, likesText, dislikesText);

            {
                likesText.setOnMouseClicked(event -> {
                    Forum forum = getTableView().getItems().get(getIndex());
                    handleLike(forum);
                });

                dislikesText.setOnMouseClicked(event -> {
                    Forum forum = getTableView().getItems().get(getIndex());
                    handleDislike(forum);
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableView().getItems().isEmpty()) {
                    setGraphic(null);
                } else {
                    Forum forum = getTableView().getItems().get(getIndex());
                    Users loggedInUser = SessionManager.getInstance().getLoggedInUser();


                    likesText.setText("👍 " + forum.getLikes());
                    dislikesText.setText("👎 " + forum.getDislikes());


                    boolean hasLiked = loggedInUser != null && fs.hasUserLiked(loggedInUser.getId_user(), forum.getIdForum());
                    boolean hasDisliked = loggedInUser != null && fs.hasUserDisliked(loggedInUser.getId_user(), forum.getIdForum());


                    if (hasLiked) {
                        likesText.setOpacity(1.0);
                        dislikesText.setOpacity(0.3);
                        dislikesText.setDisable(true);
                    } else if (hasDisliked) {
                        dislikesText.setOpacity(1.0);
                        likesText.setOpacity(0.3);
                        likesText.setDisable(true);
                    } else {
                        likesText.setOpacity(1.0);
                        dislikesText.setOpacity(1.0);
                        likesText.setDisable(false);
                        dislikesText.setDisable(false);
                    }

                    setGraphic(hbox);
                }
            }
        });


        addActionButtons();
    }

    private void reloadForums() {

        ObservableList<Forum> observableForumList = FXCollections.observableList(fs.getAllForums());


        for (Forum forum : observableForumList) {
            int likesCount = fs.getLikesCount(forum.getIdForum());
            int dislikesCount = fs.getDislikesCount(forum.getIdForum());
            forum.setLikes(likesCount);
            forum.setDislikes(dislikesCount);
        }

        ForumsTable.setItems(observableForumList);
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

                    Forum forum = getTableView().getItems().get(getIndex());


                    Users loggedInUser = SessionManager.getInstance().getLoggedInUser();
                    int loggedInUserId = loggedInUser != null ? loggedInUser.getId_user() : -1;


                    if (forum.getIdAuthor() == loggedInUserId) {

                        editerButton.setVisible(true);
                        supprimerButton.setVisible(true);
                        buttonsBox.getChildren().setAll(reponsesButton, editerButton, supprimerButton);
                    } else {

                        editerButton.setVisible(false);
                        supprimerButton.setVisible(false);
                        buttonsBox.getChildren().setAll(reponsesButton);
                    }

                    setGraphic(buttonsBox);
                }
            }
        });
    }

    private void handleLike(Forum forum) {
        Users loggedInUser = SessionManager.getInstance().getLoggedInUser();
        if (loggedInUser == null) {
            showAlert(Alert.AlertType.WARNING, "Not Logged In", "You must be logged in to like a forum.");
            return;
        }

        try {
            fs.incrementLikes(forum.getIdForum(), loggedInUser.getId_user());
            forum.setLikes(fs.getLikesCount(forum.getIdForum()));
            if (fs.hasUserDisliked(loggedInUser.getId_user(), forum.getIdForum())) {
                forum.setDislikes(fs.getDislikesCount(forum.getIdForum()));
            }
            ForumsTable.refresh();
        } catch (RuntimeException e) {
            showAlert(Alert.AlertType.WARNING, "Error", e.getMessage());
        }
    }

    private void handleDislike(Forum forum) {
        Users loggedInUser = SessionManager.getInstance().getLoggedInUser();
        if (loggedInUser == null) {
            showAlert(Alert.AlertType.WARNING, "Not Logged In", "You must be logged in to dislike a forum.");
            return;
        }

        try {
            fs.incrementDislikes(forum.getIdForum(), loggedInUser.getId_user());
            forum.setDislikes(fs.getDislikesCount(forum.getIdForum()));
            if (fs.hasUserLiked(loggedInUser.getId_user(), forum.getIdForum())) {
                forum.setLikes(fs.getLikesCount(forum.getIdForum()));
            }
            ForumsTable.refresh();
        } catch (RuntimeException e) {
            showAlert(Alert.AlertType.WARNING, "Error", e.getMessage());
        }
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
        alert.setHeaderText("Are you sure you want to delete this forum?");
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

    @FXML
    public void RedirectToListCommunautes(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ListCommunautes.fxml"));
            ForumsTable.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void goToMyProfile(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Profile.fxml"));
            ForumsTable.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void gotocours(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherCours.fxml"));
            ForumsTable.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void goToQuiz(ActionEvent actionEvent) {
    }

    public void goToForum(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ListForums.fxml"));
            ForumsTable.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}