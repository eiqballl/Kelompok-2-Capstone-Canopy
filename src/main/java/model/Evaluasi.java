package model;

public class Evaluasi {
    private String idEval;
    private String tglAwal;
    private String tglAkhir;
    private double totalPanen;
    private double persenTidakLayak;
    private String status;
    private String catatan;

    public Evaluasi(String idEval, String tglAwal, String tglAkhir, double totalPanen,
                    double persenTidakLayak, String status, String catatan) {
        this.idEval = idEval;
        this.tglAwal = tglAwal;
        this.tglAkhir = tglAkhir;
        this.totalPanen = totalPanen;
        this.persenTidakLayak = persenTidakLayak;
        this.status = status;
        this.catatan = catatan;
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

    public String getCatatan() { return catatan; }
    public void setCatatan(String catatan) { this.catatan = catatan; }
}
