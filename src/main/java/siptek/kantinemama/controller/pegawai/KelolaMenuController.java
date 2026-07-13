package siptek.kantinemama.controller.pegawai;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import siptek.kantinemama.model.AppState;
import siptek.kantinemama.model.MenuItem;
import siptek.kantinemama.util.SceneNavigator;

public class KelolaMenuController {

    @FXML private Button btnTambahMenu;
    @FXML private Button filterCamilan;
    @FXML private Button filterMakanan;
    @FXML private Button filterMinuman;
    @FXML private Button filterSemua;
    
    @FXML private Button navAntrian;
    @FXML private Button navKelolaMenu;
    @FXML private Button navKeluar;
    
    @FXML private FlowPane menuGrid;
    @FXML private Label lblTotalKatalog;
    @FXML private Label lblAktifAplikasi;
    @FXML private Label lblMenuNonAktif;

    private AppState appState = AppState.getInstance();
    private String currentCategory = "Semua";

    @FXML
    public void initialize() {
        renderMenuGrid();
    }

    private void renderMenuGrid() {
        if (menuGrid == null) return;
        menuGrid.getChildren().clear();

        VBox addCard = new VBox();
        addCard.setPrefWidth(220);
        addCard.setSpacing(8);
        addCard.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #D1D5DB; -fx-border-width: 1.5; -fx-border-style: dashed; -fx-border-radius: 10; -fx-background-radius: 10; -fx-padding: 10; -fx-alignment: center; -fx-cursor: hand;");
        addCard.setOnMouseClicked(e -> showTambahMenuPopup());

        Label plusLabel = new Label("＋");
        plusLabel.setStyle("-fx-text-fill: #1565C0; -fx-font-size: 32; -fx-font-weight: bold;");
        Label addText = new Label("Tambah Menu Baru");
        addText.setStyle("-fx-text-fill: #1565C0; -fx-font-size: 14; -fx-font-weight: bold;");

        addCard.getChildren().addAll(plusLabel, addText);
        menuGrid.getChildren().add(addCard);

        for (MenuItem item : appState.getMenuItems()) {
            if (!"Semua".equals(currentCategory) && !item.getKategori().equalsIgnoreCase(currentCategory)) {
                continue;
            }

            VBox card = new VBox();
            card.setPrefWidth(220);
            card.setSpacing(8);
            card.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #E5E7EB; -fx-border-width: 1; -fx-border-radius: 10; -fx-background-radius: 10; -fx-padding: 10;");

            StackPane sp = new StackPane();
            sp.setPrefHeight(130);
            sp.setStyle("-fx-background-color: #E5E7EB; -fx-background-radius: 8;");

            ImageView iv = new ImageView();
            iv.setFitWidth(200);
            iv.setFitHeight(130);
            iv.setPreserveRatio(false);
            loadCardImage(iv, item.getGambarPath());
            sp.getChildren().add(iv);
            card.getChildren().add(sp);

            Label nameLabel = new Label(item.getNama());
            nameLabel.setWrapText(true);
            nameLabel.setStyle("-fx-text-fill: #111827; -fx-font-size: 15; -fx-font-weight: bold;");
            card.getChildren().add(nameLabel);

            Label priceLabel = new Label(siptek.kantinemama.util.CurrencyUtil.formatRupiah(item.getHarga()));
            priceLabel.setStyle("-fx-text-fill: #0F2044; -fx-font-size: 16; -fx-font-weight: bold;");
            card.getChildren().add(priceLabel);

            HBox actionRow = new HBox();
            actionRow.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
            actionRow.setSpacing(10);

            Label statusLabel = new Label();
            updateStatusLabel(statusLabel, item.isAktif());
            actionRow.getChildren().add(statusLabel);

            Region spacer = new Region();
            HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
            actionRow.getChildren().add(spacer);

            Button btnEdit = new Button("Edit");
            btnEdit.setStyle("-fx-background-color: transparent; -fx-text-fill: #4B5563; -fx-font-size: 14; -fx-cursor: hand;");
            btnEdit.setOnAction(e -> showEditMenuPopup(item));
            actionRow.getChildren().add(btnEdit);

            Button btnDelete = new Button("Hapus");
            btnDelete.setStyle("-fx-background-color: transparent; -fx-text-fill: #EF4444; -fx-font-size: 14; -fx-cursor: hand;");
            btnDelete.setOnAction(e -> showHapusMenuPopup(item));
            actionRow.getChildren().add(btnDelete);

            card.getChildren().add(actionRow);
            menuGrid.getChildren().add(card);
        }

        updateStats();
    }

