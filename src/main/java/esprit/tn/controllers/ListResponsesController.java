/*package esprit.tn.controllers;

import esprit.tn.entities.Forum;
import esprit.tn.entities.Response;
import esprit.tn.services.ForumService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ListResponsesController implements Initializable {

    @FXML
    private Label forumTitleLabel;

    @FXML
    private TextArea forumDescriptionTextArea;

    @FXML
    private Label forumCreationDateLabel;

    @FXML
    private ListView<Response> responsesListView;

    @FXML
    private TextField responseTextField;

    private Forum forum;

    public void setForum(Forum forum) {
        this.forum = forum;
        updateUI();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize the ListView to display responses
        responsesListView.setCellFactory(param -> new ResponseListCell());
    }

    private void updateUI() {
        if (forum != null) {
            // Set forum details
            forumTitleLabel.setText(forum.getTitle());
            forumDescriptionTextArea.setText(forum.getDescription());
            forumCreationDateLabel.setText(forum.getDateCreation().format(DateTimeFormatter.ofPattern("d MMMM yyyy")));

            // Load responses into the ListView
            responsesListView.getItems().setAll(forum.getResponses());
        }
    }

    @FXML
    private void handleAddResponse() {
        String responseText = responseTextField.getText().trim();
        if (!responseText.isEmpty()) {
            // Create a new response
            Response newResponse = new Response(responseText, 1, LocalDateTime.now()); // Replace 1 with the actual author ID
            forum.addResponse(newResponse);

            // Add the response to the ListView
            responsesListView.getItems().add(newResponse);

            // Clear the input field
            responseTextField.clear();
        }
    }


}*/