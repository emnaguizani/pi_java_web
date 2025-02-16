package esprit.tn.controllers;

import esprit.tn.entities.Absence;
import esprit.tn.services.AbsenceService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class AfficherAbsenceController {

    @FXML
    private TableView<Absence> tableView;

    @FXML
    private TableColumn<Absence, Integer> idAbsence;

    @FXML
    private TableColumn<Absence, Integer> idSeance;

    @FXML
    private TableColumn<Absence, Integer> idEleve;

    @FXML
    private TableColumn<Absence, String> etat;

    @FXML
    private TableColumn<Absence, Void> modifierColumn;

    @FXML
    private TableColumn<Absence, Void> supprimerColumn;

    @FXML
    private TextField searchField;

    private final AbsenceService absenceService = new AbsenceService();
    private ObservableList<Absence> allAbsences;

    @FXML
    void initialize() {
        allAbsences = FXCollections.observableList(absenceService.getAll());
        tableView.setItems(allAbsences);

        idAbsence.setCellValueFactory(new PropertyValueFactory<>("idAbsence"));
        idSeance.setCellValueFactory(new PropertyValueFactory<>("idSeance"));
        idEleve.setCellValueFactory(new PropertyValueFactory<>("idEleve"));
        etat.setCellValueFactory(new PropertyValueFactory<>("etat"));

        ajouterBoutonModifier();
        ajouterBoutonSupprimer();
    }

    /**
     * ✅ Méthode pour rechercher une absence selon l'ID de la séance ou de l'élève.
     */
    @FXML
    void rechercherAbsence() {
        String searchText = searchField.getText().trim();
        if (searchText.isEmpty()) {
            showError("Erreur", "Veuillez entrer un ID pour la recherche.");
            return;
        }

        try {
            int searchId = Integer.parseInt(searchText);

            List<Absence> filteredList = allAbsences.stream()
                    .filter(a -> a.getIdSeance() == searchId || a.getIdEleve() == searchId)
                    .collect(Collectors.toList());

            tableView.setItems(FXCollections.observableList(filteredList));
        } catch (NumberFormatException e) {
            showError("Erreur", "Veuillez entrer un nombre valide !");
        }
    }

    /**
     * ✅ Méthode pour réinitialiser la recherche et afficher toutes les absences.
     */
    @FXML
    void resetSearch() {
        searchField.clear();
        tableView.setItems(allAbsences);
    }

    /**
     * ✅ Méthode pour retourner à l'interface précédente.
     */
    @FXML
    void retour() {
        try {
            // Charger la page précédente (AjouterAbsence.fxml)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterAbsence.fxml"));
            Parent root = loader.load();

            // Ouvrir la nouvelle scène
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter Absence");
            stage.show();

            // Fermer la fenêtre actuelle
            tableView.getScene().getWindow().hide();
        } catch (IOException e) {
            showError("Erreur", "Impossible de charger la fenêtre précédente.");
        }
    }

    /**
     * ✅ Méthode pour ajouter un bouton de modification dans la table.
     */
    private void ajouterBoutonModifier() {
        modifierColumn.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("Modifier");

            {
                btn.setOnAction(event -> {
                    Absence absence = getTableView().getItems().get(getIndex());
                    ouvrirFenetreModification(absence);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btn);
                }
            }
        });
    }

    /**
     * ✅ Méthode pour ajouter un bouton de suppression dans la table.
     */
    private void ajouterBoutonSupprimer() {
        supprimerColumn.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("Supprimer");

            {
                btn.setOnAction(event -> {
                    Absence absence = getTableView().getItems().get(getIndex());
                    supprimerAbsence(absence);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btn);
                }
            }
        });
    }

    /**
     * ✅ Ouvrir la fenêtre de modification d'absence.
     */
    private void ouvrirFenetreModification(Absence absence) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierAbsence.fxml"));
            Parent root = loader.load();
            ModifierAbsenceController controller = loader.getController();
            controller.setAbsence(absence);

            Stage stage = new Stage();
            stage.setTitle("Modifier Absence");
            stage.setScene(new Scene(root, 400, 300));
            stage.show();
        } catch (IOException e) {
            showError("Erreur", "Impossible de charger ModifierAbsence.fxml !");
        }
    }

    /**
     * ✅ Supprimer une absence après confirmation.
     */
    private void supprimerAbsence(Absence absence) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Supprimer l'absence ?");
        alert.setContentText("Voulez-vous vraiment supprimer cette absence ?");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                absenceService.supprimer(absence);
                allAbsences.remove(absence);
                tableView.refresh();
            }
        });
    }

    /**
     * ✅ Affichage d'une erreur avec une alerte.
     */
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
