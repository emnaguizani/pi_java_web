package esprit.tn.controllers;

import esprit.tn.entities.Absence;
import esprit.tn.services.AbsenceService;
import esprit.tn.services.SeanceService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class AjouterAbsenceController {

    @FXML
    private TextField idSeanceField;

    @FXML
    private TextField idEleveField;

    @FXML
    private ComboBox<String> etatComboBox;

    private final AbsenceService absenceService = new AbsenceService();

    @FXML
    public void initialize() {
        etatComboBox.setItems(FXCollections.observableArrayList("Présent", "Absent"));
    }

    @FXML
    public void ajouterAbsence() {
        try {
            int idSeance = Integer.parseInt(idSeanceField.getText().trim());
            int idEleve = Integer.parseInt(idEleveField.getText().trim());
            String etat = etatComboBox.getValue();

            // Vérifier si la séance existe
            SeanceService seanceService = new SeanceService();
            if (seanceService.getone(idSeance) == null) {
                showAlert("Erreur", "L'ID de la séance n'existe pas !");
                return;
            }

            Absence absence = new Absence(0, idSeance, idEleve, etat);
            absenceService.ajouter(absence);
            showAlert("Succès", "Absence ajoutée avec succès !");
        } catch (NumberFormatException e) {
            showAlert("Erreur", "L'ID doit être un nombre valide !");
        } catch (Exception e) {
            showAlert("Erreur", "Vérifiez vos saisies !");
        }
    }


    @FXML
    public void afficherAbsences() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherAbsence.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Liste des Absences");
            stage.setScene(new Scene(root, 600, 400));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("❌ Erreur lors du chargement de AfficherAbsence.fxml !");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
