package siptek.kantinemama.controller.owner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.scene.image.ImageView;
import siptek.kantinemama.model.AppState;
import siptek.kantinemama.model.MenuItem;
import siptek.kantinemama.util.SceneNavigator;

import java.util.HashMap;
import java.util.Map;

public class ManajemenKatalogMenuController {

    @FXML private Button btnEditAyamGeprek;
    @FXML private Button btnEditEsTeh;
    @FXML private Button btnEditKopiSusu;
    @FXML private Button btnEditNasiGoreng;
    @FXML private Button btnEditPisangGoreng;
    @FXML private Button btnEditSateMaranggi;
    @FXML private Button btnHapusAyamGeprek;
    @FXML private Button btnHapusEsTeh;
    @FXML private Button btnHapusKopiSusu;
    @FXML private Button btnHapusNasiGoreng;
    @FXML private Button btnHapusPisangGoreng;
    @FXML private Button btnHapusSateMaranggi;
    
    @FXML private Button btnNextPage;
    @FXML private Button btnPage1;
    @FXML private Button btnPage2;
    @FXML private Button btnPage3;
    @FXML private Button btnPrevPage;
    @FXML private Button btnTambahMenu;
    
    @FXML private VBox cardAyamGeprek;
    @FXML private VBox cardEsTeh;
    @FXML private VBox cardKopiSusu;
    @FXML private VBox cardNasiGoreng;
    @FXML private VBox cardPisangGoreng;
    @FXML private VBox cardSateMaranggi;
    @FXML private VBox cardTambahProduk;
    
    @FXML private Button filterCamilan;
    @FXML private Button filterMakanan;
    @FXML private Button filterMinuman;
    @FXML private Button filterSemua;
    
    @FXML private Button navDasbor;
    @FXML private Button navKelolaMenu;
    @FXML private Button navKeluar;
    @FXML private Button navLaporan;
    @FXML private Button navPengaturan;
    @FXML private Button navStok;
    @FXML private Label statusAyamGeprek;

    private AppState appState = AppState.getInstance();
    private Map<String, VBox> cardMap = new HashMap<>();
    private Map<String, String> itemIds = new HashMap<>();

    @FXML
    public void initialize() {
        cardMap.put("M007", cardAyamGeprek);
        cardMap.put("M008", cardNasiGoreng);
        cardMap.put("M009", cardEsTeh);
        cardMap.put("M010", cardSateMaranggi);
        cardMap.put("M011", cardKopiSusu);
        cardMap.put("M012", cardPisangGoreng);

        itemIds.put("AyamGeprek", "M007");
        itemIds.put("NasiGoreng", "M008");
        itemIds.put("EsTeh", "M009");
        itemIds.put("SateMaranggi", "M010");
        itemIds.put("KopiSusu", "M011");
        itemIds.put("PisangGoreng", "M012");

        refreshMenuStatusUI();
        loadCardImages();
    }

