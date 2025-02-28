package esprit.tn.controllers;

import esprit.tn.entities.Users;
import esprit.tn.main.DatabaseConnection;
import esprit.tn.services.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AfficherUser {
    @FXML
    private Button coursbutton;
    @FXML
    private Button addNew;
    @FXML
    private TableView<Users> idtable;
    @FXML
    private TableColumn<?, ?> dateofbirthid;

    @FXML
    private TableColumn<?, ?> emailid;

    @FXML
    private TableColumn<?, ?> fullnameid;

    @FXML
    private TableColumn<?, ?> passwordid;

    @FXML
    private TableColumn<?, ?> roleid;
    @FXML
    private TableColumn<?, ?> access;
    @FXML
    private TableColumn<?, ?> phoneNumber;
    @FXML
    private TableColumn<?, ?> userid;
    @FXML
    private TableColumn<Users, Boolean> colAccess;
    @FXML
    private Button blockButton;
    @FXML
    private Button myProfile;
    @FXML
    private TableColumn<Users, Void> colAction;
    @FXML
    private Button deleteid;
    @FXML
    private Button updateid;
    @FXML
    private TextField searchField;
    @FXML
    private TableColumn<Users, Void> colBlock;
    @FXML
    private PieChart ageChart;
    @FXML
    private Button btnShowChart;
    private ObservableList<Users> userList = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        DatabaseConnection.getInstance();
        UserService ps=new UserService();

        ObservableList<Users> observableList= FXCollections.observableList(ps.getall());
        idtable.getStylesheets().add(getClass().getResource("/tableStyle.css").toExternalForm());
        idtable.setItems(observableList);
        userid.setCellValueFactory(new PropertyValueFactory<>("id_user"));
        fullnameid.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        emailid.setCellValueFactory(new PropertyValueFactory<>("email"));
        dateofbirthid.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        passwordid.setCellValueFactory(new PropertyValueFactory<>("password"));
        roleid.setCellValueFactory(new PropertyValueFactory<>("role"));
        access.setCellValueFactory(new PropertyValueFactory<>("access"));
        phoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
       idtable.setOnMouseClicked(this::onUserSelected);
    /*    colAction.setCellFactory(param -> new TableCell<>() {
            private final Button actionButton = new Button("Action");

            {
                actionButton.setOnAction(event -> {
                    Users user = getTableView().getItems().get(getIndex());
                    // Handle the action button click here
                    System.out.println("Action button clicked for user: " + user.getFullName());
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(actionButton);
                }
            }
        });*/

    }

    private void onUserSelected(MouseEvent mouseEvent) {
        Users selectedUser = idtable.getSelectionModel().getSelectedItem();

        // Enable the block/unblock button and update its text
        if (selectedUser != null) {
            blockButton.setDisable(false);

            // Handle null access value
            Boolean access = selectedUser.getAccess();
            if (access == null) {
                access = false; // Default to false if null
            }

            blockButton.setText(access ? "Block" : "Unblock");
        } else {
            blockButton.setDisable(true);
        }
    }


    public void goToAjouterUser(javafx.event.ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/addNewUser.fxml"));
            addNew.getScene().setRoot(root); // Change root without creating a new scene
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void UpdateUser(javafx.event.ActionEvent actionEvent) throws IOException{
        Users selectedUser = idtable.getSelectionModel().getSelectedItem();

        if (selectedUser == null) {
            showAlert(Alert.AlertType.ERROR, "Selection Error", "Please select a user to update.");
            return;
        }

        // Load the UpdateUsers.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateUser.fxml"));
        Parent root = loader.load();

        // Get the UpdateUsersController from the loader
        UpdateUser updateController = loader.getController();

        // Pass the selected user to the UpdateUsersController
        updateController.setUserData(selectedUser);

        // Show the UpdateUsers scene (same window)
        Stage stage = (Stage) idtable.getScene().getWindow();
        stage.setScene(new Scene(root));


    }

    public void deleteUser(javafx.event.ActionEvent actionEvent) {
        Users selectedUser = idtable.getSelectionModel().getSelectedItem();

        if (selectedUser == null) {
            showAlert(Alert.AlertType.ERROR, "Selection Error", "Please select a user to delete.");
            return;
        }

        // Call the delete function from UserService
        UserService us = new UserService();
        us.supprimer(selectedUser);

        // Remove the user from the table immediately
        idtable.getItems().remove(selectedUser);

        // Show success alert
        showAlert(Alert.AlertType.INFORMATION, "Success", "User deleted successfully!");
    }

    private void showAlert(Alert.AlertType alertType, String selectionError, String s) {
        Alert alert = new Alert(alertType);
        alert.setTitle(selectionError);
        alert.setContentText(s);
        alert.showAndWait();
    }


    public void gotocours(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherCours.fxml"));
            coursbutton.getScene().setRoot(root); // Change root without creating a new scene
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void goToQuiz(ActionEvent actionEvent) {
    }

    public void goToForum(ActionEvent actionEvent) {
    }

    public void goTopending(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/PendingRequests.fxml"));
            coursbutton.getScene().setRoot(root); // Change root without creating a new scene
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleBlockAction(ActionEvent actionEvent) {

    }

    public void blockUser(ActionEvent actionEvent) {
        /*Users selectedUser = idtable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            boolean newAccessStatus = !selectedUser.getAccess(); // Toggle access
            selectedUser.setAccess(newAccessStatus);
            UserService userService = new UserService();
            userService.updateUserAccess(selectedUser.getId_user(), newAccessStatus);

            // Show confirmation alert
            showAlert(newAccessStatus ? "User Unblocked" : "User Blocked",
                    "The user has been " + (newAccessStatus ? "unblocked" : "blocked") + ".");

            // Update the table after changing the access status
            loadUsers();
        } else {
            showAlert("No User Selected", "Please select a user to block or unblock.");
        }*/
        Users selectedUser = idtable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            // Handle null access value
            Boolean currentAccess = selectedUser.getAccess();
            if (currentAccess == null) {
                currentAccess = false; // Default to false if null
            }

            boolean newAccessStatus = !currentAccess; // Toggle access
            selectedUser.setAccess(newAccessStatus);

            UserService userService = new UserService();
            boolean success = userService.updateUserAccess(selectedUser.getId_user(), newAccessStatus);

            if (success) {
                // Show confirmation alert
                showAlert(newAccessStatus ? "User Unblocked" : "User Blocked",
                        "The user has been " + (newAccessStatus ? "unblocked" : "blocked") + ".");

                // Update the table after changing the access status
                loadUsers();
            } else {
                showAlert("Error", "Failed to update user access in the database.");
            }
        } else {
            showAlert("No User Selected", "Please select a user to block or unblock.");
        }   }

    private void loadUsers() {
        UserService userService = new UserService();
        ObservableList<Users> observableList = FXCollections.observableList(userService.getall());
        idtable.setItems(observableList);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }



    public void goToMyProfile(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Profile.fxml"));
            myProfile.getScene().setRoot(root); // Change root without creating a new scene
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void searchUsers(KeyEvent keyEvent) {
        String keyword = searchField.getText().trim();

        if (keyword.isEmpty()) {
            loadUsers(); // Reload original list if search is cleared
            return;
        }

        UserService userService = new UserService();
        List<Users> filteredUsers = userService.searchUsers(keyword);

        idtable.getItems().setAll(filteredUsers);
    }

    public void handleShowChart(ActionEvent actionEvent) {
        loadUserAgeStatistics();
        ageChart.setVisible(true);
    }

    private void loadUserAgeStatistics() {
        UserService userService = new UserService();
        Map<String, Integer> ageStats = userService.getUserAgeStatistics();

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        for (Map.Entry<String, Integer> entry : ageStats.entrySet()) {
            pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }

        ageChart.setData(pieChartData);

    }
}

