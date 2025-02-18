package esprit.tn.controllers;

import esprit.tn.entities.Users;
import esprit.tn.services.UserService;
import esprit.tn.utils.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class SignUp {
    @FXML
    private DatePicker dateofbirthid;

    @FXML
    private TextField emailid;

    @FXML
    private TextField fullnameid;

    @FXML
    private PasswordField passwordid;

    @FXML
    private TextField roleid;

    @FXML
    private Button signupid;
    @FXML
    private Button signUp;
    public void signUp(ActionEvent actionEvent) throws SQLException {
        Users u = new Users();

        u.setFullNAme(fullnameid.getText());
        u.setEmail(emailid.getText());
        u.setDateOfBirth(java.sql.Date.valueOf(dateofbirthid.getValue()));
        u.setPassword(passwordid.getText());
        u.setRole("eleve");
        if (fullnameid.getText().isEmpty() || emailid.getText().isEmpty() || passwordid.getText().isEmpty() || dateofbirthid.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "All fields must be filled.");
            return;
        }



        if (passwordid.getText().length() < 5) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Password must be at least 5 characters long.");
            return;
        }


        UserService us = new UserService();
        SessionManager.getInstance().setLoggedInUser(u);
        try {
            // Try to add the user to the database
            us.ajouter(u);

            // If no exception is thrown, the user was added successfully
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Person ADDED");
            alert.setContentText("Person is added successfully!");
            alert.showAndWait();

            // Load the new page after successful addition
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/AfficherCours.fxml"));
                signupid.getScene().setRoot(root);
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Navigation Error", "Failed to load the new page.");
            }

        } catch (RuntimeException e) {
            // Handle SQL exceptions that may have occurred during insertion
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to add the person.");
        }

    }
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
