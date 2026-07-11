package siptek.kantinemama.e2e;

import javafx.collections.ObservableList;
import org.junit.jupiter.api.*;
import siptek.kantinemama.model.*;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("E2E — Manajemen Stok Barang")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class StokManagementE2ETest {

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
    @DisplayName("Step 1: Stok awal ter-load dengan benar")
    void step1_stokAwalTerload() {
        ObservableList<StokItem> stok = state.getStokItems();
        assertEquals(5, stok.size());
    }

    @Test
    @Order(2)
    @DisplayName("Step 2: Status awal semua stok — STOK AMAN")
    void step2_statusAwalStokAman() {
        for (StokItem item : state.getStokItems()) {
            assertEquals("STOK AMAN", item.getStatus(),
                item.getNama() + " seharusnya STOK AMAN saat awal");
        }
    }

    @Test
    @Order(3)
    @DisplayName("Step 3: Tambah stok — status tetap STOK AMAN")
    void step3_tambahStok() {
        StokItem ayam = state.getStokItems().get(0); // Ayam Goreng, 20, batas 5
        ayam.setSisaPorsi(30);
        assertEquals(30, ayam.getSisaPorsi());
        assertEquals("STOK AMAN", ayam.getStatus());
    }

    @Test
    @Order(4)
    @DisplayName("Step 4: Kurangi stok ke batas minimum — HAMPIR HABIS")
    void step4_kurangiKeBatas() {
        StokItem ayam = state.getStokItems().get(0);
        ayam.setSisaPorsi(5); // = batasMinimum
        assertEquals("HAMPIR HABIS", ayam.getStatus());
    }

    @Test
    @Order(5)
    @DisplayName("Step 5: Kurangi stok ke nol — HABIS")
    void step5_kurangiKeNol() {
        StokItem ayam = state.getStokItems().get(0);
        ayam.setSisaPorsi(0);
        assertEquals("HABIS", ayam.getStatus());
    }

    @Test
    @Order(6)
    @DisplayName("Step 6: Restock dari habis — kembali STOK AMAN")
    void step6_restock() {
        StokItem ayam = state.getStokItems().get(0);
        ayam.setSisaPorsi(20);
        assertEquals("STOK AMAN", ayam.getStatus());
    }

    @Test
    @Order(7)
    @DisplayName("Step 7: Multiple items — perubahan independen")
    void step7_multipleItemsIndependen() {
        StokItem esTeh = state.getStokItems().get(1); // Es Teh, 50, batas 10
        StokItem nasi = state.getStokItems().get(2);  // Nasi Putih, 30, batas 8

        esTeh.setSisaPorsi(3);
        nasi.setSisaPorsi(100);

        assertEquals("HAMPIR HABIS", esTeh.getStatus());
        assertEquals("STOK AMAN", nasi.getStatus());
    }

    @Test
    @Order(8)
    @DisplayName("Step 8: Stok negatif — tetap HABIS")
    void step8_stokNegatif() {
        StokItem rendang = state.getStokItems().get(3); // Rendang
        rendang.setSisaPorsi(-5);
        assertEquals("HABIS", rendang.getStatus());
    }
}
