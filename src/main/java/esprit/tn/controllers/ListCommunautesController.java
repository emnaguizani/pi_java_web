package esprit.tn.controllers;

import esprit.tn.entities.Community;
import esprit.tn.services.CommunityService;
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
    private TableColumn<Community, Void> CommunityActions;

    @FXML
    private TableView<Community> CommunitiesTable;

    private final CommunityService communityService = new CommunityService();

    @FXML
    void initialize() {
        ObservableList<Community> observableCommunityList = FXCollections.observableList(communityService.getAllCommunities());
        CommunitiesTable.setItems(observableCommunityList);

        CommunityName.setCellValueFactory(new PropertyValueFactory<>("name"));
        CommunityDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        CommunityCreatorId.setCellValueFactory(new PropertyValueFactory<>("creatorId"));

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
}
