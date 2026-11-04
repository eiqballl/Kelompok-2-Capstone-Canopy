package model;

/**
 * Interface untuk standarisasi kalkulasi mutu dan persentase panen (Sawit & Karet).
 * Mengimplementasikan prinsip Abstraction & Interface pada PBO.
 *
 * Method dalam interface ini menyediakan hierarki kalkulasi mutu:
 * 1. hitungPersentaseTidakLayak(): Menghitung persentase kuantitatif reject terhadap total panen kotor.
 * 2. isMemenuhiStandar(): Pengecekan cepat lolos mutu standar (<= 5%).
 * 3. tentukanKualitas(): Klasifikasi 3-tier standar perusahaan (Baik, Cukup, Kurang).
 * 4. tentukanKualitas(ambangBaik, ambangCukup): Overload klasifikasi dengan ambang batas kustom.
 */
public interface KalkulasiPanen {
    /**
     * Menghitung persentase hasil panen yang tidak layak/reject terhadap total panen kotor.
     * Formula: (tidakLayak / totalPanen) * 100%
     *
     * @return Persentase tidak layak (0.0 - 100.0)
     */
    double hitungPersentaseTidakLayak();

    /**
     * Memeriksa apakah kualitas panen memenuhi standar mutu perusahaan (<= 5%).
     *
     * @return true jika persentase tidak layak <= 5.0%
     */
    boolean isMemenuhiStandar();

    /**
     * Menentukan klasifikasi mutu panen 3-tier default (<= 5% Baik, <= 10% Cukup, > 10% Kurang).
     *
     * @return Label mutu: "Baik", "Cukup", atau "Kurang"
     */
    String tentukanKualitas();

    /**
     * Method Overloading: Menentukan mutu panen berdasarkan ambang batas kustom.
     *
     * @param ambangBaik Batas maksimal persentase tidak layak untuk mutu Baik
     * @param ambangCukup Batas maksimal persentase tidak layak untuk mutu Cukup
     * @return Label mutu: "Baik", "Cukup", atau "Kurang"
     */
    String tentukanKualitas(double ambangBaik, double ambangCukup);
}
