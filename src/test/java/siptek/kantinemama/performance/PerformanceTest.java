package siptek.kantinemama.performance;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.*;
import siptek.kantinemama.model.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Performance Test — Kinerja Operasi Kritis")
class PerformanceTest {

    @BeforeEach
    void resetSingleton() throws Exception {
        Field instance = AppState.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);
    }

    @Test
    @DisplayName("AppState inisialisasi selesai dalam < 100ms")
    void appState_inisialisasiCepat() {
        long start = System.nanoTime();
        AppState.getInstance();
        long elapsed = (System.nanoTime() - start) / 1_000_000;
        assertTrue(elapsed < 100, "AppState init butuh " + elapsed + "ms, seharusnya < 100ms");
    }

    @Test
    @DisplayName("Buat 1000 MenuItem dalam < 50ms")
    void create1000MenuItems() {
        long start = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            new MenuItem("M" + i, "Item " + i, "Makanan", 10000 + i, 50, true, "images/test.jpg");
        }
        long elapsed = (System.nanoTime() - start) / 1_000_000;
        assertTrue(elapsed < 50, "1000 MenuItem butuh " + elapsed + "ms, seharusnya < 50ms");
    }

    @Test
    @DisplayName("Buat 1000 CartItem dan hitung subtotal dalam < 50ms")
    void create1000CartItems_hitungSubtotal() {
        MenuItem item = new MenuItem("M001", "Test", "Makanan", 15000, 50, true, "test.jpg");
        long start = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            CartItem cart = new CartItem(item, i + 1);
            cart.getSubtotal();
        }
        long elapsed = (System.nanoTime() - start) / 1_000_000;
        assertTrue(elapsed < 50, "1000 CartItem + subtotal butuh " + elapsed + "ms, seharusnya < 50ms");
    }

    @Test
    @DisplayName("100 Pesanan × 10 items, getTotalPembayaran() dalam < 100ms")
    void create100Pesanan_hitungTotal() {
        MenuItem item = new MenuItem("M001", "Test", "Makanan", 15000, 50, true, "test.jpg");

        long start = System.nanoTime();
        for (int i = 0; i < 100; i++) {
            List<CartItem> items = new ArrayList<>();
            for (int j = 0; j < 10; j++) {
                items.add(new CartItem(item, j + 1));
            }
            Pesanan p = new Pesanan("ORD-" + i, items, "Lunas", "QRIS", "mejaMO1", "Selesai", "01/01/2026");
            p.getTotalPembayaran();
        }
        long elapsed = (System.nanoTime() - start) / 1_000_000;
        assertTrue(elapsed < 100, "100 Pesanan butuh " + elapsed + "ms, seharusnya < 100ms");
    }

    @Test
    @DisplayName("10000 panggilan getInstance() dalam < 100ms")
    void getInstance10000kali() {
        AppState.getInstance(); // warm up
        long start = System.nanoTime();
        for (int i = 0; i < 10000; i++) {
            AppState.getInstance();
        }
        long elapsed = (System.nanoTime() - start) / 1_000_000;
        assertTrue(elapsed < 100, "10000x getInstance butuh " + elapsed + "ms, seharusnya < 100ms");
    }

    @Test
    @DisplayName("10000 panggilan getStatus() dalam < 50ms")
    void getStatus10000kali() {
        StokItem item = new StokItem("ST-001", "Test", "Test", 20, 5);
        long start = System.nanoTime();
        for (int i = 0; i < 10000; i++) {
            item.getStatus();
        }
        long elapsed = (System.nanoTime() - start) / 1_000_000;
        assertTrue(elapsed < 50, "10000x getStatus butuh " + elapsed + "ms, seharusnya < 50ms");
    }

    @Test
    @DisplayName("ObservableList 10000 items dalam < 200ms")
    void observableList10000items() {
        long start = System.nanoTime();
        ObservableList<MenuItem> list = FXCollections.observableArrayList();
        for (int i = 0; i < 10000; i++) {
            list.add(new MenuItem("M" + i, "Item " + i, "Makanan", 10000, 50, true, "test.jpg"));
        }
        long elapsed = (System.nanoTime() - start) / 1_000_000;
        assertTrue(elapsed < 200, "10000 ObservableList items butuh " + elapsed + "ms, seharusnya < 200ms");
        assertEquals(10000, list.size());
    }

    @Test
    @DisplayName("getFormattedItems() dengan 50 items dalam < 50ms")
    void formattedItems50items() {
        MenuItem item = new MenuItem("M001", "Nasi Goreng", "Makanan", 15000, 50, true, "test.jpg");
        List<CartItem> items = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            items.add(new CartItem(item, i + 1));
        }
        Pesanan p = new Pesanan("ORD-001", items, "Lunas", "QRIS", "mejaMO1", "Selesai", "01/01/2026");

        long start = System.nanoTime();
        String result = p.getFormattedItems();
        long elapsed = (System.nanoTime() - start) / 1_000_000;

        assertFalse(result.isEmpty());
        assertTrue(elapsed < 50, "getFormattedItems(50) butuh " + elapsed + "ms, seharusnya < 50ms");
    }
}
