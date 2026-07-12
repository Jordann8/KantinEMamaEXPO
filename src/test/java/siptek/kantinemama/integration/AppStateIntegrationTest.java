package siptek.kantinemama.integration;

import javafx.collections.ObservableList;
import org.junit.jupiter.api.*;
import siptek.kantinemama.model.*;

import java.lang.reflect.Field;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AppState — Integration Test Singleton & Data Awal")
class AppStateIntegrationTest {

    @BeforeEach
    void resetSingleton() throws Exception {
        Field instance = AppState.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);
    }

    @Test
    @DisplayName("getInstance() selalu mengembalikan instance yang sama")
    void getInstance_sameInstance() {
        AppState a = AppState.getInstance();
        AppState b = AppState.getInstance();
        assertSame(a, b);
    }

    @Test
    @DisplayName("getInstance() thread-safe — multi-thread menghasilkan instance yang sama")
    void getInstance_threadSafe() throws Exception {
        int threadCount = 10;
        ExecutorService exec = Executors.newFixedThreadPool(threadCount);
        CyclicBarrier barrier = new CyclicBarrier(threadCount);
        AtomicReference<AppState> first = new AtomicReference<>();

        for (int i = 0; i < threadCount; i++) {
            exec.submit(() -> {
                try {
                    barrier.await();
                    AppState state = AppState.getInstance();
                    first.compareAndSet(null, state);
                    assertSame(first.get(), state);
                } catch (Exception e) {
                    fail("Thread error: " + e.getMessage());
                }
            });
        }
        exec.shutdown();
        assertTrue(exec.awaitTermination(5, TimeUnit.SECONDS));
    }

    @Test
    @DisplayName("Data awal: 12 menu items ter-load")
    void menuItems_duaBelasItem() {
        ObservableList<MenuItem> items = AppState.getInstance().getMenuItems();
        assertEquals(12, items.size());
    }

    @Test
    @DisplayName("Data awal: 17 meja ter-load")
    void tables_tujuhBelasItem() {
        ObservableList<Meja> tables = AppState.getInstance().getTables();
        assertEquals(17, tables.size());
    }

    @Test
    @DisplayName("Data awal: 5 stok item ter-load")
    void stokItems_limaItem() {
        ObservableList<StokItem> stok = AppState.getInstance().getStokItems();
        assertEquals(5, stok.size());
    }

    @Test
    @DisplayName("Data awal: 2 pesanan dummy ter-load")
    void orders_duaPesanan() {
        ObservableList<Pesanan> orders = AppState.getInstance().getOrders();
        assertEquals(2, orders.size());
    }

    @Test
    @DisplayName("Data awal: 4 transaksi harian ter-load")
    void transaksi_empatRecord() {
        ObservableList<TransaksiHarian> t = AppState.getInstance().getTransaksiHarians();
        assertEquals(4, t.size());
    }

    @Test
    @DisplayName("Meja 3, 7, 12 berstatus TERISI, sisanya KOSONG")
    void mejaStatus_terisiDanKosong() {
        ObservableList<Meja> tables = AppState.getInstance().getTables();
        for (Meja m : tables) {
            int num = Integer.parseInt(m.getKode().replace("mejaMO", ""));
            if (num == 3 || num == 7 || num == 12) {
                assertEquals("TERISI", m.getStatus(), "mejaMO" + num + " seharusnya TERISI");
            } else {
                assertEquals("KOSONG", m.getStatus(), "mejaMO" + num + " seharusnya KOSONG");
            }
        }
    }

    @Test
    @DisplayName("Menu items memiliki 3 kategori: Makanan, Minuman, Camilan")
    void menuKategori_tigaJenis() {
        ObservableList<MenuItem> items = AppState.getInstance().getMenuItems();
        assertTrue(items.stream().anyMatch(i -> "Makanan".equals(i.getKategori())));
        assertTrue(items.stream().anyMatch(i -> "Minuman".equals(i.getKategori())));
        assertTrue(items.stream().anyMatch(i -> "Camilan".equals(i.getKategori())));
    }

    @Test
    @DisplayName("Operasi cart: tambah, verifikasi, clear")
    void cartOperations() {
        AppState state = AppState.getInstance();
        assertTrue(state.getCurrentCart().isEmpty());

        CartItem item = new CartItem(state.getMenuItems().get(0), 2);
        state.getCurrentCart().add(item);
        assertEquals(1, state.getCurrentCart().size());

        state.clearCart();
        assertTrue(state.getCurrentCart().isEmpty());
    }

    @Test
    @DisplayName("Checkout flow: set dan get semua field checkout")
    void checkoutFlow() {
        AppState state = AppState.getInstance();
        state.setSelectedMeja("mejaMO5");
        state.setSelectedMetodeBayar("QRIS");
        state.setCurrentTotal(45000);
        state.setCurrentOrderId("ORD-99999");

        assertEquals("mejaMO5", state.getSelectedMeja());
        assertEquals("QRIS", state.getSelectedMetodeBayar());
        assertEquals(45000, state.getCurrentTotal(), 0.001);
        assertEquals("ORD-99999", state.getCurrentOrderId());
    }

    @Test
    @DisplayName("Pesanan dummy pertama memiliki data yang benar")
    void pesananDummy_dataBenar() {
        Pesanan p = AppState.getInstance().getOrders().get(0);
        assertEquals("ORD-88219", p.getOrderId());
        assertEquals("Lunas", p.getStatusPembayaran());
        assertEquals("QRIS", p.getMetodeBayar());
        assertEquals("mejaMO5", p.getMeja());
    }
}
