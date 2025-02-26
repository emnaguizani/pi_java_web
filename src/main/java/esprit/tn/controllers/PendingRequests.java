package esprit.tn.controllers;
import esprit.tn.entities.Users;
import esprit.tn.services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;


public class PendingRequests {
    @FXML
    private TableView<Users> tableView;
    @FXML private TableColumn<Users, Integer> colId;
    @FXML private TableColumn<Users, String> colFullName;
    @FXML private TableColumn<Users, String> colEmail;
    @FXML private TableColumn<Users, Button> colAction;

    @FXML
    private Button backid;
    private final UserService userService = new UserService();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id_user"));
        colFullName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        loadPendingFormateurs();
    }

    private void loadPendingFormateurs() {
        ObservableList<Users> pendingUsers = FXCollections.observableArrayList(userService.getPendingFormateurs());

        colAction.setCellFactory(tc -> new TableCell<>() {
            final Button approveButton = new Button("Approve");

            {
                approveButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
                approveButton.setOnAction(event -> {
                    Users formateur = getTableView().getItems().get(getIndex());
                    showApprovalAlert(formateur);
                });
            }

            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(approveButton);
                }
            }
        });

        tableView.setItems(pendingUsers);
    }

    private void showApprovalAlert(Users formateur) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Approve Formateur");
        alert.setHeaderText("Are you sure you want to approve " + formateur.getFullName() + "?");
        alert.setContentText("Click OK to confirm.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                userService.approveFormateur(formateur.getId_user());
                loadPendingFormateurs(); // Refresh list
            }
        });
    }

    public void gotoAfficherUsers(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherUser.fxml"));
            backid.getScene().setRoot(root); // Change root without creating a new scene
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

