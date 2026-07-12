package siptek.kantinemama.controller.owner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import siptek.kantinemama.util.SceneNavigator;

public class PengaturanSistemController {

    @FXML private Button btnBatal;
    @FXML private Button btnNotif;
    @FXML private Button btnSimpanPerubahan;
    
    @FXML private Button navDasbor;
    @FXML private Button navKelolaMenu;
    @FXML private Button navKeluar;
    @FXML private Button navLaporan;
    @FXML private Button navPengaturan;
    @FXML private Button navStok;
    
    @FXML private TextField txtIdKantin;
    @FXML private TextField txtJamBuka;
    @FXML private TextField txtJamTutup;
    @FXML private PasswordField txtKonfirmasiPassword;
    @FXML private TextField txtNamaKantin;
    @FXML private PasswordField txtPasswordBaru;
    @FXML private PasswordField txtPasswordSaatIni;

    @FXML
    public void initialize() {
        if (txtIdKantin != null) txtIdKantin.setText("KT-MAMA-01");
        if (txtNamaKantin != null) txtNamaKantin.setText("Kantin E Mama");
        if (txtJamBuka != null) txtJamBuka.setText("08:00");
        if (txtJamTutup != null) txtJamTutup.setText("21:00");
    }

    @FXML
    void onBatal(ActionEvent event) {
        initialize();
        showInfo("Batal", "Perubahan pengaturan dibatalkan.");
    }

    @FXML
    void onNotif(ActionEvent event) {
        showInfo("Notifikasi", "Tidak ada notifikasi baru.");
    }

    @FXML
    void onSimpanPerubahan(ActionEvent event) {
        String nama = txtNamaKantin.getText();
        String jamBuka = txtJamBuka.getText();
        String jamTutup = txtJamTutup.getText();
        
        showInfo("Sukses", "Pengaturan sistem berhasil diperbarui:\n" +
                "- Nama Kantin: " + nama + "\n" +
                "- Jam Operasional: " + jamBuka + " - " + jamTutup);
    }

    @FXML void onNavDasbor(ActionEvent event) { SceneNavigator.loadScene(event, "/siptek/kantinemama/view/owner/PortalOwner.fxml"); }
    @FXML void onNavKelolaMenu(ActionEvent event) { SceneNavigator.loadScene(event, "/siptek/kantinemama/view/owner/ManajemenKatalogMenu.fxml"); }
    @FXML void onNavStok(ActionEvent event) { SceneNavigator.loadScene(event, "/siptek/kantinemama/view/pegawai/StokBarang.fxml"); }
    @FXML void onNavLaporan(ActionEvent event) { SceneNavigator.loadScene(event, "/siptek/kantinemama/view/owner/LaporanKeuangan1.fxml"); }
    @FXML void onNavPengaturan(ActionEvent event) { /* Already here */ }
    @FXML void onNavKeluar(ActionEvent event) { SceneNavigator.loadScene(event, "/siptek/kantinemama/view/pelanggan/HalamanAwalUtama.fxml"); }

    private void showInfo(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
