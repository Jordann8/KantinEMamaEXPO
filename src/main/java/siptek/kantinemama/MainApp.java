package siptek.kantinemama;

import javafx.application.Application;
import javafx.stage.Stage;
import siptek.kantinemama.model.AppState;
import siptek.kantinemama.model.AppStateSnapshot;
import siptek.kantinemama.util.PersistenceManager;
import siptek.kantinemama.util.SceneNavigator;
import java.util.Optional;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Load the saved state from XML on startup
        Optional<AppStateSnapshot> snapshot = PersistenceManager.loadState();
        if (snapshot.isPresent()) {
            AppState.getInstance().loadFromSnapshot(snapshot.get());
        }

        primaryStage.setTitle("Kantin E Mama");

        // Save the state to XML on close request
        primaryStage.setOnCloseRequest(event -> PersistenceManager.saveState(AppState.getInstance()));

        // Load scene first (sets the scene and shows the stage internally)
        SceneNavigator.loadScene(primaryStage, "/siptek/kantinemama/view/pelanggan/HalamanAwalUtama.fxml");

        // Set resizable and maximized AFTER stage is shown to prevent platform-specific
        // window-peer bugs on macOS
        primaryStage.setResizable(true);
        primaryStage.setMaximized(true);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
