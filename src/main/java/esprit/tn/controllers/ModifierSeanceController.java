package esprit.tn.controllers;
import javafx.stage.Stage;

import esprit.tn.entities.Seance;
import esprit.tn.services.SeanceService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.regex.Pattern;

public class ModifierSeanceController {

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

    private Seance selectedSeance;
    private final SeanceService seanceService = new SeanceService();

    @FXML
    public void initialize() {
        // Initialisation du ComboBox avec des heures de 00:00 à 23:59
        ObservableList<String> hours = FXCollections.observableArrayList();
        for (int h = 0; h < 24; h++) {
            for (int m = 0; m < 60; m += 15) {
                hours.add(String.format("%02d:%02d:00", h, m));
            }
        }
        hourComboBox.setItems(hours);
    }

    public void setSeance(Seance seance) {
        this.selectedSeance = seance;
        titreId.setText(seance.getTitre());
        contenuId.setText(seance.getContenu());
        datePicker.setValue(seance.getDatetime().toLocalDateTime().toLocalDate());
        hourComboBox.setValue(seance.getDatetime().toLocalDateTime().toLocalTime().toString());
        idFormateurId.setText(String.valueOf(seance.getIdFormateur()));
    }

    @FXML
    void modifierSeance(ActionEvent event) {
        try {
            if (selectedSeance == null) {
                showError("Erreur", "Aucune séance sélectionnée.");
                return;
            }

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

            // Mise à jour de l'objet
            LocalDateTime localDateTime = LocalDateTime.of(selectedDate, LocalTime.parse(selectedTime));
            Timestamp datetime = Timestamp.valueOf(localDateTime);

            selectedSeance.setTitre(titre);
            selectedSeance.setContenu(contenu);
            selectedSeance.setDatetime(datetime);
            selectedSeance.setIdFormateur(idFormateur);

            seanceService.modifier(selectedSeance);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Modification réussie");
            alert.setContentText("Séance modifiée avec succès !");
            alert.showAndWait();
        } catch (Exception e) {
            showError("Erreur", "Une erreur est survenue : " + e.getMessage());
        }
    }

    @FXML
    void annuler(ActionEvent event) {
        ((Stage) titreId.getScene().getWindow()).close();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
