package esprit.tn.controllers;

import esprit.tn.services.GeminiService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.io.IOException;

public class APISummaryController {

    @FXML
    private TextArea SummaryField;

    @FXML
    private TextArea ResponseSummaryField;

    @FXML
    private Button backButton;

    private String forumContent;
    private String responsesContent;
    private final GeminiService geminiService = new GeminiService();

    @FXML
    public void initialize() {

        if (forumContent != null) {
            fetchSummary();
        }
        if (responsesContent != null) {
            fetchResponseSummary();
        }
    }

    private void fetchSummary() {
        try {
            String summary = geminiService.summarizeForumContent(forumContent);
            SummaryField.setText(summary);
        } catch (IOException e) {
            e.printStackTrace();
            SummaryField.setText("Failed to fetch forum summary: " + e.getMessage());
        }
    }

    private void fetchResponseSummary() {
        try {
            String responseSummary = geminiService.summarizeResponses(responsesContent);
            ResponseSummaryField.setText(responseSummary);
        } catch (IOException e) {
            e.printStackTrace();
            ResponseSummaryField.setText("Failed to fetch response summary: " + e.getMessage());
        }
    }

    @FXML
    void goToListForums(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ListForums.fxml"));
            SummaryField.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setForumContent(String forumContent, String responsesContent) {
        this.forumContent = forumContent;
        this.responsesContent = responsesContent;
        fetchSummary();
        fetchResponseSummary();
    }
}