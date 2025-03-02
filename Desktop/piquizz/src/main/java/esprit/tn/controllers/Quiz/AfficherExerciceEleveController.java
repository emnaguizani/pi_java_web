package esprit.tn.controllers.Quiz;

import esprit.tn.entities.Exercice;
import esprit.tn.entities.Reponse;
import esprit.tn.services.ExerciceService;
import esprit.tn.services.ReponseService;
import esprit.tn.services.NoteService;
import esprit.tn.main.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AfficherExerciceEleveController {

    @FXML private ListView<Exercice> exerciceListView;
    @FXML private Button submitButton; // Reference to the main Submit button

    private ExerciceService exerciceService;
    private ReponseService reponseService;
    private NoteService noteService;
    private Connection cnx = DatabaseConnection.getInstance().getCnx();
    private int quizId;
    private int eleveId;
    private boolean isQuizSubmitted = false;
    private Map<Exercice, ToggleGroup> exerciseToggleGroups = new HashMap<>(); // Map to store ToggleGroups

    public void setQuizId(int quizId) {
        this.quizId = quizId;
        loadExercices();
    }

    public void setEleveId(int eleveId) {
        this.eleveId = eleveId;
    }

    @FXML
    public void initialize() {
        exerciceService = new ExerciceService(cnx);
        reponseService = new ReponseService(cnx);
        noteService = new NoteService(cnx);
        exerciceListView.setPrefWidth(900);
    }

    private void loadExercices() {
        try {
            List<Exercice> exercices = exerciceService.getExercicesByQuizId(quizId);
            exerciceListView.getItems().setAll(exercices);

            exerciceListView.setCellFactory(param -> new ListCell<Exercice>() {
                @Override
                protected void updateItem(Exercice exercice, boolean empty) {
                    super.updateItem(exercice, empty);
                    if (empty || exercice == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        System.out.println("Exercise Image Path: " + exercice.getImagePath());

                        VBox vbox = new VBox(10);
                        vbox.setAlignment(Pos.CENTER);
                        vbox.setStyle("-fx-padding: 10; -fx-border-color: #cccccc; -fx-border-radius: 5;");

                        Label questionLabel = new Label("Question: " + exercice.getQuestion());
                        questionLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
                        questionLabel.setAlignment(Pos.CENTER);

                        ToggleGroup toggleGroup = new ToggleGroup();
                        exerciseToggleGroups.put(exercice, toggleGroup); // Store the ToggleGroup
                        VBox optionsBox = new VBox(5);
                        optionsBox.setAlignment(Pos.CENTER);

                        String optionsString = exercice.getOptions().toString();
                        if (optionsString != null && !optionsString.isEmpty()) {
                            optionsString = optionsString.replace("[", "").replace("]", "").trim();
                            String[] optionsArray = optionsString.split(",");

                            for (String option : optionsArray) {
                                RadioButton radioButton = new RadioButton(option.trim());
                                radioButton.setToggleGroup(toggleGroup);
                                radioButton.setAlignment(Pos.CENTER);
                                radioButton.setStyle("-fx-font-size: 14px; -fx-padding: 5px;");
                                radioButton.setDisable(isQuizSubmitted); // Disable if submitted
                                optionsBox.getChildren().add(radioButton);
                            }
                        } else {
                            optionsBox.getChildren().add(new Label("No options available."));
                        }

                        ImageView imageView = new ImageView();
                        String imagePath = exercice.getImagePath();
                        if (imagePath != null && !imagePath.isEmpty()) {
                            Image image = loadImageFromPath(imagePath);
                            if (image != null) {
                                imageView.setImage(image);
                                imageView.setFitWidth(400);
                                imageView.setPreserveRatio(true);
                                vbox.getChildren().add(imageView);
                            } else {
                                System.out.println("Failed to load image for path: " + imagePath);
                            }
                        }

                        vbox.getChildren().addAll(questionLabel, optionsBox);
                        setGraphic(vbox);
                    }
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
            showMessage("Error loading exercises from the database.");
        }
    }

    @FXML
    private void handleSubmitAll() {
        try {
            if (isQuizSubmitted) {
                showMessage("You have already submitted your responses for this quiz.");
                return;
            }

            List<Reponse> reponses = new ArrayList<>();

            for (Exercice exercice : exerciceListView.getItems()) {
                ToggleGroup toggleGroup = exerciseToggleGroups.get(exercice);
                if (toggleGroup != null) {
                    RadioButton selectedRadioButton = (RadioButton) toggleGroup.getSelectedToggle();
                    if (selectedRadioButton != null) {
                        String answer = selectedRadioButton.getText();
                        Reponse reponse = new Reponse(exercice.getIdE(), answer, eleveId);
                        reponseService.ajouterReponse(reponse);
                        reponses.add(reponse);
                    } else {
                        showMessage("No answer selected for exercise: " + exercice.getQuestion());
                    }
                } else {
                    showMessage("No options found for exercise: " + exercice.getQuestion());
                }
            }

            double noteTotale = reponses.stream()
                    .mapToDouble(Reponse::getNoteExercice)
                    .sum();

            if (noteService.noteExiste(eleveId, quizId)) {
                noteService.mettreAJourNote(eleveId, quizId, noteTotale);
            } else {
                noteService.ajouterNote(eleveId, quizId, noteTotale);
            }

            isQuizSubmitted = true;
            showMessage("Total score for the quiz: " + noteTotale);
            disableSubmitButton();
            exerciceListView.refresh(); // Refresh to apply disabled state to RadioButtons
            handleExit();
        } catch (SQLException e) {
            e.printStackTrace();
            showMessage("Error calculating or saving the total score.");
        }
    }

    @FXML
    private void handleExit() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Quiz/AfficherQuizEleve.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) exerciceListView.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showMessage("Error loading the quiz list page.");
        }
    }

    private void showMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void disableSubmitButton() {
        if (submitButton != null) {
            submitButton.setDisable(true);
        }
    }

    public Image loadImageFromPath(String imagePath) {
        if (imagePath == null || imagePath.isEmpty()) {
            return null;
        }
        try {
            return new Image("file:" + imagePath);
        } catch (Exception e) {
            System.err.println("Error loading image: " + e.getMessage());
            return null;
        }
    }
}