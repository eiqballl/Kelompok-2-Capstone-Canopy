package model;

public class Evaluasi {
    private String idEval;
    private String tglAwal;
    private String tglAkhir;
    private double totalPanen;
    private double persenTidakLayak;
    private String status;
    private String catatanTindakLanjut;
    private String catatanEvaluasi;
    private String catatanPetugas;

    public Evaluasi(String idEval, String tglAwal, String tglAkhir, double totalPanen,
                    double persenTidakLayak, String status, String catatanTindakLanjut,
                    String catatanEvaluasi, String catatanPetugas) {
        this.idEval = idEval;
        this.tglAwal = tglAwal;
        this.tglAkhir = tglAkhir;
        this.totalPanen = totalPanen;
        this.persenTidakLayak = persenTidakLayak;
        this.status = status;
        this.catatanTindakLanjut = (catatanTindakLanjut == null || catatanTindakLanjut.trim().isEmpty()) ? "-" : catatanTindakLanjut.trim();
        this.catatanEvaluasi = (catatanEvaluasi == null || catatanEvaluasi.trim().isEmpty()) ? "-" : catatanEvaluasi.trim();
        this.catatanPetugas = (catatanPetugas == null || catatanPetugas.trim().isEmpty()) ? "-" : catatanPetugas.trim();
    }

    public Evaluasi(String idEval, String tglAwal, String tglAkhir, double totalPanen,
                    double persenTidakLayak, String status, String catatanEvaluasi, String catatanPetugas) {
        this(idEval, tglAwal, tglAkhir, totalPanen, persenTidakLayak, status, "-", catatanEvaluasi, catatanPetugas);
    }

    public Evaluasi(String idEval, String tglAwal, String tglAkhir, double totalPanen,
                    double persenTidakLayak, String status, String catatan) {
        this(idEval, tglAwal, tglAkhir, totalPanen, persenTidakLayak, status, "-", catatan, "-");
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

    public String getCatatanTindakLanjut() { return catatanTindakLanjut; }
    public void setCatatanTindakLanjut(String catatanTindakLanjut) {
        this.catatanTindakLanjut = (catatanTindakLanjut == null || catatanTindakLanjut.trim().isEmpty()) ? "-" : catatanTindakLanjut.trim();
    }

    public String getCatatanEvaluasi() { return catatanEvaluasi; }
    public void setCatatanEvaluasi(String catatanEvaluasi) {
        this.catatanEvaluasi = (catatanEvaluasi == null || catatanEvaluasi.trim().isEmpty()) ? "-" : catatanEvaluasi.trim();
    }

    public String getCatatanPetugas() { return catatanPetugas; }
    public void setCatatanPetugas(String catatanPetugas) {
        this.catatanPetugas = (catatanPetugas == null || catatanPetugas.trim().isEmpty()) ? "-" : catatanPetugas.trim();
    }

    // Aliases for backward compatibility
    public String getCatatanDireksi() { return getCatatanEvaluasi(); }
    public void setCatatanDireksi(String catatanDireksi) { setCatatanEvaluasi(catatanDireksi); }

    public String getCatatanPegawai() { return getCatatanPetugas(); }
    public void setCatatanPegawai(String catatanPegawai) { setCatatanPetugas(catatanPegawai); }

    public String getCatatan() {
        if (catatanEvaluasi != null && !"-".equals(catatanEvaluasi)) return catatanEvaluasi;
        if (catatanTindakLanjut != null && !"-".equals(catatanTindakLanjut)) return catatanTindakLanjut;
        return catatanPetugas;
    }
    public void setCatatan(String catatan) { setCatatanEvaluasi(catatan); }
}
