package siptek.kantinemama.controller.pelanggan;

import java.io.FileWriter;
import java.io.IOException;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import siptek.kantinemama.model.AppState;
import siptek.kantinemama.model.Pesanan;
import siptek.kantinemama.util.SceneNavigator;

public class PesananSayaController {

    @FXML private Button btnExportPdf;
    @FXML private Button btnFilter;
    @FXML private Button btnHubungiKantin;

    @FXML private TableColumn<Pesanan, String> colIdPesanan;
    @FXML private TableColumn<Pesanan, String> colItemMakanan;
    @FXML private TableColumn<Pesanan, String> colStatusPesanan;
    @FXML private TableColumn<Pesanan, String> colStatusPembayaran;
    @FXML private TableColumn<Pesanan, String> colTanggal;
    @FXML private TableColumn<Pesanan, Double> colTotalPengeluaran;

    @FXML private Button navCart;
    @FXML private Button navHelp;
    @FXML private Button navMenu;
    @FXML private Button navOrders;
    @FXML private TableView<Pesanan> tblRiwayat;
    @FXML private Label lblFooterInfo;

    private AppState appState = AppState.getInstance();

    @FXML
    public void initialize() {
        colTanggal.setCellValueFactory(new PropertyValueFactory<>("waktu"));
        colIdPesanan.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colItemMakanan.setCellValueFactory(new PropertyValueFactory<>("formattedItems"));

        colTotalPengeluaran.setCellValueFactory(new PropertyValueFactory<>("totalPembayaran"));
        colTotalPengeluaran.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : siptek.kantinemama.util.CurrencyUtil.formatRupiah(item));
            }
        });

        colStatusPesanan.setCellValueFactory(new PropertyValueFactory<>("statusDapur"));
        colStatusPesanan.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String statusDapur, boolean empty) {
                super.updateItem(statusDapur, empty);
                if (empty || statusDapur == null) {
                    setText(null);
                    setStyle("");
                } else if ("Selesai".equalsIgnoreCase(statusDapur)) {
                    setText("Selesai");
                    setStyle("-fx-text-fill: #16A34A; -fx-font-weight: bold;");
                } else {
                    setText("Sedang Diproses");
                    setStyle("-fx-text-fill: #1565C0; -fx-font-weight: bold;");
                }
            }
        });

        colStatusPembayaran.setCellValueFactory(new PropertyValueFactory<>("statusPembayaran"));
        colStatusPembayaran.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    if ("Lunas".equalsIgnoreCase(item)) {
                        setStyle("-fx-text-fill: #16A34A; -fx-font-weight: bold;");
                    } else if ("Belum Bayar".equalsIgnoreCase(item)) {
                        setStyle("-fx-text-fill: #EF4444; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-text-fill: #374151;");
                    }
                }
            }
        });

        tblRiwayat.setItems(appState.getOrders());
        if (lblFooterInfo != null) {
            lblFooterInfo.setText("Menampilkan " + appState.getOrders().size() + " dari " + appState.getOrders().size() + " item");
        }

        updateActiveOrderUI();
    }

    private void updateActiveOrderUI() {
        try {
            VBox activeOrderBox = (VBox) btnHubungiKantin.getParent().getParent();

            Pesanan activeOrder = null;
            ObservableList<Pesanan> orders = appState.getOrders();
            for (int i = 0; i < orders.size(); i++) {
                Pesanan p = orders.get(i);
                if (!"Selesai".equalsIgnoreCase(p.getStatusDapur())) {
                    activeOrder = p;
                    break;
                }
            }

            if (activeOrder == null) {
                activeOrderBox.setVisible(false);
                activeOrderBox.setManaged(false);
                return;
            }

            activeOrderBox.setVisible(true);
            activeOrderBox.setManaged(true);

            HBox header = (HBox) activeOrderBox.getChildren().get(0);
            VBox orderIdCol = (VBox) header.getChildren().get(0);
            HBox badgeBox = (HBox) orderIdCol.getChildren().get(0);
            Label statusBadge = (Label) badgeBox.getChildren().get(0);
            Label orderIdLabel = (Label) orderIdCol.getChildren().get(1);

            VBox totalCol = (VBox) header.getChildren().get(2);
            Label totalLabel = (Label) totalCol.getChildren().get(1);

            VBox tableCol = (VBox) header.getChildren().get(4);
            Label tableLabel = (Label) tableCol.getChildren().get(1);

            statusBadge.setText(activeOrder.getStatusDapur().toUpperCase());
            orderIdLabel.setText("Order ID #" + activeOrder.getOrderId());
            totalLabel.setText(siptek.kantinemama.util.CurrencyUtil.formatRupiah(activeOrder.getTotalPembayaran()));

            String mejaLabelText = activeOrder.getMeja().replace("mejaMO", "MEJA ");
            if ("-".equals(mejaLabelText)) mejaLabelText = "Kasir (Tunai)";
            tableLabel.setText(mejaLabelText);

            HBox progressRow = (HBox) activeOrderBox.getChildren().get(1);
            StackPane step1 = (StackPane) progressRow.getChildren().get(0);
            Region line1 = (Region) progressRow.getChildren().get(1);
            StackPane step2 = (StackPane) progressRow.getChildren().get(2);
            Region line2 = (Region) progressRow.getChildren().get(3);
            StackPane step3 = (StackPane) progressRow.getChildren().get(4);

            HBox labelsRow = (HBox) activeOrderBox.getChildren().get(2);
            Label lblStep1 = (Label) labelsRow.getChildren().get(0);
            Label lblStep2 = (Label) labelsRow.getChildren().get(2);
            Label lblStep3 = (Label) labelsRow.getChildren().get(4);

            String inactiveCircle = "-fx-min-width: 34; -fx-min-height: 34; -fx-max-width: 34; -fx-max-height: 34; -fx-background-color: #E5E7EB; -fx-background-radius: 17;";
            String activeCircle = "-fx-min-width: 34; -fx-min-height: 34; -fx-max-width: 34; -fx-max-height: 34; -fx-background-color: #1565C0; -fx-background-radius: 17;";
            step1.setStyle(inactiveCircle);
            step2.setStyle(inactiveCircle);
            step3.setStyle(inactiveCircle);
            line1.setStyle("-fx-background-color: #E5E7EB;");
            line2.setStyle("-fx-background-color: #E5E7EB;");
            lblStep1.setStyle("-fx-text-fill: #9CA3AF; -fx-font-size: 11;");
            lblStep2.setStyle("-fx-text-fill: #9CA3AF; -fx-font-size: 11;");
            lblStep3.setStyle("-fx-text-fill: #9CA3AF; -fx-font-size: 11;");

            String statusDapur = activeOrder.getStatusDapur();
            if ("Pesanan Baru".equalsIgnoreCase(statusDapur)) {
                step1.setStyle(activeCircle);
                lblStep1.setStyle("-fx-text-fill: #1565C0; -fx-font-size: 11; -fx-font-weight: bold;");
            } else if ("Sedang Dimasak".equalsIgnoreCase(statusDapur)) {
                step1.setStyle(activeCircle);
                line1.setStyle("-fx-background-color: #1565C0;");
                step2.setStyle(activeCircle);
                lblStep1.setStyle("-fx-text-fill: #1565C0; -fx-font-size: 11;");
                lblStep2.setStyle("-fx-text-fill: #1565C0; -fx-font-size: 11; -fx-font-weight: bold;");
            } else if ("Siap Diambil".equalsIgnoreCase(statusDapur) || "Selesai".equalsIgnoreCase(statusDapur)) {
                step1.setStyle(activeCircle);
                line1.setStyle("-fx-background-color: #1565C0;");
                step2.setStyle(activeCircle);
                line2.setStyle("-fx-background-color: #1565C0;");
                step3.setStyle(activeCircle);
                lblStep1.setStyle("-fx-text-fill: #1565C0; -fx-font-size: 11;");
                lblStep2.setStyle("-fx-text-fill: #1565C0; -fx-font-size: 11;");
                lblStep3.setStyle("-fx-text-fill: #1565C0; -fx-font-size: 11; -fx-font-weight: bold;");
            }

            HBox detailsRow = (HBox) activeOrderBox.getChildren().get(4);
            HBox estimasiCol = (HBox) detailsRow.getChildren().get(0);
            VBox estimasiTexts = (VBox) estimasiCol.getChildren().get(1);
            Label estimasiLabel = (Label) estimasiTexts.getChildren().get(1);

            HBox menuCol = (HBox) detailsRow.getChildren().get(1);
            VBox menuTexts = (VBox) menuCol.getChildren().get(1);
            Label menuLabel = (Label) menuTexts.getChildren().get(1);

            menuLabel.setText(activeOrder.getFormattedItems());
            if ("Pesanan Baru".equalsIgnoreCase(statusDapur)) {
                estimasiLabel.setText("15 - 20 Menit Tersisa");
            } else if ("Sedang Dimasak".equalsIgnoreCase(statusDapur)) {
                estimasiLabel.setText("8 - 12 Menit Tersisa");
            } else {
                estimasiLabel.setText("Pesanan Selesai!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onExportPdf(ActionEvent event) {
        try (FileWriter writer = new FileWriter("riwayat_pesanan.txt")) {
            writer.write("==================================================\n");
            writer.write("            RIWAYAT PESANAN KANTIN E MAMA         \n");
            writer.write("==================================================\n\n");
            for (Pesanan p : appState.getOrders()) {
                writer.write(String.format("Tanggal: %s\nID Pesanan: %s\nItem: %s\nTotal: %s\nStatus Pesanan: %s\nStatus Pembayaran: %s\n",
                        p.getWaktu(), p.getOrderId(), p.getFormattedItems(), 
                        siptek.kantinemama.util.CurrencyUtil.formatRupiah(p.getTotalPembayaran()),
                        "Selesai".equalsIgnoreCase(p.getStatusDapur()) ? "Selesai" : "Sedang Diproses",
                        p.getStatusPembayaran()));
                writer.write("--------------------------------------------------\n");
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Ekspor Berhasil");
            alert.setHeaderText(null);
            alert.setContentText("Riwayat pesanan berhasil diekspor ke file 'riwayat_pesanan.txt'!");
            alert.showAndWait();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ekspor Gagal");
            alert.setHeaderText(null);
            alert.setContentText("Gagal menyimpan file riwayat pesanan: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void onFilter(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Filter Pesanan");
        alert.setHeaderText(null);
        alert.setContentText("Fitur filter riwayat pesanan akan menyaring berdasarkan tanggal atau status. Saat ini semua data ditampilkan.");
        alert.showAndWait();
    }

    @FXML
    void onHubungiKantin(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Hubungi Kantin");
        alert.setHeaderText("Hubungi Staf Kantin E Mama");
        alert.setContentText("Silakan hubungi WhatsApp kami di +62-812-3456-7890 jika ada kendala pada pesanan Anda.");
        alert.showAndWait();
    }

    @FXML
    void onNavCart(ActionEvent event) {
        SceneNavigator.loadScene(event, "/siptek/kantinemama/view/pelanggan/HalamanKatalogMenu.fxml");
    }

    @FXML
    void onNavHelp(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Bantuan");
        alert.setHeaderText(null);
        alert.setContentText("Halaman ini menampilkan status pesanan aktif Anda dan riwayat pesanan lampau.");
        alert.showAndWait();
    }

    @FXML
    void onNavMenu(ActionEvent event) {
        SceneNavigator.loadScene(event, "/siptek/kantinemama/view/pelanggan/HalamanKatalogMenu.fxml");
    }

    @FXML
    void onNavOrders(ActionEvent event) {
    }
}
