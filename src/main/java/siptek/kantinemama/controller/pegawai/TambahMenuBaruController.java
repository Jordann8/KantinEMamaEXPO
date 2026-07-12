package siptek.kantinemama.controller.pegawai;

import java.io.File;

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
}
