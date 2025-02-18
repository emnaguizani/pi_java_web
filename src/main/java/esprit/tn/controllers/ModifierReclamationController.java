package esprit.tn.controllers;

import esprit.tn.entities.Reclamation;
import esprit.tn.services.ReclamationService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class ModifierReclamationController {

    @FXML
    private TextField titreIdM;

    @FXML
    private TextArea descriptionIdM;

    @FXML
    private ComboBox<String> statusIdM;

    private Reclamation reclamation;

    @FXML
    private Button nextM;

    @FXML
    private Button annulerModifR;


    public class AjouterReclamationController implements Initializable {

        @FXML
        private ComboBox<String> statusComboBox; // fx:id must match the one in FXML

        @Override
        public void initialize(URL location, ResourceBundle resources) {
            // Add the status options to the ComboBox
            statusComboBox.getItems().addAll(
                    "Soumise",
                    "En cours de traitement",
                    "Résolue",
                    "Fermée"
            );

            // Set a default value (optional)
            statusComboBox.setValue("Soumise");
        }
    }

    public void setReclamation(Reclamation reclamation) {
        this.reclamation = reclamation;

        // Initialiser les champs avec les données de la réclamation
        titreIdM.setText(reclamation.getTitre());
        descriptionIdM.setText(reclamation.getDescription());
        statusIdM.setValue(reclamation.getStatus());
    }


    @FXML
    void enregistrerModifications() {
        try{
            // Mettre à jour la réclamation avec les nouvelles valeurs
            reclamation.setTitre(titreIdM.getText());
            reclamation.setDescription(descriptionIdM.getText());
            String selectedStatus = statusIdM.getValue();

            if (selectedStatus != null) {
                System.out.println("Statut sélectionné : " + selectedStatus);
                reclamation.setStatus(selectedStatus);
            } else {
                System.out.println("Aucun statut sélectionné.");
            }

            if (titreIdM.getText().trim().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Champ obligatoire");
                alert.setContentText("Le champ 'Titre' est obligatoire. Veuillez le remplir.");
                alert.showAndWait();
                return; // Arrêter l'exécution de la méthode
            }
            else if(selectedStatus.equals("Soumise"))
            {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Champ obligatoire");
                alert.setContentText("Le champ 'Status' doit être différent de 'Soumise'. Veuillez le modifier.");
                alert.showAndWait();
                return; // Arrêter l'exécution de la méthode
            }

            // Mettre à jour la réclamation dans la base de données
            ReclamationService rs = new ReclamationService();
            rs.modifier(reclamation);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setContentText("Réclamation modifiée avec succès !");
            alert.showAndWait();

            // Fermer la fenêtre de modification
            titreIdM.getScene().getWindow().hide();
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
    void annulerModifReclamation() {
        // Fermer la fenêtre de modification sans enregistrer les modifications
        titreIdM.getScene().getWindow().hide();
    }
}
