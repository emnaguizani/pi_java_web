package esprit.tn.controllers;

import esprit.tn.entities.Absence;
import esprit.tn.services.AbsenceService;
import esprit.tn.utils.SessionManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.beans.property.SimpleStringProperty;

import java.util.List;

public class AbsencesEleveController {

    @FXML
    private TableView<Absence> tableViewAbsencesEleve;
    @FXML
    private TableColumn<Absence, String> eleveColumn;
    @FXML
    private TableColumn<Absence, String> seanceColumn; // ✅ Nouvelle colonne pour la séance
    @FXML
    private TableColumn<Absence, String> etatColumn;
    @FXML
    private TableColumn<Absence, String> commentaireColumn;

    private final AbsenceService absenceService = new AbsenceService();

    @FXML
    public void initialize() {
        int idEleve = SessionManager.getInstance().getUserId();
        if (idEleve != -1) {
            System.out.println("✅ Élève connecté : " + SessionManager.getInstance().getLoggedInUser().getFullName() + " (ID : " + idEleve + ")");
            setupColumnBindings(); // ✅ Configuration des colonnes
            loadAbsences(idEleve);
        } else {
            System.err.println("❌ Aucun utilisateur connecté !");
        }
    }

    private void setupColumnBindings() {
        eleveColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEleveFullName())); // ✅ Nom de l'élève
        seanceColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSeanceTitre())); // ✅ Titre de la séance
        etatColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEtat())); // ✅ Présent / Absent
        commentaireColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCommentaire())); // ✅ Commentaire
    }



    private void loadAbsences(int idEleve) {
        List<Absence> absences = absenceService.getAbsencesForEleve(idEleve);

        if (absences.isEmpty()) {
            System.err.println("⚠️ Aucune absence trouvée pour cet élève !");
        } else {
            System.out.println("✅ " + absences.size() + " absences trouvées pour l'élève ID: " + idEleve);
        }

        tableViewAbsencesEleve.setItems(FXCollections.observableArrayList(absences));
        tableViewAbsencesEleve.refresh(); // ✅ Assurez-vous que la table est rafraîchie
    }
}
