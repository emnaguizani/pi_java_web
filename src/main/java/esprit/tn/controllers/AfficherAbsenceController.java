package esprit.tn.controllers;

import esprit.tn.entities.Absence;
import esprit.tn.services.AbsenceService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class AfficherAbsenceController {

    @FXML
    private TableView<Absence> tableViewAbsences;

    @FXML
    private TableColumn<Absence, String> columnEleveName;

    @FXML
    private TableColumn<Absence, String> columnEtat;

    @FXML
    private TableColumn<Absence, String> columnSeanceTitre; // ✅ Ajout de la colonne "Séance"
    @FXML
    private TableColumn<Absence, String> columnCommentaire;
    @FXML
    private TextField searchField;

    private final AbsenceService absenceService = new AbsenceService();
    private ObservableList<Absence> allAbsences;

    @FXML
    public void initialize() {
        List<Absence> absences = absenceService.getAllWithUserDetails();

        if (absences.isEmpty()) {
            System.out.println("⚠️ Aucune absence récupérée !");
        } else {
            System.out.println("✅ " + absences.size() + " absences récupérées !");
        }

        allAbsences = FXCollections.observableArrayList(absences);
        tableViewAbsences.setItems(allAbsences);



        columnEleveName.setCellValueFactory(new PropertyValueFactory<>("eleveFullName"));
        columnEtat.setCellValueFactory(new PropertyValueFactory<>("etat"));
        columnSeanceTitre.setCellValueFactory(new PropertyValueFactory<>("seanceTitre"));
        columnCommentaire.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCommentaire()));
        columnCommentaire.setCellFactory(TextFieldTableCell.forTableColumn());


        tableViewAbsences.refresh();
    }



    /**
     * ✅ Recherche une absence par le nom de l'élève.
     */
    @FXML
    void rechercherAbsence() {
        String searchText = searchField.getText().trim();
        if (searchText.isEmpty()) {
            showError("Erreur", "Veuillez entrer un nom pour la recherche.");
            return;
        }

        List<Absence> filteredList = allAbsences.stream()
                .filter(a -> a.getEleveFullName().toLowerCase().contains(searchText.toLowerCase()))
                .collect(Collectors.toList());

        tableViewAbsences.setItems(FXCollections.observableList(filteredList));
    }

    /**
     * ✅ Réinitialiser la recherche et afficher toutes les absences.
     */
    @FXML
    void resetSearch() {
        searchField.clear();
        tableViewAbsences.setItems(allAbsences);
    }

    /**
     * ✅ Ouvrir l'interface précédente (Marquer Absence).
     */
    @FXML
    void retour() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterAbsence.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Marquer Absences");
            stage.show();

            // Fermer la fenêtre actuelle
            tableViewAbsences.getScene().getWindow().hide();
        } catch (IOException e) {
            showError("Erreur", "Impossible de charger AjouterAbsence.fxml !");
        }
    }

    /**
     * ✅ Affichage d'une alerte d'erreur.
     */
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    void exportAbsences() {
        System.out.println("🔍 Nombre d'absences disponibles : " + allAbsences.size());

        if (allAbsences == null || allAbsences.isEmpty()) {
            showError("Erreur Export", "Aucune absence à exporter !");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer le fichier d'absences");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichier CSV", "*.csv"));
        fileChooser.setInitialFileName("export_absences.csv");

        File file = fileChooser.showSaveDialog(null);
        if (file == null) return;

        try (FileWriter writer = new FileWriter(file)) {
            writer.append("Séance,Nom Élève,État,Commentaire\n");

            for (Absence absence : allAbsences) {
                writer.append(absence.getSeanceTitre()).append(",");
                writer.append(absence.getEleveFullName()).append(",");
                writer.append(absence.getEtat()).append(",");
                writer.append(absence.getCommentaire()).append("\n");
            }

            showSuccess("Export Réussi", "Les absences ont été exportées avec succès.\nFichier : " + file.getAbsolutePath());

        } catch (IOException e) {
            showError("Erreur Export", "Impossible d'exporter les absences.\n" + e.getMessage());
        }
    }






    /**
     * ✅ Affichage d'une alerte de succès.
     */
    private void showSuccess(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
