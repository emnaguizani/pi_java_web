package esprit.tn.controllers;

import esprit.tn.entities.Cours;
import esprit.tn.services.CourService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AfficherCoursAdminController implements Initializable {


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


    private final CourService service = new CourService();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateCreation"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        levelColumn.setCellValueFactory(new PropertyValueFactory<>("niveau"));
        videoColumn.setCellValueFactory(new PropertyValueFactory<>("videoPaths"));



        // courses Loading
        loadCourses();

    }

    private void loadCourses() {
        List<Cours> courses = service.findAll();
        ObservableList<Cours> courseList = FXCollections.observableArrayList(courses);
        courseTable.setItems(courseList);
    }







}
