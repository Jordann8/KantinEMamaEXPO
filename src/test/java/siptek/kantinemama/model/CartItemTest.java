package siptek.kantinemama.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CartItem — Unit Test Model Item Keranjang")
class CartItemTest {

    private MenuItem nasiGoreng;
    private MenuItem esTeh;

    @BeforeEach
    void setUp() {
        nasiGoreng = new MenuItem("M001", "Nasi Goreng", "Makanan", 15000.0, 50, true, "images/menu/nasi-goreng.jpg");
        esTeh = new MenuItem("M002", "Es Teh", "Minuman", 5000.0, 100, true, "images/menu/es-teh.jpg");
    }

    // === CONSTRUCTOR TESTS ===

    @Test
    @DisplayName("Constructor mengatur menuItem dan qty dengan benar")
    void constructor_fieldTerisi() {
        CartItem cartItem = new CartItem(nasiGoreng, 2);
        assertEquals(nasiGoreng, cartItem.getMenuItem());
        assertEquals(2, cartItem.getQty());
    }

    @Test
    @DisplayName("Constructor dengan qty = 0")
    void constructor_qtyNol() {
        CartItem cartItem = new CartItem(nasiGoreng, 0);
        assertEquals(0, cartItem.getQty());
    }

    @Test
    @DisplayName("Constructor dengan qty = 1")
    void constructor_qtySatu() {
        CartItem cartItem = new CartItem(esTeh, 1);
        assertEquals(1, cartItem.getQty());
    }

    // === SUBTOTAL TESTS ===

    @Test
    @DisplayName("getSubtotal() menghitung harga × qty dengan benar")
    void getSubtotal_hitungBenar() {
        CartItem cartItem = new CartItem(nasiGoreng, 3);
        // 15000 × 3 = 45000
        assertEquals(45000.0, cartItem.getSubtotal(), 0.001);
    }

    @Test
    @DisplayName("getSubtotal() dengan qty = 0 mengembalikan 0")
    void getSubtotal_qtyNol_hasilNol() {
        CartItem cartItem = new CartItem(nasiGoreng, 0);
        assertEquals(0.0, cartItem.getSubtotal(), 0.001);
    }

    @Test
    @DisplayName("getSubtotal() dengan qty = 1 mengembalikan harga satuan")
    void getSubtotal_qtySatu_samaHarga() {
        CartItem cartItem = new CartItem(nasiGoreng, 1);
        assertEquals(15000.0, cartItem.getSubtotal(), 0.001);
    }

    @Test
    @DisplayName("getSubtotal() dengan qty besar — tidak overflow")
    void getSubtotal_qtyBesar() {
        CartItem cartItem = new CartItem(nasiGoreng, 10000);
        // 15000 × 10000 = 150_000_000
        assertEquals(150_000_000.0, cartItem.getSubtotal(), 0.01);
    }

    @Test
    @DisplayName("getSubtotal() dengan harga desimal")
    void getSubtotal_hargaDesimal() {
        MenuItem itemDesimal = new MenuItem("M003", "Kue Lapis", "Camilan", 7500.50, 20, true, "images/menu/kue-lapis.jpg");
        CartItem cartItem = new CartItem(itemDesimal, 4);
        // 7500.50 × 4 = 30002.0
        assertEquals(30002.0, cartItem.getSubtotal(), 0.01);
    }

    // === SETTER TESTS ===

    @Test
    @DisplayName("setQty() mengubah qty dan mempengaruhi subtotal")
    void setQty_ubahSubtotal() {
        CartItem cartItem = new CartItem(nasiGoreng, 2);
        assertEquals(30000.0, cartItem.getSubtotal(), 0.001);

        cartItem.setQty(5);
        assertEquals(5, cartItem.getQty());
        assertEquals(75000.0, cartItem.getSubtotal(), 0.001);
    }

    @Test
    @DisplayName("setMenuItem() mengubah item dan mempengaruhi subtotal")
    void setMenuItem_ubahSubtotal() {
        CartItem cartItem = new CartItem(nasiGoreng, 2);
        assertEquals(30000.0, cartItem.getSubtotal(), 0.001);

        cartItem.setMenuItem(esTeh);
        assertEquals(esTeh, cartItem.getMenuItem());
        // 5000 × 2 = 10000
        assertEquals(10000.0, cartItem.getSubtotal(), 0.001);
    }

    @Test
    @DisplayName("setQty ke 0 setelah sebelumnya positif — subtotal jadi 0")
    void setQty_keNol() {
        CartItem cartItem = new CartItem(nasiGoreng, 3);
        assertEquals(45000.0, cartItem.getSubtotal(), 0.001);

        cartItem.setQty(0);
        assertEquals(0.0, cartItem.getSubtotal(), 0.001);
    }

    @Test
    @DisplayName("getMenuItem() mengembalikan referensi objek yang sama")
    void getMenuItem_referensiSama() {
        CartItem cartItem = new CartItem(nasiGoreng, 1);
        assertSame(nasiGoreng, cartItem.getMenuItem());
    }

    @Test
    @DisplayName("Perubahan harga di MenuItem mempengaruhi subtotal CartItem")
    void perubahanHargaMenuItem_mempengaruhiSubtotal() {
        CartItem cartItem = new CartItem(nasiGoreng, 2);
        assertEquals(30000.0, cartItem.getSubtotal(), 0.001);

        // Ubah harga di objek MenuItem yang sama
        nasiGoreng.setHarga(20000.0);
        // Karena CartItem menyimpan referensi, subtotal ikut berubah
        assertEquals(40000.0, cartItem.getSubtotal(), 0.001);
    }
}
