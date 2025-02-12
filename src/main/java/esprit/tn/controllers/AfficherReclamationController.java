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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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
    private Button soumettreFeedback;

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
    void ModifierRecAff() {
        // Récupérer la réclamation sélectionnée
        Reclamation reclamationChoisi = TableViewR.getSelectionModel().getSelectedItem();

        if (reclamationChoisi != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierReclamation.fxml"));
                Parent root = loader.load();

                // Passer la réclamation sélectionnée au contrôleur de la deuxième interface
                ModifierReclamationController controller = loader.getController();
                controller.setReclamation(reclamationChoisi);

                // Afficher la deuxième interface
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Aucune réclamation sélectionnée.");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Vous devez sélectionner une réclamation !");
            alert.setContentText("Veuillez sélectionner une réclamation afin de modifier");
            alert.showAndWait();
        }
    }

    @FXML
    void SupprimerRecAff() {
        Reclamation reclamationChoisi = TableViewR.getSelectionModel().getSelectedItem();

        ReclamationService rs = new ReclamationService();

        if (reclamationChoisi != null) {
            rs.supprimer(reclamationChoisi);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setContentText("Réclamation supprimée avec succès !");
            alert.showAndWait();
        }
        else {
            System.out.println("Aucune réclamation sélectionnée.");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Vous devez sélectionner une réclamation!");
            alert.setContentText("Veuillez sélectionner une réclamation afin de la supprimer");
            alert.showAndWait();
        }
    }

    @FXML
    void SoumettreFeed() {
        // Récupérer la réclamation sélectionnée
        Reclamation selectedReclamation = TableViewR.getSelectionModel().getSelectedItem();

        if (selectedReclamation != null) {
            try {
                // Charger la nouvelle interface pour soumettre un feedback
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterFeedback.fxml"));
                Parent root = loader.load();

                // Passer l'ID de la réclamation au contrôleur de la nouvelle interface
                AjouterFeedbackController controller = loader.getController();
                controller.setReclamationId(selectedReclamation.getId());

                // Afficher la nouvelle interface
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Aucune réclamation sélectionnée.");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune réclamation sélectionnée");
            alert.setContentText("Veuillez sélectionner une réclamation pour soumettre un feedback.");
            alert.showAndWait();
        }
    }
}