package esprit.tn.controllers;

import esprit.tn.entities.Users;
import esprit.tn.services.UserService;
import esprit.tn.utils.SessionManager;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;

import java.io.IOException;

public class EditProfile {
    @FXML
    private Button backid;
    private final UserService userService = new UserService(); // Initialize user service
    private Users loggedInUser; // Store the logged-in user
    @FXML
    private DatePicker dateofbirthid;

    @FXML
    private TextField emailid;

    @FXML
    private TextField fullnameid;

    @FXML
    private TextField passwordid;

    @FXML
    private Button updateid;
    @FXML
    public void initialize() {
        // Get the logged-in user from session
        loggedInUser = SessionManager.getInstance().getLoggedInUser();

        if (loggedInUser != null) {
            // Pre-fill fields with user data
            fullnameid.setText(loggedInUser.getFullName());
            emailid.setText(loggedInUser.getEmail());
            passwordid.setText(loggedInUser.getPassword()); // Consider hiding or hashing passwords
          //  dateofbirthid.setValue(loggedInUser.getDateOfBirth().toLocalDate());
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "No user is logged in.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }


    @FXML
    public void Update(ActionEvent actionEvent) throws SQLException{
        if (loggedInUser == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "No user is logged in.");
            return;
        }

        // Validate inputs
        if (fullnameid.getText().isEmpty() || emailid.getText().isEmpty() || passwordid.getText().isEmpty() || dateofbirthid.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "All fields must be filled.");
            return;
        }

        // Update user object
        loggedInUser.setFullNAme(fullnameid.getText()); // Fixed typo: 'setFullNAme' to 'setFullName'
        loggedInUser.setEmail(emailid.getText());
        loggedInUser.setPassword(passwordid.getText()); // Hashing should be applied for security
        loggedInUser.setDateOfBirth(java.sql.Date.valueOf(dateofbirthid.getValue()));

        try {
            userService.modifier(loggedInUser); // Update in database
            SessionManager.getInstance().setLoggedInUser(loggedInUser); // Update session data

            showAlert(Alert.AlertType.INFORMATION, "Success", "Profile updated successfully!");
        } catch ( RuntimeException e) {
            // Log the error for debugging purposes
            e.printStackTrace();  // This logs the full stack trace, which helps identify the source of the error
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to update profile. Please try again.");
        }
    }

    public void goback(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherCoursEleve.fxml"));
            backid.getScene().setRoot(root); // Change root without creating a new scene
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