    private void loadCardImages() {
        for (Map.Entry<String, VBox> entry : cardMap.entrySet()) {
            String itemId = entry.getKey();
            VBox card = entry.getValue();
            if (card == null) continue;

            MenuItem item = findItem(itemId);
            if (item != null) {
                try {
                    StackPane sp = (StackPane) card.getChildren().get(0);
                    ImageView iv = (ImageView) sp.getChildren().get(0);
                    
                    java.io.InputStream stream = getClass().getResourceAsStream("/siptek/kantinemama/" + item.getGambarPath());
                    if (stream != null) {
                        iv.setImage(new javafx.scene.image.Image(stream));
                    } else {
                        java.io.InputStream ph = getClass().getResourceAsStream("/siptek/kantinemama/images/placeholder.jpg");
                        if (ph != null) {
                            iv.setImage(new javafx.scene.image.Image(ph));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void refreshMenuStatusUI() {
        for (Map.Entry<String, VBox> entry : cardMap.entrySet()) {
            String itemId = entry.getKey();
            VBox card = entry.getValue();
            if (card == null) continue;

            MenuItem item = findItem(itemId);
            if (item != null) {
                try {
                    HBox actionRow = (HBox) card.getChildren().get(4);
                    Label statusLabel = (Label) actionRow.getChildren().get(0);
                    updateStatusLabel(statusLabel, item.isAktif());
                } catch (Exception e) {
                    if (statusAyamGeprek != null && "M007".equals(itemId)) {
                        updateStatusLabel(statusAyamGeprek, item.isAktif());
                    }
                }
            }
        }
    }

    private void updateStatusLabel(Label statusLabel, boolean active) {
        if (active) {
            statusLabel.setText("●  Aktif");
            statusLabel.setStyle("-fx-text-fill: #1565C0; -fx-font-size: 12; -fx-font-weight: bold;");
        } else {
            statusLabel.setText("○  Non-Aktif");
            statusLabel.setStyle("-fx-text-fill: #9CA3AF; -fx-font-size: 12; -fx-font-weight: bold;");
        }
    }

    private MenuItem findItem(String id) {
        for (MenuItem item : appState.getMenuItems()) {
            if (item.getId().equals(id)) {
                return item;
            }
        }
        return null;
    }

    private void toggleItemStatus(String alias, Button editBtn) {
        String id = itemIds.get(alias);
        MenuItem item = findItem(id);
        if (item != null) {
            item.setAktif(!item.isAktif());
            Label statusLabel = (Label) ((HBox) editBtn.getParent()).getChildren().get(0);
            updateStatusLabel(statusLabel, item.isAktif());
            
            String statusStr = item.isAktif() ? "Diaktifkan" : "Dinonaktifkan";
            showInfo("Status Diubah", "Menu '" + item.getNama() + "' berhasil " + statusStr + ".");
        }
    }

    private void deleteItem(String alias, VBox card) {
        String id = itemIds.get(alias);
        MenuItem item = findItem(id);
        if (item != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Hapus Menu");
            alert.setHeaderText("Apakah Anda yakin ingin menghapus menu ini?");
            alert.setContentText(item.getNama());
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    card.setVisible(false);
                    card.setManaged(false);
                    item.setAktif(false);
                    showInfo("Menu Dihapus", "Menu '" + item.getNama() + "' berhasil dihapus.");
                }
            });
        }
    }

    private void filterCards(String category) {
        for (Map.Entry<String, VBox> entry : cardMap.entrySet()) {
            String itemId = entry.getKey();
            VBox card = entry.getValue();
            if (card == null) continue;

            MenuItem item = findItem(itemId);
            if (item != null) {
                if ("Semua".equals(category) || item.getKategori().equalsIgnoreCase(category)) {
                    card.setVisible(true);
                    card.setManaged(true);
                } else {
                    card.setVisible(false);
                    card.setManaged(false);
                }
            }
        }
        
        filterSemua.setStyle("-fx-background-color: " + ("Semua".equals(category) ? "#0F2044; -fx-text-fill: #FFFFFF;" : "#FFFFFF; -fx-text-fill: #374151;") + " -fx-font-size: 12; -fx-background-radius: 6; -fx-cursor: hand;");
        filterMakanan.setStyle("-fx-background-color: " + ("Makanan".equals(category) ? "#0F2044; -fx-text-fill: #FFFFFF;" : "#FFFFFF; -fx-text-fill: #374151;") + " -fx-font-size: 12; -fx-background-radius: 6; -fx-cursor: hand;");
        filterMinuman.setStyle("-fx-background-color: " + ("Minuman".equals(category) ? "#0F2044; -fx-text-fill: #FFFFFF;" : "#FFFFFF; -fx-text-fill: #374151;") + " -fx-font-size: 12; -fx-background-radius: 6; -fx-cursor: hand;");
        filterCamilan.setStyle("-fx-background-color: " + ("Camilan".equals(category) ? "#0F2044; -fx-text-fill: #FFFFFF;" : "#FFFFFF; -fx-text-fill: #374151;") + " -fx-font-size: 12; -fx-background-radius: 6; -fx-cursor: hand;");
    }

    @FXML void onEditAyamGeprek(ActionEvent event) { toggleItemStatus("AyamGeprek", btnEditAyamGeprek); }
    @FXML void onEditEsTeh(ActionEvent event) { toggleItemStatus("EsTeh", btnEditEsTeh); }
    @FXML void onEditKopiSusu(ActionEvent event) { toggleItemStatus("KopiSusu", btnEditKopiSusu); }
    @FXML void onEditNasiGoreng(ActionEvent event) { toggleItemStatus("NasiGoreng", btnEditNasiGoreng); }
    @FXML void onEditPisangGoreng(ActionEvent event) { toggleItemStatus("PisangGoreng", btnEditPisangGoreng); }
    @FXML void onEditSateMaranggi(ActionEvent event) { toggleItemStatus("SateMaranggi", btnEditSateMaranggi); }

    @FXML void onHapusAyamGeprek(ActionEvent event) { deleteItem("AyamGeprek", cardAyamGeprek); }
    @FXML void onHapusEsTeh(ActionEvent event) { deleteItem("EsTeh", cardEsTeh); }
    @FXML void onHapusKopiSusu(ActionEvent event) { deleteItem("KopiSusu", cardKopiSusu); }
    @FXML void onHapusNasiGoreng(ActionEvent event) { deleteItem("NasiGoreng", cardNasiGoreng); }
    @FXML void onHapusPisangGoreng(ActionEvent event) { deleteItem("PisangGoreng", cardPisangGoreng); }
    @FXML void onHapusSateMaranggi(ActionEvent event) { deleteItem("SateMaranggi", cardSateMaranggi); }

    @FXML void onFilterSemua(ActionEvent event) { filterCards("Semua"); }
    @FXML void onFilterMakanan(ActionEvent event) { filterCards("Makanan"); }
    @FXML void onFilterMinuman(ActionEvent event) { filterCards("Minuman"); }
    @FXML void onFilterCamilan(ActionEvent event) { filterCards("Camilan"); }

    @FXML
    void onTambahMenu(ActionEvent event) {
        showInfo("Tambah Menu", "Fitur menambah menu baru untuk Owner.");
    }

    @FXML
    void onTambahProdukBaru(ActionEvent event) {
        showInfo("Tambah Menu", "Membuka form penambahan produk baru ke dalam katalog oleh Owner.");
    }

    @FXML void onNavDasbor(ActionEvent event) { SceneNavigator.loadScene(event, "/siptek/kantinemama/view/owner/PortalOwner.fxml"); }
    @FXML void onNavKelolaMenu(ActionEvent event) { /* Already here */ }
    @FXML void onNavStok(ActionEvent event) { SceneNavigator.loadScene(event, "/siptek/kantinemama/view/pegawai/StokBarang.fxml"); }
    @FXML void onNavLaporan(ActionEvent event) { SceneNavigator.loadScene(event, "/siptek/kantinemama/view/owner/LaporanKeuangan1.fxml"); }
    @FXML void onNavPengaturan(ActionEvent event) { SceneNavigator.loadScene(event, "/siptek/kantinemama/view/owner/PengaturanSistem.fxml"); }
    @FXML void onNavKeluar(ActionEvent event) { SceneNavigator.loadScene(event, "/siptek/kantinemama/view/pelanggan/HalamanAwalUtama.fxml"); }

    @FXML void onNextPage(ActionEvent event) {}
    @FXML void onPage1(ActionEvent event) {}
    @FXML void onPage2(ActionEvent event) {}
    @FXML void onPage3(ActionEvent event) {}
    @FXML void onPrevPage(ActionEvent event) {}

    private void showInfo(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
