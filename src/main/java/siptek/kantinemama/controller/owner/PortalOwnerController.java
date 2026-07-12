package siptek.kantinemama.controller.owner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import siptek.kantinemama.model.AppState;
import siptek.kantinemama.model.Pesanan;
import siptek.kantinemama.util.SceneNavigator;

public class PortalOwnerController {

    @FXML private Button btnBulanan;
    @FXML private Button btnMingguan;
    @FXML private Button btnNotif;
    @FXML private Button btnSearch;
    
    @FXML private Button navDasbor;
    @FXML private Button navKelolaMenu;
    @FXML private Button navKeluar;
    @FXML private Button navLaporan;
    @FXML private Button navPengaturan;
    @FXML private Button navStok;

    private AppState appState = AppState.getInstance();

    @FXML
    public void initialize() {
        updateDashboardStats();
    }

    private void updateDashboardStats() {
        try {
            VBox mainCol = (VBox) btnSearch.getParent().getParent();
            HBox statsHBox = (HBox) mainCol.getChildren().get(2);

            int totalRevenue = 0;
            int orderCount = 0;

            for (Pesanan p : appState.getOrders()) {
                if ("Lunas".equalsIgnoreCase(p.getStatusPembayaran())) {
                    totalRevenue += (int) p.getTotalPembayaran();
                    orderCount++;
                }
            }

            int averageValue = orderCount > 0 ? totalRevenue / orderCount : 0;

            
            VBox cardRevenue = (VBox) statsHBox.getChildren().get(0);
            Label lblRevenueVal = (Label) cardRevenue.getChildren().get(1);
            lblRevenueVal.setText("Rp " + String.format("%,d", totalRevenue).replace(',', '.'));

            
            VBox cardOrders = (VBox) statsHBox.getChildren().get(1);
            Label lblOrdersVal = (Label) cardOrders.getChildren().get(1);
            lblOrdersVal.setText(String.valueOf(orderCount));

            
            VBox cardAvg = (VBox) statsHBox.getChildren().get(2);
            Label lblAvgVal = (Label) cardAvg.getChildren().get(1);
            lblAvgVal.setText("Rp " + String.format("%,d", averageValue).replace(',', '.'));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int parseTotal(String totalStr) {
        if (totalStr == null) return 0;
        String digits = totalStr.replaceAll("[^\\d]", "");
        if (digits.isEmpty()) return 0;
        try {
            return Integer.parseInt(digits);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @FXML
    void onBulanan(ActionEvent event) {
        showInfo("Tren Penjualan", "Menampilkan tren penjualan bulanan.");
    }

    @FXML
    void onMingguan(ActionEvent event) {
        showInfo("Tren Penjualan", "Menampilkan tren penjualan mingguan.");
    }

    @FXML
    void onNotif(ActionEvent event) {
        showInfo("Notifikasi", "Tidak ada notifikasi baru untuk Owner.");
    }

    @FXML
    void onSearch(ActionEvent event) {
        showInfo("Pencarian", "Fitur pencarian data operasional.");
    }

    @FXML void onNavDasbor(ActionEvent event) { /* Already here */ }
    @FXML void onNavKelolaMenu(ActionEvent event) { SceneNavigator.loadScene(event, "/siptek/kantinemama/view/owner/ManajemenKatalogMenu.fxml"); }
    @FXML void onNavStok(ActionEvent event) { SceneNavigator.loadScene(event, "/siptek/kantinemama/view/pegawai/StokBarang.fxml"); }
    @FXML void onNavLaporan(ActionEvent event) { SceneNavigator.loadScene(event, "/siptek/kantinemama/view/owner/LaporanKeuangan1.fxml"); }
    @FXML void onNavPengaturan(ActionEvent event) { SceneNavigator.loadScene(event, "/siptek/kantinemama/view/owner/PengaturanSistem.fxml"); }
    @FXML void onNavKeluar(ActionEvent event) { SceneNavigator.loadScene(event, "/siptek/kantinemama/view/pelanggan/HalamanAwalUtama.fxml"); }

    private void showInfo(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
