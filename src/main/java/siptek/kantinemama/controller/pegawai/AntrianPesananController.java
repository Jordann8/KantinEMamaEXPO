package siptek.kantinemama.controller.pegawai;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import siptek.kantinemama.model.AppState;
import siptek.kantinemama.model.Pesanan;
import siptek.kantinemama.util.SceneNavigator;

public class AntrianPesananController {

    @FXML
    private Button btnMulaiMemasak1;
    @FXML
    private Button btnMulaiMemasak2;
    @FXML
    private Button btnSelesaiAntar;

    @FXML
    private Button navAntrian;
    @FXML
    private Button navKelolaMenu;
    @FXML
    private Button navKeluar;

    @FXML
    private VBox colPesananBaru;
    @FXML
    private VBox colSedangDimasak;
    @FXML
    private VBox colSiapDiambil;

    private AppState appState = AppState.getInstance();

    private Pesanan orderNew1;
    private Pesanan orderNew2;
    private Pesanan orderCooking;

    @FXML
    public void initialize() {
        refreshQueueUI();
    }

    private void refreshQueueUI() {
        try {
            ObservableList<Pesanan> allOrders = appState.getOrders();

            List<Pesanan> newOrdersList = new ArrayList<>();
            List<Pesanan> cookingOrdersList = new ArrayList<>();
            List<Pesanan> readyOrdersList = new ArrayList<>();

            for (int i = allOrders.size() - 1; i >= 0; i--) {
                Pesanan p = allOrders.get(i);
                if ("Pesanan Baru".equalsIgnoreCase(p.getStatusDapur())) {
                    newOrdersList.add(p);
                } else if ("Sedang Dimasak".equalsIgnoreCase(p.getStatusDapur())) {
                    cookingOrdersList.add(p);
                } else if ("Siap Diambil".equalsIgnoreCase(p.getStatusDapur())) {
                    readyOrdersList.add(p);
                }
            }

            Label lblCountNew = (Label) ((HBox) colPesananBaru.getChildren().get(0)).getChildren().get(1);
            lblCountNew.setText(String.valueOf(newOrdersList.size()));

            orderNew1 = newOrdersList.size() > 0 ? newOrdersList.get(0) : null;
            orderNew2 = newOrdersList.size() > 1 ? newOrdersList.get(1) : null;

            updateNewCard(colPesananBaru, 1, orderNew1);
            updateNewCard(colPesananBaru, 2, orderNew2);

            Label lblCountCooking = (Label) ((HBox) colSedangDimasak.getChildren().get(0)).getChildren().get(1);
            lblCountCooking.setText(String.valueOf(cookingOrdersList.size()));

            orderCooking = cookingOrdersList.size() > 0 ? cookingOrdersList.get(0) : null;
            updateCookingCard(colSedangDimasak, orderCooking);

            Label lblCountReady = (Label) ((HBox) colSiapDiambil.getChildren().get(0)).getChildren().get(1);
            lblCountReady.setText(String.valueOf(readyOrdersList.size()));

            VBox placeholderBox = (VBox) colSiapDiambil.getChildren().get(1);
            placeholderBox.setVisible(true);
            placeholderBox.setManaged(true);
            Label labelPlaceholder = (Label) placeholderBox.getChildren().get(1);
            if (readyOrdersList.isEmpty()) {
                labelPlaceholder.setText("Tidak ada pesanan menunggu");
            } else {
                labelPlaceholder.setText(readyOrdersList.size() + " pesanan siap diambil pelanggan.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateNewCard(VBox colNew, int cardIndex, Pesanan order) {
        try {
            if (cardIndex < 1 || cardIndex >= colNew.getChildren().size())
                return;
            VBox card = (VBox) colNew.getChildren().get(cardIndex);

            if (order == null) {
                card.setVisible(false);
                card.setManaged(false);
                return;
            }

            card.setVisible(true);
            card.setManaged(true);

            HBox header = (HBox) card.getChildren().get(0);
            Label orderIdLabel = (Label) header.getChildren().get(0);
            Label paymentStatusLabel = (Label) header.getChildren().get(2);
            Label tableLabel = (Label) header.getChildren().get(4);

            Label timeLabel = (Label) card.getChildren().get(1);

            VBox itemBox = (VBox) card.getChildren().get(2);
            itemBox.getChildren().clear();
            for (siptek.kantinemama.model.CartItem item : order.getItems()) {
                Label itemLabel = new Label(item.getQty() + "x  " + item.getMenuItem().getNama());
                itemLabel.setStyle("-fx-text-fill: #111827; -fx-font-size: 12; -fx-font-weight: bold;");
                itemBox.getChildren().add(itemLabel);
            }

            orderIdLabel.setText("#" + order.getOrderId());
            paymentStatusLabel.setText(order.getStatusPembayaran().toUpperCase());
            paymentStatusLabel.setStyle("-fx-background-color: "
                    + ("Lunas".equalsIgnoreCase(order.getStatusPembayaran()) ? "#DCFCE7; -fx-text-fill: #16A34A;"
                            : "#FEE2E2; -fx-text-fill: #EF4444;")
                    + " -fx-font-size: 9; -fx-font-weight: bold; -fx-padding: 2 6 2 6; -fx-background-radius: 8;");

            String mejaStr = order.getMeja().replace("mejaMO", "MEJA ");
            if ("-".equals(mejaStr))
                mejaStr = "KASIR";
            tableLabel.setText("🏠 " + mejaStr);

            timeLabel.setText(order.getWaktu() + " • Dine-In");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateCookingCard(VBox colCooking, Pesanan order) {
        try {
            if (colCooking.getChildren().size() < 2)
                return;
            VBox card = (VBox) colCooking.getChildren().get(1);

            if (order == null) {
                card.setVisible(false);
                card.setManaged(false);
                return;
            }

            card.setVisible(true);
            card.setManaged(true);

            HBox header = (HBox) card.getChildren().get(0);
            Label orderIdLabel = (Label) header.getChildren().get(0);
            Label tableLabel = (Label) header.getChildren().get(4);

            Label timeLabel = (Label) card.getChildren().get(1);

            VBox itemBox = (VBox) card.getChildren().get(2);
            itemBox.getChildren().clear();
            for (siptek.kantinemama.model.CartItem item : order.getItems()) {
                Label itemLabel = new Label(item.getQty() + "x  " + item.getMenuItem().getNama());
                itemLabel.setStyle("-fx-text-fill: #111827; -fx-font-size: 12; -fx-font-weight: bold;");
                itemBox.getChildren().add(itemLabel);
            }

            orderIdLabel.setText("#" + order.getOrderId());
            String mejaStr = order.getMeja().replace("mejaMO", "MEJA ");
            if ("-".equals(mejaStr))
                mejaStr = "KASIR";
            tableLabel.setText("🏠 " + mejaStr);
            timeLabel.setText(order.getWaktu() + " • Dine-In");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onMulaiMemasak1(ActionEvent event) {
        if (orderNew1 != null) {
            Pesanan target = orderNew1;
            target.setStatusDapur("Sedang Dimasak");
            refreshQueueUI();
            showInfo("Proses Memasak", "Pesanan #" + target.getOrderId() + " dipindahkan ke dapur.");
        }
    }

    @FXML
    void onMulaiMemasak2(ActionEvent event) {
        if (orderNew2 != null) {
            Pesanan target = orderNew2;
            target.setStatusDapur("Sedang Dimasak");
            refreshQueueUI();
            showInfo("Proses Memasak", "Pesanan #" + target.getOrderId() + " dipindahkan ke dapur.");
        }
    }

    @FXML
    void onSelesaiAntar(ActionEvent event) {
        if (orderCooking != null) {
            Pesanan target = orderCooking;
            target.setStatusDapur("Selesai");
            if (!"Lunas".equalsIgnoreCase(target.getStatusPembayaran())) {
                target.setStatusPembayaran("Lunas");
            }

            // Kosongkan meja secara instan
            String kodeMeja = target.getMeja();
            if (kodeMeja != null) {
                for (siptek.kantinemama.model.Meja meja : appState.getTables()) {
                    if (meja.getKode().equals(kodeMeja)) {
                        meja.setStatus("KOSONG");
                        siptek.kantinemama.util.PersistenceManager.saveState(appState);
                        break;
                    }
                }
            }

            refreshQueueUI();
            showInfo("Pesanan Selesai", "Pesanan #" + target.getOrderId() + " telah disajikan.\n" + target.getMeja().replace("mejaMO", "MEJA ") + " kini telah kosong dan tersedia kembali.");
        }
    }

    @FXML
    void onNavKelolaMenu(ActionEvent event) {
        SceneNavigator.loadScene(event, "/siptek/kantinemama/view/pegawai/KelolaMenu.fxml");
    }

    @FXML
    void onNavAntrian(ActionEvent event) {
        /* sudah di halaman ini */ }

    @FXML
    void onNavKeluar(ActionEvent event) {
        SceneNavigator.loadScene(event, "/siptek/kantinemama/view/pelanggan/HalamanAwalUtama.fxml");
    }

    private void showInfo(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
