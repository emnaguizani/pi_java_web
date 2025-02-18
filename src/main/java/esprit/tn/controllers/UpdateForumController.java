package esprit.tn.controllers;

import esprit.tn.entities.Forum;
import esprit.tn.services.ForumService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;

public class UpdateForumController {

    @FXML
    private Button ResetForum;

    @FXML
    private TextArea forumDescription;

    @FXML
    private TextField forumTitle;


    private Forum forum;

    public void setForum(Forum forum) {
        this.forum = forum;
        forumTitle.setText(forum.getTitle());
        forumDescription.setText(forum.getDescription());
    }

    @FXML
    void MettreAJourForum() {
        String newTitle = forumTitle.getText();
        String newDescription = forumDescription.getText();


        if (newTitle.isEmpty() || newDescription.isEmpty()) {

            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText("Invalid Input");
            errorAlert.setContentText("Title and description cannot be empty.");
            errorAlert.showAndWait();
            return;
        }


        forum.setTitle(newTitle);
        forum.setDescription(newDescription);


        ForumService fs = new ForumService();
        fs.updateForum(forum);


        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("Success");
        successAlert.setHeaderText("Forum Updated");
        successAlert.setContentText("The forum has been updated successfully.");
        successAlert.showAndWait();


        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ListForums.fxml"));
            forumTitle.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void resetFields(ActionEvent event) {
        forumTitle.clear();
        forumDescription.clear();
    }

    @FXML
    private void goToListForums(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ListForums.fxml"));
            forumTitle.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}