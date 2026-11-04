import config.Koneksi;
import java.sql.*;

public class CheckPassHex {
    public static void main(String[] args) throws Exception {
        Connection c = Koneksi.getKoneksi();
        Statement st = c.createStatement();
        ResultSet rs = st.executeQuery("SELECT id_user, username, password, LENGTH(password), HEX(password) FROM user");
        while (rs.next()) {
            System.out.println(rs.getString(1) + " | " + rs.getString(2) + " | pass: [" + rs.getString(3) + "] len: " + rs.getInt(4) + " hex: " + rs.getString(5));
        }
    }
}

