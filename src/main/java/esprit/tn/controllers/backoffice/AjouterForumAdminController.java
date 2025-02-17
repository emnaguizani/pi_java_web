package esprit.tn.controllers.backoffice;

import esprit.tn.entities.Forum;
import esprit.tn.services.ForumService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class AjouterForumAdminController {

    @FXML
    private TextField forumAuthorId;

    @FXML
    private TextArea forumDescription;

    @FXML
    private TextField forumTitle;

    @FXML
    void AjouteForum(ActionEvent event) {
        if (!validateInput()) {
            return;
        }


        Forum f = new Forum();
        f.setIdAuthor(Integer.parseInt(forumAuthorId.getText()));
        f.setTitle(forumTitle.getText().trim());
        f.setDescription(forumDescription.getText().trim());
        f.setDateCreation(LocalDateTime.now());

        ForumService fs = new ForumService();
        try {
            fs.ajouterForum(f);
        } catch (SQLException e) {
            showErrorAlert("Database Error", e.getMessage());
            return;
        }

        showSuccessAlert("Forum Added", "Forum added successfully!");

        try {
            Parent root= FXMLLoader.load(getClass().getResource("/backoffice/ListForumsAdmin.fxml"));
            forumAuthorId.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private boolean validateInput() {
        String title = forumTitle.getText().trim();
        String description = forumDescription.getText().trim();
        String authorIdText = forumAuthorId.getText().trim();

        if (title.isEmpty()) {
            showErrorAlert("Invalid Title", "Title cannot be empty.");
            return false;
        }
        if (!title.matches("[a-zA-Z0-9 ]+")) {
            showErrorAlert("Invalid Title", "Title can only contain letters, numbers, and spaces.");
            return false;
        }

        if (description.isEmpty()) {
            showErrorAlert("Invalid Description", "Description cannot be empty.");
            return false;
        }
        if (description.length() < 20) {
            showErrorAlert("Invalid Description", "Description must be at least 20 characters long.");
            return false;
        }


        if (!authorIdText.matches("\\d+")) {
            showErrorAlert("Invalid Author ID", "Author ID must be a positive number.");
            return false;
        }

        return true;
    }


    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private void showSuccessAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void resetFields(ActionEvent event) {
        forumAuthorId.clear();
        forumTitle.clear();
        forumDescription.clear();
    }

    @FXML
    private void goToListForums(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/backoffice/ListForumsAdmin.fxml"));
            forumAuthorId.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
