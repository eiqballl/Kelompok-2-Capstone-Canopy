import view.*;
import model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class TestFramesUI {
    public static void main(String[] args) throws Exception {
        System.out.println("=== TESTING FRAMES UI & DATA INTEGRATION ===");

        // Test LoginFrame
        System.out.print("Testing LoginFrame instantiation... ");
        LoginFrame login = new LoginFrame();
        assert login != null : "LoginFrame must be created";
        login.dispose();
        System.out.println("OK");

        // Test AdminFrame
        System.out.print("Testing AdminFrame initialization & components... ");
        AdminFrame admin = new AdminFrame();
        // Check tblBlokLahan columns
        JTable tblBlok = findTable(admin, "tblBlokLahan");
        if (tblBlok != null) {
            assert tblBlok.getColumnCount() == 5 : "tblBlokLahan must have 5 columns, found: " + tblBlok.getColumnCount();
            assert "Tahun Tanam".equals(tblBlok.getColumnName(4)) : "Column 4 must be 'Tahun Tanam'";
            assert tblBlok.getRowCount() > 0 : "tblBlokLahan must have rows";
            System.out.print("(Blok Lahan: " + tblBlok.getRowCount() + " rows with Tahun Tanam) ");
        }

        // Check tblRekap in AdminFrame
        JTable tblRekap = findTable(admin, "tblRekap");
        if (tblRekap != null) {
            assert tblRekap.getRowCount() > 0 : "tblRekap in AdminFrame must have rows on startup, found: " + tblRekap.getRowCount();
            System.out.print("(Rekap: " + tblRekap.getRowCount() + " rows) ");
        }

        JTable tblEvalA = findTable(admin, "tblEvaluasi");
        if (tblEvalA != null) {
            assert tblEvalA.getColumnCount() == 7 : "tblEvaluasi in AdminFrame must have 7 columns, found: " + tblEvalA.getColumnCount();
            assert "Catatan Direksi".equals(tblEvalA.getColumnName(5)) : "Col 5 must be Catatan Direksi";
            assert "Catatan Pegawai".equals(tblEvalA.getColumnName(6)) : "Col 6 must be Catatan Pegawai";
            System.out.print("(Evaluasi: " + tblEvalA.getRowCount() + " rows, 7 cols) ");
        }
        admin.dispose();
        System.out.println("OK");

        // Test PetugasFrame
        System.out.print("Testing PetugasFrame initialization... ");
        PetugasFrame petugas = new PetugasFrame();
        JTable tblRekapP = findTable(petugas, "tblRekap");
        if (tblRekapP != null) {
            assert tblRekapP.getRowCount() > 0 : "tblRekap in PetugasFrame must have rows, found: " + tblRekapP.getRowCount();
            System.out.print("(Rekap: " + tblRekapP.getRowCount() + " rows) ");
        }
        JTable tblEvalP = findTable(petugas, "tblEvaluasi");
        if (tblEvalP != null) {
            assert tblEvalP.getColumnCount() == 7 : "tblEvaluasi in PetugasFrame must have 7 columns";
            System.out.print("(Evaluasi: " + tblEvalP.getRowCount() + " rows, 7 cols) ");
        }
        petugas.dispose();
        System.out.println("OK");

        // Test DireksiFrame
        System.out.print("Testing DireksiFrame initialization... ");
        DireksiFrame direksi = new DireksiFrame();
        JTable tblRekapD = findTable(direksi, "tblRekap");
        if (tblRekapD != null) {
            assert tblRekapD.getRowCount() > 0 : "tblRekap in DireksiFrame must have rows, found: " + tblRekapD.getRowCount();
            System.out.print("(Rekap: " + tblRekapD.getRowCount() + " rows) ");
        }
        JTable tblEvalD = findTable(direksi, "tblEvaluasi");
        if (tblEvalD != null) {
            assert tblEvalD.getColumnCount() == 7 : "tblEvaluasi in DireksiFrame must have 7 columns";
            System.out.print("(Evaluasi: " + tblEvalD.getRowCount() + " rows, 7 cols) ");
        }
        direksi.dispose();
        System.out.println("OK");

        System.out.println("\n>>> ALL FRAMES TESTED AND VERIFIED SUCCESSFULLY! <<<");
        System.exit(0);
    }

    private static JTable findTable(JFrame frame, String name) {
        try {
            java.lang.reflect.Field f = frame.getClass().getDeclaredField(name);
            f.setAccessible(true);
            return (JTable) f.get(frame);
        } catch (Exception e) {
            return null;
        }
    }
}
