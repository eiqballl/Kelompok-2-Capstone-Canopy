import subprocess

# Run java to inspect DataStore.getInstance().getPanenList()
test_java = """
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
"""

with open("scratch/TestRekapDates.java", "w", encoding="utf-8") as f:
    f.write(test_java)

cmd = '& "C:\\Users\\Fareru\\java\\jdk-17.0.20.1+1\\bin\\javac.exe" --release 8 -cp "lib/*;target/classes" -d target/classes scratch/TestRekapDates.java; java -cp "lib/*;target/classes" TestRekapDates'
res = subprocess.run(["powershell", "-Command", cmd], capture_output=True, text=True)
print(res.stdout)
if res.stderr:
    print("ERR:", res.stderr)

