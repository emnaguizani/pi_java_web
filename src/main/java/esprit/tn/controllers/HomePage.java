package esprit.tn.controllers;

import javafx.event.ActionEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;

import java.io.IOException;

public class HomePage {


    @FXML
    private Button LogInid;
    @FXML
    private Button SignUpid;



    public void SignUp(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/SignUp.fxml"));
            SignUpid.getScene().setRoot(root); // Change root without creating a new scene
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void LogIn(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/LogIn.fxml"));
            LogInid.getScene().setRoot(root); // Change root without creating a new scene
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
