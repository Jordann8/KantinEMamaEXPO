package siptek.kantinemama.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import siptek.kantinemama.util.SceneNavigator;

import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("FXML Loading — Integration Test 11 File FXML Aktif (pasca Revisi V2)")
class FxmlLoadingIntegrationTest {

    private void assertFxmlExists(String path) {
        URL url = SceneNavigator.class.getResource(path);
        assertNotNull(url, "FXML tidak ditemukan: " + path);
    }

    // === PELANGGAN (8 files) ===

    @Test
    @DisplayName("Pelanggan: HalamanAwalUtama.fxml ada")
    void pelanggan_halamanAwalUtama() {
        assertFxmlExists("/siptek/kantinemama/view/pelanggan/HalamanAwalUtama.fxml");
    }

    @Test
    @DisplayName("Pelanggan: KatalogMenu.fxml ada")
    void pelanggan_katalogMenu() {
        assertFxmlExists("/siptek/kantinemama/view/pelanggan/KatalogMenu.fxml");
    }

    @Test
    @DisplayName("Pelanggan: KonfirmasiPesanan.fxml ada")
    void pelanggan_konfirmasiPesanan() {
        assertFxmlExists("/siptek/kantinemama/view/pelanggan/KonfirmasiPesanan.fxml");
    }

    @Test
    @DisplayName("Pelanggan: PembayaranQRIS.fxml ada")
    void pelanggan_pembayaranQris() {
        assertFxmlExists("/siptek/kantinemama/view/pelanggan/PembayaranQRIS.fxml");
    }

    @Test
    @DisplayName("Pelanggan: PembayaranTunai.fxml ada")
    void pelanggan_pembayaranTunai() {
        assertFxmlExists("/siptek/kantinemama/view/pelanggan/PembayaranTunai.fxml");
    }

    @Test
    @DisplayName("Pelanggan: PembayaranBerhasil.fxml ada")
    void pelanggan_pembayaranBerhasil() {
        assertFxmlExists("/siptek/kantinemama/view/pelanggan/PembayaranBerhasil.fxml");
    }

    @Test
    @DisplayName("Pelanggan: PilihMeja.fxml ada")
    void pelanggan_pilihMeja() {
        assertFxmlExists("/siptek/kantinemama/view/pelanggan/PilihMeja.fxml");
    }

    @Test
    @DisplayName("Pelanggan: PesananSaya.fxml ada")
    void pelanggan_pesananSaya() {
        assertFxmlExists("/siptek/kantinemama/view/pelanggan/PesananSaya.fxml");
    }

    // === PEGAWAI (2 file aktif — Dashboard/StokBarang/LaporanKeuangan lama
    // sudah dihapus navigasinya sejak Revisi V2, lihat REVISI-V2-KANTINEMAMA.md) ===

    @Test
    @DisplayName("Pegawai: KelolaMenu.fxml ada")
    void pegawai_kelolaMenu() {
        assertFxmlExists("/siptek/kantinemama/view/pegawai/KelolaMenu.fxml");
    }

    @Test
    @DisplayName("Pegawai: AntrianPesanan.fxml ada")
    void pegawai_antrianPesanan() {
        assertFxmlExists("/siptek/kantinemama/view/pegawai/AntrianPesanan.fxml");
    }

    // === OWNER (1 file aktif — scope Owner dipangkas jadi HANYA Laporan
    // Keuangan sejak Revisi V2, lihat REVISI-V2-KANTINEMAMA.md) ===

    @Test
    @DisplayName("Owner: LaporanKeuangan1.fxml ada")
    void owner_laporanKeuangan1() {
        assertFxmlExists("/siptek/kantinemama/view/owner/LaporanKeuangan1.fxml");
    }
}
