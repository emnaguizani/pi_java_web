package esprit.tn.controllers;

import esprit.tn.entities.Reclamation;
import esprit.tn.services.ReclamationService;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
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

    @FXML
    void AjouterRecAff(ActionEvent event) {
        try {
            // Charger le fichier FXML de la page AjouterReclamation
            Parent root = FXMLLoader.load(getClass().getResource("/AjouterReclamation.fxml"));
            Scene scene = new Scene(root);

            // Obtenir la fenêtre actuelle (stage) à partir de l'événement
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            // Définir la nouvelle scène sur le stage
            stage.setScene(scene);
            stage.show(); // Afficher la nouvelle scène

        } catch (IOException e) {
            // Gérer les erreurs de chargement du FXML
            e.printStackTrace();
            System.err.println("Erreur lors du chargement de AjouterReclamation.fxml");
        }
    }

    @FXML
    void ModifierRecAff(ActionEvent event) {
    }

    @FXML
    void SupprimerRecAff(ActionEvent event) {
    }
}