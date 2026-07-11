package siptek.kantinemama.controller.pegawai;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Controller stub untuk popup konfirmasi Hapus Menu (HapusMenu.fxml).
 * Method setNamaMenu(...) perlu dipanggil oleh KelolaMenuController
 * setelah FXMLLoader.load(), sebelum dialog ditampilkan, supaya teks
 * konfirmasi menyebut nama menu yang benar.
 */
public class HapusMenuController {

    @FXML private Label lblKonfirmasiHapus;
    @FXML private Button btnBatalHapus;
    @FXML private Button btnYaHapusData;

    private String menuItemIdYangDihapus;
    private boolean dikonfirmasi = false;

    public void setNamaMenu(String id, String nama) {
        this.menuItemIdYangDihapus = id;
        lblKonfirmasiHapus.setText("Apakah Anda yakin ingin menghapus \"" + nama + "\" dari katalog?");
    }

    public boolean isDikonfirmasi() {
        return dikonfirmasi;
    }

    @FXML
    void onBatalHapus(ActionEvent event) {
        dikonfirmasi = false;
        tutupDialog(event);
    }

    @FXML
    void onYaHapusData(ActionEvent event) {
        siptek.kantinemama.model.AppState.getInstance().getMenuItems().removeIf(item -> item.getId().equals(menuItemIdYangDihapus));
        siptek.kantinemama.util.PersistenceManager.saveState(siptek.kantinemama.model.AppState.getInstance());
        dikonfirmasi = true;
        tutupDialog(event);
    }

    private void tutupDialog(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
