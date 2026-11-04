package controller;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;

/**
 * Utility validasi input untuk controller dan GUI Form.
 * Mengikuti pola InputValidator pada Minpro-4.
 */
public class InputValidator {

    public static boolean isValidString(String str) {
        return str != null && !str.trim().isEmpty();
    }

    public static boolean isPositiveNumber(String str) {
        if (!isValidString(str)) return false;
        try {
            double val = Double.parseDouble(str.trim());
            return Double.isFinite(val) && val > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isNonNegativeNumber(String str) {
        if (!isValidString(str)) return false;
        try {
            double val = Double.parseDouble(str.trim());
            return Double.isFinite(val) && val >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidInteger(String str) {
        if (!isValidString(str)) return false;
        try {
            Integer.parseInt(str.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidDate(String dateStr) {
        if (!isValidString(dateStr)) return false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        String value = dateStr.trim();
        ParsePosition position = new ParsePosition(0);
        return sdf.parse(value, position) != null && position.getIndex() == value.length();
    }

    public static boolean isValidDateRange(String startDate, String endDate) {
        return isValidDate(startDate) && isValidDate(endDate)
                && startDate.trim().compareTo(endDate.trim()) <= 0;
    }

    public static boolean isValidOptionalDateRange(String startDate, String endDate) {
        String start = startDate == null ? "" : startDate.trim();
        String end = endDate == null ? "" : endDate.trim();
        if (!start.isEmpty() && !isValidDate(start)) return false;
        if (!end.isEmpty() && !isValidDate(end)) return false;
        return start.isEmpty() || end.isEmpty() || start.compareTo(end) <= 0;
    }

    public static double parseDoubleSafe(String str, double defaultVal) {
        try {
            return Double.parseDouble(str.trim());
        } catch (Exception e) {
            return defaultVal;
        }
    }

    public static int parseIntSafe(String str, int defaultVal) {
        try {
            return Integer.parseInt(str.trim());
        } catch (Exception e) {
            return defaultVal;
        }
    }

    /**
     * Memvalidasi format kode blok lahan (3-10 karakter alfanumerik, tanda hubung '-', atau garis bawah '_').
     * Tidak memperbolehkan spasi sembarangan (misal: "SW 01" tidak valid, harus "SW01" atau "SW-01").
     *
     * @param kode Kode blok yang divalidasi
     * @return true jika sesuai format baku
     */
    public static boolean isValidKodeBlok(String kode) {
        if (!isValidString(kode)) return false;
        return kode.trim().matches("^[A-Za-z0-9_-]{3,10}$");
    }

    /**
     * Memvalidasi kode blok lahan menggunakan exception (try-catch pattern).
     *
     * @param kode Kode blok yang diuji
     * @throws IllegalArgumentException jika kode kosong atau format tidak sesuai
     */
    public static void validateKodeBlok(String kode) throws IllegalArgumentException {
        if (!isValidString(kode)) {
            throw new IllegalArgumentException("Kode blok lahan wajib diisi!");
        }
        String clean = kode.trim();
        if (!clean.matches("^[A-Za-z0-9_-]{3,10}$")) {
            throw new IllegalArgumentException("Format kode blok tidak valid! Gunakan 3-10 karakter (huruf, angka, '-', '_', tanpa spasi). Contoh: SW001 atau KR001.");
        }
    }
}

