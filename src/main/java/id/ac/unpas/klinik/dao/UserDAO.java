package id.ac.unpas.klinik.dao;

import id.ac.unpas.klinik.model.User;
import id.ac.unpas.klinik.util.KoneksiDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    // Method untuk cek login
    public User login(String username, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        
        try (Connection conn = KoneksiDB.configDB();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setString(1, username);
            pst.setString(2, password); // Di aplikasi nyata, password input di-hash dulu di sini
            
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setFullname(rs.getString("fullname"));
                    user.setRole(rs.getString("role"));
                    return user; // Login Sukses, kembalikan objek User
                }
            }
        } catch (SQLException e) {
            System.err.println("Error login: " + e.getMessage());
            throw e;
        }
        return null; // Login Gagal (user tidak ditemukan / password salah)
    }
}