package esprit.tn.controllers;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

import esprit.tn.entities.Absence;
import esprit.tn.services.AbsenceService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

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
    }

    @FXML
    void rechercherAbsence() {
        String searchText = searchField.getText().trim().toLowerCase();

        if (searchText.isEmpty()) {
            tableView.setItems(allAbsences);
            return;
        }

        List<Absence> filteredList = allAbsences.stream()
                .filter(absence -> String.valueOf(absence.getIdSeance()).contains(searchText)
                        || String.valueOf(absence.getIdEleve()).contains(searchText)
                        || absence.getEtat().toLowerCase().contains(searchText))
                .collect(Collectors.toList());

        tableView.setItems(FXCollections.observableList(filteredList));
    }

    @FXML
    void resetSearch() {
        searchField.clear();
        tableView.setItems(allAbsences);
    }
    @FXML
    void retour() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterAbsence.fxml"));
            Parent root = loader.load();

            // Récupérer la scène actuelle et changer le contenu
            Stage stage = (Stage) tableView.getScene().getWindow();
            stage.setScene(new Scene(root, 600, 400));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("❌ Erreur lors du retour à AjouterAbsence.fxml !");
        }
    }

}
