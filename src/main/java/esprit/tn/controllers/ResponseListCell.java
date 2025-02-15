/*package esprit.tn.controllers;

import esprit.tn.entities.Response;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class ResponseListCell extends ListCell<Response> {

    @FXML
    private Label responseContentLabel;

    @FXML
    private Label responseAuthorLabel;

    @FXML
    private Label responseDateLabel;

    @FXML
    private HBox responseHBox;

    public ResponseListCell() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ResponseCell.fxml"));
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void updateItem(Response response, boolean empty) {
        super.updateItem(response, empty);

        if (empty || response == null) {
            setGraphic(null);
        } else {
            responseContentLabel.setText(response.getContent());
            responseAuthorLabel.setText("By User " + response.getAuthor());
            responseDateLabel.setText(response.getCreatedAt().format(DateTimeFormatter.ofPattern("d MMMM yyyy")));
            setGraphic(responseHBox);
        }
    }
}*/