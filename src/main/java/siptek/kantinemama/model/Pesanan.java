package siptek.kantinemama.model;

import java.util.ArrayList;
import java.util.List;

public class Pesanan {
    private String orderId;
    private List<CartItem> items;
    private String statusPembayaran; // Belum Bayar, Lunas, Dibatalkan
    private String metodeBayar; // QRIS, Tunai
    private String meja;
    private String statusDapur; // Pesanan Baru, Sedang Dimasak, Siap Diambil, Selesai
    private String waktu;

    public Pesanan(String orderId, List<CartItem> items, String statusPembayaran, String metodeBayar, String meja, String statusDapur, String waktu) {
        this.orderId = orderId;
        this.items = items != null ? items : new ArrayList<>();
        this.statusPembayaran = statusPembayaran;
        this.metodeBayar = metodeBayar;
        this.meja = meja;
        this.statusDapur = statusDapur;
        this.waktu = waktu;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public String getStatusPembayaran() {
        return statusPembayaran;
    }

    public void setStatusPembayaran(String statusPembayaran) {
        this.statusPembayaran = statusPembayaran;
    }

    public String getMetodeBayar() {
        return metodeBayar;
    }

    public void setMetodeBayar(String metodeBayar) {
        this.metodeBayar = metodeBayar;
    }

    public String getMeja() {
        return meja;
    }

    public void setMeja(String meja) {
        this.meja = meja;
    }

    public String getStatusDapur() {
        return statusDapur;
    }

    public void setStatusDapur(String statusDapur) {
        this.statusDapur = statusDapur;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public double getTotalPembayaran() {
        double total = 0;
        for (CartItem item : items) {
            total += item.getSubtotal();
        }
        return total;
    }

    // Helper for table display
    public String getFormattedItems() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            CartItem item = items.get(i);
            sb.append(item.getMenuItem().getNama()).append(" (").append(item.getQty()).append(")");
            if (i < items.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
}
