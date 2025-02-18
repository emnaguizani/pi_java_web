package esprit.tn.controllers;

import esprit.tn.entities.Seance;
import esprit.tn.services.SeanceService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public class AjouterSeanceController {

    @FXML
    private TextField titreId;

    @FXML
    private TextField contenuId;

    @FXML
    private DatePicker datePicker;

    @FXML
    private ComboBox<String> hourComboBox;

    @FXML
    private TextField idFormateurId;

    private final SeanceService seanceService = new SeanceService();

    @FXML
    public void initialize() {
        // Initialisation du ComboBox avec des heures de 00:00 à 23:59
        ObservableList<String> hours = FXCollections.observableArrayList();
        for (int h = 0; h < 24; h++) {
            for (int m = 0; m < 60; m += 15) { // Ajout d'intervalles de 15 minutes
                hours.add(String.format("%02d:%02d:00", h, m));
            }
        }
        hourComboBox.setItems(hours);
        hourComboBox.setValue("12:00:00"); // Heure par défaut

        // Personnalisation du format du DatePicker
        datePicker.setConverter(new StringConverter<>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            @Override
            public String toString(LocalDate date) {
                return (date != null) ? dateFormatter.format(date) : "";
            }

            @Override
            public LocalDate fromString(String text) {
                return (text != null && !text.isEmpty()) ? LocalDate.parse(text, dateFormatter) : null;
            }
        });

        // Sélection de la date actuelle par défaut
        datePicker.setValue(LocalDate.now());
    }

    @FXML
    void ajouterSeance(ActionEvent event) {
        try {
            // Validation des champs
            String titre = titreId.getText().trim();
            String contenu = contenuId.getText().trim();
            LocalDate selectedDate = datePicker.getValue();
            String selectedTime = hourComboBox.getValue();
            String idFormateurText = idFormateurId.getText().trim();

            if (titre.isEmpty() || contenu.isEmpty() || selectedDate == null || selectedTime.isEmpty() || idFormateurText.isEmpty()) {
                showError("Erreur de Saisie", "Tous les champs doivent être remplis.");
                return;
            }

            if (!Pattern.matches("[a-zA-Z0-9 ]{3,}", titre)) {
                showError("Erreur", "Le titre doit contenir au moins 3 caractères alphanumériques.");
                return;
            }

            if (!Pattern.matches("[a-zA-Z0-9.,!? ]{5,}", contenu)) {
                showError("Erreur", "Le contenu doit contenir au moins 5 caractères valides.");
                return;
            }

            if (selectedDate.isBefore(LocalDate.now())) {
                showError("Erreur", "La date ne peut pas être dans le passé.");
                return;
            }

            int idFormateur;
            try {
                idFormateur = Integer.parseInt(idFormateurText);
                if (idFormateur <= 0) {
                    showError("Erreur", "L'ID du formateur doit être un nombre positif.");
                    return;
                }
            } catch (NumberFormatException e) {
                showError("Erreur", "ID Formateur doit être un nombre valide.");
                return;
            }

            // Création de l'objet Timestamp
            LocalDateTime localDateTime = LocalDateTime.of(selectedDate, LocalTime.parse(selectedTime));
            Timestamp datetime = Timestamp.valueOf(localDateTime);

            // Création de l'objet Séance et insertion
            Seance seance = new Seance(0, titre, contenu, datetime, idFormateur);
            seanceService.ajouter(seance);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Ajout réussi");
            alert.setContentText("Séance ajoutée avec succès !");
            alert.showAndWait();

        } catch (Exception e) {
            showError("Erreur", "Une erreur est survenue : " + e.getMessage());
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

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}