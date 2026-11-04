
import model.*;
import java.util.List;

public class TestRekapDates {
    public static void main(String[] args) {
        DataStore ds = DataStore.getInstance();
        List<Panen> list = ds.getPanenList();
        System.out.println("Total panen di DataStore: " + list.size());
        for (int i = 0; i < Math.min(5, list.size()); i++) {
            Panen p = list.get(i);
            System.out.println("  - " + p.getIdPanen() + " | Tgl: " + p.getTanggal() + " | Blok: " + p.getBlokLahan() + " | Total: " + p.getTotalPanen());
        }
        
        // Cek jika tglAwal = 2026-11-01
        String tglAwal = "2026-11-01";
        int matchCount = 0;
        for (Panen p : list) {
            if (p.getTanggal().compareTo(tglAwal) >= 0) {
                matchCount++;
            }
        }
        System.out.println("Data yang cocok jika tglAwal = 2026-11-01: " + matchCount);
    }
}
