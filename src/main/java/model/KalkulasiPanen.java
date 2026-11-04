package model;

public interface KalkulasiPanen {

    double hitungPersentaseTidakLayak();

    boolean isMemenuhiStandar();

    String tentukanKualitas();

    String tentukanKualitas(double ambangBaik, double ambangCukup);
}
