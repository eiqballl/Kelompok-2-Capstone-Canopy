package model;

public class Riwayat {
    private String idRiwayat;
    private String waktuPerubahan;
    private String jenisTindakan;
    private String userId;
    private String idPanen;
    private String dataSebelum;
    private String dataSetelah;

    public Riwayat(String idRiwayat, String waktuPerubahan, String jenisTindakan,
                   String userId, String idPanen, String dataSebelum, String dataSetelah) {
        this.idRiwayat = idRiwayat;
        this.waktuPerubahan = waktuPerubahan;
        this.jenisTindakan = jenisTindakan;
        this.userId = userId;
        this.idPanen = idPanen;
        this.dataSebelum = dataSebelum;
        this.dataSetelah = dataSetelah;
    }

    public String getIdRiwayat() { return idRiwayat; }
    public void setIdRiwayat(String idRiwayat) { this.idRiwayat = idRiwayat; }

    public String getWaktuPerubahan() { return waktuPerubahan; }
    public void setWaktuPerubahan(String waktuPerubahan) { this.waktuPerubahan = waktuPerubahan; }

    public String getJenisTindakan() { return jenisTindakan; }
    public void setJenisTindakan(String jenisTindakan) { this.jenisTindakan = jenisTindakan; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getIdPanen() { return idPanen; }
    public void setIdPanen(String idPanen) { this.idPanen = idPanen; }

    public String getDataSebelum() { return dataSebelum; }
    public void setDataSebelum(String dataSebelum) { this.dataSebelum = dataSebelum; }

    public String getDataSetelah() { return dataSetelah; }
    public void setDataSetelah(String dataSetelah) { this.dataSetelah = dataSetelah; }
}
