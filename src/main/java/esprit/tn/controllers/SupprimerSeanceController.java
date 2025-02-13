package esprit.tn.controllers;

import esprit.tn.entities.Seance;
import esprit.tn.services.SeanceService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class SupprimerSeanceController {

    @FXML
    private TextField idSeanceField;

    @FXML
    private Button supprimerButton;

    @FXML
    private Label messageLabel;

    private final SeanceService seanceService = new SeanceService(); // Instance du service

    @FXML
    public void supprimerSeance() {
        try {
            String idText = idSeanceField.getText().trim();
            if (idText.isEmpty()) {
                showAlert("Erreur", "Veuillez entrer un ID de séance valide !");
                return;
            }

            int id = Integer.parseInt(idText);

            // Vérification si la séance existe avant suppression
            Seance seanceASupprimer = seanceService.getone(id); // On récupère l'objet
            if (seanceASupprimer == null) {
                showAlert("Erreur", "Aucune séance trouvée avec cet ID !");
                return;
            }

            // Suppression de la séance en passant l'objet
            seanceService.supprimer(seanceASupprimer); // Suppression correcte
            showAlert("Succès", "Séance supprimée avec succès !");
            idSeanceField.clear(); // Nettoyage du champ après suppression
        } catch (NumberFormatException e) {
            showAlert("Erreur", "L'ID de la séance doit être un nombre valide.");
        } catch (Exception e) {
            showAlert("Erreur", "Une erreur est survenue : " + e.getMessage());
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
