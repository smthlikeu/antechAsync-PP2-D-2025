package id.ac.unpas.klinik.model;

public class Dokter {
    private int id;
    private String nama;
    private String spesialisasi;
    private String jadwal;

    // Default Constructor
    public Dokter() {}

    // Parameterized Constructor
    public Dokter(int id, String nama, String spesialisasi, String jadwal) {
        this.id = id;
        this.nama = nama;
        this.spesialisasi = spesialisasi;
        this.jadwal = jadwal;
    }

    // Constructor without ID (untuk insert baru)
    public Dokter(String nama, String spesialisasi, String jadwal) {
        this.nama = nama;
        this.spesialisasi = spesialisasi;
        this.jadwal = jadwal;
    }

    // Getters and Setters
    public int getId() { 
        return id; 
    }
    
    public void setId(int id) { 
        this.id = id; 
    }

    public String getNama() { 
        return nama; 
    }
    
    public void setNama(String nama) { 
        this.nama = nama; 
    }

    public String getSpesialisasi() { 
        return spesialisasi; 
    }
    
    public void setSpesialisasi(String spesialisasi) { 
        this.spesialisasi = spesialisasi; 
    }

    public String getJadwal() { 
        return jadwal; 
    }
    
    public void setJadwal(String jadwal) { 
        this.jadwal = jadwal; 
    }

    // ===== BUSINESS VALIDATION LOGIC (UPDATED) =====
    
    // Validasi: Return true jika semua field penting terisi
    public boolean isValid() {
        return nama != null && !nama.trim().isEmpty()
            && spesialisasi != null && !spesialisasi.trim().isEmpty()
            && jadwal != null && !jadwal.trim().isEmpty();
    }

    // Validasi dengan pesan error spesifik
    public String getValidationError() {
        if (nama == null || nama.trim().isEmpty()) {
            return "Nama dokter wajib diisi";
        }
        if (nama.trim().length() < 3) {
            return "Nama dokter minimal 3 karakter";
        }
        if (spesialisasi == null || spesialisasi.trim().isEmpty()) {
            return "Spesialisasi wajib diisi";
        }
        if (jadwal == null || jadwal.trim().isEmpty()) {
            return "Jadwal praktek wajib diisi";
        }
        return null; // null = valid
    }

    // Business Logic: Check apakah dokter memiliki spesialisasi
    public boolean hasSpesialisasi() {
        return spesialisasi != null && !spesialisasi.trim().isEmpty();
    }

    // Display format untuk ComboBox
    @Override
    public String toString() {
        return id + " - " + nama + 
               (hasSpesialisasi() ? " (" + spesialisasi + ")" : "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dokter dokter = (Dokter) o;
        return id == dokter.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}