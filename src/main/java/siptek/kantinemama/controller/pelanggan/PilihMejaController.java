package siptek.kantinemama.controller.pelanggan;

import java.util.HashMap;
import java.util.Map;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import siptek.kantinemama.model.AppState;
import siptek.kantinemama.model.Meja;
import siptek.kantinemama.util.SceneNavigator;

public class PilihMejaController {

    @FXML private Button btnBatal;
    @FXML private Button btnKonfirmasi;
    @FXML private Button btnTutup;
    @FXML private Label lblMejaTerpilih;

    @FXML private VBox mejaMO1;
    @FXML private VBox mejaMO2;
    @FXML private VBox mejaMO3;
    @FXML private VBox mejaMO4;
    @FXML private VBox mejaMO5;
    @FXML private VBox mejaMO6;
    @FXML private VBox mejaMO7;
    @FXML private VBox mejaMO8;
    @FXML private VBox mejaMO9;
    @FXML private VBox mejaMO10;
    @FXML private VBox mejaMO11;
    @FXML private VBox mejaMO12;
    @FXML private VBox mejaMO13;
    @FXML private VBox mejaMO14;
    @FXML private VBox mejaMO15;
    @FXML private VBox mejaMO16;
    @FXML private VBox mejaMO17;

    private AppState appState = AppState.getInstance();
    private Map<String, VBox> mejaNodes = new HashMap<>();

    @FXML
    public void initialize() {
        mejaNodes.put("mejaMO1", mejaMO1);
        mejaNodes.put("mejaMO2", mejaMO2);
        mejaNodes.put("mejaMO3", mejaMO3);
        mejaNodes.put("mejaMO4", mejaMO4);
        mejaNodes.put("mejaMO5", mejaMO5);
        mejaNodes.put("mejaMO6", mejaMO6);
        mejaNodes.put("mejaMO7", mejaMO7);
        mejaNodes.put("mejaMO8", mejaMO8);
        mejaNodes.put("mejaMO9", mejaMO9);
        mejaNodes.put("mejaMO10", mejaMO10);
        mejaNodes.put("mejaMO11", mejaMO11);
        mejaNodes.put("mejaMO12", mejaMO12);
        mejaNodes.put("mejaMO13", mejaMO13);
        mejaNodes.put("mejaMO14", mejaMO14);
        mejaNodes.put("mejaMO15", mejaMO15);
        mejaNodes.put("mejaMO16", mejaMO16);
        mejaNodes.put("mejaMO17", mejaMO17);

        appState.setSelectedMeja(null);

        refreshMejaStyles();
        lblMejaTerpilih.setText("Belum ada meja terpilih");
    }

    private void refreshMejaStyles() {
        for (Meja meja : appState.getTables()) {
            VBox node = mejaNodes.get(meja.getKode());
            if (node == null || node.getChildren().isEmpty()) continue;

            Label textLabel = (Label) node.getChildren().get(0);

            if ("KOSONG".equals(meja.getStatus())) {
                node.setStyle("-fx-background-color: #E0F9F9; -fx-border-color: #26C6DA; -fx-border-width: 1.5; -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand;");
                textLabel.setStyle("-fx-text-fill: #0F2044; -fx-font-size: 14; -fx-font-weight: bold;");
            } else if ("TERISI".equals(meja.getStatus())) {
                node.setStyle("-fx-background-color: #FFEBEE; -fx-border-color: #EF4444; -fx-border-width: 1.5; -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand;");
                textLabel.setStyle("-fx-text-fill: #EF4444; -fx-font-size: 14; -fx-font-weight: bold;");
            } else if ("TERPILIH".equals(meja.getStatus())) {
                node.setStyle("-fx-background-color: #BBDEFB; -fx-border-color: #1565C0; -fx-border-width: 1.5; -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand;");
                textLabel.setStyle("-fx-text-fill: #1565C0; -fx-font-size: 14; -fx-font-weight: bold;");
            }
        }
    }

    @FXML
    void onPilihMeja(MouseEvent event) {
        VBox clickedNode = (VBox) event.getSource();
        String kodeMeja = clickedNode.getId();

        Meja clickedMeja = null;
        for (Meja meja : appState.getTables()) {
            if (meja.getKode().equals(kodeMeja)) {
                clickedMeja = meja;
                break;
            }
        }

        if (clickedMeja == null) return;

        if ("TERISI".equals(clickedMeja.getStatus())) {
            showWarning("Meja Terisi", "Maaf, meja ini sedang terisi oleh pelanggan lain.");
            return;
        }

        for (Meja meja : appState.getTables()) {
            if ("TERPILIH".equals(meja.getStatus())) {
                meja.setStatus("KOSONG");
            }
        }

        clickedMeja.setStatus("TERPILIH");
        appState.setSelectedMeja(kodeMeja);
        
        Label textLabel = (Label) clickedNode.getChildren().get(0);
        lblMejaTerpilih.setText(textLabel.getText());

        refreshMejaStyles();
    }

    @FXML
    void onBatal(ActionEvent event) {
        SceneNavigator.loadScene(event, "/siptek/kantinemama/view/pelanggan/KatalogMenu.fxml");
    }

    @FXML
    void onTutup(ActionEvent event) {
        SceneNavigator.loadScene(event, "/siptek/kantinemama/view/pelanggan/KatalogMenu.fxml");
    }

    @FXML
    void onKonfirmasi(ActionEvent event) {
        if (appState.getSelectedMeja() == null) {
            showWarning("Pilih Meja", "Silakan pilih meja terlebih dahulu!");
            return;
        }

        for (Meja meja : appState.getTables()) {
            if (meja.getKode().equals(appState.getSelectedMeja())) {
                meja.setStatus("TERISI");
                break;
            }
        }

        SceneNavigator.loadScene(event, "/siptek/kantinemama/view/pelanggan/KonfirmasiPesanan.fxml");
    }

    private void showWarning(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
