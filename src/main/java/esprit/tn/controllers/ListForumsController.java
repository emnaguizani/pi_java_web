package esprit.tn.controllers;

import esprit.tn.entities.Forum;
import esprit.tn.services.ForumService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ListForumsController {

    @FXML
    private Button CreateButton;

    @FXML
    private TableColumn<Forum, LocalDateTime> ForumDateCreation;

    @FXML
    private TableColumn<Forum, String> ForumDescription;

    @FXML
    private TableColumn<Forum, String> ForumTitle;

    @FXML
    private TableView<Forum> ForumsTable;

    @FXML
    void initialize(){
        ForumService fs = new ForumService();
        ObservableList<Forum> observableForumList = FXCollections.observableList(fs.getAllForums());

        ForumsTable.setItems(observableForumList);

        ForumTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        ForumDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy");
        ForumDateCreation.setCellValueFactory(new PropertyValueFactory<>("dateCreation"));
        ForumDateCreation.setCellFactory(column -> new TableCell<Forum, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date == null) {
                    setText(null);
                } else {
                    setText(date.format(formatter));
                }
            }
        });
    }

}
