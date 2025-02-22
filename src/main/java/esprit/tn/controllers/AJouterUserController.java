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

public class AJouterUserController {
    @FXML
    private Button backtothelistid;

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
    void ajouterUser(ActionEvent event) throws SQLException {

        Users u = new Users();

        u.setFullNAme(fullnameid.getText());
        u.setEmail(emailid.getText());
        u.setDateOfBirth(java.sql.Date.valueOf(dateofbirthid.getValue()));
        u.setPassword(passwordid.getText());
        u.setRole(roleid.getText());
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


        UserService us = new UserService();
        us.ajouter(u);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Person ADDED");
        alert.setContentText("person is added");
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
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
