package esprit.tn.main;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class MainFX extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root= FXMLLoader.load(getClass().getResource(""));
    }


    public static void main(String[] args) {
        launch(args);
    }
}
