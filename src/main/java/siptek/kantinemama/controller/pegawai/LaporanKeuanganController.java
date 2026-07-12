package siptek.kantinemama.controller.pegawai;

import java.io.FileWriter;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import siptek.kantinemama.util.SceneNavigator;

public class LaporanKeuanganController {

    @FXML private Button btnEksporPdf;
    @FXML private Button btnFilter;
    @FXML private Button btnNextPage;
    @FXML private Button btnPage1;
    @FXML private Button btnPage2;
    @FXML private Button btnPage3;
    @FXML private Button btnPilihBulan;
    @FXML private Button btnPrevPage;
    
    @FXML private Button navAntrian;
    @FXML private Button navDasbor;
    @FXML private Button navKelolaMenu;
    @FXML private Button navKeluar;
    @FXML private Button navLaporan;
    @FXML private Button navPengaturan;
    @FXML private Button navStok;

    @FXML
    public void initialize() {
    }

    @FXML
    void onEksporPdf(ActionEvent event) {
        try (FileWriter writer = new FileWriter("laporan_keuangan_staf.txt")) {
            writer.write("==================================================\n");
            writer.write("           LAPORAN KEUANGAN KANTIN E MAMA         \n");
            writer.write("               (LAPORAN OPERASIONAL)              \n");
            writer.write("==================================================\n\n");
            writer.write("Pendapatan Bersih: Rp 10.480.000\n");
            writer.write("Pajak Terkumpul (10%): Rp 148.000\n");
            writer.write("Volume Transaksi: 1.212 Order\n");
            writer.write("Rata-rata Harian: 40 order/hari\n");
            writer.write("--------------------------------------------------\n");
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Ekspor Laporan");
            alert.setContentText("Laporan keuangan operasional berhasil disimpan ke 'laporan_keuangan_staf.txt'!");
            alert.showAndWait();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Gagal menyimpan file laporan: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void onFilter(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Filter Laporan");
        alert.setContentText("Menyaring laporan keuangan untuk periode terpilih.");
        alert.showAndWait();
    }

    @FXML
    void onPilihBulan(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Pilih Bulan");
        alert.setContentText("Membuka pilihan bulan operasional.");
        alert.showAndWait();
    }

    @FXML void onNavDasbor(ActionEvent event) { SceneNavigator.loadScene(event, "/siptek/kantinemama/view/pegawai/DashboardUtamaPegawai.fxml"); }
    @FXML void onNavKelolaMenu(ActionEvent event) { SceneNavigator.loadScene(event, "/siptek/kantinemama/view/pegawai/KelolaMenu.fxml"); }
    @FXML void onNavAntrian(ActionEvent event) { SceneNavigator.loadScene(event, "/siptek/kantinemama/view/pegawai/AntrianPesanan.fxml"); }
    @FXML void onNavStok(ActionEvent event) { SceneNavigator.loadScene(event, "/siptek/kantinemama/view/pegawai/StokBarang.fxml"); }
    @FXML void onNavLaporan(ActionEvent event) { /* Already here */ }
    
    @FXML
    void onNavPengaturan(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Akses Ditolak");
        alert.setHeaderText("Hak Akses Terbatas");
        alert.setContentText("Menu Pengaturan Sistem hanya dapat diakses oleh Owner/Admin.");
        alert.showAndWait();
    }

    @FXML void onNavKeluar(ActionEvent event) { SceneNavigator.loadScene(event, "/siptek/kantinemama/view/pelanggan/HalamanAwalUtama.fxml"); }

    @FXML void onNextPage(ActionEvent event) {}
    @FXML void onPage1(ActionEvent event) {}
    @FXML void onPage2(ActionEvent event) {}
    @FXML void onPage3(ActionEvent event) {}
    @FXML void onPrevPage(ActionEvent event) {}
}
