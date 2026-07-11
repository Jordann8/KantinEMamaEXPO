package siptek.kantinemama.controller.pelanggan;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import siptek.kantinemama.model.AppState;
import siptek.kantinemama.model.CartItem;
import siptek.kantinemama.model.Pesanan;
import siptek.kantinemama.util.SceneNavigator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;

public class PembayaranTunaiController {

    @FXML
    private Button btnKembaliKatalog;

    @FXML
    private Button btnBatal;

    @FXML
    private Label lblTotal;

    private AppState appState = AppState.getInstance();

    @FXML
    public void initialize() {
        if (appState.getCurrentOrderId() == null) {
            int randomNum = 10000 + new Random().nextInt(90000);
            appState.setCurrentOrderId("ORD-" + randomNum);
        }

        try {
            updateReceiptUI();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateReceiptUI() {
        HBox parentHBox = (HBox) lblTotal.getParent().getParent().getParent();
        
        // Update left side description
        VBox leftBox = (VBox) parentHBox.getChildren().get(0);
        Label descLabel = (Label) leftBox.getChildren().get(1);
        descLabel.setText("Silakan tunjukkan struk ini atau sebutkan Order ID #" + appState.getCurrentOrderId() + " kepada Kasir untuk melakukan pembayaran tunai.");

        // Update receipt details
        VBox receiptBox = (VBox) parentHBox.getChildren().get(1);
        Label transIdLabel = (Label) receiptBox.getChildren().get(1);
        transIdLabel.setText("TRANS ID: #" + appState.getCurrentOrderId());

        Label dateLabel = (Label) receiptBox.getChildren().get(2);
        dateLabel.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy - HH:mm")));

        ObservableList<CartItem> cart = appState.getCurrentCart();

        // Row 1
        HBox row1 = (HBox) receiptBox.getChildren().get(5);
        if (cart.size() > 0) {
            row1.setVisible(true);
            row1.setManaged(true);
            CartItem item = cart.get(0);
            Label nameLabel = (Label) row1.getChildren().get(0);
            Label priceLabel = (Label) row1.getChildren().get(2);
            nameLabel.setText(item.getQty() + "x " + item.getMenuItem().getNama());
            priceLabel.setText(siptek.kantinemama.util.CurrencyUtil.formatRaw(item.getSubtotal()));
        } else {
            row1.setVisible(false);
            row1.setManaged(false);
        }

        // Row 2
        HBox row2 = (HBox) receiptBox.getChildren().get(6);
        if (cart.size() > 1) {
            row2.setVisible(true);
            row2.setManaged(true);
            CartItem item = cart.get(1);
            Label nameLabel = (Label) row2.getChildren().get(0);
            Label priceLabel = (Label) row2.getChildren().get(2);
            nameLabel.setText(item.getQty() + "x " + item.getMenuItem().getNama());
            priceLabel.setText(siptek.kantinemama.util.CurrencyUtil.formatRaw(item.getSubtotal()));
        } else {
            row2.setVisible(false);
            row2.setManaged(false);
        }

        double subtotal = 0;
        for (CartItem item : cart) {
            subtotal += item.getSubtotal();
        }
        double tax = subtotal * 0.10;
        double total = subtotal + tax;

        // Subtotal
        HBox subtotalHBox = (HBox) receiptBox.getChildren().get(8);
        Label subtotalVal = (Label) subtotalHBox.getChildren().get(2);
        subtotalVal.setText(siptek.kantinemama.util.CurrencyUtil.formatRaw(subtotal));

        // Tax
        HBox taxHBox = (HBox) receiptBox.getChildren().get(9);
        Label taxVal = (Label) taxHBox.getChildren().get(2);
        taxVal.setText(siptek.kantinemama.util.CurrencyUtil.formatRaw(tax));

        // Total
        lblTotal.setText(siptek.kantinemama.util.CurrencyUtil.formatRaw(total));
    }

    @FXML
    void onBatal(ActionEvent event) {
        // Jika batal, order id yang digenerate di reset supaya bisa buat metode bayar lain
        appState.setCurrentOrderId(null);
        SceneNavigator.loadScene(event, "/siptek/kantinemama/view/pelanggan/KonfirmasiPesanan.fxml");
    }

    @FXML
    void onKembaliKatalog(ActionEvent event) {
        // REVISI V2: meja sekarang sudah dipilih SEBELUM sampai ke halaman ini
        // (lewat PilihMeja -> KonfirmasiPesanan -> sini), jadi tidak lagi
        // hardcode "-". Status pembayaran tetap "Belum Bayar" karena tunai
        // baru divalidasi kasir saat pesanan diantar (lihat AntrianPesananController).
        ArrayList<CartItem> orderedItems = new ArrayList<>(appState.getCurrentCart());
        Pesanan pesanan = new Pesanan(
                appState.getCurrentOrderId(),
                orderedItems,
                "Belum Bayar",
                "Tunai",
                appState.getSelectedMeja(),
                "Pesanan Baru",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
        );
        appState.getOrders().add(0, pesanan); // di awal list, supaya muncul paling atas di riwayat

        appState.clearCart();
        appState.setCurrentOrderId(null);
        appState.setSelectedMeja(null);
        SceneNavigator.loadScene(event, "/siptek/kantinemama/view/pelanggan/KatalogMenu.fxml");
    }
}
