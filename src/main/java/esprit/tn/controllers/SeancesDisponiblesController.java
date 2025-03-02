package esprit.tn.controllers;

import esprit.tn.entities.Seance;
import esprit.tn.services.SeanceService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;

public class SeancesDisponiblesController {

    @FXML
    private TableView<Seance> tableViewSeances;
    @FXML
    private TableColumn<Seance, String> titreColumn;
    @FXML
    private TableColumn<Seance, String> descriptionColumn;
    @FXML
    private TableColumn<Seance, String> dateHeureColumn;
    @FXML
    private TableColumn<Seance, Void> actionColumn;

    private final SeanceService seanceService = new SeanceService();

    @FXML
    public void initialize() {
        setupColumnBindings();
        loadSeances();
    }

    private void setupColumnBindings() {
        titreColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("contenu"));
        dateHeureColumn.setCellValueFactory(new PropertyValueFactory<>("datetime"));

        // Ajout du bouton "Participer" pour chaque séance
        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button btnParticiper = new Button("Participer");

            {
                btnParticiper.setOnAction(event -> {
                    Seance seance = getTableView().getItems().get(getIndex());
                    participerASeance(seance);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(new HBox(5, btnParticiper));
                }
            }
        });
    }

    private void loadSeances() {
        List<Seance> seances = seanceService.getAllSeances();
        tableViewSeances.setItems(FXCollections.observableArrayList(seances));
        tableViewSeances.refresh();
    }

    private void participerASeance(Seance seance) {
        System.out.println("✅ L'élève participe à la séance : " + seance.getTitre());
        ouvrirVisio(seance);
    }

    private void ouvrirVisio(Seance seance) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SeanceVisio.fxml"));
            Parent root = loader.load();

            SeanceVisioController controller = loader.getController();
            controller.setSeance(seance);

            Stage stage = new Stage();
            stage.setTitle("Session en Direct - " + seance.getTitre());
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
