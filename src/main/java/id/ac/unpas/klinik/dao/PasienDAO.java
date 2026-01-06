/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package id.ac.unpas.klinik.dao;

import id.ac.unpas.klinik.model.Pasien;
import id.ac.unpas.klinik.util.KoneksiDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object untuk Pasien BERTANGGUNG JAWAB: Database operations ONLY
 *
 * CRITICAL PRINCIPLES: 1. ALWAYS use try-with-resources untuk auto-close
 * connections 2. ALWAYS use PreparedStatement untuk prevent SQL injection 3.
 * NEVER expose SQLException details ke upper layers 4. Keep methods focused and
 * single-purpose
 */
public class PasienDAO {

    /**
     * Retrieve all pasien from database
     *
     * @return List of all Pasien objects
     * @throws SQLException if database error occurs
     */
    public List<Pasien> getAll() throws SQLException {
        List<Pasien> list = new ArrayList<>();
        String sql = "SELECT * FROM pasien ORDER BY nama ASC";

        try (Connection conn = KoneksiDB.configDB(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(mapResultSetToPasien(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getAll Pasien: " + e.getMessage());
            throw e;
        }
        return list;
    }

    /**
     * Retrieve pasien by ID
     *
     * @param id Primary key
     * @return Pasien object or null if not found
     * @throws SQLException if database error occurs
     */
    public Pasien getById(int id) throws SQLException {
        String sql = "SELECT * FROM pasien WHERE id = ?";

        try (Connection conn = KoneksiDB.configDB(); PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, id);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPasien(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getById Pasien: " + e.getMessage());
            throw e;
        }
        return null;
    }

    /**
     * Search pasien by name (partial match)
     *
     * @param keyword Search keyword
     * @return List of matching Pasien
     * @throws SQLException if database error occurs
     */
    public List<Pasien> searchByName(String keyword) throws SQLException {
        List<Pasien> list = new ArrayList<>();
        String sql = "SELECT * FROM pasien WHERE nama LIKE ? ORDER BY nama ASC";

        try (Connection conn = KoneksiDB.configDB(); PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, "%" + keyword + "%");

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToPasien(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error searchByName Pasien: " + e.getMessage());
            throw e;
        }
        return list;
    }

    /**
     * Find pasien by NIK (unique identifier)
     *
     * @param nik NIK to search
     * @return Pasien object or null if not found
     * @throws SQLException if database error occurs
     */
    public Pasien getByNIK(String nik) throws SQLException {
        String sql = "SELECT * FROM pasien WHERE nik = ?";

        try (Connection conn = KoneksiDB.configDB(); PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, nik);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPasien(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getByNIK: " + e.getMessage());
            throw e;
        }
        return null;
    }

    /**
     * Check if NIK already exists (untuk prevent duplicate)
     *
     * @param nik NIK to check
     * @param excludeId ID to exclude (for update operation)
     * @return true if NIK exists
     * @throws SQLException if database error occurs
     */
    public boolean isNIKExists(String nik, int excludeId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM pasien WHERE nik = ? AND id != ?";

        try (Connection conn = KoneksiDB.configDB(); PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, nik);
            pst.setInt(2, excludeId);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking NIK exists: " + e.getMessage());
            throw e;
        }
        return false;
    }

    /**
     * Insert new pasien
     *
     * @param pasien Pasien object to insert
     * @return Generated ID
     * @throws SQLException if database error occurs
     */
    public int insert(Pasien pasien) throws SQLException {
        String sql = "INSERT INTO pasien (nama, nik, alamat, telepon) VALUES (?, ?, ?, ?)";

        try (Connection conn = KoneksiDB.configDB(); PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pst.setString(1, pasien.getNama());
            pst.setString(2, pasien.getNik());
            pst.setString(3, pasien.getAlamat());
            pst.setString(4, pasien.getTelepon());

            int affectedRows = pst.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Insert pasien failed, no rows affected.");
            }

            // Get generated ID
            try (ResultSet generatedKeys = pst.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Insert pasien failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error insert Pasien: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Update existing pasien
     *
     * @param pasien Pasien object with updated data
     * @return Number of rows affected
     * @throws SQLException if database error occurs
     */
    public int update(Pasien pasien) throws SQLException {
        String sql = "UPDATE pasien SET nama=?, nik=?, alamat=?, telepon=? WHERE id=?";

        try (Connection conn = KoneksiDB.configDB(); PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, pasien.getNama());
            pst.setString(2, pasien.getNik());
            pst.setString(3, pasien.getAlamat());
            pst.setString(4, pasien.getTelepon());
            pst.setInt(5, pasien.getId());

            return pst.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error update Pasien: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Delete pasien by ID
     *
     * @param id ID to delete
     * @return Number of rows affected
     * @throws SQLException if database error occurs
     */
    public int delete(int id) throws SQLException {
        String sql = "DELETE FROM pasien WHERE id=?";

        try (Connection conn = KoneksiDB.configDB(); PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, id);
            return pst.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error delete Pasien: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Get total count of pasien
     *
     * @return Total number of records
     * @throws SQLException if database error occurs
     */
    public int getCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM pasien";

        try (Connection conn = KoneksiDB.configDB(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getCount: " + e.getMessage());
            throw e;
        }
        return 0;
    }

    /**
     * Helper method: Map ResultSet to Pasien object DRY principle - avoid code
     * duplication
     */
    private Pasien mapResultSetToPasien(ResultSet rs) throws SQLException {
        Pasien p = new Pasien();
        p.setId(rs.getInt("id"));
        p.setNama(rs.getString("nama"));
        p.setNik(rs.getString("nik"));
        p.setAlamat(rs.getString("alamat"));
        p.setTelepon(rs.getString("telepon"));
        return p;
    }
}
