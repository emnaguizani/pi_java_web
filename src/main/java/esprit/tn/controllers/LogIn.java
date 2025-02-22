package esprit.tn.controllers;

import esprit.tn.entities.Users;
import esprit.tn.services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LogIn {
    @FXML
    private TextField emailid;

    @FXML
    private Button loginid;

    @FXML
    private PasswordField passwordid;

    public void login(ActionEvent actionEvent)throws IOException {
        String email = emailid.getText();
        String password = passwordid.getText();

        UserService userService = new UserService();
        Users user = userService.authenticateUser(email, password);

        if (user != null) {
            // Redirect user based on role
            String fxmlPath = "";
            switch (user.getRole().toLowerCase()) {
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
                    showAlert(Alert.AlertType.ERROR, "Login Failed", "Unknown role.");
                    return;
            }

            // Load the correct page
            try {
                Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
                emailid.getScene().setRoot(root);
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Navigation Error", "Failed to load the dashboard.");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid email or password.");
        }
    }
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
