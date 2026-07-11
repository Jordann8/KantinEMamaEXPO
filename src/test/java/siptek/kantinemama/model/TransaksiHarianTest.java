package siptek.kantinemama.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TransaksiHarian — Unit Test Model Transaksi Harian")
class TransaksiHarianTest {

    private TransaksiHarian transaksi;

    @BeforeEach
    void setUp() {
        transaksi = new TransaksiHarian("05/07/2026", "QRIS", 12, 340000, 170000, "Selesai");
    }

    @Test
    @DisplayName("Constructor mengatur semua field dengan benar")
    void constructor_fieldTerisi() {
        assertEquals("05/07/2026", transaksi.getTanggal());
        assertEquals("QRIS", transaksi.getMetodeBayar());
        assertEquals(12, transaksi.getVolume());
        assertEquals(340000.0, transaksi.getPendapatanKotor(), 0.001);
        assertEquals(170000.0, transaksi.getPendapatanBersih(), 0.001);
        assertEquals("Selesai", transaksi.getStatus());
    }

    @Test
    @DisplayName("setTanggal() mengubah tanggal")
    void setTanggal() {
        transaksi.setTanggal("06/07/2026");
        assertEquals("06/07/2026", transaksi.getTanggal());
    }

    @Test
    @DisplayName("setMetodeBayar() mengubah metode bayar")
    void setMetodeBayar() {
        transaksi.setMetodeBayar("Tunai");
        assertEquals("Tunai", transaksi.getMetodeBayar());
    }

    @Test
    @DisplayName("setVolume() mengubah volume")
    void setVolume() {
        transaksi.setVolume(25);
        assertEquals(25, transaksi.getVolume());
    }

    @Test
    @DisplayName("setPendapatanKotor() mengubah pendapatan kotor")
    void setPendapatanKotor() {
        transaksi.setPendapatanKotor(500000);
        assertEquals(500000.0, transaksi.getPendapatanKotor(), 0.001);
    }

    @Test
    @DisplayName("setPendapatanBersih() mengubah pendapatan bersih")
    void setPendapatanBersih() {
        transaksi.setPendapatanBersih(250000);
        assertEquals(250000.0, transaksi.getPendapatanBersih(), 0.001);
    }

    @Test
    @DisplayName("setStatus() mengubah status")
    void setStatus() {
        transaksi.setStatus("Pending");
        assertEquals("Pending", transaksi.getStatus());
    }

    @Test
    @DisplayName("Volume nol — edge case")
    void volumeNol() {
        TransaksiHarian t = new TransaksiHarian("01/01/2026", "QRIS", 0, 0, 0, "Selesai");
        assertEquals(0, t.getVolume());
        assertEquals(0.0, t.getPendapatanKotor(), 0.001);
    }
}
