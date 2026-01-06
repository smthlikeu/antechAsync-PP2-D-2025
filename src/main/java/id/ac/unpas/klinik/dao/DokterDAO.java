package id.ac.unpas.klinik.dao;

import id.ac.unpas.klinik.model.Dokter;
import id.ac.unpas.klinik.util.KoneksiDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DokterDAO {

    public List<Dokter> getAll() throws SQLException {
        List<Dokter> list = new ArrayList<>();
        String sql = "SELECT * FROM dokter ORDER BY nama ASC";

        try (Connection conn = KoneksiDB.configDB(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(mapResultSetToDokter(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getAll Dokter: " + e.getMessage());
            throw e;
        }
        return list;
    }

    public Dokter getById(int id) throws SQLException {
        String sql = "SELECT * FROM dokter WHERE id = ?";

        try (Connection conn = KoneksiDB.configDB(); PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, id);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToDokter(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getById Dokter: " + e.getMessage());
            throw e;
        }
        return null;
    }

    public List<Dokter> searchByName(String keyword) throws SQLException {
        List<Dokter> list = new ArrayList<>();
        String sql = "SELECT * FROM dokter WHERE nama LIKE ? ORDER BY nama ASC";

        try (Connection conn = KoneksiDB.configDB(); PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, "%" + keyword + "%");

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToDokter(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error searchByName Dokter: " + e.getMessage());
            throw e;
        }
        return list;
    }

    public List<Dokter> getBySpesialisasi(String spesialisasi) throws SQLException {
        List<Dokter> list = new ArrayList<>();
        String sql = "SELECT * FROM dokter WHERE spesialisasi = ? ORDER BY nama ASC";

        try (Connection conn = KoneksiDB.configDB(); PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, spesialisasi);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToDokter(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getBySpesialisasi: " + e.getMessage());
            throw e;
        }
        return list;
    }

    public List<String> getAllSpesialisasi() throws SQLException {
        List<String> list = new ArrayList<>();
        String sql = "SELECT DISTINCT spesialisasi FROM dokter WHERE spesialisasi IS NOT NULL AND spesialisasi != '' ORDER BY spesialisasi ASC";

        try (Connection conn = KoneksiDB.configDB(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(rs.getString("spesialisasi"));
            }
        } catch (SQLException e) {
            System.err.println("Error getAllSpesialisasi: " + e.getMessage());
            throw e;
        }
        return list;
    }

    public List<Dokter> getByJadwal(String hari) throws SQLException {
        List<Dokter> list = new ArrayList<>();
        String sql = "SELECT * FROM dokter WHERE jadwal LIKE ? ORDER BY nama ASC";

        try (Connection conn = KoneksiDB.configDB(); PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, "%" + hari + "%");

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToDokter(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getByJadwal: " + e.getMessage());
            throw e;
        }
        return list;
    }

    public int insert(Dokter dokter) throws SQLException {
        String sql = "INSERT INTO dokter (nama, spesialisasi, jadwal) VALUES (?, ?, ?)";

        try (Connection conn = KoneksiDB.configDB(); PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pst.setString(1, dokter.getNama());
            pst.setString(2, dokter.getSpesialisasi());
            pst.setString(3, dokter.getJadwal());

            int affectedRows = pst.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Insert dokter failed, no rows affected.");
            }

            try (ResultSet generatedKeys = pst.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Insert dokter failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error insert Dokter: " + e.getMessage());
            throw e;
        }
    }

    public int update(Dokter dokter) throws SQLException {
        String sql = "UPDATE dokter SET nama=?, spesialisasi=?, jadwal=? WHERE id=?";

        try (Connection conn = KoneksiDB.configDB(); PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, dokter.getNama());
            pst.setString(2, dokter.getSpesialisasi());
            pst.setString(3, dokter.getJadwal());
            pst.setInt(4, dokter.getId());

            return pst.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error update Dokter: " + e.getMessage());
            throw e;
        }
    }

    public int delete(int id) throws SQLException {
        String sql = "DELETE FROM dokter WHERE id=?";

        try (Connection conn = KoneksiDB.configDB(); PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, id);
            return pst.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error delete Dokter: " + e.getMessage());
            throw e;
        }
    }

    public boolean hasRekamMedis(int idDokter) throws SQLException {
        String sql = "SELECT COUNT(*) FROM rekam_medis WHERE id_dokter = ?";

        try (Connection conn = KoneksiDB.configDB(); PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, idDokter);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking hasRekamMedis: " + e.getMessage());
            throw e;
        }
        return false;
    }

    public int getCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM dokter";

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

    private Dokter mapResultSetToDokter(ResultSet rs) throws SQLException {
        Dokter d = new Dokter();
        d.setId(rs.getInt("id"));
        d.setNama(rs.getString("nama"));
        d.setSpesialisasi(rs.getString("spesialisasi"));
        d.setJadwal(rs.getString("jadwal"));
        return d;
    }
}
