package esprit.tn.controllers.Quiz;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class SearchController {

    @FXML
    private TextField queryField;

    @FXML
    private VBox imageContainer;

    @FXML
    private Button backButton;

    private static final String API_KEY = "AIzaSyD0_CTNEXeM_PGoKZnvGCAZ5qCmO2Bgjrk";
    private static final String CX = "f7f8f740f76f44999";

    @FXML
    protected void handleSearchButtonAction() {
        String query = queryField.getText();
        if (query.isEmpty()) {
            return;
        }

        try {
            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8.toString());
            String url = "https://www.googleapis.com/customsearch/v1?q=" + encodedQuery +
                    "&key=" + API_KEY +
                    "&cx=" + CX +
                    "&searchType=image" +
                    "&num=5";

            try (CloseableHttpClient client = HttpClients.createDefault()) {
                HttpGet request = new HttpGet(url);
                HttpResponse response = client.execute(request);

                if (response.getStatusLine().getStatusCode() == 200) {
                    String jsonResponse = EntityUtils.toString(response.getEntity());
                    JSONObject jsonObject = new JSONObject(jsonResponse);

                    if (jsonObject.has("items")) {
                        JSONArray items = jsonObject.getJSONArray("items");
                        imageContainer.getChildren().clear(); // Clear previous content

                        for (int i = 0; i < items.length(); i++) {
                            JSONObject image = items.getJSONObject(i);
                            String imageURL = image.getString("link");

                            ImageView imageView = new ImageView(new Image(imageURL));
                            imageView.setFitWidth(150);
                            imageView.setFitHeight(150);
                            imageView.setPreserveRatio(true);

                            Hyperlink link = new Hyperlink(imageURL);
                            link.setOnAction(event -> openURL(imageURL));

                            VBox imageBox = new VBox(imageView, link);
                            imageBox.setSpacing(5);
                            imageContainer.getChildren().add(imageBox);
                        }
                    } else {
                        System.out.println("No images found for the query: " + query);
                    }
                } else {
                    System.err.println("HTTP Error: " + response.getStatusLine().getStatusCode());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openURL(String url) {
        try {
            Desktop.getDesktop().browse(URI.create(url));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void handleBackButtonAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Quiz/AjouterExercice.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
