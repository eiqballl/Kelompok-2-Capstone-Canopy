package controller;

import java.text.ParseException;
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
            return val > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isNonNegativeNumber(String str) {
        if (!isValidString(str)) return false;
        try {
            double val = Double.parseDouble(str.trim());
            return val >= 0;
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
        try {
            sdf.parse(dateStr.trim());
            return true;
        } catch (ParseException e) {
            return false;
        }
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
}

