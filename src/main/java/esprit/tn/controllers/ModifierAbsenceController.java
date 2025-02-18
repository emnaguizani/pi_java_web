package esprit.tn.controllers;

import esprit.tn.entities.Absence;
import esprit.tn.services.AbsenceService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ModifierAbsenceController {

    @FXML
    private TextField idSeanceField;

    @FXML
    private TextField idEleveField;

    @FXML
    private ComboBox<String> etatComboBox;

    private Absence absence;
    private final AbsenceService absenceService = new AbsenceService();

    @FXML
    public void initialize() {
        etatComboBox.setItems(FXCollections.observableArrayList("Présent", "Absent"));
    }

    public void setAbsence(Absence absence) {
        this.absence = absence;
        idSeanceField.setText(String.valueOf(absence.getIdSeance()));
        idEleveField.setText(String.valueOf(absence.getIdEleve()));
        etatComboBox.setValue(absence.getEtat());
    }

    @FXML
    public void modifierAbsence() {
        try {
            int idSeance = Integer.parseInt(idSeanceField.getText().trim());
            int idEleve = Integer.parseInt(idEleveField.getText().trim());
            String etat = etatComboBox.getValue();

            absence.setIdSeance(idSeance);
            absence.setIdEleve(idEleve);
            absence.setEtat(etat);

            absenceService.modifier(absence);
            showAlert("Succès", "Absence modifiée avec succès !");

            // Fermer la fenêtre après modification
            ((Stage) idSeanceField.getScene().getWindow()).close();
        } catch (NumberFormatException e) {
            showAlert("Erreur", "L'ID doit être un nombre valide !");
        } catch (Exception e) {
            showAlert("Erreur", "Une erreur est survenue !");
        }
    }

    @FXML
    public void annuler() {
        ((Stage) idSeanceField.getScene().getWindow()).close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
