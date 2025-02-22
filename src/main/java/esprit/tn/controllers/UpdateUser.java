package esprit.tn.controllers;

import esprit.tn.entities.Users;
import esprit.tn.services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;

public class UpdateUser {
    @FXML
    private DatePicker dateofbirthid;

    @FXML
    private TextField emailid;

    @FXML
    private TextField fullnameid;

    @FXML
    private TextField passwordid;

    @FXML
    private TextField roleid;

    @FXML
    private Button updaetid;

    @FXML
    private Button backtothelistid;
    private Users currentUser;

    public void Update(ActionEvent actionEvent)throws SQLException {
        if (currentUser == null) {
            showAlert(Alert.AlertType.ERROR, "Selection Error", "No user data found.");
            return;
        }

        // Get the updated data from the form fields
        currentUser.setFullNAme(fullnameid.getText());
        currentUser.setEmail(emailid.getText());
        currentUser.setPassword(passwordid.getText());
        currentUser.setRole(roleid.getText());
        currentUser.setDateOfBirth(java.sql.Date.valueOf(dateofbirthid.getValue()));

        // Validate the input
        if (fullnameid.getText().isEmpty() || emailid.getText().isEmpty() || passwordid.getText().isEmpty() || roleid.getText().isEmpty() || dateofbirthid.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "All fields must be filled.");
            return;
        }

        if (!roleid.getText().equals("eleve") && !roleid.getText().equals("formateur")) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Role must be 'eleve' or 'formateur'.");
            return;
        }

        if (passwordid.getText().length() < 5) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Password must be at least 5 characters long.");
            return;
        }

        // Call the service to update the user in the database
        UserService userService = new UserService();
        userService.modifier(currentUser);

        // Show success alert
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("User Updated");
        alert.setContentText("User has been updated successfully.");
        alert.showAndWait();
    }

    public void backtothelist(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherUser.fxml"));
            backtothelistid.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setUserData(Users user) {
        this.currentUser = user;

        // Populate the form fields with the selected user's data
        fullnameid.setText(user.getFullName());
        emailid.setText(user.getEmail());
        passwordid.setText(user.getPassword());
        roleid.setText(user.getRole());

        // Handle DatePicker (dateofbirthid)
      //  if (user.getDateOfBirth() != null) {
     //       dateofbirthid.setValue(user.getDateOfBirth().toLocalDate());
      //  } else {
     //       dateofbirthid.setValue(null);  // Or set a default value if necessary
      //  }
    }
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
