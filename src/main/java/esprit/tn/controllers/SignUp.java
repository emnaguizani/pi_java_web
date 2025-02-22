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
import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            if (!isValidEmail(emailid.getText())) {
            showAlert(Alert.AlertType.ERROR, "Invalid Email", "Please enter a valid email address (e.g., example@mail.com).");
            return;
            }
        if (!isValidPassword(passwordid.getText())) {
            showAlert(Alert.AlertType.ERROR, "Weak Password", "Password must be at least 5 characters long, contain at least 2 numbers, and include at least 1 special character (* / + - $ ^ é \" ' ( - è _ ç à).");
            return;
        }
            LocalDate birthDate = dateofbirthid.getValue();
            LocalDate today = LocalDate.now();
            int age = Period.between(birthDate, today).getYears();

            if (age < 6) {
                showAlert(Alert.AlertType.ERROR, "Age Restriction", "You must be at least 6 years old to sign up.");
                return;
            }

            if (passwordid.getText().length() < 5) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Password must be at least 5 characters long.");
                return;
            }


            UserService us = new UserService();
            if (us.isEmailExists(emailid.getText())) {
                showAlert(Alert.AlertType.ERROR, "Email Already in Use", "The email you entered is already registered.");
                return; // Stop execution if email exists
            }
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

    private boolean isValidPassword(String text) {
        String passwordRegex = "^(?=.*\\d.*\\d)(?=.*[*/+\\-$^é\"'(-è_çà]).{5,}$";
        Pattern pattern = Pattern.compile(passwordRegex);
        Matcher matcher = pattern.matcher(passwordid.getText());
        return matcher.matches();
    }

    private boolean isValidEmail(String text) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(emailid.getText());
        return matcher.matches();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
 