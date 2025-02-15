package esprit.tn.main;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

public class MainFX extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root= FXMLLoader.load(getClass().getResource("/AfficherCours.fxml"));
        Scene scene=new Scene(root);
        stage.setScene(scene);
        stage.setTitle("addAnewCourse");

        stage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
