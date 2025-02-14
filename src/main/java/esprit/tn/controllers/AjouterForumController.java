package esprit.tn.controllers;

import esprit.tn.entities.Forum;
import esprit.tn.services.ForumService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.sql.Timestamp;
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
        Forum f = new Forum();
        f.setIdAuthor(Integer.parseInt(forumAuthorId.getText()));
        f.setTitle(forumTitle.getText());
        f.setDescription(forumDescription.getText());
        f.setDateCreation(LocalDateTime.now());

        ForumService fs = new ForumService();
        fs.ajouterForum(f);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Forum Added");
        alert.setContentText("Forum Added successfully!!");
        alert.showAndWait();

    }

}
