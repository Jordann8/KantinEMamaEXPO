package siptek.kantinemama.util;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;

public class SceneNavigator {
    public static void loadScene(Stage stage, String fxmlPath) {
        try {
            URL url = SceneNavigator.class.getResource(fxmlPath);
            if (url == null) {
                System.err.println("Cannot find FXML file: " + fxmlPath);
                return;
            }
            Parent root = FXMLLoader.load(url);
            if (stage.getScene() != null) {
                stage.getScene().setRoot(root);
            } else {
                stage.setScene(new Scene(root));
            }
            if (!stage.isShowing()) {
                stage.show();
            }
        } catch (IOException e) {
            System.err.println("Error loading FXML scene: " + fxmlPath);
            e.printStackTrace();
        }
    }

    public static void loadScene(Event event, String fxmlPath) {
        if (event == null || event.getSource() == null) {
            System.err.println("Event or Event source is null");
            return;
        }
        if (!(event.getSource() instanceof Node)) {
            System.err.println("Event source is not a JavaFX Node");
            return;
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        loadScene(stage, fxmlPath);
    }
}
