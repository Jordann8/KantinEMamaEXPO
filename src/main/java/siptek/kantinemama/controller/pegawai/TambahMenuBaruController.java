package siptek.kantinemama.controller.pegawai;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

/**
 * Controller stub untuk popup Tambah Menu Baru (TambahMenuBaru.fxml).
 * Field dan handler sudah lengkap sesuai fx:id di FXML, tapi logic
 * penyimpanan ke AppState/MenuItem masih perlu diisi oleh Antigravity
 * (lihat komentar TODO). Ini dibuat supaya FXML bisa langsung di-load
 * tanpa "LoadException: No controller specified".
 */
public class TambahMenuBaruController {

    @FXML private Button btnTutupTambah;
    @FXML private StackPane fotoUploadBoxTambah;
    @FXML private VBox placeholderFotoTambah;
    @FXML private ImageView imgPreviewTambah;
    @FXML private TextField txtNamaMenuTambah;
    @FXML private ComboBox<String> cmbKategoriTambah;
    @FXML private TextField txtHargaTambah;
    @FXML private TextArea txtDeskripsiTambah;
    @FXML private ToggleButton toggleStatusTambah;
    @FXML private Button btnBatalTambah;
    @FXML private Button btnSimpanTambah;

    private File fotoTerpilih;

    @FXML
    public void initialize() {
        setupToggleSwitch(toggleStatusTambah);
    }

    @FXML
    void onTutupTambah(ActionEvent event) {
        tutupDialog(event);
    }

    @FXML
    void onBatalTambah(ActionEvent event) {
        tutupDialog(event);
    }

    @FXML
    void onPilihFotoTambah(MouseEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Pilih Foto Menu");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Gambar (*.jpg, *.png)", "*.jpg", "*.jpeg", "*.png"));
        Stage stage = (Stage) fotoUploadBoxTambah.getScene().getWindow();
        File file = chooser.showOpenDialog(stage);
        if (file != null) {
            fotoTerpilih = file;
            imgPreviewTambah.setImage(new Image(file.toURI().toString()));
            imgPreviewTambah.setVisible(true);
            imgPreviewTambah.setManaged(true);
            placeholderFotoTambah.setVisible(false);
            placeholderFotoTambah.setManaged(false);
        }
    }

    @FXML
    void onSimpanMenuBaru(ActionEvent event) {
        String nama = txtNamaMenuTambah.getText();
        String kategori = cmbKategoriTambah.getValue();
        String hargaStr = txtHargaTambah.getText();

        if (nama == null || nama.trim().isEmpty()) {
            showAlert("Input Tidak Valid", "Nama menu wajib diisi!", Alert.AlertType.WARNING);
            return;
        }
        if (kategori == null || kategori.trim().isEmpty()) {
            showAlert("Input Tidak Valid", "Kategori wajib dipilih!", Alert.AlertType.WARNING);
            return;
        }
        if (hargaStr == null || hargaStr.trim().isEmpty()) {
            showAlert("Input Tidak Valid", "Harga wajib diisi!", Alert.AlertType.WARNING);
            return;
        }

        double harga = 0;
        try {
            harga = Double.parseDouble(hargaStr.trim());
            if (harga < 0) {
                showAlert("Input Tidak Valid", "Harga tidak boleh negatif!", Alert.AlertType.WARNING);
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Input Tidak Valid", "Harga harus berupa angka valid!", Alert.AlertType.WARNING);
            return;
        }

        // Generate ID
        int maxId = 0;
        for (siptek.kantinemama.model.MenuItem item : siptek.kantinemama.model.AppState.getInstance().getMenuItems()) {
            try {
                int num = Integer.parseInt(item.getId().replaceAll("[^0-9]", ""));
                if (num > maxId) maxId = num;
            } catch (Exception ex) {}
        }
        String newId = String.format("M%03d", maxId + 1);

        // Process image path
        String gambarPath = "images/placeholder.jpg";
        if (fotoTerpilih != null) {
            String slug = slugify(nama);
            saveImage(fotoTerpilih, slug);
            gambarPath = "images/menu/" + slug + ".jpg";
        }

        boolean aktif = toggleStatusTambah.isSelected();

        siptek.kantinemama.model.MenuItem newItem = new siptek.kantinemama.model.MenuItem(
                newId, nama.trim(), kategori, harga, 0, aktif, gambarPath
        );

        siptek.kantinemama.model.AppState.getInstance().getMenuItems().add(newItem);
        siptek.kantinemama.util.PersistenceManager.saveState(siptek.kantinemama.model.AppState.getInstance());

        showAlert("Sukses", "Menu baru \"" + nama + "\" berhasil ditambahkan.\n(Stok default: 0, Status: " + (aktif ? "Aktif" : "Non-Aktif") + ")", Alert.AlertType.INFORMATION);

        tutupDialog(event);
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private String slugify(String name) {
        return name.toLowerCase().replaceAll("[^a-z0-9\\s]", "").replaceAll("\\s+", "-").trim();
    }

    private void saveImage(File srcFile, String slug) {
        String filename = slug + ".jpg";
        File resDir = new File("src/main/resources/siptek/kantinemama/images/menu");
        File targetDir = new File("target/classes/siptek/kantinemama/images/menu");
        try {
            if (!resDir.exists()) {
                resDir.mkdirs();
            }
            if (!targetDir.exists()) {
                targetDir.mkdirs();
            }
            java.nio.file.Files.copy(srcFile.toPath(), new File(resDir, filename).toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            java.nio.file.Files.copy(srcFile.toPath(), new File(targetDir, filename).toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            System.err.println("Failed to copy image: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void tutupDialog(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void setupToggleSwitch(ToggleButton toggle) {
        javafx.scene.shape.Circle circle = new javafx.scene.shape.Circle(8);
        circle.setFill(javafx.scene.paint.Color.WHITE);
        toggle.setGraphic(circle);
        toggle.setContentDisplay(javafx.scene.control.ContentDisplay.GRAPHIC_ONLY);

        toggle.selectedProperty().addListener((obs, oldVal, newVal) -> {
            updateToggleStyle(toggle, newVal);
        });

        updateToggleStyle(toggle, toggle.isSelected());
    }

    private void updateToggleStyle(ToggleButton toggle, boolean isSelected) {
        if (isSelected) {
            toggle.setStyle("-fx-background-color: #1565C0; -fx-background-radius: 13; -fx-border-color: transparent; -fx-cursor: hand;");
            toggle.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);
            toggle.setPadding(new javafx.geometry.Insets(0, 4, 0, 0));
        } else {
            toggle.setStyle("-fx-background-color: #D1D5DB; -fx-background-radius: 13; -fx-border-color: transparent; -fx-cursor: hand;");
            toggle.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
            toggle.setPadding(new javafx.geometry.Insets(0, 0, 0, 4));
        }
    }
}
