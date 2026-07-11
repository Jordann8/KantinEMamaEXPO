package siptek.kantinemama.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("MenuItem — Unit Test Model Item Menu")
class MenuItemTest {

    private MenuItem menuItem;

    @BeforeEach
    void setUp() {
        menuItem = new MenuItem("M001", "Nasi Goreng", "Makanan", 15000.0, 50, true, "images/menu/nasi-goreng.jpg");
    }

    // === CONSTRUCTOR TESTS ===

    @Test
    @DisplayName("Constructor mengatur semua field dengan benar")
    void constructor_semuaFieldTerisi() {
        assertEquals("M001", menuItem.getId());
        assertEquals("Nasi Goreng", menuItem.getNama());
        assertEquals("Makanan", menuItem.getKategori());
        assertEquals(15000.0, menuItem.getHarga(), 0.001);
        assertEquals(50, menuItem.getSisaPorsi());
        assertTrue(menuItem.isAktif());
        assertEquals("images/menu/nasi-goreng.jpg", menuItem.getGambarPath());
    }

    @Test
    @DisplayName("Constructor dengan nilai null untuk field String")
    void constructor_denganNilaiNull() {
        MenuItem item = new MenuItem(null, null, null, 0, 0, false, null);
        assertNull(item.getId());
        assertNull(item.getNama());
        assertNull(item.getKategori());
        assertNull(item.getGambarPath());
    }

    @Test
    @DisplayName("Constructor dengan string kosong")
    void constructor_denganStringKosong() {
        MenuItem item = new MenuItem("", "", "", 0, 0, false, "");
        assertEquals("", item.getId());
        assertEquals("", item.getNama());
        assertEquals("", item.getKategori());
        assertEquals("", item.getGambarPath());
    }

    // === GETTER TESTS ===

    @Test
    @DisplayName("getId() mengembalikan ID yang benar")
    void getId_nilaiBenar() {
        assertEquals("M001", menuItem.getId());
    }

    @Test
    @DisplayName("getNama() mengembalikan nama yang benar")
    void getNama_nilaiBenar() {
        assertEquals("Nasi Goreng", menuItem.getNama());
    }

    @Test
    @DisplayName("getKategori() mengembalikan kategori yang benar")
    void getKategori_nilaiBenar() {
        assertEquals("Makanan", menuItem.getKategori());
    }

    @Test
    @DisplayName("getHarga() mengembalikan harga yang benar")
    void getHarga_nilaiBenar() {
        assertEquals(15000.0, menuItem.getHarga(), 0.001);
    }

    @Test
    @DisplayName("getSisaPorsi() mengembalikan sisa porsi yang benar")
    void getSisaPorsi_nilaiBenar() {
        assertEquals(50, menuItem.getSisaPorsi());
    }

    @Test
    @DisplayName("isAktif() mengembalikan status aktif yang benar")
    void isAktif_nilaiBenar() {
        assertTrue(menuItem.isAktif());
    }

    @Test
    @DisplayName("getGambarPath() mengembalikan path gambar yang benar")
    void getGambarPath_nilaiBenar() {
        assertEquals("images/menu/nasi-goreng.jpg", menuItem.getGambarPath());
    }

    // === SETTER TESTS ===

    @Test
    @DisplayName("setId() mengubah nilai ID")
    void setId_mengubahNilai() {
        menuItem.setId("M999");
        assertEquals("M999", menuItem.getId());
    }

    @Test
    @DisplayName("setNama() mengubah nama menu")
    void setNama_mengubahNilai() {
        menuItem.setNama("Mie Goreng");
        assertEquals("Mie Goreng", menuItem.getNama());
    }

    @Test
    @DisplayName("setKategori() mengubah kategori")
    void setKategori_mengubahNilai() {
        menuItem.setKategori("Minuman");
        assertEquals("Minuman", menuItem.getKategori());
    }

    @Test
    @DisplayName("setHarga() mengubah harga")
    void setHarga_mengubahNilai() {
        menuItem.setHarga(25000.0);
        assertEquals(25000.0, menuItem.getHarga(), 0.001);
    }

    @Test
    @DisplayName("setSisaPorsi() mengubah sisa porsi")
    void setSisaPorsi_mengubahNilai() {
        menuItem.setSisaPorsi(30);
        assertEquals(30, menuItem.getSisaPorsi());
    }

    @Test
    @DisplayName("setAktif() mengubah status aktif")
    void setAktif_mengubahNilai() {
        menuItem.setAktif(false);
        assertFalse(menuItem.isAktif());
    }

    @Test
    @DisplayName("setGambarPath() mengubah path gambar")
    void setGambarPath_mengubahNilai() {
        menuItem.setGambarPath("images/menu/mie-goreng.jpg");
        assertEquals("images/menu/mie-goreng.jpg", menuItem.getGambarPath());
    }

    // === EDGE CASE TESTS ===

    @Test
    @DisplayName("Harga nol — edge case valid")
    void hargaNol_tidakError() {
        MenuItem item = new MenuItem("M002", "Air Mineral", "Minuman", 0.0, 100, true, "images/menu/air-mineral.jpg");
        assertEquals(0.0, item.getHarga(), 0.001);
    }

    @Test
    @DisplayName("Harga sangat besar — edge case valid")
    void hargaSangatBesar() {
        MenuItem item = new MenuItem("M003", "Wagyu Steak", "Makanan", 999999999.99, 1, true, "images/menu/wagyu.jpg");
        assertEquals(999999999.99, item.getHarga(), 0.01);
    }

    @Test
    @DisplayName("SisaPorsi negatif — edge case")
    void sisaPorsiNegatif() {
        MenuItem item = new MenuItem("M004", "Sate Ayam", "Makanan", 20000, -5, true, "images/menu/sate-ayam.jpg");
        assertEquals(-5, item.getSisaPorsi());
    }

    @Test
    @DisplayName("setNama dengan null — edge case")
    void setNama_denganNull() {
        menuItem.setNama(null);
        assertNull(menuItem.getNama());
    }

    @Test
    @DisplayName("Toggle aktif: true → false → true")
    void toggleAktif_bolakBalik() {
        assertTrue(menuItem.isAktif());
        menuItem.setAktif(false);
        assertFalse(menuItem.isAktif());
        menuItem.setAktif(true);
        assertTrue(menuItem.isAktif());
    }

    @Test
    @DisplayName("Constructor dengan aktif=false")
    void constructor_aktifFalse() {
        MenuItem item = new MenuItem("M005", "Es Teh", "Minuman", 5000, 0, false, "images/menu/es-teh.jpg");
        assertFalse(item.isAktif());
    }

    @Test
    @DisplayName("Setter mengubah sisa porsi ke nol")
    void setSisaPorsi_keNol() {
        menuItem.setSisaPorsi(0);
        assertEquals(0, menuItem.getSisaPorsi());
    }
}
