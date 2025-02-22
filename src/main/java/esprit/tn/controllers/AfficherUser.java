package esprit.tn.controllers;

import esprit.tn.entities.Users;
import esprit.tn.main.DatabaseConnection;
import esprit.tn.services.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class AfficherUser {
    @FXML
    private Button coursbutton;
    @FXML
    private Button addNew;
    @FXML
    private TableView<Users> idtable;
    @FXML
    private TableColumn<?, ?> dateofbirthid;

    @FXML
    private TableColumn<?, ?> emailid;

    @FXML
    private TableColumn<?, ?> fullnameid;

    @FXML
    private TableColumn<?, ?> passwordid;

    @FXML
    private TableColumn<?, ?> roleid;

    @FXML
    private TableColumn<?, ?> userid;

    @FXML
    private Button deleteid;
    @FXML
    private Button updateid;


    @FXML
    void initialize() {
        DatabaseConnection.getInstance();
        UserService ps=new UserService();

        ObservableList<Users> observableList= FXCollections.observableList(ps.getall());

        idtable.setItems(observableList);
        userid.setCellValueFactory(new PropertyValueFactory<>("id_user"));
        fullnameid.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        emailid.setCellValueFactory(new PropertyValueFactory<>("email"));
        dateofbirthid.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        passwordid.setCellValueFactory(new PropertyValueFactory<>("password"));
        roleid.setCellValueFactory(new PropertyValueFactory<>("role"));



    }


    public void goToAjouterUser(javafx.event.ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/addNewUser.fxml"));
            addNew.getScene().setRoot(root); // Change root without creating a new scene
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void UpdateUser(javafx.event.ActionEvent actionEvent) throws IOException{
        Users selectedUser = idtable.getSelectionModel().getSelectedItem();

        if (selectedUser == null) {
            showAlert(Alert.AlertType.ERROR, "Selection Error", "Please select a user to update.");
            return;
        }

        // Load the UpdateUsers.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateUser.fxml"));
        Parent root = loader.load();

        // Get the UpdateUsersController from the loader
        UpdateUser updateController = loader.getController();

        // Pass the selected user to the UpdateUsersController
        updateController.setUserData(selectedUser);

        // Show the UpdateUsers scene (same window)
        Stage stage = (Stage) idtable.getScene().getWindow();
        stage.setScene(new Scene(root));


    }

    public void deleteUser(javafx.event.ActionEvent actionEvent) {
        Users selectedUser = idtable.getSelectionModel().getSelectedItem();

        if (selectedUser == null) {
            showAlert(Alert.AlertType.ERROR, "Selection Error", "Please select a user to delete.");
            return;
        }

        // Call the delete function from UserService
        UserService us = new UserService();
        us.supprimer(selectedUser);

        // Remove the user from the table immediately
        idtable.getItems().remove(selectedUser);

        // Show success alert
        showAlert(Alert.AlertType.INFORMATION, "Success", "User deleted successfully!");
    }

    private void showAlert(Alert.AlertType alertType, String selectionError, String s) {
        Alert alert = new Alert(alertType);
        alert.setTitle(selectionError);
        alert.setContentText(s);
        alert.showAndWait();
    }


    public void gotocours(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherCours.fxml"));
            coursbutton.getScene().setRoot(root); // Change root without creating a new scene
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void goToQuiz(ActionEvent actionEvent) {
    }

    public void goToForum(ActionEvent actionEvent) {
    }
}