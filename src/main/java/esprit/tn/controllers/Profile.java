package esprit.tn.controllers;

import esprit.tn.entities.Users;
import esprit.tn.services.UserService;
import esprit.tn.utils.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;
import java.sql.SQLException;

public class Profile {

    @FXML
    private Label dateOfBirthLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label fullnameLabel;

    @FXML
    private Label roleLabel;
    @FXML
    private Button getBackid;
    @FXML
    private Button editid;
    private final UserService userService = new UserService(); // Initialize here

    @FXML
    public void initialize() {
       // loadUserProfile();
        Users loggedInUser = SessionManager.getInstance().getLoggedInUser();
        if (loggedInUser != null) {
            fullnameLabel.setText(loggedInUser.getFullName());
            emailLabel.setText(loggedInUser.getEmail());
            roleLabel.setText(loggedInUser.getRole());
        } else {
            fullnameLabel.setText("Unknown");
            emailLabel.setText("Unknown");
            roleLabel.setText("Unknown");
        }
    }

    private void loadUserProfile() {
        Users user = SessionManager.getInstance().getLoggedInUser(); // Get logged-in user
        if (user != null) {
            fullnameLabel.setText(user.getFullName());
            emailLabel.setText(user.getEmail());
            dateOfBirthLabel.setText(user.getDateOfBirth().toString());
            roleLabel.setText(user.getRole());
        } else {
            System.out.println("No user is logged in.");
        }
    }
    public void goBack(ActionEvent actionEvent) {
        Users loggedInUser = SessionManager.getInstance().getLoggedInUser();

        if (loggedInUser == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "No user is logged in.");
            return;
        }

        // Determine the correct FXML based on role
        String fxmlPath = "";
        switch (loggedInUser.getRole().toLowerCase()) {
            case "eleve":
                fxmlPath = "/AfficherCoursEleve.fxml";
                break;
            case "admin":
                fxmlPath = "/AfficherUser.fxml";
                break;
            case "formateur":
                fxmlPath = "/AfficherCours.fxml";
                break;
            default:
                showAlert(Alert.AlertType.ERROR, "Navigation Error", "Unknown role.");
                return;
        }

        // Navigate back
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            getBackid.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Failed to load the previous page.");
        }
    }
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
    public void editProfile(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/editProfile.fxml"));
            editid.getScene().setRoot(root); // Change root without creating a new scene
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
