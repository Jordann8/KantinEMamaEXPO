package siptek.kantinemama.controller.pegawai;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import siptek.kantinemama.model.AppState;
import siptek.kantinemama.model.Meja;
import siptek.kantinemama.model.Pesanan;
import siptek.kantinemama.util.SceneNavigator;

public class DashboardUtamaPegawaiController {

    @FXML private Button btnSegarkan;
    @FXML private Button navAntrian;
    @FXML private Button navDasbor;
    @FXML private Button navKelolaMenu;
    @FXML private Button navKeluar;
    @FXML private Button navLaporan;
    @FXML private Button navPengaturan;
    @FXML private Button navStok;

    private AppState appState = AppState.getInstance();

    @FXML
    public void initialize() {
        updateStats();
    }

    private void updateStats() {
        try {
            VBox mainCol = (VBox) btnSegarkan.getParent().getParent().getParent();
            HBox statsHBox = (HBox) mainCol.getChildren().get(2);

            int activeCount = 0;
            int completedCount = 0;
            for (Pesanan p : appState.getOrders()) {
                if ("Selesai".equalsIgnoreCase(p.getStatusDapur())) {
                    completedCount++;
                } else {
                    activeCount++;
                }
            }

            int occupiedTables = 0;
            for (Meja m : appState.getTables()) {
                if ("TERISI".equalsIgnoreCase(m.getStatus())) {
                    occupiedTables++;
                }
            }

            VBox cardActive = (VBox) statsHBox.getChildren().get(0);
            Label lblActiveVal = (Label) cardActive.getChildren().get(1);
            lblActiveVal.setText(String.valueOf(activeCount));

            VBox cardCompleted = (VBox) statsHBox.getChildren().get(1);
            Label lblCompletedVal = (Label) cardCompleted.getChildren().get(1);
            lblCompletedVal.setText(String.valueOf(completedCount));

            VBox cardTables = (VBox) statsHBox.getChildren().get(2);
            HBox tableHBox = (HBox) cardTables.getChildren().get(1);
            Label lblTablesVal = (Label) tableHBox.getChildren().get(0);
            lblTablesVal.setText(String.valueOf(occupiedTables));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onSegarkan(ActionEvent event) {
        updateStats();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText("Data statistik berhasil diperbarui.");
        alert.showAndWait();
    }

    @FXML
    void onNavDasbor(ActionEvent event) {
        // Already here
    }

    @FXML
    void onNavKelolaMenu(ActionEvent event) {
        SceneNavigator.loadScene(event, "/siptek/kantinemama/view/pegawai/KelolaMenu.fxml");
    }

    @FXML
    void onNavAntrian(ActionEvent event) {
        SceneNavigator.loadScene(event, "/siptek/kantinemama/view/pegawai/AntrianPesanan.fxml");
    }

    @FXML
    void onNavStok(ActionEvent event) {
        SceneNavigator.loadScene(event, "/siptek/kantinemama/view/pegawai/StokBarang.fxml");
    }

    @FXML
    void onNavLaporan(ActionEvent event) {
        SceneNavigator.loadScene(event, "/siptek/kantinemama/view/pegawai/LaporanKeuangan.fxml");
    }

    @FXML
    void onNavPengaturan(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Akses Ditolak");
        alert.setHeaderText("Hak Akses Terbatas");
        alert.setContentText("Menu Pengaturan Sistem hanya dapat diakses oleh Owner/Admin.");
        alert.showAndWait();
    }

    @FXML
    void onNavKeluar(ActionEvent event) {
        SceneNavigator.loadScene(event, "/siptek/kantinemama/view/pelanggan/HalamanAwalUtama.fxml");
    }
}
