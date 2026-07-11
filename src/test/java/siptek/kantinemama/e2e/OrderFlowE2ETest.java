package siptek.kantinemama.e2e;

import javafx.collections.ObservableList;
import org.junit.jupiter.api.*;
import siptek.kantinemama.model.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("E2E — Alur Pemesanan Lengkap (Tanpa UI)")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OrderFlowE2ETest {

    private static AppState state;

    @BeforeAll
    static void init() throws Exception {
        Field instance = AppState.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);
        state = AppState.getInstance();
    }

    @Test
    @Order(1)
    @DisplayName("Step 1: Menu tersedia di AppState")
    void step1_menuTersedia() {
        ObservableList<MenuItem> menu = state.getMenuItems();
        assertFalse(menu.isEmpty());
        assertEquals(6, menu.size());
    }

    @Test
    @Order(2)
    @DisplayName("Step 2: Tambah item ke keranjang")
    void step2_tambahKeKeranjang() {
        state.clearCart();
        MenuItem nasiGoreng = state.getMenuItems().get(0);
        MenuItem esTeh = state.getMenuItems().get(4);

        state.getCurrentCart().add(new CartItem(nasiGoreng, 2));
        state.getCurrentCart().add(new CartItem(esTeh, 1));

        assertEquals(2, state.getCurrentCart().size());
    }

    @Test
    @Order(3)
    @DisplayName("Step 3: Hitung total keranjang")
    void step3_hitungTotal() {
        double total = 0;
        for (CartItem item : state.getCurrentCart()) {
            total += item.getSubtotal();
        }
        // 28000*2 + 18000*1 = 74000
        assertEquals(74000.0, total, 0.001);
        state.setCurrentTotal(total);
    }

    @Test
    @Order(4)
    @DisplayName("Step 4: Set metode bayar ke QRIS")
    void step4_setMetodeBayar() {
        state.setSelectedMetodeBayar("QRIS");
        assertEquals("QRIS", state.getSelectedMetodeBayar());
    }

    @Test
    @Order(5)
    @DisplayName("Step 5: Buat pesanan dari keranjang")
    void step5_buatPesanan() {
        String orderId = "ORD-" + System.currentTimeMillis();
        state.setCurrentOrderId(orderId);
        assertNotNull(state.getCurrentOrderId());
        assertTrue(state.getCurrentOrderId().startsWith("ORD-"));
    }

    @Test
    @Order(6)
    @DisplayName("Step 6: Pilih meja")
    void step6_pilihMeja() {
        state.setSelectedMeja("mejaMO5");
        assertEquals("mejaMO5", state.getSelectedMeja());
    }

    @Test
    @Order(7)
    @DisplayName("Step 7: Tambah pesanan ke riwayat")
    void step7_tambahKeRiwayat() {
        int countBefore = state.getOrders().size();
        List<CartItem> orderItems = new ArrayList<>(state.getCurrentCart());
        Pesanan pesanan = new Pesanan(
            state.getCurrentOrderId(), orderItems,
            "Lunas", state.getSelectedMetodeBayar(),
            state.getSelectedMeja(), "Pesanan Baru",
            "07/07/2026 21:00"
        );
        state.getOrders().add(pesanan);
        assertEquals(countBefore + 1, state.getOrders().size());
    }

    @Test
    @Order(8)
    @DisplayName("Step 8: Clear cart setelah pemesanan selesai")
    void step8_clearCart() {
        state.clearCart();
        assertTrue(state.getCurrentCart().isEmpty());
    }
}
