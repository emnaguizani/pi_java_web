package esprit.tn.controllers;

import esprit.tn.entities.Users;
import esprit.tn.services.UserService;
import esprit.tn.utils.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;

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
        loadUserProfile();
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
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherCours.fxml"));
            getBackid.getScene().setRoot(root); // Change root without creating a new scene
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
