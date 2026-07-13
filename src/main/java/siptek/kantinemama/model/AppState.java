package siptek.kantinemama.model;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AppState {
    private static AppState instance;

    private ObservableList<MenuItem> menuItems;
    private ObservableList<CartItem> currentCart;
    private ObservableList<Pesanan> orders;
    private ObservableList<Meja> tables;
    private ObservableList<StokItem> stokItems;
    private ObservableList<TransaksiHarian> transaksiHarians;

    private String selectedMeja;
    private String selectedMetodeBayar;
    private double currentTotal;
    private String currentOrderId;
    private String currentRole; // "Admin" or "Pegawai"

    public String getCurrentRole() {
        return currentRole;
    }

    public void setCurrentRole(String currentRole) {
        this.currentRole = currentRole;
    }

    private AppState() {
        menuItems = FXCollections.observableArrayList();
        currentCart = FXCollections.observableArrayList();
        orders = FXCollections.observableArrayList();
        tables = FXCollections.observableArrayList();
        stokItems = FXCollections.observableArrayList();
        transaksiHarians = FXCollections.observableArrayList();

        initializeDummyData();
    }

    public static synchronized AppState getInstance() {
        if (instance == null) {
            instance = new AppState();
        }
        return instance;
    }

    public void loadFromSnapshot(AppStateSnapshot snapshot) {
        if (snapshot.getMenuItems() != null && !snapshot.getMenuItems().isEmpty()) {
            this.menuItems.setAll(snapshot.getMenuItems());
        }
        if (snapshot.getCurrentCart() != null) {
            this.currentCart.setAll(snapshot.getCurrentCart());
        }
        if (snapshot.getOrders() != null) {
            this.orders.setAll(snapshot.getOrders());
        }
        if (snapshot.getTables() != null && !snapshot.getTables().isEmpty()) {
            this.tables.setAll(snapshot.getTables());
        }
        if (snapshot.getStokItems() != null && !snapshot.getStokItems().isEmpty()) {
            this.stokItems.setAll(snapshot.getStokItems());
        }
        if (snapshot.getTransaksiHarians() != null && !snapshot.getTransaksiHarians().isEmpty()) {
            this.transaksiHarians.setAll(snapshot.getTransaksiHarians());
        }
        this.currentRole = snapshot.getCurrentRole();
    }

    private void initializeDummyData() {
        menuItems.add(new MenuItem("M001", "Nasi Goreng Spesial", "Makanan", 15000, 20, true,
                "images/menu/nasi-goreng-spesial.jpg"));
        menuItems.add(new MenuItem("M002", "Ayam Geprek Sambal Matah", "Makanan", 18000, 15, true,
                "images/menu/ayam-geprek-sambal-matah.jpg"));
        menuItems.add(
                new MenuItem("M003", "Mie Goreng Jawa", "Makanan", 12000, 25, true, "images/menu/mie-goreng-jawa.jpg"));
        menuItems.add(new MenuItem("M004", "Sate Ayam Madura (10 Tusuk)", "Makanan", 22000, 10, true,
                "images/menu/sate-ayam-madura.jpg"));
        menuItems.add(new MenuItem("M005", "Es Teh Manis", "Minuman", 5000, 50, true, "images/menu/es-teh-manis.jpg"));
        menuItems.add(new MenuItem("M006", "Es Kopi Susu Aren", "Minuman", 10000, 30, true,
                "images/menu/es-kopi-susu-aren.jpg"));

        menuItems.add(new MenuItem("M007", "Ayam Geprek Sambal Korek", "Makanan", 18000, 15, true,
                "images/menu/ayam-geprek-sambal-korek.jpg"));
        menuItems.add(new MenuItem("M008", "Nasi Goreng Gila Spesial", "Makanan", 20000, 12, true,
                "images/menu/nasi-goreng-gila-spesial.jpg"));
        menuItems.add(new MenuItem("M009", "Es Teh Manis Selasih Jumbo", "Minuman", 8000, 40, true,
                "images/menu/es-teh-manis-selasih-jumbo.jpg"));
        menuItems.add(new MenuItem("M010", "Sate Maranggi Purwakarta", "Makanan", 25000, 10, true,
                "images/menu/sate-maranggi-purwakarta.jpg"));
        menuItems.add(new MenuItem("M011", "Kopi Susu Gula Aren", "Minuman", 10000, 30, true,
                "images/menu/kopi-susu-gula-aren.jpg"));
        menuItems.add(new MenuItem("M012", "Pisang Goreng Keju Cokelat", "Camilan", 10000, 20, true,
                "images/menu/pisang-goreng-keju-cokelat.jpg"));

        for (int i = 1; i <= 17; i++) {
            tables.add(new Meja("mejaMO" + i, "KOSONG"));
        }

        stokItems.add(new StokItem("ST-001", "Ayam Goreng", "Bahan Makanan", 20, 5));
        stokItems.add(new StokItem("ST-002", "Es Teh", "Bahan Minuman", 50, 10));
        stokItems.add(new StokItem("ST-003", "Nasi Putih", "Bahan Makanan", 30, 8));
        stokItems.add(new StokItem("ST-004", "Rendang", "Bahan Makanan", 15, 4));
        stokItems.add(new StokItem("ST-005", "Tahu Gehu", "Camilan", 25, 6));

        ArrayList<CartItem> orderItems1 = new ArrayList<>();
        orderItems1.add(new CartItem(menuItems.get(0), 2));
        orderItems1.add(new CartItem(menuItems.get(4), 2));
        Pesanan dummyOrder1 = new Pesanan("ORD-88219", orderItems1, "Lunas", "QRIS", "mejaMO5", "Selesai",
                "07/07/2026 10:15");
        orders.add(dummyOrder1);

        ArrayList<CartItem> orderItems2 = new ArrayList<>();
        orderItems2.add(new CartItem(menuItems.get(1), 1));
        orderItems2.add(new CartItem(menuItems.get(5), 1));
        Pesanan dummyOrder2 = new Pesanan("ORD-88220", orderItems2, "Lunas", "Tunai", "mejaMO2", "Sedang Dimasak",
                "07/07/2026 11:30");
        orders.add(dummyOrder2);

        transaksiHarians.add(new TransaksiHarian("05/07/2026", "QRIS", 12, 340000, 170000, "Selesai"));
        transaksiHarians.add(new TransaksiHarian("05/07/2026", "Tunai", 8, 210000, 105000, "Selesai"));
        transaksiHarians.add(new TransaksiHarian("06/07/2026", "QRIS", 18, 520000, 260000, "Selesai"));
        transaksiHarians.add(new TransaksiHarian("06/07/2026", "Tunai", 14, 380000, 190000, "Selesai"));
    }

    public ObservableList<MenuItem> getMenuItems() {
        return menuItems;
    }

    public ObservableList<CartItem> getCurrentCart() {
        return currentCart;
    }

    public ObservableList<Pesanan> getOrders() {
        return orders;
    }

    public ObservableList<Meja> getTables() {
        return tables;
    }

    public ObservableList<StokItem> getStokItems() {
        return stokItems;
    }

    public ObservableList<TransaksiHarian> getTransaksiHarians() {
        return transaksiHarians;
    }

    public String getSelectedMeja() {
        return selectedMeja;
    }

    public void setSelectedMeja(String selectedMeja) {
        this.selectedMeja = selectedMeja;
    }

    public String getSelectedMetodeBayar() {
        return selectedMetodeBayar;
    }

    public void setSelectedMetodeBayar(String selectedMetodeBayar) {
        this.selectedMetodeBayar = selectedMetodeBayar;
    }

    public double getCurrentTotal() {
        return currentTotal;
    }

    public void setCurrentTotal(double currentTotal) {
        this.currentTotal = currentTotal;
    }

    public String getCurrentOrderId() {
        return currentOrderId;
    }

    public void setCurrentOrderId(String currentOrderId) {
        this.currentOrderId = currentOrderId;
    }

    public void clearCart() {
        currentCart.clear();
    }
}
