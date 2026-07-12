package siptek.kantinemama;

import java.util.Optional;

import javafx.application.Application;
import javafx.stage.Stage;
import siptek.kantinemama.model.AppState;
import siptek.kantinemama.model.AppStateSnapshot;
import siptek.kantinemama.util.PersistenceManager;
import siptek.kantinemama.util.SceneNavigator;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        Optional<AppStateSnapshot> snapshot = PersistenceManager.loadState();
        if (snapshot.isPresent()) {
            AppState.getInstance().loadFromSnapshot(snapshot.get());
        }

        primaryStage.setTitle("Kantin E Mama");
        primaryStage.setResizable(true);

        primaryStage.setOnCloseRequest(event -> PersistenceManager.saveState(AppState.getInstance()));

        SceneNavigator.loadScene(primaryStage, "/siptek/kantinemama/view/pelanggan/HalamanAwalUtama.fxml");

        javafx.application.Platform.runLater(() -> primaryStage.setMaximized(true));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
