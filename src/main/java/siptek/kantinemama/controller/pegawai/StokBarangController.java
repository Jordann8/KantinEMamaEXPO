package siptek.kantinemama.controller.pegawai;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import siptek.kantinemama.model.AppState;
import siptek.kantinemama.model.StokItem;
import siptek.kantinemama.util.SceneNavigator;

public class StokBarangController {

    @FXML private Button btnKurangAyamGoreng;
    @FXML private Button btnKurangEsTeh;
    @FXML private Button btnKurangNasiPutih;
    @FXML private Button btnKurangRendang;
    @FXML private Button btnKurangTahuGehu;
    @FXML private Button btnNextPage;
    @FXML private Button btnNotif;
    @FXML private Button btnPrevPage;
    @FXML private Button btnRestockEsTeh;
    @FXML private Button btnTambahAyamGoreng;
    @FXML private Button btnTambahNasiPutih;
    @FXML private Button btnTambahRendang;
    @FXML private Button btnTambahTahuGehu;
    
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
        updateRowUI("ST-003", btnKurangNasiPutih); // Nasi Putih
        updateRowUI("ST-001", btnKurangAyamGoreng); // Ayam Goreng
        updateRowUI("ST-002", btnKurangEsTeh); // Es Teh
        updateRowUI("ST-005", btnKurangTahuGehu); // Tahu Gehu
        updateRowUI("ST-004", btnKurangRendang); // Rendang

