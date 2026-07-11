package siptek.kantinemama.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pesanan — Unit Test Model Pesanan")
class PesananTest {

    private MenuItem nasiGoreng;
    private MenuItem esTeh;
    private List<CartItem> items;

    @BeforeEach
    void setUp() {
        nasiGoreng = new MenuItem("M001", "Nasi Goreng", "Makanan", 15000, 20, true, "images/menu/nasi-goreng.jpg");
        esTeh = new MenuItem("M005", "Es Teh Manis", "Minuman", 5000, 50, true, "images/menu/es-teh-manis.jpg");
        items = new ArrayList<>();
        items.add(new CartItem(nasiGoreng, 2));
        items.add(new CartItem(esTeh, 3));
    }

    @Test
    @DisplayName("Constructor mengatur semua field dengan benar")
    void constructor_fieldTerisi() {
        Pesanan p = new Pesanan("ORD-001", items, "Lunas", "QRIS", "mejaMO5", "Selesai", "07/07/2026 10:00");
        assertEquals("ORD-001", p.getOrderId());
        assertEquals(2, p.getItems().size());
        assertEquals("Lunas", p.getStatusPembayaran());
        assertEquals("QRIS", p.getMetodeBayar());
        assertEquals("mejaMO5", p.getMeja());
        assertEquals("Selesai", p.getStatusDapur());
        assertEquals("07/07/2026 10:00", p.getWaktu());
    }

    @Test
    @DisplayName("getTotalPembayaran() menjumlahkan semua subtotal")
    void getTotalPembayaran_hitungBenar() {
        Pesanan p = new Pesanan("ORD-001", items, "Lunas", "QRIS", "mejaMO5", "Selesai", "07/07/2026 10:00");
        // (15000 * 2) + (5000 * 3) = 30000 + 15000 = 45000
        assertEquals(45000.0, p.getTotalPembayaran(), 0.001);
    }

    @Test
    @DisplayName("getTotalPembayaran() dengan satu item")
    void getTotalPembayaran_satuItem() {
        List<CartItem> single = new ArrayList<>();
        single.add(new CartItem(nasiGoreng, 1));
        Pesanan p = new Pesanan("ORD-002", single, "Lunas", "Tunai", "mejaMO1", "Selesai", "07/07/2026 11:00");
        assertEquals(15000.0, p.getTotalPembayaran(), 0.001);
    }

    @Test
    @DisplayName("getTotalPembayaran() dengan items kosong mengembalikan 0")
    void getTotalPembayaran_itemsKosong() {
        Pesanan p = new Pesanan("ORD-003", new ArrayList<>(), "Belum Bayar", "Tunai", "mejaMO1", "Pesanan Baru", "07/07/2026 12:00");
        assertEquals(0.0, p.getTotalPembayaran(), 0.001);
    }

    @Test
    @DisplayName("Constructor dengan null items → default ke list kosong")
    void constructor_nullItems() {
        Pesanan p = new Pesanan("ORD-004", null, "Belum Bayar", "Tunai", "mejaMO1", "Pesanan Baru", "07/07/2026 12:00");
        assertNotNull(p.getItems());
        assertTrue(p.getItems().isEmpty());
        assertEquals(0.0, p.getTotalPembayaran(), 0.001);
    }

    @Test
    @DisplayName("getFormattedItems() format benar dengan banyak item")
    void getFormattedItems_banyakItem() {
        Pesanan p = new Pesanan("ORD-001", items, "Lunas", "QRIS", "mejaMO5", "Selesai", "07/07/2026 10:00");
        assertEquals("Nasi Goreng (2), Es Teh Manis (3)", p.getFormattedItems());
    }

    @Test
    @DisplayName("getFormattedItems() format benar dengan satu item — tanpa koma")
    void getFormattedItems_satuItem() {
        List<CartItem> single = new ArrayList<>();
        single.add(new CartItem(nasiGoreng, 1));
        Pesanan p = new Pesanan("ORD-002", single, "Lunas", "Tunai", "mejaMO1", "Selesai", "07/07/2026 11:00");
        assertEquals("Nasi Goreng (1)", p.getFormattedItems());
    }

    @Test
    @DisplayName("getFormattedItems() dengan items kosong — string kosong")
    void getFormattedItems_kosong() {
        Pesanan p = new Pesanan("ORD-003", new ArrayList<>(), "Belum Bayar", "Tunai", "mejaMO1", "Pesanan Baru", "07/07/2026 12:00");
        assertEquals("", p.getFormattedItems());
    }

    @Test
    @DisplayName("Setter statusPembayaran dan statusDapur berfungsi")
    void setters_status() {
        Pesanan p = new Pesanan("ORD-001", items, "Belum Bayar", "QRIS", "mejaMO5", "Pesanan Baru", "07/07/2026 10:00");
        p.setStatusPembayaran("Lunas");
        assertEquals("Lunas", p.getStatusPembayaran());
        p.setStatusDapur("Sedang Dimasak");
        assertEquals("Sedang Dimasak", p.getStatusDapur());
    }

    @Test
    @DisplayName("Transisi status dapur lengkap")
    void transisiStatusDapur() {
        Pesanan p = new Pesanan("ORD-001", items, "Lunas", "QRIS", "mejaMO5", "Pesanan Baru", "07/07/2026 10:00");
        assertEquals("Pesanan Baru", p.getStatusDapur());
        p.setStatusDapur("Sedang Dimasak");
        assertEquals("Sedang Dimasak", p.getStatusDapur());
        p.setStatusDapur("Siap Diambil");
        assertEquals("Siap Diambil", p.getStatusDapur());
        p.setStatusDapur("Selesai");
        assertEquals("Selesai", p.getStatusDapur());
    }
}
