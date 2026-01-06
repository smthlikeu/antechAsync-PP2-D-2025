package id.ac.unpas.klinik.model;

public class Pasien {
    private int id;
    private String nama;
    private String nik;
    private String alamat;
    private String telepon;

    // Constructor Kosong
    public Pasien() {}

    // Constructor Lengkap
    public Pasien(int id, String nama, String nik, String alamat, String telepon) {
        this.id = id;
        this.nama = nama;
        this.nik = nik;
        this.alamat = alamat;
        this.telepon = telepon;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public String getNik() { return nik; }
    public void setNik(String nik) { this.nik = nik; }

    public String getAlamat() { return alamat; }
    public void setAlamat(String alamat) { this.alamat = alamat; }

    public String getTelepon() { return telepon; }
    public void setTelepon(String telepon) { this.telepon = telepon; }

    // ===== BUSINESS LOGIC: VALIDATION =====
    
    // Validasi sederhana: Return true jika semua field terisi
    public boolean isValid() {
        return nama != null && !nama.trim().isEmpty() 
            && nik != null && !nik.trim().isEmpty()
            && alamat != null && !alamat.trim().isEmpty()    // Tambahan Validasi Alamat
            && telepon != null && !telepon.trim().isEmpty(); // Tambahan Validasi Telepon
    }

    // Validasi mendetail (Optional: untuk pesan error yang lebih spesifik)
    public String getValidationError() {
        if (nama == null || nama.trim().isEmpty()) {
            return "Nama Pasien wajib diisi!";
        }
        if (nik == null || nik.trim().isEmpty()) {
            return "NIK wajib diisi!";
        }
        if (alamat == null || alamat.trim().isEmpty()) {
            return "Alamat wajib diisi!";
        }
        if (telepon == null || telepon.trim().isEmpty()) {
            return "Nomor Telepon wajib diisi!";
        }
        // Validasi tambahan: NIK harus angka (opsional)
        if (!nik.matches("\\d+")) {
            return "NIK harus berupa angka!";
        }
        return null; // Null berarti valid
    }

    @Override
    public String toString() {
        return id + " - " + nama;
    }
}