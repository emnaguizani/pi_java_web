package esprit.tn.controllers;

import esprit.tn.entities.Feedback;
import esprit.tn.entities.Reclamation;
import esprit.tn.services.FeedbackService;
import esprit.tn.services.ReclamationService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.w3c.dom.Text;

//import javax.swing.*;
//import javax.swing.text.html.ImageView;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class AfficherFeedbackController {

    @FXML
    private TableView<Feedback> TableViewF;

    @FXML
    private TableColumn<Feedback, LocalDateTime> dateCreationF;

    @FXML
    private TableColumn<Feedback, Integer> idF;

    @FXML
    private TableColumn<Feedback, Text> messageF;

    @FXML
    private TableColumn<Feedback, Integer> noteF;

    @FXML
    private TableColumn<Feedback, String> typeF;

    @FXML
    private TableColumn<Feedback, String> imageF;

    @FXML
    private TableColumn<Feedback, Integer> ReclamationId;

    @FXML
    private Button backReclamation;

    @FXML
    void initialize() {
        FeedbackService fs = new FeedbackService();

        ObservableList<Feedback> observableList = FXCollections.observableList(fs.getallF());

        TableViewF.setItems(observableList);
        idF.setCellValueFactory(new PropertyValueFactory<>("IdFeedback"));
        typeF.setCellValueFactory(new PropertyValueFactory<>("TypeFeedback"));
        messageF.setCellValueFactory(new PropertyValueFactory<>("Message"));
        noteF.setCellValueFactory(new PropertyValueFactory<>("Note"));
        dateCreationF.setCellValueFactory(new PropertyValueFactory<>("DateFeedback"));
        ReclamationId.setCellValueFactory(new PropertyValueFactory<>("ReclamationIdValue"));

        imageF.setCellValueFactory(new PropertyValueFactory<>("PieceJointeF"));
        imageF.setCellFactory(column -> new TableCell<Feedback, String>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(String imagePath, boolean empty) {
                super.updateItem(imagePath, empty);

                if (empty || imagePath == null || imagePath.isEmpty()) {
                    setGraphic(null); // Afficher rien si l'image est vide
                } else {
                    // Charger l'image depuis le chemin
                    Image image = new Image(new File(imagePath).toURI().toString());
                    imageView.setImage(image); // Définir l'image dans l'ImageView
                    imageView.setFitWidth(50); // Ajuster la largeur de l'image
                    imageView.setFitHeight(50); // Ajuster la hauteur de l'image
                    imageView.setPreserveRatio(true); // Conserver le ratio de l'image
                    setGraphic(imageView); // Afficher l'ImageView dans la cellule
                }
            }
        });
    }


    @FXML
    void BackReclamation(ActionEvent event) {
        // Fermer la fenêtre de modification sans enregistrer les modifications
        backReclamation.getScene().getWindow().hide();

        try {
            // Charger le fichier FXML de la page AjouterReclamation
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/AfficherReclamation.fxml")));
            Scene scene = new Scene(root);

            // Obtenir la fenêtre actuelle (stage) à partir de l'événement
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            // Définir la nouvelle scène sur le stage
            stage.setScene(scene);
            stage.show(); // Afficher la nouvelle scène

        } catch (IOException e) {
            // Gérer les erreurs de chargement du FXML
            e.printStackTrace();
            System.err.println("Erreur lors du chargement de AjouterReclamation.fxml");
        }
    }

    @FXML
    void ModifierFeedback() {
        Feedback feedbackChoisi = TableViewF.getSelectionModel().getSelectedItem();

        if (feedbackChoisi != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierFeedback.fxml"));
                Parent root = loader.load();

                // Passer la réclamation sélectionnée au contrôleur de la deuxième interface
                ModifierFeedbackController controller = loader.getController();
                controller.setFeedback(feedbackChoisi);

                // Afficher la deuxième interface
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Aucune réclamation sélectionnée.");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Vous devez sélectionner une réclamation !");
            alert.setContentText("Veuillez sélectionner une réclamation afin de modifier");
            alert.showAndWait();
        }
    }


    @FXML
    void SupprimerFeedback(ActionEvent event) {
        // Récupérer le feedback sélectionné
        Feedback feedbackChoisi = TableViewF.getSelectionModel().getSelectedItem();

        if (feedbackChoisi != null) {
            // Supprimer le feedback de la base de données
            FeedbackService fs = new FeedbackService();
            fs.supprimerF(feedbackChoisi);

            // Rafraîchir la TableView
            ObservableList<Feedback> observableList = FXCollections.observableList(fs.getallF());
            TableViewF.setItems(observableList);

            // Afficher un message de succès
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setContentText("Feedback supprimé avec succès !");
            alert.showAndWait();
        } else {
            System.out.println("Aucun feedback sélectionné.");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Vous devez sélectionner un feedback !");
            alert.setContentText("Veuillez sélectionner un feedback afin de le supprimer");
            alert.showAndWait();
        }
    }

}
