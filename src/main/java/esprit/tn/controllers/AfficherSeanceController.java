package esprit.tn.controllers;

import esprit.tn.entities.Seance;
import esprit.tn.services.SeanceService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;

public class AfficherSeanceController {

    @FXML
    private TableView<Seance> tableView;

    @FXML
    private TableColumn<Seance, Integer> idSeance;

    @FXML
    private TableColumn<Seance, String> titre;

    @FXML
    private TableColumn<Seance, String> contenu;

    @FXML
    private TableColumn<Seance, String> datetime;

    @FXML
    private TableColumn<Seance, Integer> idFormateur;

    @FXML
    void initialize() {
        SeanceService seanceService = new SeanceService();
        ObservableList<Seance> observableList = FXCollections.observableList(seanceService.getAll());

        tableView.setItems(observableList);

        idSeance.setCellValueFactory(new PropertyValueFactory<>("idSeance"));
        titre.setCellValueFactory(new PropertyValueFactory<>("titre"));
        contenu.setCellValueFactory(new PropertyValueFactory<>("contenu"));
        datetime.setCellValueFactory(new PropertyValueFactory<>("datetime"));
        idFormateur.setCellValueFactory(new PropertyValueFactory<>("idFormateur"));
    }

    @FXML
    void retour() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AjouterSeance.fxml"));
            tableView.getScene().getWindow().hide(); // Cache la fenêtre actuelle
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
