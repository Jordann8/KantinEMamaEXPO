package siptek.kantinemama.controller.pegawai;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class EditMenuController {

    @FXML private Button btnTutupEdit;
    @FXML private StackPane fotoUploadBoxEdit;
    @FXML private ImageView imgPreviewEdit;
    @FXML private TextField txtNamaMenuEdit;
    @FXML private ComboBox<String> cmbKategoriEdit;
    @FXML private TextField txtHargaEdit;
    @FXML private TextField txtStokEdit;
    @FXML private TextArea txtDeskripsiEdit;
    @FXML private ToggleButton toggleStatusEdit;
    @FXML private Button btnBatalEdit;
    @FXML private Button btnSimpanEdit;

    private String menuItemIdYangDiedit;
    private File fotoTerpilih;

    @FXML
    public void initialize() {
    }

    public void setMenuItem(siptek.kantinemama.model.MenuItem item) {
        this.menuItemIdYangDiedit = item.getId();
        txtNamaMenuEdit.setText(item.getNama());
        cmbKategoriEdit.setValue(item.getKategori());
        txtHargaEdit.setText(String.valueOf((long)item.getHarga()));
        txtStokEdit.setText(String.valueOf(item.getSisaPorsi()));
        txtDeskripsiEdit.setText("");
        toggleStatusEdit.setSelected(item.isAktif());

        try {
            java.io.InputStream stream = getClass().getResourceAsStream("/siptek/kantinemama/" + item.getGambarPath());
            if (stream != null) {
                imgPreviewEdit.setImage(new Image(stream));
            } else {
                java.io.InputStream phStream = getClass().getResourceAsStream("/siptek/kantinemama/images/placeholder.jpg");
                if (phStream != null) {
                    imgPreviewEdit.setImage(new Image(phStream));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onTutupEdit(ActionEvent event) {
        tutupDialog(event);
    }

    @FXML
    void onBatalEdit(ActionEvent event) {
        tutupDialog(event);
    }

    @FXML
    void onPilihFotoEdit(MouseEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Ubah Foto Menu");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Gambar (*.jpg, *.png)", "*.jpg", "*.jpeg", "*.png"));
        Stage stage = (Stage) fotoUploadBoxEdit.getScene().getWindow();
        File file = chooser.showOpenDialog(stage);
        if (file != null) {
            fotoTerpilih = file;
            imgPreviewEdit.setImage(new Image(file.toURI().toString()));
        }
    }

    @FXML
    void onSimpanEdit(ActionEvent event) {
        String nama = txtNamaMenuEdit.getText();
        String kategori = cmbKategoriEdit.getValue();
        String hargaStr = txtHargaEdit.getText();
        String stokStr = txtStokEdit.getText();

        if (nama == null || nama.trim().isEmpty()) {
            showAlert("Input Tidak Valid", "Nama menu wajib diisi!", javafx.scene.control.Alert.AlertType.WARNING);
            return;
        }
        if (kategori == null || kategori.trim().isEmpty()) {
            showAlert("Input Tidak Valid", "Kategori wajib dipilih!", javafx.scene.control.Alert.AlertType.WARNING);
            return;
        }
        if (hargaStr == null || hargaStr.trim().isEmpty()) {
            showAlert("Input Tidak Valid", "Harga wajib diisi!", javafx.scene.control.Alert.AlertType.WARNING);
            return;
        }
        if (stokStr == null || stokStr.trim().isEmpty()) {
            showAlert("Input Tidak Valid", "Stok wajib diisi!", javafx.scene.control.Alert.AlertType.WARNING);
            return;
        }

        double harga = 0;
        int stok = 0;
        try {
            harga = Double.parseDouble(hargaStr.trim());
            if (harga < 0) {
                showAlert("Input Tidak Valid", "Harga tidak boleh negatif!", javafx.scene.control.Alert.AlertType.WARNING);
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Input Tidak Valid", "Harga harus berupa angka valid!", javafx.scene.control.Alert.AlertType.WARNING);
            return;
        }

        try {
            stok = Integer.parseInt(stokStr.trim());
            if (stok < 0) {
                showAlert("Input Tidak Valid", "Stok tidak boleh negatif!", javafx.scene.control.Alert.AlertType.WARNING);
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Input Tidak Valid", "Stok harus berupa angka bulat valid!", javafx.scene.control.Alert.AlertType.WARNING);
            return;
        }

        siptek.kantinemama.model.MenuItem item = null;
        for (siptek.kantinemama.model.MenuItem m : siptek.kantinemama.model.AppState.getInstance().getMenuItems()) {
            if (m.getId().equals(menuItemIdYangDiedit)) {
                item = m;
                break;
            }
        }

        if (item == null) {
            showAlert("Error", "Menu yang sedang diedit tidak ditemukan di sistem!", javafx.scene.control.Alert.AlertType.ERROR);
            return;
        }

        item.setNama(nama.trim());
        item.setKategori(kategori);
        item.setHarga(harga);
        item.setSisaPorsi(stok);
        item.setAktif(toggleStatusEdit.isSelected());

        if (fotoTerpilih != null) {
            String slug = slugify(nama);
            saveImage(fotoTerpilih, slug);
            item.setGambarPath("images/menu/" + slug + ".jpg");
        }

        siptek.kantinemama.util.PersistenceManager.saveState(siptek.kantinemama.model.AppState.getInstance());

        showAlert("Sukses", "Menu \"" + nama + "\" berhasil diperbarui.", javafx.scene.control.Alert.AlertType.INFORMATION);

        tutupDialog(event);
    }

    private void showAlert(String title, String content, javafx.scene.control.Alert.AlertType type) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(type);
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
