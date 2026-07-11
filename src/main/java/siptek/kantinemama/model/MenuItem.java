package siptek.kantinemama.model;

public class MenuItem {
    private String id;
    private String nama;
    private String kategori; // Makanan, Minuman, Camilan
    private double harga;
    private int sisaPorsi;
    private boolean aktif;
    private String gambarPath;

    public MenuItem(String id, String nama, String kategori, double harga, int sisaPorsi, boolean aktif, String gambarPath) {
        this.id = id;
        this.nama = nama;
        this.kategori = kategori;
        this.harga = harga;
        this.sisaPorsi = sisaPorsi;
        this.aktif = aktif;
        this.gambarPath = gambarPath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public double getHarga() {
        return harga;
    }

    public void setHarga(double harga) {
        this.harga = harga;
    }

    public int getSisaPorsi() {
        return sisaPorsi;
    }

    public void setSisaPorsi(int sisaPorsi) {
        this.sisaPorsi = sisaPorsi;
    }

    public boolean isAktif() {
        return aktif;
    }

    public void setAktif(boolean aktif) {
        this.aktif = aktif;
    }

    public String getGambarPath() {
        return gambarPath;
    }

    public void setGambarPath(String gambarPath) {
        this.gambarPath = gambarPath;
    }
}
