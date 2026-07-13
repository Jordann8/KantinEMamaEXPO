package siptek.kantinemama.controller.pelanggan;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import siptek.kantinemama.model.AppState;
import siptek.kantinemama.model.CartItem;
import siptek.kantinemama.model.MenuItem;
import siptek.kantinemama.util.SceneNavigator;

public class KatalogMenuController {

    @FXML private Button btnKonfirmasi;
    @FXML private Button btnMinus1;
    @FXML private Button btnMinus2;
    @FXML private Button btnPlus1;
    @FXML private Button btnPlus2;
    @FXML private Label lblTotal;
    @FXML private Button navCart;
    @FXML private Button navHelp;
    @FXML private Button navMenu;
    @FXML private Button navNotif;
    @FXML private Button navOrders;

    @FXML private FlowPane menuGrid;
    @FXML private ImageView imgCartItem1;
    @FXML private ImageView imgCartItem2;

    private AppState appState = AppState.getInstance();

    @FXML
    public void initialize() {
        updateCartUI();
        renderMenuGrid();
    }

    private void renderMenuGrid() {
        if (menuGrid == null) return;
        menuGrid.getChildren().clear();

        for (MenuItem item : appState.getMenuItems()) {
            if (!item.isAktif()) continue;

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
            setImage(iv, item.getGambarPath());
            sp.getChildren().add(iv);

            card.getChildren().add(sp);

            Label nameLabel = new Label(item.getNama());
            nameLabel.setWrapText(true);
            nameLabel.setStyle("-fx-text-fill: #111827; -fx-font-size: 15; -fx-font-weight: bold;");
            card.getChildren().add(nameLabel);

            Label priceLabel = new Label(siptek.kantinemama.util.CurrencyUtil.formatRupiah(item.getHarga()));
            priceLabel.setStyle("-fx-text-fill: #0F2044; -fx-font-size: 16; -fx-font-weight: bold;");
            card.getChildren().add(priceLabel);

            Button btnAdd = new Button("Tambah ke Keranjang");
            btnAdd.setMaxWidth(Double.MAX_VALUE);
            btnAdd.setPrefHeight(38);
            btnAdd.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #1565C0; -fx-font-size: 12; -fx-font-weight: bold; -fx-border-color: #1565C0; -fx-border-width: 1.5; -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand;");
            btnAdd.setOnAction(e -> addToCart(item.getId()));

            card.getChildren().add(btnAdd);

            menuGrid.getChildren().add(card);
        }
    }

