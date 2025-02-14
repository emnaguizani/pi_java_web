package esprit.tn.controllers;

import esprit.tn.entities.Feedback;
import esprit.tn.entities.Reclamation;
import esprit.tn.services.FeedbackService;
import esprit.tn.services.ReclamationService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.w3c.dom.Text;

import javax.swing.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

public class AfficherFeedbackController {

    @FXML
    private TableView<Feedback> TableViewF;

    @FXML
    private TableColumn<Feedback, LocalDateTime> dateCreationF;

    @FXML
    private TableColumn<Feedback, Integer> idF;

    @FXML
    private TableColumn<Feedback, Text> messageF;

    @FXML
    private TableColumn<Feedback, Integer> noteF;

    @FXML
    private TableColumn<Feedback, String> typeF;

    @FXML
    private TableColumn<Feedback, Integer> ReclamationId;

    @FXML
    private Button backReclamation;

    @FXML
    void initialize() {
        FeedbackService fs = new FeedbackService();

        ObservableList<Feedback> observableList = FXCollections.observableList(fs.getallF());

        TableViewF.setItems(observableList);
        idF.setCellValueFactory(new PropertyValueFactory<>("IdFeedback"));
        typeF.setCellValueFactory(new PropertyValueFactory<>("TypeFeedback"));
        messageF.setCellValueFactory(new PropertyValueFactory<>("Message"));
        noteF.setCellValueFactory(new PropertyValueFactory<>("Note"));
        dateCreationF.setCellValueFactory(new PropertyValueFactory<>("DateFeedback"));
        ReclamationId.setCellValueFactory(new PropertyValueFactory<>("ReclamationId"));
    }

    @FXML
    void BackReclamation(ActionEvent event) {
        // Fermer la fenêtre de modification sans enregistrer les modifications
        backReclamation.getScene().getWindow().hide();

        try {
            // Charger le fichier FXML de la page AjouterReclamation
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/AfficherReclamation.fxml")));
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
    void ModifierFeedback() {

    }

    @FXML
    void SupprimerFeedback() {

    }

}
