package esprit.tn.controllers;

import esprit.tn.entities.Forum;
import esprit.tn.services.ForumService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.sql.SQLException;
import java.time.LocalDateTime;

public class AjouterForumController {

    @FXML
    private TextField forumAuthorId;

    @FXML
    private TextArea forumDescription;

    @FXML
    private TextField forumTitle;

    @FXML
    void AjouteForum(ActionEvent event) {
        // Validate input fields
        if (!validateInput()) {
            return; // Stop execution if validation fails
        }

        // Create Forum object
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
    }

    // Validate user input
    private boolean validateInput() {
        String title = forumTitle.getText().trim();
        String description = forumDescription.getText().trim();
        String authorIdText = forumAuthorId.getText().trim();

        // Title validation (no special characters, not empty)
        if (title.isEmpty()) {
            showErrorAlert("Invalid Title", "Title cannot be empty.");
            return false;
        }
        if (!title.matches("[a-zA-Z0-9 ]+")) {
            showErrorAlert("Invalid Title", "Title can only contain letters, numbers, and spaces.");
            return false;
        }

        // Description validation (at least 20 characters)
        if (description.isEmpty()) {
            showErrorAlert("Invalid Description", "Description cannot be empty.");
            return false;
        }
        if (description.length() < 20) {
            showErrorAlert("Invalid Description", "Description must be at least 20 characters long.");
            return false;
        }

        // Author ID validation (must be a positive integer)
        if (!authorIdText.matches("\\d+")) {
            showErrorAlert("Invalid Author ID", "Author ID must be a positive number.");
            return false;
        }

        return true;
    }

    // Show error alert
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Show success alert
    private void showSuccessAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
