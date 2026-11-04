import config.Koneksi;
import java.sql.*;

public class CheckDB {
    public static void main(String[] args) throws Exception {
        Connection c = Koneksi.getKoneksi();
        System.out.println("Catalog (Current DB): " + c.getCatalog());

        Statement st = c.createStatement();
        ResultSet rs = st.executeQuery("SHOW DATABASES");
        System.out.println("Available Databases:");
        while (rs.next()) {
            System.out.println(" - " + rs.getString(1));
        }
        rs.close();

        System.out.println("\nUsers in current DB (" + c.getCatalog() + "):");
        ResultSet rsUsers = st.executeQuery("SELECT id_user, nama, username, password FROM user");
        while (rsUsers.next()) {
            System.out.println("   " + rsUsers.getString("id_user") + " | " + rsUsers.getString("nama") + " | username: '" + rsUsers.getString("username") + "' | password: '" + rsUsers.getString("password") + "'");
        }
        rsUsers.close();
    }
}

