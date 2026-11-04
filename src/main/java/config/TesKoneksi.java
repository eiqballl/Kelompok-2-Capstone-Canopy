package config;

import java.sql.Connection;
import javax.swing.JOptionPane;

public class TesKoneksi {
    public static void main(String[] args) {
        Connection c = Koneksi.getKoneksi();
        if (c != null) {
            System.out.println("Koneksi ke database canopy Berhasil!");
            if (!java.awt.GraphicsEnvironment.isHeadless()) {
                JOptionPane.showMessageDialog(null, "Koneksi ke database canopy Berhasil!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            String err = Koneksi.getLastError();
            String msg = "Koneksi ke database canopy Gagal!" + (err != null ? "\nDetail: " + err : "");
            System.err.println(msg);
            if (!java.awt.GraphicsEnvironment.isHeadless()) {
                JOptionPane.showMessageDialog(null, msg, "Gagal", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
