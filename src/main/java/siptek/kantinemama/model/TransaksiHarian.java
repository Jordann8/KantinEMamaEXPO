package siptek.kantinemama.model;

public class TransaksiHarian {
    private String tanggal;
    private String metodeBayar;
    private int volume;
    private double pendapatanKotor;
    private double pendapatanBersih;
    private String status;

    public TransaksiHarian(String tanggal, String metodeBayar, int volume, double pendapatanKotor, double pendapatanBersih, String status) {
        this.tanggal = tanggal;
        this.metodeBayar = metodeBayar;
        this.volume = volume;
        this.pendapatanKotor = pendapatanKotor;
        this.pendapatanBersih = pendapatanBersih;
        this.status = status;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getMetodeBayar() {
        return metodeBayar;
    }

    public void setMetodeBayar(String metodeBayar) {
        this.metodeBayar = metodeBayar;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public double getPendapatanKotor() {
        return pendapatanKotor;
    }

    public void setPendapatanKotor(double pendapatanKotor) {
        this.pendapatanKotor = pendapatanKotor;
    }

    public double getPendapatanBersih() {
        return pendapatanBersih;
    }

    public void setPendapatanBersih(double pendapatanBersih) {
        this.pendapatanBersih = pendapatanBersih;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
