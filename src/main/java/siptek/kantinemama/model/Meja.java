package siptek.kantinemama.model;

public class Meja {
    private String kode; // e.g. "mejaMO1"
    private String status; // KOSONG, TERISI, TERPILIH

    public Meja(String kode, String status) {
        this.kode = kode;
        this.status = status;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
