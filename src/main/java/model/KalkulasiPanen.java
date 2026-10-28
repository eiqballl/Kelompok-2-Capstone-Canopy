package model;

/**
 * Interface untuk standarisasi kalkulasi mutu dan persentase panen.
 * Mengimplementasikan prinsip Abstraction & Interface pada PBO (seperti DapatDidiskon pada Minpro-4).
 */
public interface KalkulasiPanen {
    double hitungPersentaseTidakLayak();
    boolean isMemenuhiStandar();
}

