package esprit.tn.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherCoursEleve.fxml"));
            loginid.getScene().setRoot(root); // Change root without creating a new scene
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
