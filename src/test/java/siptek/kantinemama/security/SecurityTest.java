package siptek.kantinemama.security;

import org.junit.jupiter.api.*;
import siptek.kantinemama.model.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Security Test — Keamanan Aplikasi")
class SecurityTest {

    @BeforeEach
    void resetSingleton() throws Exception {
        Field instance = AppState.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);
    }

    @Test
    @DisplayName("AppState constructor private — tidak bisa diakses langsung")
    void appState_constructorPrivate() {
        Constructor<?>[] constructors = AppState.class.getDeclaredConstructors();
        for (Constructor<?> c : constructors) {
            assertTrue(Modifier.isPrivate(c.getModifiers()),
                "Constructor AppState harus private");
        }
    }

    @Test
    @DisplayName("Null safety: setSelectedMeja(null) tidak crash")
    void nullSafety_selectedMeja() {
        AppState state = AppState.getInstance();
        assertDoesNotThrow(() -> state.setSelectedMeja(null));
        assertNull(state.getSelectedMeja());
    }

    @Test
    @DisplayName("Null safety: setSelectedMetodeBayar(null) tidak crash")
    void nullSafety_selectedMetodeBayar() {
        AppState state = AppState.getInstance();
        assertDoesNotThrow(() -> state.setSelectedMetodeBayar(null));
        assertNull(state.getSelectedMetodeBayar());
    }

    @Test
    @DisplayName("Null safety: setCurrentOrderId(null) tidak crash")
    void nullSafety_currentOrderId() {
        AppState state = AppState.getInstance();
        assertDoesNotThrow(() -> state.setCurrentOrderId(null));
        assertNull(state.getCurrentOrderId());
    }

    @Test
    @DisplayName("CartItem dengan null MenuItem — NullPointerException pada getSubtotal")
    void cartItem_nullMenuItem_throwsOnSubtotal() {
        CartItem item = new CartItem(null, 2);
        assertThrows(NullPointerException.class, item::getSubtotal);
    }

    @Test
    @DisplayName("Pesanan dengan null items → default ke list kosong")
    void pesanan_nullItems_defaultEmpty() {
        Pesanan p = new Pesanan("ORD-001", null, "Belum Bayar", "Tunai", "mejaMO1", "Pesanan Baru", "01/01/2026");
        assertNotNull(p.getItems());
        assertTrue(p.getItems().isEmpty());
    }

    @Test
    @DisplayName("MenuItem menerima harga negatif — tidak ada validasi (temuan keamanan)")
    void menuItem_hargaNegatif_tidakAdaValidasi() {
        MenuItem item = new MenuItem("M999", "Test", "Makanan", -5000, 10, true, "test.jpg");
        assertEquals(-5000, item.getHarga(), 0.001);
        // FINDING: No input validation on negative prices
    }

    @Test
    @DisplayName("StokItem dengan sisaPorsi negatif — status tetap HABIS")
    void stokItem_sisaPorsiNegatif() {
        StokItem item = new StokItem("ST-999", "Test", "Test", -10, 5);
        assertEquals("HABIS", item.getStatus());
    }

    @Test
    @DisplayName("Thread safety: multi-thread getInstance() menghasilkan satu instance")
    void threadSafety_singleton() throws Exception {
        int threadCount = 20;
        ExecutorService exec = Executors.newFixedThreadPool(threadCount);
        CyclicBarrier barrier = new CyclicBarrier(threadCount);
        ConcurrentHashMap<Integer, AppState> results = new ConcurrentHashMap<>();

        for (int i = 0; i < threadCount; i++) {
            final int idx = i;
            exec.submit(() -> {
                try {
                    barrier.await();
                    results.put(idx, AppState.getInstance());
                } catch (Exception e) {
                    fail(e.getMessage());
                }
            });
        }
        exec.shutdown();
        assertTrue(exec.awaitTermination(5, TimeUnit.SECONDS));

        AppState first = results.values().iterator().next();
        for (AppState s : results.values()) {
            assertSame(first, s);
        }
    }

    @Test
    @DisplayName("XSS-like strings disimpan dan dikembalikan apa adanya")
    void xssStrings_storedCorrectly() {
        String xss = "<script>alert('XSS')</script>";
        String sql = "'; DROP TABLE users; --";
        MenuItem item = new MenuItem("M999", xss, sql, 10000, 10, true, "test.jpg");
        assertEquals(xss, item.getNama());
        assertEquals(sql, item.getKategori());
    }

    @Test
    @DisplayName("StokItem dengan batasMinimum negatif — edge case")
    void stokItem_batasMinimumNegatif() {
        StokItem item = new StokItem("ST-999", "Test", "Test", 5, -1);
        // sisaPorsi (5) > batasMinimum (-1) → STOK AMAN
        assertEquals("STOK AMAN", item.getStatus());
    }
}
