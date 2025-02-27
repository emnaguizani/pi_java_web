package esprit.tn.controllers;

import esprit.tn.services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
public class ForgotPasswordInterface {

    @FXML
    private Button backid;

    @FXML
    private Button confirmid;

    @FXML
    private PasswordField passwordid;

    @FXML
    private PasswordField passwordid1;
    private UserService userService = new UserService();
    public void confirmPassword(ActionEvent actionEvent) {
        String emailOrPhone = VerifyPassword.getUserEmailOrPhone();
        String newPassword = passwordid.getText();
        String confirmPassword = passwordid1.getText();
         // Retrieve the email/phone

        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showAlert("Error", "Please fill in all fields.");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            showAlert("Error", "Passwords do not match!");
            return;
        }

        boolean success = userService.resetPassword(emailOrPhone, newPassword);
        if (success) {
            showAlert("Success", "Your password has been reset successfully!");
            // Close the window after reset
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/LogIn.fxml"));
                confirmid.getScene().setRoot(root); // Change root without creating a new scene
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            showAlert("Error", "Failed to reset password. Try again.");
        }
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void goBack(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/LogIn.fxml"));
            backid.getScene().setRoot(root); // Change root without creating a new scene
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