        if ("Admin".equals(appState.getCurrentRole())) {
            if (navAntrian != null) {
                navAntrian.setVisible(false);
                navAntrian.setManaged(false);
            }
        }
    }

    private void updateRowUI(String idBahan, Button relativeBtn) {
        StokItem item = findStokItem(idBahan);
        if (item == null) return;

        HBox row = (HBox) relativeBtn.getParent().getParent();
        Label sisaLabel = (Label) row.getChildren().get(3);
        Label statusLabel = (Label) row.getChildren().get(5);

        sisaLabel.setText(String.valueOf(item.getSisaPorsi()));
        statusLabel.setText(item.getStatus());

        if ("STOK AMAN".equals(item.getStatus())) {
            sisaLabel.setStyle("-fx-text-fill: #111827; -fx-font-size: 13; -fx-font-weight: bold;");
            statusLabel.setStyle("-fx-background-color: #DCFCE7; -fx-text-fill: #16A34A; -fx-font-size: 10; -fx-font-weight: bold; -fx-padding: 4 8 4 8; -fx-background-radius: 6;");
        } else if ("HAMPIR HABIS".equals(item.getStatus())) {
            sisaLabel.setStyle("-fx-text-fill: #D97706; -fx-font-size: 13; -fx-font-weight: bold;");
            statusLabel.setStyle("-fx-background-color: #FEF3C7; -fx-text-fill: #D97706; -fx-font-size: 10; -fx-font-weight: bold; -fx-padding: 4 8 4 8; -fx-background-radius: 6;");
        } else {
            sisaLabel.setStyle("-fx-text-fill: #EF4444; -fx-font-size: 13; -fx-font-weight: bold;");
            statusLabel.setStyle("-fx-background-color: #EF4444; -fx-text-fill: #FFFFFF; -fx-font-size: 10; -fx-font-weight: bold; -fx-padding: 4 8 4 8; -fx-background-radius: 6;");
        }
    }

    private StokItem findStokItem(String idBahan) {
        for (StokItem si : appState.getStokItems()) {
            if (si.getIdBahan().equals(idBahan)) {
                return si;
            }
        }
        return null;
    }

    private void modifyStock(String idBahan, Button btn, int delta) {
        StokItem item = findStokItem(idBahan);
        if (item != null) {
            int newVal = item.getSisaPorsi() + delta;
            if (newVal < 0) newVal = 0;
            item.setSisaPorsi(newVal);
            updateRowUI(idBahan, btn);
        }
    }

    @FXML void onKurangNasiPutih(ActionEvent event) { modifyStock("ST-003", btnKurangNasiPutih, -5); }
    @FXML void onTambahNasiPutih(ActionEvent event) { modifyStock("ST-003", btnKurangNasiPutih, 5); }

    @FXML void onKurangAyamGoreng(ActionEvent event) { modifyStock("ST-001", btnKurangAyamGoreng, -5); }
    @FXML void onTambahAyamGoreng(ActionEvent event) { modifyStock("ST-001", btnKurangAyamGoreng, 5); }

    @FXML void onKurangEsTeh(ActionEvent event) { modifyStock("ST-002", btnKurangEsTeh, -10); }
    @FXML void onRestockEsTeh(ActionEvent event) { modifyStock("ST-002", btnKurangEsTeh, 50); }

    @FXML void onKurangTahuGehu(ActionEvent event) { modifyStock("ST-005", btnKurangTahuGehu, -5); }
    @FXML void onTambahTahuGehu(ActionEvent event) { modifyStock("ST-005", btnKurangTahuGehu, 5); }

    @FXML void onKurangRendang(ActionEvent event) { modifyStock("ST-004", btnKurangRendang, -5); }
    @FXML void onTambahRendang(ActionEvent event) { modifyStock("ST-004", btnKurangRendang, 5); }

    @FXML
    void onNotif(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Notifikasi Stok");
        alert.setHeaderText(null);
        alert.setContentText("Stok Es Teh saat ini berada di bawah batas minimum!");
        alert.showAndWait();
    }

    @FXML
    void onNavDasbor(ActionEvent event) {
        if ("Admin".equals(appState.getCurrentRole())) {
            SceneNavigator.loadScene(event, "/siptek/kantinemama/view/owner/PortalOwner.fxml");
        } else {
            SceneNavigator.loadScene(event, "/siptek/kantinemama/view/pegawai/DashboardUtamaPegawai.fxml");
        }
    }

    @FXML
    void onNavKelolaMenu(ActionEvent event) {
        if ("Admin".equals(appState.getCurrentRole())) {
            SceneNavigator.loadScene(event, "/siptek/kantinemama/view/owner/ManajemenKatalogMenu.fxml");
        } else {
            SceneNavigator.loadScene(event, "/siptek/kantinemama/view/pegawai/KelolaMenu.fxml");
        }
    }

    @FXML
    void onNavAntrian(ActionEvent event) {
        SceneNavigator.loadScene(event, "/siptek/kantinemama/view/pegawai/AntrianPesanan.fxml");
    }

    @FXML void onNavStok(ActionEvent event) { /* Already here */ }

    @FXML
    void onNavLaporan(ActionEvent event) {
        if ("Admin".equals(appState.getCurrentRole())) {
            SceneNavigator.loadScene(event, "/siptek/kantinemama/view/owner/LaporanKeuangan1.fxml");
        } else {
            SceneNavigator.loadScene(event, "/siptek/kantinemama/view/pegawai/LaporanKeuangan.fxml");
        }
    }
    
    @FXML
    void onNavPengaturan(ActionEvent event) {
        if ("Admin".equals(appState.getCurrentRole())) {
            SceneNavigator.loadScene(event, "/siptek/kantinemama/view/owner/PengaturanSistem.fxml");
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Akses Ditolak");
            alert.setHeaderText("Hak Akses Terbatas");
            alert.setContentText("Menu Pengaturan Sistem hanya dapat diakses oleh Owner/Admin.");
            alert.showAndWait();
        }
    }

    @FXML void onNavKeluar(ActionEvent event) { SceneNavigator.loadScene(event, "/siptek/kantinemama/view/pelanggan/HalamanAwalUtama.fxml"); }

    @FXML void onNextPage(ActionEvent event) {}
    @FXML void onPrevPage(ActionEvent event) {}
}
