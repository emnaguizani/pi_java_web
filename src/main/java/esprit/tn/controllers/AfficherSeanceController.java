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
import javafx.util.Callback;

import java.io.IOException;
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
    private TableColumn<Seance, Void> modifierColumn;

    @FXML
    private TableColumn<Seance, Void> supprimerColumn;

    @FXML
    private TextField searchField;

    private final SeanceService seanceService = new SeanceService();
    private ObservableList<Seance> allSeances;

    @FXML
    void initialize() {
        allSeances = FXCollections.observableList(seanceService.getAll());

        tableView.setItems(allSeances);
        idSeance.setCellValueFactory(new PropertyValueFactory<>("idSeance"));
        titre.setCellValueFactory(new PropertyValueFactory<>("titre"));
        contenu.setCellValueFactory(new PropertyValueFactory<>("contenu"));
        datetime.setCellValueFactory(new PropertyValueFactory<>("datetime"));
        idFormateur.setCellValueFactory(new PropertyValueFactory<>("idFormateur"));

        ajouterBoutonModifier();
        ajouterBoutonSupprimer();
    }

    private void ajouterBoutonModifier() {
        Callback<TableColumn<Seance, Void>, TableCell<Seance, Void>> cellFactory = param -> new TableCell<>() {
            private final Button btn = new Button("Modifier");

            {
                btn.setOnAction(event -> {
                    Seance seance = getTableView().getItems().get(getIndex());
                    ouvrirFenetreModification(seance);
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
        };

        modifierColumn.setCellFactory(cellFactory);
    }

    private void ajouterBoutonSupprimer() {
        Callback<TableColumn<Seance, Void>, TableCell<Seance, Void>> cellFactory = param -> new TableCell<>() {
            private final Button btn = new Button("Supprimer");

            {
                btn.setOnAction(event -> {
                    Seance seance = getTableView().getItems().get(getIndex());
                    supprimerSeance(seance);
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
        };

        supprimerColumn.setCellFactory(cellFactory);
    }

    private void ouvrirFenetreModification(Seance seance) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierSeance.fxml"));
            Parent root = loader.load();
            ModifierSeanceController controller = loader.getController();
            controller.setSeance(seance);

            Stage stage = new Stage();
            stage.setTitle("Modifier Séance");
            stage.setScene(new Scene(root, 400, 300));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("❌ Erreur lors du chargement de ModifierSeance.fxml !");
        }
    }

    private void supprimerSeance(Seance seance) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Supprimer la séance ?");
        alert.setContentText("Voulez-vous vraiment supprimer cette séance ?");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                seanceService.supprimer(seance);
                allSeances.remove(seance);
                tableView.refresh();
            }
        });
    }

    @FXML
    void rechercherSeance() {
        String searchText = searchField.getText().trim().toLowerCase();

        if (searchText.isEmpty()) {
            tableView.setItems(allSeances);
            return;
        }

        List<Seance> filteredList = allSeances.stream()
                .filter(seance -> seance.getTitre().toLowerCase().contains(searchText)
                        || seance.getDatetime().toString().contains(searchText))
                .collect(Collectors.toList());

        tableView.setItems(FXCollections.observableList(filteredList));
    }

    @FXML
    void resetSearch() {
        searchField.clear();
        tableView.setItems(allSeances);
    }

    @FXML
    void retour() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AjouterSeance.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
            tableView.getScene().getWindow().hide();
        } catch (IOException e) {
            showError("Erreur", "Impossible de charger la fenêtre.");
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