    private void setImage(ImageView iv, String resourcePath) {
        if (iv == null) return;
        try {
            java.io.InputStream stream = getClass().getResourceAsStream("/siptek/kantinemama/" + resourcePath);
            if (stream != null) {
                iv.setImage(new Image(stream));
            } else {
                java.io.InputStream phStream = getClass().getResourceAsStream("/siptek/kantinemama/images/placeholder.jpg");
                if (phStream != null) {
                    iv.setImage(new Image(phStream));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addToCart(String itemId) {
        MenuItem menuItem = null;
        for (MenuItem item : appState.getMenuItems()) {
            if (item.getId().equals(itemId)) {
                menuItem = item;
                break;
            }
        }

        if (menuItem == null) return;

        if (menuItem.getSisaPorsi() <= 0) {
            showWarning("Stok Habis", "Maaf, menu " + menuItem.getNama() + " sudah habis!");
            return;
        }

        CartItem cartItem = null;
        for (CartItem item : appState.getCurrentCart()) {
            if (item.getMenuItem().getId().equals(itemId)) {
                cartItem = item;
                break;
            }
        }

        if (cartItem != null) {
            if (cartItem.getQty() >= menuItem.getSisaPorsi()) {
                showWarning("Stok Terbatas", "Tidak bisa menambah lebih banyak. Stok terbatas!");
                return;
            }
            cartItem.setQty(cartItem.getQty() + 1);
        } else {
            if (appState.getCurrentCart().size() >= 2) {
                showWarning("Keranjang Penuh", "Untuk simulasi ini, Anda hanya dapat memesan maksimal 2 jenis menu berbeda!");
                return;
            }
            appState.getCurrentCart().add(new CartItem(menuItem, 1));
        }

        updateCartUI();
    }

    private void updateCartUI() {
        ObservableList<CartItem> cart = appState.getCurrentCart();
        
        CartItem item1 = cart.size() > 0 ? cart.get(0) : null;
        CartItem item2 = cart.size() > 1 ? cart.get(1) : null;

        updateCartRow(btnMinus1, imgCartItem1, item1);
        updateCartRow(btnMinus2, imgCartItem2, item2);

        double subtotal = 0;
        int totalQty = 0;
        for (CartItem item : cart) {
            subtotal += item.getSubtotal();
            totalQty += item.getQty();
        }

        double tax = subtotal * 0.10;
        double total = subtotal + tax;

        lblTotal.setText(siptek.kantinemama.util.CurrencyUtil.formatRupiah(total));

        try {
            VBox summaryBox = (VBox) lblTotal.getParent().getParent();
            HBox subtotalHBox = (HBox) summaryBox.getChildren().get(0);
            Label subtotalValue = (Label) subtotalHBox.getChildren().get(2);
            subtotalValue.setText(siptek.kantinemama.util.CurrencyUtil.formatRupiah(subtotal));

            HBox taxHBox = (HBox) summaryBox.getChildren().get(1);
            Label taxValue = (Label) taxHBox.getChildren().get(2);
            taxValue.setText(siptek.kantinemama.util.CurrencyUtil.formatRupiah(tax));
        } catch (Exception e) {
        }

        try {
            HBox headerBox = (HBox) navCart.getParent();
            Label badgeLabel = (Label) headerBox.getChildren().get(headerBox.getChildren().indexOf(navCart) + 1);
            badgeLabel.setText(totalQty + " item");
        } catch (Exception e) {
        }
    }

    private void updateCartRow(Button btnMinus, ImageView imgView, CartItem item) {
        try {
            HBox row = (HBox) btnMinus.getParent().getParent();
            if (item == null) {
                row.setVisible(false);
                row.setManaged(false);
                return;
            }
            row.setVisible(true);
            row.setManaged(true);

            if (imgView != null) {
                String imageName = slugify(item.getMenuItem().getNama());
                setImage(imgView, "images/menu/" + imageName + ".jpg");

                Rectangle clip = new Rectangle(46, 46);
                clip.setArcWidth(12);
                clip.setArcHeight(12);
                imgView.setClip(clip);
            }

            VBox textVBox = (VBox) row.getChildren().get(1);
            Label nameLabel = (Label) textVBox.getChildren().get(0);
            Label subtotalLabel = (Label) textVBox.getChildren().get(1);
            Label descLabel = (Label) textVBox.getChildren().get(2);

            HBox btnBox = (HBox) btnMinus.getParent();
            Label qtyLabel = (Label) btnBox.getChildren().get(1);

            nameLabel.setText(item.getMenuItem().getNama());
            subtotalLabel.setText(siptek.kantinemama.util.CurrencyUtil.formatRupiah(item.getSubtotal()));
            descLabel.setText(String.format("(%dx %s)", item.getQty(), siptek.kantinemama.util.CurrencyUtil.formatRupiah(item.getMenuItem().getHarga())));
            qtyLabel.setText(String.valueOf(item.getQty()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String slugify(String name) {
        String slug = name.toLowerCase();
        if (slug.contains("sate ayam")) {
            return "sate-ayam-madura";
        }
        return slug.replace(" ", "-");
    }

    private void changeQty(int index, int delta) {
        ObservableList<CartItem> cart = appState.getCurrentCart();
        if (index < 0 || index >= cart.size()) return;

        CartItem item = cart.get(index);
        int newQty = item.getQty() + delta;

        if (newQty <= 0) {
            cart.remove(index);
        } else {
            if (newQty > item.getMenuItem().getSisaPorsi()) {
                showWarning("Stok Terbatas", "Stok bahan menu tidak mencukupi!");
                return;
            }
            item.setQty(newQty);
        }
        updateCartUI();
    }

    @FXML
    void onMinus1(ActionEvent event) {
        changeQty(0, -1);
    }

    @FXML
    void onMinus2(ActionEvent event) {
        changeQty(1, -1);
    }

    @FXML
    void onPlus1(ActionEvent event) {
        changeQty(0, 1);
    }

    @FXML
    void onPlus2(ActionEvent event) {
        changeQty(1, 1);
    }

    @FXML
    void onKonfirmasi(ActionEvent event) {
        if (appState.getCurrentCart().isEmpty()) {
            showWarning("Keranjang Kosong", "Silakan tambahkan menu ke keranjang terlebih dahulu!");
            return;
        }

        double subtotal = 0;
        for (CartItem item : appState.getCurrentCart()) {
            subtotal += item.getSubtotal();
        }
        double total = subtotal * 1.10;
        appState.setCurrentTotal(total);

        SceneNavigator.loadScene(event, "/siptek/kantinemama/view/pelanggan/PilihMeja.fxml");
    }

    @FXML void onNavMenu(ActionEvent event) {
    }

    @FXML
    void onNavOrders(ActionEvent event) {
        SceneNavigator.loadScene(event, "/siptek/kantinemama/view/pelanggan/PesananSaya.fxml");
    }

    @FXML
    void onNavHelp(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Bantuan Pemesanan");
        alert.setHeaderText("Alur Pemesanan Kantin E Mama");
        alert.setContentText("1. Pilih hidangan dari katalog menu.\n" +
                "2. Klik 'Tambah ke Keranjang'.\n" +
                "3. Atur jumlah pesanan di keranjang belanja.\n" +
                "4. Klik 'Konfirmasi Pesanan' untuk memilih metode pembayaran.");
        alert.showAndWait();
    }

    @FXML
    void onNavCart(ActionEvent event) {
    }

    @FXML
    void onNavNotif(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Notifikasi");
        alert.setHeaderText(null);
        alert.setContentText("Anda belum memiliki notifikasi baru.");
        alert.showAndWait();
    }

    private void showWarning(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
