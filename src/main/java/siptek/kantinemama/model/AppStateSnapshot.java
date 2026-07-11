package siptek.kantinemama.model;

import java.io.Serializable;
import java.util.ArrayList;

public class AppStateSnapshot implements Serializable {
    private static final long serialVersionUID = 1L;

    private ArrayList<MenuItem> menuItems;
    private ArrayList<CartItem> currentCart;
    private ArrayList<Pesanan> orders;
    private ArrayList<Meja> tables;
    private ArrayList<StokItem> stokItems;
    private ArrayList<TransaksiHarian> transaksiHarians;
    private String currentRole;

    public AppStateSnapshot() {
        this.menuItems = new ArrayList<>();
        this.currentCart = new ArrayList<>();
        this.orders = new ArrayList<>();
        this.tables = new ArrayList<>();
        this.stokItems = new ArrayList<>();
        this.transaksiHarians = new ArrayList<>();
    }

    public ArrayList<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(ArrayList<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    public ArrayList<CartItem> getCurrentCart() {
        return currentCart;
    }

    public void setCurrentCart(ArrayList<CartItem> currentCart) {
        this.currentCart = currentCart;
    }

    public ArrayList<Pesanan> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<Pesanan> orders) {
        this.orders = orders;
    }

    public ArrayList<Meja> getTables() {
        return tables;
    }

    public void setTables(ArrayList<Meja> tables) {
        this.tables = tables;
    }

    public ArrayList<StokItem> getStokItems() {
        return stokItems;
    }

    public void setStokItems(ArrayList<StokItem> stokItems) {
        this.stokItems = stokItems;
    }

    public ArrayList<TransaksiHarian> getTransaksiHarians() {
        return transaksiHarians;
    }

    public void setTransaksiHarians(ArrayList<TransaksiHarian> transaksiHarians) {
        this.transaksiHarians = transaksiHarians;
    }

    public String getCurrentRole() {
        return currentRole;
    }

    public void setCurrentRole(String currentRole) {
        this.currentRole = currentRole;
    }
}
