package model;

public class Evaluasi {
    private String idEval;
    private String tglAwal;
    private String tglAkhir;
    private double totalPanen;
    private double persenTidakLayak;
    private String status;
    private String catatanDireksi;
    private String catatanPegawai;

    public Evaluasi(String idEval, String tglAwal, String tglAkhir, double totalPanen,
                    double persenTidakLayak, String status, String catatanDireksi, String catatanPegawai) {
        this.idEval = idEval;
        this.tglAwal = tglAwal;
        this.tglAkhir = tglAkhir;
        this.totalPanen = totalPanen;
        this.persenTidakLayak = persenTidakLayak;
        this.status = status;
        this.catatanDireksi = (catatanDireksi == null || catatanDireksi.trim().isEmpty()) ? "-" : catatanDireksi.trim();
        this.catatanPegawai = (catatanPegawai == null || catatanPegawai.trim().isEmpty()) ? "-" : catatanPegawai.trim();
    }

    public Evaluasi(String idEval, String tglAwal, String tglAkhir, double totalPanen,
                    double persenTidakLayak, String status, String catatan) {
        this(idEval, tglAwal, tglAkhir, totalPanen, persenTidakLayak, status, catatan, "-");
    }

    public double getRataRataPerBulan() {
        int months = DataStore.countCalendarMonths(tglAwal, tglAkhir);
        return months > 0 ? (totalPanen / months) : totalPanen;
    }

    public String getIdEval() { return idEval; }
    public void setIdEval(String idEval) { this.idEval = idEval; }

    public String getTglAwal() { return tglAwal; }
    public void setTglAwal(String tglAwal) { this.tglAwal = tglAwal; }

    public String getTglAkhir() { return tglAkhir; }
    public void setTglAkhir(String tglAkhir) { this.tglAkhir = tglAkhir; }

    public double getTotalPanen() { return totalPanen; }
    public void setTotalPanen(double totalPanen) { this.totalPanen = totalPanen; }

    public double getPersenTidakLayak() { return persenTidakLayak; }
    public void setPersenTidakLayak(double persenTidakLayak) { this.persenTidakLayak = persenTidakLayak; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCatatanDireksi() { return catatanDireksi; }
    public void setCatatanDireksi(String catatanDireksi) {
        this.catatanDireksi = (catatanDireksi == null || catatanDireksi.trim().isEmpty()) ? "-" : catatanDireksi.trim();
    }

    public String getCatatanPegawai() { return catatanPegawai; }
    public void setCatatanPegawai(String catatanPegawai) {
        this.catatanPegawai = (catatanPegawai == null || catatanPegawai.trim().isEmpty()) ? "-" : catatanPegawai.trim();
    }

    // Alias compatibility
    public String getCatatan() { return catatanDireksi != null && !"-".equals(catatanDireksi) ? catatanDireksi : catatanPegawai; }
    public void setCatatan(String catatan) { setCatatanDireksi(catatan); }
}
