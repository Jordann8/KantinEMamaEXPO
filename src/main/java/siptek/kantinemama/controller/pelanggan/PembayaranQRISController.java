package siptek.kantinemama.controller.pelanggan;

import java.util.Random;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import siptek.kantinemama.model.AppState;
import siptek.kantinemama.util.QRCodeGenerator;
import siptek.kantinemama.util.SceneNavigator;

public class PembayaranQRISController {

    @FXML
    private Button btnBatal;

    @FXML
    private Button btnCekStatus;

    @FXML
    private ImageView imgQrCode;

    @FXML
    private Label lblCountdown;

    private AppState appState = AppState.getInstance();
    private Timeline timeline;
    private int timeRemainingSeconds = 15 * 60; // 15 menit

    @FXML
    public void initialize() {
        if (appState.getCurrentOrderId() == null) {
            int randomNum = 10000 + new Random().nextInt(90000);
            appState.setCurrentOrderId("ORD-" + randomNum);
        }

        try {
            String qrData = String.format("KANTINEMAMA|%s|%.0f", appState.getCurrentOrderId(),
                    appState.getCurrentTotal());
            Image qrImage = QRCodeGenerator.generate(qrData, 150);
            imgQrCode.setImage(qrImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            VBox qrContainer = (VBox) imgQrCode.getParent();
            Label amountLabel = (Label) qrContainer.getChildren().get(4);
            amountLabel.setText(
                    "Total Amount: " + siptek.kantinemama.util.CurrencyUtil.formatRupiah(appState.getCurrentTotal()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            timeRemainingSeconds--;
            if (timeRemainingSeconds <= 0) {
                timeline.stop();
                lblCountdown.setText("⏱  Waktu pembayaran habis");
                showError("Pembayaran Kadaluarsa", "Waktu pembayaran Anda telah habis. Silakan pesan kembali.");
                javafx.stage.Stage stage = (javafx.stage.Stage) btnBatal.getScene().getWindow();
                SceneNavigator.loadScene(stage, "/siptek/kantinemama/view/pelanggan/KatalogMenu.fxml");
            } else {
                int minutes = timeRemainingSeconds / 60;
                int seconds = timeRemainingSeconds % 60;
                lblCountdown.setText(String.format("⏱  Sisa waktu pembayaran: %02d:%02d", minutes, seconds));
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    @FXML
    void onBatal(ActionEvent event) {
        if (timeline != null) {
            timeline.stop();
        }
        appState.clearCart();
        appState.setCurrentOrderId(null);
        SceneNavigator.loadScene(event, "/siptek/kantinemama/view/pelanggan/KatalogMenu.fxml");
    }

    @FXML
    void onCekStatus(ActionEvent event) {
        if (timeline != null) {
            timeline.stop();
        }

        java.util.ArrayList<siptek.kantinemama.model.CartItem> orderedItems = new java.util.ArrayList<>(
                appState.getCurrentCart());
        siptek.kantinemama.model.Pesanan pesanan = new siptek.kantinemama.model.Pesanan(
                appState.getCurrentOrderId(),
                orderedItems,
                "Lunas",
                "QRIS",
                appState.getSelectedMeja(),
                "Pesanan Baru",
                java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        appState.getOrders().add(0, pesanan);

        SceneNavigator.loadScene(event, "/siptek/kantinemama/view/pelanggan/PembayaranBerhasil.fxml");
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
