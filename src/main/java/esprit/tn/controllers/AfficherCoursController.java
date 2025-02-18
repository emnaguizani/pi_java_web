package esprit.tn.controllers;

import esprit.tn.entities.Cours;
import esprit.tn.services.CourService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AfficherCoursController implements Initializable {

    @FXML
    private TableView<Cours> courseTable;

    @FXML
    private TableColumn<Cours, String> titleColumn;

    @FXML
    private TableColumn<Cours, String> descriptionColumn;

    @FXML
    private TableColumn<Cours, String> dateColumn;

    @FXML
    private TableColumn<Cours, String> statusColumn;

    @FXML
    private TableColumn<Cours, String> levelColumn;

    @FXML
    private TableColumn<Cours, String> videoColumn;

    @FXML
    private TableColumn<Cours, Void> modifyColumn;

    @FXML
    private TableColumn<Cours, Void> deleteColumn;

    @FXML
    private Button MyProfileId;

    @FXML
    private Button addCourseId;

    private final CourService service = new CourService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateCreation"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        levelColumn.setCellValueFactory(new PropertyValueFactory<>("niveau"));
        videoColumn.setCellValueFactory(new PropertyValueFactory<>("videoPaths"));

        // Zieda mta3 les buttons
        addModifyButton();
        addDeleteButton();

        // courses Loading
        loadCourses();

    }

    private void loadCourses() {
        List<Cours> courses = service.findAll();
        ObservableList<Cours> courseList = FXCollections.observableArrayList(courses);
        courseTable.setItems(courseList);
    }
    // lhna bech na3mlou ajout mta3 new update button m3a lcours jdid illi bech ytajouta w bech yetzedlou action illi howa openModifyCourseWindow
    private void addModifyButton() {
        Callback<TableColumn<Cours, Void>, TableCell<Cours, Void>> cellFactory = param -> new TableCell<>() {
            private final Button modifyButton = new Button("Update");

            {
                modifyButton.setOnAction(event -> {
                    Cours course = getTableView().getItems().get(getIndex());
                    openModifyCourseWindow(course);
                });
            }


            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(modifyButton);
                }
            }
        };

        modifyColumn.setCellFactory(cellFactory);
    }

    // lhna bech na3mlou ajout mta3 new delete button m3a lcours jdid illi bech ytajouta w bech yetzedlou action illi howa deleteCourse
    private void addDeleteButton() {
        Callback<TableColumn<Cours, Void>, TableCell<Cours, Void>> cellFactory = param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setOnAction(event -> {
                    Cours course = getTableView().getItems().get(getIndex());
                    deleteCourse(course);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        };

        deleteColumn.setCellFactory(cellFactory);
    }
    // kif na3mlou click 3al l update bech tall3elna interface mta3 l modification fiha les attribut a modifier avec controle de saisie
    private void openModifyCourseWindow(Cours course) {
        System.out.println("Modify Course: " + course.getTitle());
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierCours.fxml"));
            Parent root = loader.load();

            ModifierCoursController controller = loader.getController();
            controller.setCourse(course);

            Stage stage = new Stage();
            stage.setTitle("Modify Course");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // fn ta3mel delete l cours mais 9balha t5arrjelna alert kan n7ebbou nfass5ou si ok if ifPresent bech texecuti la fn delete du service w ba3d ta3mel reload lil liste mta3 les cours li 3anna
    private void deleteCourse(Cours course) {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Delete Confirmation");
        confirmationAlert.setHeaderText("Do you really want to delete this course ?");
        confirmationAlert.setContentText(course.getTitle());

        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                service.delete(course.getIdCours());
                loadCourses();
            }
        });
    }

    public void addCourse(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AjouterCours.fxml"));
            addCourseId.getScene().setRoot(root); // Change root without creating a new scene
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void goToMyProfile(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Profile.fxml"));
            MyProfileId.getScene().setRoot(root); // Change root without creating a new scene
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
