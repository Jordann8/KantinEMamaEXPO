package siptek.kantinemama.controller.pelanggan;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import siptek.kantinemama.model.AppState;
import siptek.kantinemama.model.CartItem;
import siptek.kantinemama.util.SceneNavigator;

public class KonfirmasiPesananController {

    @FXML
    private VBox cardQRIS;

    @FXML
    private VBox cardTunai;

    private AppState appState = AppState.getInstance();

    @FXML
    public void initialize() {
        // We will update the summary box dynamically.
        // Wait, wait! The nodes might not be laid out or accessible immediately in initialize()
        // but since FXML properties are populated, getParent() works. Let's do it safely.
        
        // Let's use Platform.runLater or just run it directly. Running directly is fine in initialize().
        try {
            updateSummaryUI();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateSummaryUI() {
        // Retrieve the parent elements
        HBox parentHBox = (HBox) cardQRIS.getParent().getParent().getParent();
        VBox summaryBox = (VBox) parentHBox.getChildren().get(0);

        ObservableList<CartItem> cart = appState.getCurrentCart();
        
        // Get badge label
        HBox headerHBox = (HBox) summaryBox.getChildren().get(0);
        Label badgeLabel = (Label) headerHBox.getChildren().get(2);
        
        int totalQty = 0;
        double subtotal = 0;
        for (CartItem item : cart) {
            totalQty += item.getQty();
            subtotal += item.getSubtotal();
        }
        badgeLabel.setText(totalQty + " ITEMS");

        // Row 1
        HBox row1 = (HBox) summaryBox.getChildren().get(2);
        if (cart.size() > 0) {
            row1.setVisible(true);
            row1.setManaged(true);
            CartItem item = cart.get(0);
            VBox textCol = (VBox) row1.getChildren().get(0);
            Label nameLabel = (Label) textCol.getChildren().get(0);
            Label qtyLabel = (Label) textCol.getChildren().get(1);
            Label priceLabel = (Label) row1.getChildren().get(2);

            nameLabel.setText(item.getMenuItem().getNama());
            qtyLabel.setText(item.getQty() + "x");
            priceLabel.setText(siptek.kantinemama.util.CurrencyUtil.formatRupiah(item.getSubtotal()));
        } else {
            row1.setVisible(false);
            row1.setManaged(false);
        }

        // Row 2
        HBox row2 = (HBox) summaryBox.getChildren().get(3);
        if (cart.size() > 1) {
            row2.setVisible(true);
            row2.setManaged(true);
            CartItem item = cart.get(1);
            VBox textCol = (VBox) row2.getChildren().get(0);
            Label nameLabel = (Label) textCol.getChildren().get(0);
            Label qtyLabel = (Label) textCol.getChildren().get(1);
            Label priceLabel = (Label) row2.getChildren().get(2);

            nameLabel.setText(item.getMenuItem().getNama());
            qtyLabel.setText(item.getQty() + "x");
            priceLabel.setText(siptek.kantinemama.util.CurrencyUtil.formatRupiah(item.getSubtotal()));
        } else {
            row2.setVisible(false);
            row2.setManaged(false);
        }

        double tax = subtotal * 0.10;
        double total = subtotal + tax;

        // Subtotal
        HBox subtotalHBox = (HBox) summaryBox.getChildren().get(5);
        Label subtotalVal = (Label) subtotalHBox.getChildren().get(2);
        subtotalVal.setText(siptek.kantinemama.util.CurrencyUtil.formatRupiah(subtotal));

        // Tax
        HBox taxHBox = (HBox) summaryBox.getChildren().get(6);
        Label taxVal = (Label) taxHBox.getChildren().get(2);
        taxVal.setText(siptek.kantinemama.util.CurrencyUtil.formatRupiah(tax));

        // Total
        HBox totalHBox = (HBox) summaryBox.getChildren().get(7);
        Label totalVal = (Label) totalHBox.getChildren().get(2);
        totalVal.setText(siptek.kantinemama.util.CurrencyUtil.formatRupiah(total));
        
        appState.setCurrentTotal(total);
    }

    @FXML
    void onPilihQRIS(MouseEvent event) {
        appState.setSelectedMetodeBayar("QRIS");
        SceneNavigator.loadScene(event, "/siptek/kantinemama/view/pelanggan/PembayaranQRIS.fxml");
    }

    @FXML
    void onPilihTunai(MouseEvent event) {
        appState.setSelectedMetodeBayar("Tunai");
        SceneNavigator.loadScene(event, "/siptek/kantinemama/view/pelanggan/PembayaranTunai.fxml");
    }
}
