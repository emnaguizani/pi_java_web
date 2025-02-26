package esprit.tn.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.lang.*;

public class MainFX extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("/logIn.fxml"));

        Scene scene=new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("usersInterface");
        primaryStage.show();

    }
    public static void main(String[] args) {
        launch(args);
    }
}
