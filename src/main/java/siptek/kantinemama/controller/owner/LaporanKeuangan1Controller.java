package siptek.kantinemama.controller.owner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import siptek.kantinemama.model.AppState;
import siptek.kantinemama.model.CartItem;
import siptek.kantinemama.model.Pesanan;
import siptek.kantinemama.util.SceneNavigator;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * REVISI V2: Ini SATU-SATUNYA halaman sisi Owner (Dasbor Pemilik, Manajemen
 * Katalog Menu versi Owner, dan Pengaturan Sistem sudah dihapus dari scope
 * -- lihat REVISI-V2-KANTINEMAMA.md Bagian 5). Tidak ada sidebar/nav lain,
 * cuma tombol Ekspor PDF dan Keluar.
 *
 * BUG DIPERBAIKI: sebelumnya kartu ringkasan & tabel Riwayat berisi angka
 * HARDCODE yang sama persis setiap kali dibuka, tidak peduli transaksi apa
 * yang sudah masuk (ditemukan saat code review sebelumnya). Sekarang
 * semuanya dihitung dari appState.getOrders() yang sesungguhnya.
 *
 * Catatan pendapatan bersih: karena aplikasi ini tidak (belum) punya konsep
 * "biaya platform" terpisah, PENDAPATAN BERSIH dihitung sebagai 90% dari
 * PENDAPATAN KOTOR (rasio 10% ini meniru potongan biaya layanan/settlement,
 * sesuai rasio yang konsisten terlihat di rancangan desain awal). Kalau
 * nanti ada aturan bisnis yang lebih spesifik soal potongan ini, angka 0.9
 * di bawah tinggal disesuaikan.
 */
public class LaporanKeuangan1Controller {

    @FXML private Label lblPeriode;
    @FXML private Label lblTotalTunai;
    @FXML private Label lblTunaiTrend;
    @FXML private Label lblTotalQris;
    @FXML private Label lblQrisTrend;
    @FXML private Button btnEksporExcelPdf;
    @FXML private Button btnKeluar;
    @FXML private Button btnFilter;
    @FXML private Button btnPilihBulan;
    @FXML private VBox riwayatRows;
    @FXML private VBox emptyStateRiwayat;
    @FXML private Label lblFooterInfo;

    private AppState appState = AppState.getInstance();

    private static class RingkasanHarian {
        String tanggal;
        String metodeBayar;
        int volume;
        double kotor;

        double bersih() {
            return kotor * 0.9;
        }
    }

    @FXML
    public void initialize() {
        refreshLaporan();
    }

    private void refreshLaporan() {
        List<Pesanan> orders = new ArrayList<>(appState.getOrders());

        double totalTunai = 0;
        double totalQris = 0;
        Map<String, RingkasanHarian> grouped = new LinkedHashMap<>();

        for (Pesanan p : orders) {
            if (!"Lunas".equalsIgnoreCase(p.getStatusPembayaran())) {
                continue; // laporan keuangan cuma hitung transaksi yang sudah lunas
            }

            if ("Tunai".equalsIgnoreCase(p.getMetodeBayar())) {
                totalTunai += p.getTotalPembayaran();
            } else if ("QRIS".equalsIgnoreCase(p.getMetodeBayar())) {
                totalQris += p.getTotalPembayaran();
            }

            String tanggal = extractTanggal(p.getWaktu());
            String key = tanggal + "|" + p.getMetodeBayar();
            RingkasanHarian r = grouped.computeIfAbsent(key, k -> {
                RingkasanHarian baru = new RingkasanHarian();
                baru.tanggal = tanggal;
                baru.metodeBayar = p.getMetodeBayar();
                return baru;
            });
            int qty = 0;
            for (CartItem item : p.getItems()) {
                qty += item.getQty();
            }
            r.volume += qty;
            r.kotor += p.getTotalPembayaran();
        }

        lblTotalTunai.setText(siptek.kantinemama.util.CurrencyUtil.formatRupiah(totalTunai));
        lblTotalQris.setText(siptek.kantinemama.util.CurrencyUtil.formatRupiah(totalQris));
        lblTunaiTrend.setText(orders.isEmpty() ? "Belum ada transaksi" : "Dari " + orders.size() + " total transaksi tercatat");
        lblQrisTrend.setText(orders.isEmpty() ? "Belum ada transaksi" : "Dari " + orders.size() + " total transaksi tercatat");

        riwayatRows.getChildren().clear();
        List<RingkasanHarian> rows = new ArrayList<>(grouped.values());
        // Urut terbaru dulu (asumsi format tanggal dd/MM/yyyy dari Pesanan.getWaktu())
        rows.sort((a, b) -> b.tanggal.compareTo(a.tanggal));

        for (RingkasanHarian r : rows) {
            riwayatRows.getChildren().add(buildRow(r));
        }

        boolean kosong = rows.isEmpty();
        emptyStateRiwayat.setVisible(kosong);
        emptyStateRiwayat.setManaged(kosong);
        lblFooterInfo.setText("Menampilkan " + rows.size() + " dari " + rows.size() + " hari operasional");
    }

