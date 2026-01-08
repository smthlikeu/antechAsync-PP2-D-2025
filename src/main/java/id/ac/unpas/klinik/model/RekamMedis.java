package id.ac.unpas.klinik.model;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class RekamMedis {
    // Primary Key
    private int id;
    
    // Foreign Keys
    private int idPasien;
    private int idDokter;
    
    // Data Rekam Medis
    private Timestamp tanggal;
    private String keluhan;
    private String diagnosa;
    private String resep;
    
    // Display fields
    private String namaPasien;
    private String namaDokter;

    // Default Constructor
    public RekamMedis() {}

    // Full Constructor
    public RekamMedis(int id, int idPasien, int idDokter, Timestamp tanggal, 
                      String keluhan, String diagnosa, String resep) {
        this.id = id;
        this.idPasien = idPasien;
        this.idDokter = idDokter;
        this.tanggal = tanggal;
        this.keluhan = keluhan;
        this.diagnosa = diagnosa;
        this.resep = resep;
    }

    // Constructor untuk insert baru
    public RekamMedis(int idPasien, int idDokter, String keluhan, 
                      String diagnosa, String resep) {
        this.idPasien = idPasien;
        this.idDokter = idDokter;
        this.keluhan = keluhan;
        this.diagnosa = diagnosa;
        this.resep = resep;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdPasien() { return idPasien; }
    public void setIdPasien(int idPasien) { this.idPasien = idPasien; }

    public int getIdDokter() { return idDokter; }
    public void setIdDokter(int idDokter) { this.idDokter = idDokter; }

    public Timestamp getTanggal() { return tanggal; }
    public void setTanggal(Timestamp tanggal) { this.tanggal = tanggal; }

    public String getKeluhan() { return keluhan; }
    public void setKeluhan(String keluhan) { this.keluhan = keluhan; }

    public String getDiagnosa() { return diagnosa; }
    public void setDiagnosa(String diagnosa) { this.diagnosa = diagnosa; }

    public String getResep() { return resep; }
    public void setResep(String resep) { this.resep = resep; }

    public String getNamaPasien() { return namaPasien; }
    public void setNamaPasien(String namaPasien) { this.namaPasien = namaPasien; }

    public String getNamaDokter() { return namaDokter; }
    public void setNamaDokter(String namaDokter) { this.namaDokter = namaDokter; }

    // ===== BUSINESS VALIDATION LOGIC (UPDATED) =====
    
    /**
     * Validasi komprehensif dengan pesan error spesifik.
     * Aturan:
     * 1. Pasien & Dokter harus dipilih
     * 2. Keluhan, Diagnosa, Resep wajib diisi
     * 3. Keluhan & Diagnosa minimal 5 karakter (untuk kualitas data)
     */
    public String getValidationError() {
        if (idPasien <= 0) {
            return "Pasien belum dipilih!";
        }
        if (idDokter <= 0) {
            return "Dokter belum dipilih!";
        }
        if (keluhan == null || keluhan.trim().isEmpty()) {
            return "Keluhan pasien wajib diisi!";
        }
        if (diagnosa == null || diagnosa.trim().isEmpty()) {
            return "Diagnosa dokter wajib diisi!";
        }
        if (resep == null || resep.trim().isEmpty()) {
            return "Resep obat wajib diisi!";
        }
        // Validasi kualitas data (agar tidak diisi "-", ".", dll)
        if (keluhan.trim().length() < 5) {
            return "Keluhan terlalu singkat (minimal 5 karakter)";
        }
        if (diagnosa.trim().length() < 5) {
            return "Diagnosa terlalu singkat (minimal 5 karakter)";
        }
        return null; // null = valid
    }

    // Format tanggal untuk display
    public String getFormattedTanggal() {
        if (tanggal == null) return "-";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return sdf.format(tanggal);
    }
    
    @Override
    public String toString() {
        return "RekamMedis{" +
                "id=" + id +
                ", pasien=" + namaPasien +
                ", dokter=" + namaDokter +
                ", tanggal=" + getFormattedTanggal() +
                '}';
    }
}