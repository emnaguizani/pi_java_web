package esprit.tn.controllers;

import esprit.tn.entities.Seance;
import esprit.tn.services.SeanceService;
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
import java.net.URL;  // ✅ Import de URL pour corriger l'erreur
import java.util.List;
import java.util.stream.Collectors;

public class AfficherSeanceController {

    @FXML
    private TableView<Seance> tableView;

    @FXML
    private TableColumn<Seance, Integer> idSeance;

    @FXML
    private TableColumn<Seance, String> titre;

    @FXML
    private TableColumn<Seance, String> contenu;

    @FXML
    private TableColumn<Seance, String> datetime;

    @FXML
    private TableColumn<Seance, Integer> idFormateur;

    @FXML
    private TextField searchField; // ✅ Champ de recherche

    private final SeanceService seanceService = new SeanceService();
    private ObservableList<Seance> allSeances; // ✅ Liste complète des séances

    @FXML
    void initialize() {
        allSeances = FXCollections.observableList(seanceService.getAll());

        tableView.setItems(allSeances);
        idSeance.setCellValueFactory(new PropertyValueFactory<>("idSeance"));
        titre.setCellValueFactory(new PropertyValueFactory<>("titre"));
        contenu.setCellValueFactory(new PropertyValueFactory<>("contenu"));
        datetime.setCellValueFactory(new PropertyValueFactory<>("datetime"));
        idFormateur.setCellValueFactory(new PropertyValueFactory<>("idFormateur"));
    }

    // ✅ Méthode pour rechercher une séance par titre ou date
    @FXML
    void rechercherSeance() {
        String searchText = searchField.getText().trim().toLowerCase();

        if (searchText.isEmpty()) {
            tableView.setItems(allSeances);
            return;
        }

        List<Seance> filteredList = allSeances.stream()
                .filter(seance -> seance.getTitre().toLowerCase().contains(searchText)
                        || seance.getDatetime().toString().contains(searchText)) // Recherche aussi par date
                .collect(Collectors.toList());

        if (filteredList.isEmpty()) {
            showError("Aucune correspondance", "Aucune séance trouvée pour : " + searchText);
        }

        tableView.setItems(FXCollections.observableList(filteredList));
    }

    // ✅ Réinitialiser la recherche
    @FXML
    void resetSearch() {
        searchField.clear();
        tableView.setItems(allSeances);
    }

    // ✅ Retour au menu Ajouter Séance
    @FXML
    void retour() {
        try {
            URL fxmlLocation = getClass().getResource("/AjouterSeance.fxml");
            if (fxmlLocation == null) {
                showError("Erreur", "Le fichier AjouterSeance.fxml est introuvable !");
                return;
            }

            Parent root = FXMLLoader.load(fxmlLocation);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
            tableView.getScene().getWindow().hide(); // Ferme l'ancienne fenêtre
        } catch (IOException e) {
            e.printStackTrace();
            showError("Erreur", "Impossible de charger la fenêtre.");
        }
    }

    // ✅ Méthode pour afficher une alerte en cas d'erreur
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