    private void loadCardImage(ImageView iv, String path) {
        try {
            java.io.InputStream stream = getClass().getResourceAsStream("/siptek/kantinemama/" + path);
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

    private void updateStatusLabel(Label statusLabel, boolean active) {
        if (active) {
            statusLabel.setText("●  Aktif");
            statusLabel.setStyle("-fx-text-fill: #1565C0; -fx-font-size: 12; -fx-font-weight: bold;");
        } else {
            statusLabel.setText("○  Non-Aktif");
            statusLabel.setStyle("-fx-text-fill: #9CA3AF; -fx-font-size: 12; -fx-font-weight: bold;");
        }
    }

    private void updateStats() {
        int total = appState.getMenuItems().size();
        int aktif = 0;
        int nonAktif = 0;
        for (MenuItem item : appState.getMenuItems()) {
            if (item.isAktif()) {
                aktif++;
            } else {
                nonAktif++;
            }
        }
        if (lblTotalKatalog != null) lblTotalKatalog.setText(String.valueOf(total));
        if (lblAktifAplikasi != null) lblAktifAplikasi.setText(String.valueOf(aktif));
        if (lblMenuNonAktif != null) lblMenuNonAktif.setText(String.valueOf(nonAktif));
    }

    private void filterCards(String category) {
        currentCategory = category;
        renderMenuGrid();
        
        filterSemua.setStyle("-fx-background-color: " + ("Semua".equals(category) ? "#0F2044; -fx-text-fill: #FFFFFF;" : "#FFFFFF; -fx-text-fill: #374151;") + " -fx-font-size: 12; -fx-background-radius: 6; -fx-cursor: hand;");
        filterMakanan.setStyle("-fx-background-color: " + ("Makanan".equals(category) ? "#0F2044; -fx-text-fill: #FFFFFF;" : "#FFFFFF; -fx-text-fill: #374151;") + " -fx-font-size: 12; -fx-background-radius: 6; -fx-cursor: hand;");
        filterMinuman.setStyle("-fx-background-color: " + ("Minuman".equals(category) ? "#0F2044; -fx-text-fill: #FFFFFF;" : "#FFFFFF; -fx-text-fill: #374151;") + " -fx-font-size: 12; -fx-background-radius: 6; -fx-cursor: hand;");
        filterCamilan.setStyle("-fx-background-color: " + ("Camilan".equals(category) ? "#0F2044; -fx-text-fill: #FFFFFF;" : "#FFFFFF; -fx-text-fill: #374151;") + " -fx-font-size: 12; -fx-background-radius: 6; -fx-cursor: hand;");
    }

    @FXML void onFilterSemua(ActionEvent event) { filterCards("Semua"); }
    @FXML void onFilterMakanan(ActionEvent event) { filterCards("Makanan"); }
    @FXML void onFilterMinuman(ActionEvent event) { filterCards("Minuman"); }
    @FXML void onFilterCamilan(ActionEvent event) { filterCards("Camilan"); }

    @FXML
    void onTambahMenu(ActionEvent event) {
        showTambahMenuPopup();
    }

    @FXML
    void onTambahProdukBaru(ActionEvent event) {
        showTambahMenuPopup();
    }

    private void showTambahMenuPopup() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/siptek/kantinemama/view/pegawai/TambahMenuBaru.fxml"));
            javafx.scene.Parent root = loader.load();
            Stage stage = new Stage();
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.setTitle("Tambah Menu Baru");
            stage.setScene(new javafx.scene.Scene(root));
            stage.showAndWait();
            
            renderMenuGrid();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showEditMenuPopup(MenuItem item) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/siptek/kantinemama/view/pegawai/EditMenu.fxml"));
            javafx.scene.Parent root = loader.load();
            EditMenuController controller = loader.getController();
            controller.setMenuItem(item);

            Stage stage = new Stage();
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.setTitle("Edit Menu");
            stage.setScene(new javafx.scene.Scene(root));
            stage.showAndWait();
            
            renderMenuGrid();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showHapusMenuPopup(MenuItem item) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/siptek/kantinemama/view/pegawai/HapusMenu.fxml"));
            javafx.scene.Parent root = loader.load();
            HapusMenuController controller = loader.getController();
            controller.setNamaMenu(item.getId(), item.getNama());

            Stage stage = new Stage();
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.setTitle("Hapus Menu");
            stage.setScene(new javafx.scene.Scene(root));
            stage.showAndWait();
            
            renderMenuGrid();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML void onNavKelolaMenu(ActionEvent event) { /* Already here */ }
    @FXML void onNavAntrian(ActionEvent event) { SceneNavigator.loadScene(event, "/siptek/kantinemama/view/pegawai/AntrianPesanan.fxml"); }
    @FXML void onNavKeluar(ActionEvent event) { SceneNavigator.loadScene(event, "/siptek/kantinemama/view/pelanggan/HalamanAwalUtama.fxml"); }
}
