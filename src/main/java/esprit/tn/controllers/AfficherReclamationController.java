package esprit.tn.controllers;

import esprit.tn.entities.Reclamation;
import esprit.tn.services.ReclamationService;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDateTime;

public class AfficherReclamationController {

    @FXML
    private TableView<Reclamation> TableViewR;

    @FXML
    private TableColumn<Reclamation, LocalDateTime> dateCreationR;

    @FXML
    private TableColumn<Reclamation, String> descriptionR;

    @FXML
    private TableColumn<Reclamation, Integer> idR;

    @FXML
    private TableColumn<Reclamation, String> statusR;

    @FXML
    private TableColumn<Reclamation, String> titreR;

    @FXML
    void initialize() {
        ReclamationService rs = new ReclamationService();

        ObservableList<Reclamation> observableList = FXCollections.observableList(rs.getall());

        TableViewR.setItems(observableList);
        idR.setCellValueFactory(new PropertyValueFactory<>("id"));
        titreR.setCellValueFactory(new PropertyValueFactory<>("titre"));
        descriptionR.setCellValueFactory(new PropertyValueFactory<>("description"));
        statusR.setCellValueFactory(new PropertyValueFactory<>("status"));
        dateCreationR.setCellValueFactory(new PropertyValueFactory<>("dateCreation"));
    }
}