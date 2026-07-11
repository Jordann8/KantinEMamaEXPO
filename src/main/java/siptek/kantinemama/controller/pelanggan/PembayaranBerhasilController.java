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
import siptek.kantinemama.util.SceneNavigator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PembayaranBerhasilController {

    @FXML
    private Button btnPilihMeja;

    private AppState appState = AppState.getInstance();

    @FXML
    public void initialize() {
        try {
            updateReceiptUI();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateReceiptUI() {
        VBox outerVBox = (VBox) btnPilihMeja.getParent();
        VBox receiptBox = (VBox) outerVBox.getChildren().get(4);

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
        HBox totalHBox = (HBox) receiptBox.getChildren().get(10);
        Label totalVal = (Label) totalHBox.getChildren().get(2);
        totalVal.setText(siptek.kantinemama.util.CurrencyUtil.formatRupiah(total));

        // Method label
        Label methodLabel = (Label) receiptBox.getChildren().get(13);
        methodLabel.setText("PAID VIA " + appState.getSelectedMetodeBayar() + " (SUCCESS)");
    }

    @FXML
    void onPilihMeja(ActionEvent event) {
        // REVISI V2: tombol ini sekarang "Lihat Pesanan Saya" (meja sudah
        // dipilih di awal alur, bukan di titik ini lagi). Pesanan sudah
        // dibuat & disimpan di PembayaranQRISController.onCekStatus;
        // di sini tinggal bersihkan state keranjang/order aktif sebelum
        // pindah ke Pesanan Saya.
        appState.clearCart();
        appState.setCurrentOrderId(null);
        appState.setSelectedMeja(null);
        SceneNavigator.loadScene(event, "/siptek/kantinemama/view/pelanggan/PesananSaya.fxml");
    }
}
