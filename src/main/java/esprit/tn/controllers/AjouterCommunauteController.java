package esprit.tn.controllers;

import esprit.tn.entities.Community;
import esprit.tn.services.CommunityService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AjouterCommunauteController {

    @FXML
    private TextField communityNameField;

    @FXML
    private TextArea communityDescriptionField;

    @FXML
    private TextField creatorIdField;

    @FXML
    private ListView<Integer> membersListView;

    @FXML
    private Button createButton;

    private CommunityService communityService = new CommunityService();

    @FXML
    public void initialize() {
        ObservableList<Integer> memberIds = FXCollections.observableArrayList(
                IntStream.rangeClosed(1, 20).boxed().collect(Collectors.toList())
        );
        membersListView.setItems(memberIds);
        membersListView.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.MULTIPLE);
    }

    @FXML
    private void handleCreateCommunity() {
        String name = communityNameField.getText().trim();
        String description = communityDescriptionField.getText().trim();
        int creatorId;
        try {
            creatorId = Integer.parseInt(creatorIdField.getText().trim());
        } catch (NumberFormatException e) {
            showErrorAlert("Invalid Creator ID", "Creator ID must be a valid number.");
            return;
        }

        if (name.isEmpty() || description.isEmpty()) {
            showErrorAlert("Invalid Input", "Community name and description cannot be empty.");
            return;
        }

        List<Integer> selectedMembers = membersListView.getSelectionModel().getSelectedItems();
        if (selectedMembers.isEmpty()) {
            showErrorAlert("Invalid Input", "You must select at least one member.");
            return;
        }

        Community community = new Community(name, description, creatorId);
        selectedMembers.forEach(community::addMember);

        communityService.addCommunity(community);

        showSuccessAlert("Community Created", "The community has been created successfully!");

        communityNameField.clear();
        communityDescriptionField.clear();
        creatorIdField.clear();
        membersListView.getSelectionModel().clearSelection();
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccessAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void goToListCommunautes(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ListCommunautes.fxml"));
            membersListView.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}