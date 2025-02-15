package esprit.tn.controllers;

import esprit.tn.entities.Forum;
import esprit.tn.entities.Response;
import esprit.tn.services.ForumService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ListResponsesController {

    @FXML
    private Text DateCreation;

    @FXML
    private TextArea DescriptionForum;

    @FXML
    private ListView<String> ListResponses;

    @FXML
    private Text NomCreateur;

    @FXML
    private Text TitreForum;

    @FXML
    private Text authorID;

    @FXML
    private TextArea content;

    private Forum forum;
    private final ForumService fs = new ForumService();

    public void setForum(Forum forum) {
        this.forum = forum;
        populateFields();
    }

    private void populateFields() {
        if (forum != null) {

            TitreForum.setText(forum.getTitle());
            NomCreateur.setText("Author ID: " + forum.getIdAuthor());


            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy");
            String formattedDate = forum.getDateCreation().format(formatter);
            DateCreation.setText(formattedDate);

            DescriptionForum.setText(forum.getDescription());


            List<Response> responses = fs.getResponsesForForum(forum.getIdForum());
            forum.setResponses(responses);


            if (forum.getResponses() != null) {
                for (var response : forum.getResponses()) {
                    ListResponses.getItems().add(response.getContent());
                }
            }
        }
    }

    @FXML
    private void goToListForums(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ListForums.fxml"));
            TitreForum.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}