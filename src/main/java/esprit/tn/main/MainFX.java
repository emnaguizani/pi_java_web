package esprit.tn.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class MainFX extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Charger l'interface AjouterAbsence.fxml au démarrage
        Parent root = FXMLLoader.load(getClass().getResource("/AjouterSeance.fxml"));

        primaryStage.setTitle("Gestion des Absences");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();


    }

    public static void main(String[] args) {
        launch(args);
        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
        System.setProperty("jdk.httpclient.HttpClient.log", "errors");
        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");

    }
}
