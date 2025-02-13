package esprit.tn.controllers;

import esprit.tn.entities.Seance;
import esprit.tn.services.SeanceService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class AjouterSeanceController {

    @FXML
    private TextField titreId;

    @FXML
    private TextField contenuId;

    @FXML
    private TextField datetimeId;

    @FXML
    private TextField idFormateurId;

    @FXML
    void ajouterSeance(ActionEvent event) {
        try {
            String titre = titreId.getText();
            String contenu = contenuId.getText();
            String dateTimeInput = datetimeId.getText().trim();

            // Vérification du format de date
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            // Conversion sécurisée
            LocalDateTime localDateTime = LocalDateTime.parse(dateTimeInput, formatter);
            Timestamp datetime = Timestamp.valueOf(localDateTime);

            int idFormateur = Integer.parseInt(idFormateurId.getText());

            Seance seance = new Seance(0, titre, contenu, datetime, idFormateur);
            SeanceService seanceService = new SeanceService();
            seanceService.ajouter(seance);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Ajout réussi");
            alert.setContentText("Séance ajoutée avec succès !");
            alert.showAndWait();

        } catch (DateTimeParseException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de Format");
            alert.setContentText("Format de date incorrect ! Utilisez : yyyy-MM-dd HH:mm:ss");
            alert.showAndWait();
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("ID Formateur doit être un nombre valide.");
            alert.showAndWait();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Une erreur est survenue : " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void afficherSeances(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherSeance.fxml"));
            titreId.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
