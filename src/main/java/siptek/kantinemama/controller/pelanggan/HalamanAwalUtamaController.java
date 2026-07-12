package siptek.kantinemama.controller.pelanggan;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import siptek.kantinemama.model.AppState;
import siptek.kantinemama.util.SceneNavigator;

public class HalamanAwalUtamaController {

    private AppState appState = AppState.getInstance();

    @FXML
    private Button btnDenah;

    @FXML
    private Button btnLogin;

    @FXML
    private Button btnPesan;

    @FXML
    private ComboBox<String> cmbRole;

    @FXML
    private Hyperlink linkBantuan;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtUsername;

    @FXML
    void onBantuanClick(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Bantuan Akses");
        alert.setHeaderText("Informasi Login");
        alert.setContentText("Kredensial Login Default:\n" +
                "- Pegawai: username 'pegawai', password 'pegawai' (Pilih Pegawai Kantin)\n" +
                "- Owner/Admin: username 'owner', password 'owner' (Pilih Admin)");
        alert.showAndWait();
    }

    @FXML
    void onDenahClick(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Denah Kantin");
        alert.setHeaderText("Denah Kantin E Mama");
        alert.setContentText("Denah kantin saat ini menampilkan area makan utama (Meja 1-17) di area depan kasir.");
        alert.showAndWait();
    }

    @FXML
    void onLoginClick(ActionEvent event) {
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        String role = cmbRole.getValue();

        if (role == null) {
            showError("Peringatan", "Silakan pilih role terlebih dahulu!");
            return;
        }

        if (username == null || username.trim().isEmpty() || password == null || password.isEmpty()) {
            showError("Peringatan", "Username dan Password wajib diisi!");
            return;
        }

        if ("Pegawai Kantin".equals(role)) {
            if ("pegawai".equals(username) && "pegawai".equals(password)) {
                appState.setCurrentRole("Pegawai");
                SceneNavigator.loadScene(event, "/siptek/kantinemama/view/pegawai/AntrianPesanan.fxml");
            } else {
                showError("Login Gagal", "Username atau Password salah untuk role Pegawai!");
            }
        } else if ("Admin".equals(role)) {
            if ("owner".equals(username) && "owner".equals(password)) {
                appState.setCurrentRole("Admin");
                SceneNavigator.loadScene(event, "/siptek/kantinemama/view/owner/LaporanKeuangan1.fxml");
            } else {
                showError("Login Gagal", "Username atau Password salah untuk role Admin/Owner!");
            }
        }
    }

    @FXML
    void onPesanClick(ActionEvent event) {
        SceneNavigator.loadScene(event, "/siptek/kantinemama/view/pelanggan/HalamanKatalogMenu.fxml");
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
