package id.ac.unpas.klinik.dao;

import id.ac.unpas.klinik.model.RekamMedis;
import id.ac.unpas.klinik.util.KoneksiDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RekamMedisDAO {

    public List<RekamMedis> getAll() throws SQLException {
        List<RekamMedis> list = new ArrayList<>();
        String sql = "SELECT r.id, r.id_pasien, r.id_dokter, r.tanggal, "
                + "r.keluhan, r.diagnosa, r.resep, "
                + "p.nama AS nama_pasien, d.nama AS nama_dokter "
                + "FROM rekam_medis r "
                + "JOIN pasien p ON r.id_pasien = p.id "
                + "JOIN dokter d ON r.id_dokter = d.id "
                + "ORDER BY r.tanggal DESC";

        try (Connection conn = KoneksiDB.configDB(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(mapResultSetToRekamMedis(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getAll RekamMedis: " + e.getMessage());
            throw e;
        }
        return list;
    }

    public RekamMedis getById(int id) throws SQLException {
        String sql = "SELECT r.id, r.id_pasien, r.id_dokter, r.tanggal, "
                + "r.keluhan, r.diagnosa, r.resep, "
                + "p.nama AS nama_pasien, d.nama AS nama_dokter "
                + "FROM rekam_medis r "
                + "JOIN pasien p ON r.id_pasien = p.id "
                + "JOIN dokter d ON r.id_dokter = d.id "
                + "WHERE r.id = ?";

        try (Connection conn = KoneksiDB.configDB(); PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, id);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToRekamMedis(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getById RekamMedis: " + e.getMessage());
            throw e;
        }
        return null;
    }

    public List<RekamMedis> getByPasienId(int idPasien) throws SQLException {
        List<RekamMedis> list = new ArrayList<>();
        String sql = "SELECT r.id, r.id_pasien, r.id_dokter, r.tanggal, "
                + "r.keluhan, r.diagnosa, r.resep, "
                + "p.nama AS nama_pasien, d.nama AS nama_dokter "
                + "FROM rekam_medis r "
                + "JOIN pasien p ON r.id_pasien = p.id "
                + "JOIN dokter d ON r.id_dokter = d.id "
                + "WHERE r.id_pasien = ? "
                + "ORDER BY r.tanggal DESC";

        try (Connection conn = KoneksiDB.configDB(); PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, idPasien);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToRekamMedis(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getByPasienId: " + e.getMessage());
            throw e;
        }
        return list;
    }

    public List<RekamMedis> getByDokterId(int idDokter) throws SQLException {
        List<RekamMedis> list = new ArrayList<>();
        String sql = "SELECT r.id, r.id_pasien, r.id_dokter, r.tanggal, "
                + "r.keluhan, r.diagnosa, r.resep, "
                + "p.nama AS nama_pasien, d.nama AS nama_dokter "
                + "FROM rekam_medis r "
                + "JOIN pasien p ON r.id_pasien = p.id "
                + "JOIN dokter d ON r.id_dokter = d.id "
                + "WHERE r.id_dokter = ? "
                + "ORDER BY r.tanggal DESC";

        try (Connection conn = KoneksiDB.configDB(); PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, idDokter);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToRekamMedis(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getByDokterId: " + e.getMessage());
            throw e;
        }
        return list;
    }

    public List<RekamMedis> getByDateRange(String startDate, String endDate) throws SQLException {
        List<RekamMedis> list = new ArrayList<>();
        String sql = "SELECT r.id, r.id_pasien, r.id_dokter, r.tanggal, "
                + "r.keluhan, r.diagnosa, r.resep, "
                + "p.nama AS nama_pasien, d.nama AS nama_dokter "
                + "FROM rekam_medis r "
                + "JOIN pasien p ON r.id_pasien = p.id "
                + "JOIN dokter d ON r.id_dokter = d.id "
                + "WHERE DATE(r.tanggal) BETWEEN ? AND ? "
                + "ORDER BY r.tanggal DESC";

        try (Connection conn = KoneksiDB.configDB(); PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, startDate);
            pst.setString(2, endDate);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToRekamMedis(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getByDateRange: " + e.getMessage());
            throw e;
        }
        return list;
    }

    public List<RekamMedis> searchByDiagnosa(String keyword) throws SQLException {
        List<RekamMedis> list = new ArrayList<>();
        String sql = "SELECT r.id, r.id_pasien, r.id_dokter, r.tanggal, "
                + "r.keluhan, r.diagnosa, r.resep, "
                + "p.nama AS nama_pasien, d.nama AS nama_dokter "
                + "FROM rekam_medis r "
                + "JOIN pasien p ON r.id_pasien = p.id "
                + "JOIN dokter d ON r.id_dokter = d.id "
                + "WHERE r.diagnosa LIKE ? "
                + "ORDER BY r.tanggal DESC";

        try (Connection conn = KoneksiDB.configDB(); PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, "%" + keyword + "%");

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToRekamMedis(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error searchByDiagnosa: " + e.getMessage());
            throw e;
        }
        return list;
    }

    public int insert(RekamMedis rekamMedis) throws SQLException {
        String sql = "INSERT INTO rekam_medis (id_pasien, id_dokter, tanggal, keluhan, diagnosa, resep) "
                + "VALUES (?, ?, NOW(), ?, ?, ?)";

        try (Connection conn = KoneksiDB.configDB(); PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pst.setInt(1, rekamMedis.getIdPasien());
            pst.setInt(2, rekamMedis.getIdDokter());
            pst.setString(3, rekamMedis.getKeluhan());
            pst.setString(4, rekamMedis.getDiagnosa());
            pst.setString(5, rekamMedis.getResep());

            int affectedRows = pst.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Insert rekam medis failed, no rows affected.");
            }

            try (ResultSet generatedKeys = pst.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Insert rekam medis failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error insert RekamMedis: " + e.getMessage());
            throw e;
        }
    }

    public int update(RekamMedis rekamMedis) throws SQLException {
        String sql = "UPDATE rekam_medis SET id_pasien=?, id_dokter=?, keluhan=?, diagnosa=?, resep=? WHERE id=?";

        try (Connection conn = KoneksiDB.configDB(); PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, rekamMedis.getIdPasien());
            pst.setInt(2, rekamMedis.getIdDokter());
            pst.setString(3, rekamMedis.getKeluhan());
            pst.setString(4, rekamMedis.getDiagnosa());
            pst.setString(5, rekamMedis.getResep());
            pst.setInt(6, rekamMedis.getId());

            return pst.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error update RekamMedis: " + e.getMessage());
            throw e;
        }
    }

    public int delete(int id) throws SQLException {
        String sql = "DELETE FROM rekam_medis WHERE id=?";

        try (Connection conn = KoneksiDB.configDB(); PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, id);
            return pst.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error delete RekamMedis: " + e.getMessage());
            throw e;
        }
    }

    public int getCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM rekam_medis";

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

    public int getCountByPasien(int idPasien) throws SQLException {
        String sql = "SELECT COUNT(*) FROM rekam_medis WHERE id_pasien = ?";

        try (Connection conn = KoneksiDB.configDB(); PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, idPasien);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getCountByPasien: " + e.getMessage());
            throw e;
        }
        return 0;
    }

    private RekamMedis mapResultSetToRekamMedis(ResultSet rs) throws SQLException {
        RekamMedis rm = new RekamMedis();
        rm.setId(rs.getInt("id"));
        rm.setIdPasien(rs.getInt("id_pasien"));
        rm.setIdDokter(rs.getInt("id_dokter"));
        rm.setTanggal(rs.getTimestamp("tanggal"));
        rm.setKeluhan(rs.getString("keluhan"));
        rm.setDiagnosa(rs.getString("diagnosa"));
        rm.setResep(rs.getString("resep"));
        rm.setNamaPasien(rs.getString("nama_pasien"));
        rm.setNamaDokter(rs.getString("nama_dokter"));
        return rm;
    }
}
