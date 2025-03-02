package esprit.tn.controllers;
import esprit.tn.services.UserService;
import esprit.tn.entities.Seance;
import esprit.tn.services.EmailService;
import esprit.tn.services.SeanceService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;


import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

public class AfficherSeanceController {

    @FXML
    private TableView<Seance> tableView;

    @FXML
    private TableColumn<Seance, String> titre;

    @FXML
    private TableColumn<Seance, String> contenu;

    @FXML
    private TableColumn<Seance, String> modeColumn;

    @FXML
    private TableColumn<Seance, String> nomFormateur;

    @FXML
    private TableColumn<Seance, String> datetime;

    @FXML
    private TableColumn<Seance, Void> modifierColumn;

    @FXML
    private TableColumn<Seance, Void> supprimerColumn;

    @FXML
    private TableColumn<Seance, Void> participerColumn;

    @FXML
    private TextField searchField;

    private final SeanceService seanceService = new SeanceService();
    private ObservableList<Seance> allSeances;
    private final UserService userService = new UserService();
    private Seance seance;

    @FXML
    void initialize() {
        allSeances = FXCollections.observableList(seanceService.getAll());

        tableView.setItems(allSeances);
        titre.setCellValueFactory(new PropertyValueFactory<>("titre"));
        contenu.setCellValueFactory(new PropertyValueFactory<>("contenu"));
        modeColumn.setCellValueFactory(new PropertyValueFactory<>("modeSeance")); // ✅ Vérifier que l'attribut `modeSeance` existe dans `Seance`
        nomFormateur.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getNomFormateur() != null ? cellData.getValue().getNomFormateur() : "Inconnu"
        ));
        datetime.setCellValueFactory(new PropertyValueFactory<>("datetime"));

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        ajouterBoutonModifier();
        ajouterBoutonSupprimer();
        ajouterBoutonParticiper();
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
                setGraphic(empty ? null : btn);
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
                setGraphic(empty ? null : btn);
            }
        };

        supprimerColumn.setCellFactory(cellFactory);
    }

    private void ajouterBoutonParticiper() {
        Callback<TableColumn<Seance, Void>, TableCell<Seance, Void>> cellFactory = param -> new TableCell<>() {
            private final Button btn = new Button("Participer");

            {
                btn.setOnAction(event -> {
                    Seance seance = getTableView().getItems().get(getIndex());
                    participerASeance(seance);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        };

        participerColumn.setCellFactory(cellFactory);
    }
    private void envoyerEmailAuxParticipants(String meetURL) {
        List<String> emailsParticipants = userService.getEmailsParticipants();

        String sujet = "📅 Invitation à la séance : " + this.seance.getTitre();

        String message = "Bonjour,\n\n"
                + "Vous êtes invité(e) à participer à la séance : **" + seance.getTitre() + "**.\n"
                + "Cliquez sur le lien pour rejoindre la réunion : " + meetURL + "\n\n"
                + "À bientôt !";

        for (String email : emailsParticipants) {
            boolean sent = EmailService.envoyerEmail(email, sujet, message);
            if (sent) {
                System.out.println("✅ Email envoyé à : " + email);
            } else {
                System.err.println("❌ Erreur lors de l'envoi de l'email à : " + email);
            }
        }
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
            showError("Erreur", "Impossible de charger la fenêtre de modification.");
        }
    }

    private void supprimerSeance(Seance seance) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Supprimer la séance ?");
        alert.setContentText("Voulez-vous vraiment supprimer cette séance ?");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                seanceService.supprimer(seance); // ✅ Utilisation correcte de la méthode supprimer
                allSeances.remove(seance);
                tableView.refresh();
            }
        });
    }

    private void participerASeance(Seance seance) {
        if ("En ligne".equalsIgnoreCase(seance.getModeSeance())) { // ✅ Vérifier que `getModeSeance()` existe
            ouvrirJitsi(seance);
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Séance Présentielle");
            alert.setHeaderText("Cette séance est en présentiel.");
            alert.setContentText("Veuillez vous rendre dans la salle correspondante.");
            alert.showAndWait();
        }
    }

    private void ouvrirJitsi(Seance seance) {
        if (seance != null) {
            try {
                // Assurez-vous que `this.seance` est bien défini
                this.seance = seance;

                String titreEncode = seance.getTitre().replaceAll(" ", "%20");
                String meetURL = "https://meet.jit.si/" + titreEncode + "-" + seance.getIdSeance()
                        + "#config.prejoinPageEnabled=false";

                System.out.println("🔗 URL générée : " + meetURL);

                Desktop.getDesktop().browse(new URI(meetURL));

                // 📧 Envoyer l'email uniquement si la séance est bien définie
                if (this.seance != null) {
                    envoyerEmailAuxParticipants(meetURL);
                } else {
                    System.err.println("❌ Impossible d'envoyer l'email : `seance` est null.");
                }

            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
                System.err.println("❌ Erreur lors de l'ouverture du lien Jitsi !");
            }
        }
    }


    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
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

}
