package siptek.kantinemama.controller.pelanggan;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;

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

public class PembayaranTunaiController {

    @FXML
    private Button btnKembaliKatalog;

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
        
        VBox leftBox = (VBox) parentHBox.getChildren().get(0);
        Label descLabel = (Label) leftBox.getChildren().get(1);
        descLabel.setText("Silakan tunjukkan struk ini atau sebutkan Order ID #" + appState.getCurrentOrderId() + " kepada Kasir untuk melakukan pembayaran tunai.");

        VBox receiptBox = (VBox) parentHBox.getChildren().get(1);
        Label transIdLabel = (Label) receiptBox.getChildren().get(1);
        transIdLabel.setText("TRANS ID: #" + appState.getCurrentOrderId());

        Label dateLabel = (Label) receiptBox.getChildren().get(2);
        dateLabel.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy - HH:mm")));

        ObservableList<CartItem> cart = appState.getCurrentCart();

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
    void onKembaliKatalog(ActionEvent event) {
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
        appState.getOrders().add(0, pesanan); 

        appState.clearCart();
        appState.setCurrentOrderId(null);
        appState.setSelectedMeja(null);
        SceneNavigator.loadScene(event, "/siptek/kantinemama/view/pelanggan/HalamanKatalogMenu.fxml");
    }
}
