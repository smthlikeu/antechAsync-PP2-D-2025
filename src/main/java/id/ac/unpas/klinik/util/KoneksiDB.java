package id.ac.unpas.klinik.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class KoneksiDB {
    private static Connection mysqlconfig;

    public static Connection configDB() throws SQLException {
        try {
            String url = "jdbc:mysql://localhost:3306/db_klinik"; // url database
            String user = "root"; // user database
            String pass = ""; // password database (kosongkan jika default xampp)

            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            mysqlconfig = DriverManager.getConnection(url, user, pass);
        } catch (SQLException e) {
            System.err.println("Koneksi gagal " + e.getMessage());
        }
        return mysqlconfig;
    }
}