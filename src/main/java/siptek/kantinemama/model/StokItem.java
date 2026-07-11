package siptek.kantinemama.model;

public class StokItem {
    private String idBahan;
    private String nama;
    private String kategori;
    private int sisaPorsi;
    private int batasMinimum;

    public StokItem(String idBahan, String nama, String kategori, int sisaPorsi, int batasMinimum) {
        this.idBahan = idBahan;
        this.nama = nama;
        this.kategori = kategori;
        this.sisaPorsi = sisaPorsi;
        this.batasMinimum = batasMinimum;
    }

    public String getIdBahan() {
        return idBahan;
    }

    public void setIdBahan(String idBahan) {
        this.idBahan = idBahan;
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

    public int getSisaPorsi() {
        return sisaPorsi;
    }

    public void setSisaPorsi(int sisaPorsi) {
        this.sisaPorsi = sisaPorsi;
    }

    public int getBatasMinimum() {
        return batasMinimum;
    }

    public void setBatasMinimum(int batasMinimum) {
        this.batasMinimum = batasMinimum;
    }

    public String getStatus() {
        if (sisaPorsi <= 0) {
            return "HABIS";
        } else if (sisaPorsi <= batasMinimum) {
            return "HAMPIR HABIS";
        } else {
            return "STOK AMAN";
        }
    }
}
