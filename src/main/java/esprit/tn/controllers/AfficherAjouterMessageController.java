package esprit.tn.controllers;

import esprit.tn.entities.Message;
import esprit.tn.entities.Users;
import esprit.tn.services.MessageService;
import esprit.tn.services.UserService;
import esprit.tn.utils.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class AfficherAjouterMessageController {

    @FXML
    private ListView<Message> messagesListView;

    @FXML
    private TextArea messageContentField;

    @FXML
    private Button sendMessageButton;

    private int communityId;
    private final MessageService messageService = new MessageService();
    private final UserService userService = new UserService();

    public void setCommunityId(int communityId) {
        this.communityId = communityId;
        loadMessages();
    }

    private void loadMessages() {
        ObservableList<Message> messages = FXCollections.observableArrayList(messageService.getMessagesInCommunity(communityId));
        messagesListView.setItems(messages);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm");

        messagesListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Message message, boolean empty) {
                super.updateItem(message, empty);
                if (empty || message == null) {
                    setText(null);
                    setGraphic(null);
                } else {

                    TextFlow textFlow = new TextFlow();


                    Text senderText = new Text(message.getSenderName(userService) + ": ");
                    senderText.setStyle("-fx-font-weight: bold;");

                    Text contentText = new Text(message.getContent() + " . ");

                    Text dateText = new Text(message.getSentAt().format(formatter));
                    dateText.setOpacity(0.5);


                    textFlow.getChildren().addAll(senderText, contentText, dateText);

                    setGraphic(textFlow);
                }
            }
        });
    }

    @FXML
    private void handleSendMessage() {
        Users loggedInUser = SessionManager.getInstance().getLoggedInUser();
        if (loggedInUser == null) {
            showAlert("Error", "You must be logged in to send a message.");
            return;
        }
        int senderId = loggedInUser.getId_user();

        String content = messageContentField.getText().trim();
        if (content.isEmpty()) {
            showAlert("Invalid Input", "Message content cannot be empty.");
            return;
        }

        if (containsProfanity(content)) {
            showAlert("Profanity Detected", "The message contains inappropriate language.");
            return;
        }
        if (SelfHarmDetector.containsSelfHarm(content)) {
            showAlert("Self-Harm Detected", "The message contains self-harm-related content.");
            return;
        }

        Message message = new Message(content, senderId, communityId, LocalDateTime.now());
        messageService.sendMessage(message);

        messageContentField.clear();
        loadMessages();
    }

    private boolean containsProfanity(String text) {
        try {
            String encodedText = URLEncoder.encode(text, StandardCharsets.UTF_8.toString());
            String apiUrl = "https://www.purgomalum.com/service/containsprofanity?text=" + encodedText;
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                String response = new String(connection.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
                return Boolean.parseBoolean(response);
            } else {
                System.err.println("Failed to check profanity: HTTP " + responseCode);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void goToListCommunautes(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ListCommunautes.fxml"));
            messagesListView.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class SelfHarmDetector {
        private static final List<String> SELF_HARM_KEYWORDS = Arrays.asList(
                "self harm", "suicide", "cutting", "self injury", "end my life", "kill myself", "harm myself", "kill yourself"
        );

        public static boolean containsSelfHarm(String text) {
            String lowerCaseText = text.toLowerCase();
            for (String keyword : SELF_HARM_KEYWORDS) {
                if (lowerCaseText.contains(keyword)) {
                    return true;
                }
            }
            return false;
        }
    }

    public void goToMyProfile(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Profile.fxml"));
            messagesListView.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void gotocours(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherCours.fxml"));
            messagesListView.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void goToQuiz(ActionEvent actionEvent) {
    }

    public void goToForum(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ListForums.fxml"));
            messagesListView.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}