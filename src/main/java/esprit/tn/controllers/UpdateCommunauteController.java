package esprit.tn.controllers;

import esprit.tn.entities.Community;
import esprit.tn.services.CommunityService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class UpdateCommunauteController {

    @FXML
    private Button backButton;

    @FXML
    private TextArea communityDescriptionField;

    @FXML
    private TextField communityNameField;

    @FXML
    private Button createButton;

    @FXML
    private TextField creatorIdField;

    @FXML
    private ListView<Integer> membersListView;

    private Community community;
    private final CommunityService communityService = new CommunityService();

    public void setCommunity(Community community) {
        this.community = community;
        populateFields();
    }

    private void populateFields() {
        if (community != null) {
            communityNameField.setText(community.getName());
            communityDescriptionField.setText(community.getDescription());
            creatorIdField.setText(String.valueOf(community.getCreatorId()));

            ObservableList<Integer> memberIds = FXCollections.observableArrayList(community.getMembers());
            membersListView.setItems(memberIds);

            membersListView.getSelectionModel().selectAll();
        }
    }

    @FXML
    void goToListCommunautes(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ListCommunautes.fxml"));
            AnchorPane pane = (AnchorPane) backButton.getParent();
            pane.getChildren().setAll(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void updateCreateCommunity(ActionEvent event) {
        String name = communityNameField.getText().trim();
        String description = communityDescriptionField.getText().trim();
        int creatorId;
        try {
            creatorId = Integer.parseInt(creatorIdField.getText().trim());
        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Creator ID must be a valid number.");
            return;
        }

        if (name.isEmpty() || description.isEmpty()) {
            showAlert("Invalid Input", "Community name and description cannot be empty.");
            return;
        }

        List<Integer> selectedMembers = membersListView.getSelectionModel().getSelectedItems();
        if (selectedMembers.isEmpty()) {
            showAlert("Invalid Input", "You must select at least one member.");
            return;
        }

        community.setName(name);
        community.setDescription(description);
        community.setCreatorId(creatorId);
        community.setMembers(selectedMembers);

        communityService.updateCommunity(community);


        showAlert("Success", "Community updated successfully!");

        goToListCommunautes(null);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}