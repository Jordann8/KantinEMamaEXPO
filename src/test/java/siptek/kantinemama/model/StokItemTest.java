package siptek.kantinemama.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("StokItem — Unit Test Model Stok Barang")
class StokItemTest {

    private StokItem stokItem;

    @BeforeEach
    void setUp() {
        stokItem = new StokItem("ST-001", "Ayam Goreng", "Bahan Makanan", 20, 5);
    }

    @Test
    @DisplayName("Constructor mengatur semua field dengan benar")
    void constructor_fieldTerisi() {
        assertEquals("ST-001", stokItem.getIdBahan());
        assertEquals("Ayam Goreng", stokItem.getNama());
        assertEquals("Bahan Makanan", stokItem.getKategori());
        assertEquals(20, stokItem.getSisaPorsi());
        assertEquals(5, stokItem.getBatasMinimum());
    }

    @Test
    @DisplayName("getStatus() = STOK AMAN ketika sisaPorsi > batasMinimum")
    void getStatus_stokAman() {
        assertEquals("STOK AMAN", stokItem.getStatus());
    }

    @Test
    @DisplayName("getStatus() = HAMPIR HABIS ketika sisaPorsi <= batasMinimum dan > 0")
    void getStatus_hampirHabis() {
        stokItem.setSisaPorsi(5); // equal to batasMinimum
        assertEquals("HAMPIR HABIS", stokItem.getStatus());
    }

    @Test
    @DisplayName("getStatus() = HAMPIR HABIS ketika sisaPorsi < batasMinimum tapi > 0")
    void getStatus_hampirHabis_kurangDariBatas() {
        stokItem.setSisaPorsi(3);
        assertEquals("HAMPIR HABIS", stokItem.getStatus());
    }

    @Test
    @DisplayName("getStatus() = HABIS ketika sisaPorsi = 0")
    void getStatus_habis_nol() {
        stokItem.setSisaPorsi(0);
        assertEquals("HABIS", stokItem.getStatus());
    }

    @Test
    @DisplayName("getStatus() = HABIS ketika sisaPorsi negatif")
    void getStatus_habis_negatif() {
        stokItem.setSisaPorsi(-1);
        assertEquals("HABIS", stokItem.getStatus());
    }

    @Test
    @DisplayName("Boundary: sisaPorsi = batasMinimum → HAMPIR HABIS")
    void boundary_samaDenganBatas() {
        stokItem.setSisaPorsi(5); // exactly batasMinimum
        assertEquals("HAMPIR HABIS", stokItem.getStatus());
    }

    @Test
    @DisplayName("Boundary: sisaPorsi = batasMinimum + 1 → STOK AMAN")
    void boundary_satuDiAtasBatas() {
        stokItem.setSisaPorsi(6); // batasMinimum + 1
        assertEquals("STOK AMAN", stokItem.getStatus());
    }

    @Test
    @DisplayName("setSisaPorsi mengubah status secara otomatis")
    void setSisaPorsi_mengubahStatus() {
        assertEquals("STOK AMAN", stokItem.getStatus());
        stokItem.setSisaPorsi(4);
        assertEquals("HAMPIR HABIS", stokItem.getStatus());
        stokItem.setSisaPorsi(0);
        assertEquals("HABIS", stokItem.getStatus());
    }

    @Test
    @DisplayName("Setter semua field berfungsi")
    void setters_berfungsi() {
        stokItem.setIdBahan("ST-999");
        stokItem.setNama("Beras");
        stokItem.setKategori("Bahan Pokok");
        stokItem.setBatasMinimum(10);
        assertEquals("ST-999", stokItem.getIdBahan());
        assertEquals("Beras", stokItem.getNama());
        assertEquals("Bahan Pokok", stokItem.getKategori());
        assertEquals(10, stokItem.getBatasMinimum());
    }
}
