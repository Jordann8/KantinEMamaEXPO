package siptek.kantinemama.util;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;

public class SceneNavigator {
    public static void loadScene(Stage stage, String fxmlPath) {
        try {
            // Robust resource loading across multiple classloaders (critical for macOS/OpenJDK)
            URL url = SceneNavigator.class.getResource(fxmlPath);
            if (url == null) {
                String cleanedPath = fxmlPath.startsWith("/") ? fxmlPath.substring(1) : fxmlPath;
                url = Thread.currentThread().getContextClassLoader().getResource(cleanedPath);
            }
            if (url == null) {
                url = ClassLoader.getSystemResource(cleanedPath(fxmlPath));
            }

            if (url == null) {
                showExceptionDialog("FXML File Not Found", 
                    "Sistem tidak dapat menemukan file FXML: " + fxmlPath, 
                    new java.io.FileNotFoundException("Path: " + fxmlPath));
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
        } catch (Exception e) {
            System.err.println("Error loading FXML scene: " + fxmlPath);
            e.printStackTrace();
            showExceptionDialog("Error Loading Screen", 
                "Gagal memuat tampilan layar: " + fxmlPath, 
                e);
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

    private static String cleanedPath(String path) {
        if (path == null) return "";
        return path.startsWith("/") ? path.substring(1) : path;
    }

    private static void showExceptionDialog(String title, String header, Exception ex) {
        try {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(header);
            alert.setContentText(ex.getMessage());

            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            String exceptionText = sw.toString();

            Label label = new Label("Detail Stack Trace:");
            TextArea textArea = new TextArea(exceptionText);
            textArea.setEditable(false);
            textArea.setWrapText(true);

            textArea.setMaxWidth(Double.MAX_VALUE);
            textArea.setMaxHeight(Double.MAX_VALUE);
            GridPane.setVgrow(textArea, Priority.ALWAYS);
            GridPane.setHgrow(textArea, Priority.ALWAYS);

            GridPane expContent = new GridPane();
            expContent.setMaxWidth(Double.MAX_VALUE);
            expContent.add(label, 0, 0);
            expContent.add(textArea, 0, 1);

            alert.getDialogPane().setExpandableContent(expContent);
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