    private HBox buildRow(RingkasanHarian r) {
        HBox row = new HBox();
        row.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        row.setStyle("-fx-border-color: #E5E7EB; -fx-border-width: 0 0 1 0; -fx-padding: 14 16 14 16;");

        Label tanggalLbl = new Label(r.tanggal);
        tanggalLbl.setPrefWidth(180);
        tanggalLbl.setStyle("-fx-text-fill: #374151; -fx-font-size: 13;");

        Label metodeLbl = new Label(("QRIS".equalsIgnoreCase(r.metodeBayar) ? "🔲  " : "💵  ") + r.metodeBayar);
        metodeLbl.setPrefWidth(170);
        metodeLbl.setStyle("-fx-text-fill: #111827; -fx-font-size: 13;");

        Label volumeLbl = new Label(r.volume + " Item");
        volumeLbl.setPrefWidth(120);
        volumeLbl.setStyle("-fx-text-fill: #111827; -fx-font-size: 13;");

        Label kotorLbl = new Label(siptek.kantinemama.util.CurrencyUtil.formatRupiah(r.kotor));
        kotorLbl.setPrefWidth(200);
        kotorLbl.setStyle("-fx-text-fill: #111827; -fx-font-size: 13;");

        Label bersihLbl = new Label(siptek.kantinemama.util.CurrencyUtil.formatRupiah(r.bersih()));
        HBox.setHgrow(bersihLbl, javafx.scene.layout.Priority.ALWAYS);
        bersihLbl.setStyle("-fx-text-fill: #1565C0; -fx-font-size: 13; -fx-font-weight: bold;");

        row.getChildren().addAll(tanggalLbl, metodeLbl, volumeLbl, kotorLbl, bersihLbl);
        return row;
    }

    private String extractTanggal(String waktu) {
        // waktu format "dd/MM/yyyy HH:mm" -> ambil tanggal saja
        if (waktu == null) return "-";
        int spaceIdx = waktu.indexOf(' ');
        return spaceIdx > 0 ? waktu.substring(0, spaceIdx) : waktu;
    }

    @FXML
    void onEksporExcelPdf(ActionEvent event) {
        try (FileWriter writer = new FileWriter("laporan_keuangan_owner.txt")) {
            writer.write("==================================================\n");
            writer.write("        LAPORAN KEUANGAN KANTIN E MAMA (OWNER)    \n");
            writer.write("==================================================\n\n");
            writer.write(lblTotalTunai.getText().replace("Rp", "Total Tunai Kasir: Rp") + "\n");
            writer.write(lblTotalQris.getText().replace("Rp", "Total QRIS: Rp") + "\n\n");
            writer.write("RIWAYAT PER HARI:\n");
            for (javafx.scene.Node node : riwayatRows.getChildren()) {
                if (node instanceof HBox) {
                    StringBuilder line = new StringBuilder("- ");
                    for (javafx.scene.Node child : ((HBox) node).getChildren()) {
                        if (child instanceof Label) {
                            line.append(((Label) child).getText()).append(" | ");
                        }
                    }
                    writer.write(line + "\n");
                }
            }
            writer.write("--------------------------------------------------\n");

            showInfo("Ekspor Berhasil", "Laporan keuangan owner berhasil disimpan ke 'laporan_keuangan_owner.txt'!");
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Gagal menyimpan file laporan: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void onFilter(ActionEvent event) {
        showInfo("Filter Laporan", "Fitur filter akan menyaring riwayat berdasarkan rentang tanggal atau metode bayar tertentu.");
    }

    @FXML
    void onPilihBulan(ActionEvent event) {
        showInfo("Pilih Bulan", "Fitur pemilihan periode bulan akan tersedia di sini.");
    }

    @FXML
    void onKeluar(ActionEvent event) {
        SceneNavigator.loadScene(event, "/siptek/kantinemama/view/pelanggan/HalamanAwalUtama.fxml");
    }

    private void showInfo(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
