import config.Koneksi;
import model.DataStore;
import model.Pegawai;
import view.*;

public class DebugLogin {
    public static void main(String[] args) {
        try {
            System.out.println("Testing findPegawai...");
            Pegawai p1 = DataStore.getInstance().findPegawai("ahmad.admin", "admin123");
            System.out.println("ahmad.admin: " + p1);
            Pegawai p2 = DataStore.getInstance().findPegawai("andi.petugas", "petugas123");
            System.out.println("andi.petugas: " + p2);
            Pegawai p3 = DataStore.getInstance().findPegawai("budi.direksi", "direksi123");
            System.out.println("budi.direksi: " + p3);

            System.out.println("Testing PetugasFrame instantiation...");
            PetugasFrame pf = new PetugasFrame();
            System.out.println("PetugasFrame created: " + pf);

            System.out.println("Testing AdminFrame instantiation...");
            AdminFrame af = new AdminFrame();
            System.out.println("AdminFrame created: " + af);

            System.out.println("Testing DireksiFrame instantiation...");
            DireksiFrame df = new DireksiFrame();
            System.out.println("DireksiFrame created: " + df);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
