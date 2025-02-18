package esprit.tn.controllers;

import esprit.tn.entities.Reclamation;
import esprit.tn.services.ReclamationService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;

import static esprit.tn.services.ReclamationService.saisirReclamationAjout;

public class AjouterReclamationController{

    @FXML
    private TextArea descriptionid;

    @FXML
    private TextField titreid;

    @FXML
    void AjouterRec() {
        try {
            String titre = titreid.getText().trim();
            String description = descriptionid.getText().trim();

            int idUser = 27;

            // Vérifier si le titre est vide
            if (titre.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Champ obligatoire");
                alert.setContentText("Le champ 'Titre' est obligatoire. Veuillez le remplir.");
                alert.showAndWait();
                return; // Arrêter l'exécution de la méthode
            }

            Reclamation rec = saisirReclamationAjout(titre, description);
            ReclamationService rs = new ReclamationService();
            rs.ajouter(rec);

            // Affichage d'une alerte pour informer l'utilisateur
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Ajout réussi !");
            alert.setContentText("Votre réclamation a été ajoutée dans la base de données.");
            alert.showAndWait();

        } catch (IllegalArgumentException e) {
            // Affichage d'une alerte en cas de champ vide
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie !");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        } catch (Exception e) {
            // Gestion des autres erreurs inattendues
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Une erreur est survenue lors de l'ajout de la réclamation.");
            alert.showAndWait();
            e.printStackTrace(); // Afficher l'erreur dans la console pour le debug
        }
    }

    @FXML
    void nextRecAjout() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherReclamation.fxml"));

            titreid.getScene().setRoot(root);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}