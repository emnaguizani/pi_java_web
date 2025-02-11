package esprit.tn.controllers;

import esprit.tn.entities.Reclamation;
import esprit.tn.services.ReclamationService;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.time.LocalDateTime;

public class AfficherReclamationController {

    @FXML
    private TableView<Reclamation> TableView;

    @FXML
    private TableColumn<Reclamation, LocalDateTime> dateCreation;

    @FXML
    private TableColumn<Reclamation, String> description;

    @FXML
    private TableColumn<Reclamation, Integer> id;

    @FXML
    private TableColumn<Reclamation, String> status;

    @FXML
    private TableColumn<Reclamation, String> titre;

    @FXML
    public void initialize() {
        ReclamationService rs = new ReclamationService();
        ObservableList<Reclamation> observableList = FXCollections.observableList(rs.getall());
        TableView.setItems(observableList);
    }
}
