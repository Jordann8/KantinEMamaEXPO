package siptek.kantinemama.api;

import javafx.collections.ObservableList;
import org.junit.jupiter.api.*;
import siptek.kantinemama.model.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("API Test — Kontrak Publik Semua Model")
class ModelApiTest {

    @BeforeEach
    void resetSingleton() throws Exception {
        Field instance = AppState.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);
    }

    @Test
    @DisplayName("MenuItem: 7 parameter constructor → 7 getter yang benar")
    void menuItem_constructorGetterMapping() {
        MenuItem m = new MenuItem("id1", "nama1", "kat1", 999.0, 10, true, "path1");
        assertEquals("id1", m.getId());
        assertEquals("nama1", m.getNama());
        assertEquals("kat1", m.getKategori());
        assertEquals(999.0, m.getHarga(), 0.001);
        assertEquals(10, m.getSisaPorsi());
        assertTrue(m.isAktif());
        assertEquals("path1", m.getGambarPath());
    }

    @Test
    @DisplayName("MenuItem: setter mengubah field yang tepat")
    void menuItem_setterFieldMapping() {
        MenuItem m = new MenuItem("x", "x", "x", 0, 0, false, "x");
        m.setId("newId");
        m.setNama("newNama");
        m.setKategori("newKat");
        m.setHarga(12345.0);
        m.setSisaPorsi(99);
        m.setAktif(true);
        m.setGambarPath("newPath");

        assertEquals("newId", m.getId());
        assertEquals("newNama", m.getNama());
        assertEquals("newKat", m.getKategori());
        assertEquals(12345.0, m.getHarga(), 0.001);
        assertEquals(99, m.getSisaPorsi());
        assertTrue(m.isAktif());
        assertEquals("newPath", m.getGambarPath());
    }

    @Test
    @DisplayName("CartItem: getSubtotal() = harga × qty")
    void cartItem_subtotalContract() {
        MenuItem m = new MenuItem("M001", "Test", "Makanan", 12500, 10, true, "test.jpg");
        CartItem c = new CartItem(m, 4);
        assertEquals(12500.0 * 4, c.getSubtotal(), 0.001);
    }

    @Test
    @DisplayName("Meja: format kode = 'mejaMO' + angka")
    void meja_kodeFormat() {
        for (int i = 1; i <= 17; i++) {
            Meja m = new Meja("mejaMO" + i, "KOSONG");
            assertTrue(m.getKode().matches("mejaMO\\d+"), "Kode meja harus format mejaMO+angka");
        }
    }

    @Test
    @DisplayName("StokItem: kontrak getStatus() untuk 3 status")
    void stokItem_statusContract() {
        StokItem aman = new StokItem("S1", "T", "T", 20, 5);
        assertEquals("STOK AMAN", aman.getStatus());

        StokItem hampir = new StokItem("S2", "T", "T", 5, 5);
        assertEquals("HAMPIR HABIS", hampir.getStatus());

        StokItem habis = new StokItem("S3", "T", "T", 0, 5);
        assertEquals("HABIS", habis.getStatus());
    }

    @Test
    @DisplayName("Pesanan: getTotalPembayaran() = sum(subtotal)")
    void pesanan_totalContract() {
        MenuItem m1 = new MenuItem("M1", "A", "Makanan", 10000, 10, true, "a.jpg");
        MenuItem m2 = new MenuItem("M2", "B", "Minuman", 5000, 10, true, "b.jpg");
        List<CartItem> items = List.of(new CartItem(m1, 3), new CartItem(m2, 2));
        Pesanan p = new Pesanan("ORD-1", new ArrayList<>(items), "Lunas", "QRIS", "mejaMO1", "Selesai", "01/01/2026");
        assertEquals(10000.0 * 3 + 5000.0 * 2, p.getTotalPembayaran(), 0.001);
    }

    @Test
    @DisplayName("Pesanan: getFormattedItems() format = 'Nama (qty), Nama2 (qty2)'")
    void pesanan_formattedItemsContract() {
        MenuItem m1 = new MenuItem("M1", "Nasi", "Makanan", 10000, 10, true, "a.jpg");
        MenuItem m2 = new MenuItem("M2", "Teh", "Minuman", 5000, 10, true, "b.jpg");
        List<CartItem> items = List.of(new CartItem(m1, 2), new CartItem(m2, 1));
        Pesanan p = new Pesanan("ORD-1", new ArrayList<>(items), "Lunas", "QRIS", "mejaMO1", "Selesai", "01/01/2026");
        assertEquals("Nasi (2), Teh (1)", p.getFormattedItems());
    }

    @Test
    @DisplayName("TransaksiHarian: semua field accessible")
    void transaksiHarian_fieldsAccessible() {
        TransaksiHarian t = new TransaksiHarian("01/01/2026", "QRIS", 10, 200000, 100000, "Selesai");
        assertEquals("01/01/2026", t.getTanggal());
        assertEquals("QRIS", t.getMetodeBayar());
        assertEquals(10, t.getVolume());
        assertEquals(200000.0, t.getPendapatanKotor(), 0.001);
        assertEquals(100000.0, t.getPendapatanBersih(), 0.001);
        assertEquals("Selesai", t.getStatus());
    }

    @Test
    @DisplayName("AppState: singleton contract — referensi sama")
    void appState_singletonContract() {
        assertSame(AppState.getInstance(), AppState.getInstance());
    }

    @Test
    @DisplayName("AppState: semua collection getter non-null")
    void appState_collectionsNonNull() {
        AppState s = AppState.getInstance();
        assertNotNull(s.getMenuItems());
        assertNotNull(s.getCurrentCart());
        assertNotNull(s.getOrders());
        assertNotNull(s.getTables());
        assertNotNull(s.getStokItems());
        assertNotNull(s.getTransaksiHarians());
    }

    @Test
    @DisplayName("AppState: clearCart() mengosongkan keranjang")
    void appState_clearCartContract() {
        AppState s = AppState.getInstance();
        s.getCurrentCart().add(new CartItem(s.getMenuItems().get(0), 1));
        assertFalse(s.getCurrentCart().isEmpty());
        s.clearCart();
        assertTrue(s.getCurrentCart().isEmpty());
    }

    @Test
    @DisplayName("AppState: checkout state round-trip")
    void appState_checkoutRoundTrip() {
        AppState s = AppState.getInstance();
        s.setSelectedMeja("mejaMO10");
        s.setSelectedMetodeBayar("Tunai");
        s.setCurrentTotal(99999.50);
        s.setCurrentOrderId("ORD-ROUND");

        assertEquals("mejaMO10", s.getSelectedMeja());
        assertEquals("Tunai", s.getSelectedMetodeBayar());
        assertEquals(99999.50, s.getCurrentTotal(), 0.001);
        assertEquals("ORD-ROUND", s.getCurrentOrderId());
    }
}
