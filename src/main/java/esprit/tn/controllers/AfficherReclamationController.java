package esprit.tn.controllers;

import esprit.tn.entities.Reclamation;
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

}
