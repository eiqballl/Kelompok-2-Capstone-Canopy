package model;

public class Panen implements KalkulasiPanen {
    private String idPanen;
    private String tanggal;
    private String blokLahan;
    private String komoditas;
    private double input1;
    private double input2;
    private double totalPanen;
    private double tidakLayak;
    private String kualitas;
    private String tahunTanam;

    public Panen(String idPanen, String tanggal, String blokLahan, String komoditas,
                 double input1, double input2, double totalPanen, double tidakLayak, String kualitas, String tahunTanam) {
        this.idPanen = idPanen;
        this.tanggal = tanggal;
        this.blokLahan = blokLahan;
        this.komoditas = komoditas;
        this.input1 = input1;
        this.input2 = input2;
        this.totalPanen = totalPanen;
        this.tidakLayak = tidakLayak;
        this.kualitas = kualitas;
        this.tahunTanam = (tahunTanam == null || tahunTanam.trim().isEmpty()) ? "2018" : tahunTanam.trim();
    }

    public Panen(String idPanen, String tanggal, String blokLahan, String komoditas,
                 double input1, double input2, double totalPanen, double tidakLayak, String kualitas) {
        this(idPanen, tanggal, blokLahan, komoditas, input1, input2, totalPanen, tidakLayak, kualitas, "2018");
    }

    public String getIdPanen() { return idPanen; }
    public void setIdPanen(String idPanen) { this.idPanen = idPanen; }

    public String getTanggal() { return tanggal; }
    public void setTanggal(String tanggal) { this.tanggal = tanggal; }

    public String getBlokLahan() { return blokLahan; }
    public void setBlokLahan(String blokLahan) { this.blokLahan = blokLahan; }

    public String getKodeBlok() {
        if (blokLahan == null) return "";
        int idx = blokLahan.indexOf(" - ");
        return idx > 0 ? blokLahan.substring(0, idx).trim() : blokLahan.trim();
    }

    public String getKomoditas() { return komoditas; }
    public void setKomoditas(String komoditas) { this.komoditas = komoditas; }

    public double getInput1() { return input1; }
    public void setInput1(double input1) { this.input1 = input1; }

    public double getInput2() { return input2; }
    public void setInput2(double input2) { this.input2 = input2; }

    public double getTotalPanen() { return totalPanen; }
    public void setTotalPanen(double totalPanen) { this.totalPanen = totalPanen; }

    public double getTidakLayak() { return tidakLayak; }
    public void setTidakLayak(double tidakLayak) { this.tidakLayak = tidakLayak; }

    public String getKualitas() { return kualitas; }
    public void setKualitas(String kualitas) { this.kualitas = kualitas; }

    public String getTahunTanam() { return tahunTanam; }
    public void setTahunTanam(String tahunTanam) { this.tahunTanam = tahunTanam; }

    @Override
    public double hitungPersentaseTidakLayak() {
        if (totalPanen <= 0) return 0.0;
        return (tidakLayak / totalPanen) * 100.0;
    }

    @Override
    public boolean isMemenuhiStandar() {
        return hitungPersentaseTidakLayak() <= 5.0;
    }

    @Override
    public String tentukanKualitas() {
        return tentukanKualitas(5.0, 10.0);
    }

    @Override
    public String tentukanKualitas(double ambangBaik, double ambangCukup) {
        double persen = hitungPersentaseTidakLayak();
        if (persen <= ambangBaik) return "Baik";
        if (persen <= ambangCukup) return "Cukup";
        return "Kurang";
    }

    public static double hitungTotalSawit(double beratTandanKg, double beratBrondolanKg) {
        return beratTandanKg + beratBrondolanKg;
    }

    public static double hitungTotalSawit(int jumlahJanjang, double bjrKg, double beratBrondolanKg) {
        return (jumlahJanjang * bjrKg) + beratBrondolanKg;
    }

    public static double hitungKaretBersih(double beratKotorKg, double kadarAirPersen) {
        double kkk = Math.max(0.0, 100.0 - kadarAirPersen);
        return beratKotorKg * (kkk / 100.0);
    }
}
