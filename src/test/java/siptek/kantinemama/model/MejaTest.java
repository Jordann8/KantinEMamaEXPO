package siptek.kantinemama.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Meja — Unit Test Model Meja")
class MejaTest {

    private Meja meja;

    @BeforeEach
    void setUp() {
        meja = new Meja("mejaMO1", "KOSONG");
    }

    @Test
    @DisplayName("Constructor mengatur kode dan status dengan benar")
    void constructor_fieldTerisi() {
        assertEquals("mejaMO1", meja.getKode());
        assertEquals("KOSONG", meja.getStatus());
    }

    @Test
    @DisplayName("Status KOSONG — meja tersedia")
    void status_kosong() {
        Meja m = new Meja("mejaMO2", "KOSONG");
        assertEquals("KOSONG", m.getStatus());
    }

    @Test
    @DisplayName("Status TERISI — meja sudah dipakai")
    void status_terisi() {
        Meja m = new Meja("mejaMO3", "TERISI");
        assertEquals("TERISI", m.getStatus());
    }

    @Test
    @DisplayName("Status TERPILIH — meja dipilih oleh pelanggan")
    void status_terpilih() {
        Meja m = new Meja("mejaMO5", "TERPILIH");
        assertEquals("TERPILIH", m.getStatus());
    }

    @Test
    @DisplayName("setKode() mengubah kode meja")
    void setKode_mengubahNilai() {
        meja.setKode("mejaMO17");
        assertEquals("mejaMO17", meja.getKode());
    }

    @Test
    @DisplayName("setStatus() mengubah status meja")
    void setStatus_mengubahNilai() {
        meja.setStatus("TERISI");
        assertEquals("TERISI", meja.getStatus());
    }

    @Test
    @DisplayName("Transisi status: KOSONG → TERPILIH → TERISI")
    void transisiStatus() {
        assertEquals("KOSONG", meja.getStatus());
        meja.setStatus("TERPILIH");
        assertEquals("TERPILIH", meja.getStatus());
        meja.setStatus("TERISI");
        assertEquals("TERISI", meja.getStatus());
    }

    @Test
    @DisplayName("Constructor dengan null kode — edge case")
    void constructor_nullKode() {
        Meja m = new Meja(null, "KOSONG");
        assertNull(m.getKode());
    }
}
