package esprit.tn.controllers;

import esprit.tn.entities.Users;
import esprit.tn.entities.Seance;
import esprit.tn.entities.Absence;
import esprit.tn.services.SeanceService;
import esprit.tn.services.AbsenceService;
import esprit.tn.services.UserService;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AjouterAbsenceController {

    @FXML
    private TableView<Absence> tableViewAbsences;
    @FXML
    private TableColumn<Absence, String> eleveFullNameColumn;
    @FXML
    private TableColumn<Absence, Boolean> presentColumn;
    @FXML
    private TableColumn<Absence, Boolean> retardColumn;
    @FXML
    private TableColumn<Absence, Boolean> absentColumn;
    @FXML
    private TableColumn<Absence, String> commentColumn;
    @FXML
    private ComboBox<Seance> seanceComboBox;

    private final SeanceService seanceService = new SeanceService();
    private final AbsenceService absenceService = new AbsenceService();
    private final UserService userService = new UserService();

    private List<Absence> absencesList = new ArrayList<>();

    @FXML
    public void initialize() {
        List<Seance> seanceList = seanceService.getAll();
        seanceComboBox.setItems(FXCollections.observableArrayList(seanceList));

        seanceComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Seance seance) {
                return (seance != null) ? seance.getTitre() : "";
            }

            @Override
            public Seance fromString(String string) {
                return null;
            }
        });

        // Charger immédiatement la liste des élèves
        loadAllEleves();

        // Appliquer la mise à jour des séances après sélection
        seanceComboBox.setOnAction(event -> updateSeanceForAbsences());

        setupColumnBindings();
    }

    private void loadAllEleves() {
        List<Users> elevesList = userService.getAllEleves();

        for (Users eleve : elevesList) {
            absencesList.add(new Absence(0, 0, eleve.getId_user(), "", eleve.getFullName(), "", ""));
        }

        ObservableList<Absence> absences = FXCollections.observableArrayList(absencesList);
        tableViewAbsences.setItems(absences);
        tableViewAbsences.setEditable(true);
    }

    private void updateSeanceForAbsences() {
        Seance selectedSeance = seanceComboBox.getValue();
        if (selectedSeance == null) return;

        for (Absence absence : absencesList) {
            absence.setIdSeance(selectedSeance.getIdSeance());
            absence.setSeanceTitre(selectedSeance.getTitre());
        }
        tableViewAbsences.refresh();
    }

    private void setupColumnBindings() {
        eleveFullNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEleveFullName()));

        presentColumn.setCellValueFactory(cellData -> {
            SimpleBooleanProperty prop = new SimpleBooleanProperty("Présent".equals(cellData.getValue().getEtat()));
            prop.addListener((obs, oldVal, newVal) -> {
                if (newVal) enforceSingleSelection(cellData.getValue(), "Présent");
            });
            return prop;
        });
        presentColumn.setCellFactory(column -> new CheckBoxTableCell<>() {
            @Override
            public void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    Absence absence = getTableView().getItems().get(getIndex());
                    setDisable(absence.getIdAbsence() > 0); // Désactiver si l'absence est enregistrée
                }
            }
        });

        retardColumn.setCellValueFactory(cellData -> {
            SimpleBooleanProperty prop = new SimpleBooleanProperty("Retard".equals(cellData.getValue().getEtat()));
            prop.addListener((obs, oldVal, newVal) -> {
                if (newVal) enforceSingleSelection(cellData.getValue(), "Retard");
            });
            return prop;
        });
        retardColumn.setCellFactory(column -> new CheckBoxTableCell<>() {
            @Override
            public void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    Absence absence = getTableView().getItems().get(getIndex());
                    setDisable(absence.getIdAbsence() > 0);
                }
            }
        });

        absentColumn.setCellValueFactory(cellData -> {
            SimpleBooleanProperty prop = new SimpleBooleanProperty("Absent".equals(cellData.getValue().getEtat()));
            prop.addListener((obs, oldVal, newVal) -> {
                if (newVal) enforceSingleSelection(cellData.getValue(), "Absent");
            });
            return prop;
        });
        absentColumn.setCellFactory(column -> new CheckBoxTableCell<>() {
            @Override
            public void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    Absence absence = getTableView().getItems().get(getIndex());
                    setDisable(absence.getIdAbsence() > 0);
                }
            }
        });

        commentColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCommentaire()));
        commentColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        commentColumn.setOnEditCommit(event -> {
            Absence absence = event.getRowValue();
            absence.setCommentaire(event.getNewValue());
            absenceService.modifier(absence);
            tableViewAbsences.refresh();
        });
    }


    private void enforceSingleSelection(Absence absence, String selected) {
        absence.setEtat(selected);
        tableViewAbsences.refresh();
    }

    @FXML
    void enregistrerAbsences() {
        Seance selectedSeance = seanceComboBox.getValue();
        if (selectedSeance == null) {
            showAlert("Erreur", "Veuillez sélectionner une séance avant d'enregistrer.");
            return;
        }

        tableViewAbsences.getItems().forEach(absence -> {
            if (!absence.getEtat().isEmpty()) {
                absence.setIdSeance(selectedSeance.getIdSeance());
                absenceService.ajouter(absence);
            }
        });

        showAlert("Succès", "Les absences ont été enregistrées avec succès.");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private void handleViewAbsences() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherAbsence.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
