package esprit.tn.controllers;

import esprit.tn.entities.Community;
import esprit.tn.entities.Users;
import esprit.tn.services.CommunityService;
import esprit.tn.services.UserService;
import esprit.tn.utils.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class ListCommunautesController {

    @FXML
    private Button CreateButton;

    @FXML
    private TableColumn<Community, String> CommunityName;

    @FXML
    private TableColumn<Community, String> CommunityDescription;

    @FXML
    private TableColumn<Community, Integer> CommunityCreatorId;

    @FXML
    private TableColumn<Community, String> CommunityMembers;

    @FXML
    private TableColumn<Community, Void> CommunityActions;

    @FXML
    private TableView<Community> CommunitiesTable;

    private final CommunityService communityService = new CommunityService();
    private final UserService userService = new UserService();

    @FXML
    void initialize() {

        Users loggedInUser = SessionManager.getInstance().getLoggedInUser();
        if (loggedInUser == null) {
            showAlert("Error", "You must be logged in to view communities.");
            return;
        }

        ObservableList<Community> observableCommunityList = FXCollections.observableList(
                communityService.getCommunitiesForUser(loggedInUser.getId_user())
        );
        CommunitiesTable.setItems(observableCommunityList);

        CommunityName.setCellValueFactory(new PropertyValueFactory<>("name"));
        CommunityDescription.setCellValueFactory(new PropertyValueFactory<>("description"));


        CommunityMembers.setCellValueFactory(cellData -> {
            Community community = cellData.getValue();
            return javafx.beans.binding.Bindings.createStringBinding(() ->
                    community.getMembersNamesAsString(userService)
            );
        });

        addActionButtons();
    }

    private void addActionButtons() {
        CommunityActions.setCellFactory(param -> new TableCell<>() {
            private final Button participateButton = new Button("Participate");
            private final Button updateButton = new Button("Update");
            private final Button deleteButton = new Button("Delete");
            private final HBox buttonsBox = new HBox(5, participateButton, updateButton, deleteButton);

            {
                participateButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white;");
                updateButton.setStyle("-fx-background-color: #ffc107; -fx-text-fill: black;");
                deleteButton.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white;");

                participateButton.setOnAction(event -> {
                    Community community = getTableView().getItems().get(getIndex());
                    handleParticipate(community);
                });

                updateButton.setOnAction(event -> {
                    Community community = getTableView().getItems().get(getIndex());
                    handleUpdate(community);
                });

                deleteButton.setOnAction(event -> {
                    Community community = getTableView().getItems().get(getIndex());
                    handleDelete(community);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Community community = getTableView().getItems().get(getIndex());

                    Users loggedInUser = SessionManager.getInstance().getLoggedInUser();
                    int loggedInUserId = loggedInUser != null ? loggedInUser.getId_user() : -1;

                    if (loggedInUserId == community.getCreatorId()) {
                        updateButton.setVisible(true);
                        deleteButton.setVisible(true);
                        buttonsBox.getChildren().setAll(participateButton, updateButton, deleteButton);
                    } else {
                        updateButton.setVisible(false);
                        deleteButton.setVisible(false);
                        buttonsBox.getChildren().setAll(participateButton);
                    }

                    setGraphic(buttonsBox);
                }
            }
        });
    }

    private void handleParticipate(Community community) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherAjouterMessage.fxml"));
            Parent root = loader.load();

            AfficherAjouterMessageController controller = loader.getController();
            controller.setCommunityId(community.getId());

            Stage stage = (Stage) CommunitiesTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load messages page: " + e.getMessage());
        }
    }

    private void handleUpdate(Community community) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateCommunaute.fxml"));
            Parent root = loader.load();

            UpdateCommunauteController controller = loader.getController();
            controller.setCommunity(community);

            CommunitiesTable.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load Update Community page: " + e.getMessage());
        }
    }

    private void handleDelete(Community community) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("Are you sure you want to delete this community?");
        alert.setContentText("Community: " + community.getName());

        if (alert.showAndWait().get() == ButtonType.OK) {
            communityService.deleteCommunity(community.getId());
            CommunitiesTable.getItems().remove(community);
        }
    }

    @FXML
    public void RedirectToCreateCommunity(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AjouterCommunaute.fxml"));
            CommunitiesTable.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void goToListForums(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ListForums.fxml"));
            CommunitiesTable.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void goToMyProfile(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Profile.fxml"));
            CommunitiesTable.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void gotocours(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherCours.fxml"));
            CommunitiesTable.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void goToQuiz(ActionEvent actionEvent) {
    }

    public void goToForum(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ListForums.fxml"));
            CommunitiesTable.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}