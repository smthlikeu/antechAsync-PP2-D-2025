package id.ac.unpas.klinik.model;

public class User {
    private int id;
    private String username;
    private String password;
    private String fullname;
    private String role; // "Admin" atau "Petugas"

    public User() {}

    public User(int id, String username, String password, String fullname, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullname = fullname;
        this.role = role;
    }

    // Getters
    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getFullname() { return fullname; }
    public String getRole() { return role; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setFullname(String fullname) { this.fullname = fullname; }
    public void setRole(String role) { this.role = role; }
    
    // Helper untuk cek role
    public boolean isAdmin() {
        return "Admin".equalsIgnoreCase(role);
    }
}