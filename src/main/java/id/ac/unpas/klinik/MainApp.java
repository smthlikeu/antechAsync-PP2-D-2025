package id.ac.unpas.klinik;

import id.ac.unpas.klinik.controller.DokterController;
import id.ac.unpas.klinik.controller.PasienController;
import id.ac.unpas.klinik.controller.RekamMedisController;
import id.ac.unpas.klinik.model.User;
import id.ac.unpas.klinik.view.DashboardPanel;
import id.ac.unpas.klinik.view.DokterView;
import id.ac.unpas.klinik.view.LoginFrame;
import id.ac.unpas.klinik.view.PasienView;
import id.ac.unpas.klinik.view.RekamMedisView;

import javax.swing.*;
import java.awt.*;

public class MainApp extends JFrame {
    
    // Session: Menyimpan data user yang sedang login
    private User currentUser;
    
    // Views
    private DashboardPanel dashboardPanel;
    private PasienView pasienView;
    private DokterView dokterView;
    private RekamMedisView rekamMedisView;
    
    // Controllers
    private PasienController pasienController;
    private DokterController dokterController;
    private RekamMedisController rekamMedisController;
    
    private JTabbedPane tabbedPane;
    
    // Constructor WAJIB menerima objek User (hasil dari LoginFrame)
    public MainApp(User user) {
        this.currentUser = user; // Simpan user ke session
        
        initializeWindow();
        initializeComponents(); // Init View & Controller dulu
        createMenuBar();        // Baru buat Menu Bar
        setupTabChangeListener();
        
        // TERAPKAN HAK AKSES (Role Based Access Control)
        applyRolePermissions();
    }
    
    private void initializeWindow() {
        // Tampilkan Nama User & Role di Judul Jendela
        setTitle("Sistem Klinik - Login sebagai: " + currentUser.getFullname() + " (" + currentUser.getRole() + ")");
        setSize(1100, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }
    
    private void initializeComponents() {
        // Init Views
        dashboardPanel = new DashboardPanel();
        pasienView = new PasienView();
        dokterView = new DokterView();
        rekamMedisView = new RekamMedisView();
        
        // Init Controllers
        pasienController = new PasienController(pasienView);
        dokterController = new DokterController(dokterView);
        rekamMedisController = new RekamMedisController(rekamMedisView);
        
        // Setup Tabs
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        // URUTAN TAB
        tabbedPane.addTab("   Dashboard  ", dashboardPanel);     // Index 0
        tabbedPane.addTab("   Data Pasien  ", pasienView);        // Index 1
        tabbedPane.addTab("  ️ Data Dokter  ", dokterView);        // Index 2
        tabbedPane.addTab("   Rekam Medis  ", rekamMedisView);    // Index 3
        
        add(tabbedPane, BorderLayout.CENTER);
        add(createStatusBar(), BorderLayout.SOUTH);
    }

    /**
     * Logika Pembatasan Hak Akses
     * Dipanggil di akhir Constructor
     */
    private void applyRolePermissions() {
        // Jika user BUKAN Admin (artinya Petugas), matikan tombol Hapus
        if (!currentUser.isAdmin()) { 
            pasienView.setEnableDelete(false);
            dokterView.setEnableDelete(false);
            rekamMedisView.setEnableDelete(false);
        }
    }
    
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // 1. Menu File
        JMenu menuFile = new JMenu("File");
        menuFile.setMnemonic('F');
        
        // Fitur LOGOUT
        JMenuItem itemLogout = new JMenuItem("Logout");
        itemLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Apakah Anda yakin ingin logout?", "Konfirmasi Logout", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                this.dispose(); // Tutup MainApp
                new LoginFrame().setVisible(true); // Kembali ke Login Screen
            }
        });
        
        JMenuItem itemExit = new JMenuItem("Keluar Aplikasi");
        itemExit.setAccelerator(KeyStroke.getKeyStroke("ctrl Q"));
        itemExit.addActionListener(e -> System.exit(0));
        
        menuFile.add(itemLogout);
        menuFile.addSeparator(); // Garis pemisah
        menuFile.add(itemExit);
        
        // 2. Menu Navigasi
        JMenu menuNav = new JMenu("Navigasi");
        menuNav.setMnemonic('N');
        
        JMenuItem itemHome = new JMenuItem("Dashboard");
        itemHome.setAccelerator(KeyStroke.getKeyStroke("ctrl H"));
        itemHome.addActionListener(e -> tabbedPane.setSelectedIndex(0));

        JMenuItem itemPasien = new JMenuItem("Data Pasien");
        itemPasien.setAccelerator(KeyStroke.getKeyStroke("ctrl 1"));
        itemPasien.addActionListener(e -> tabbedPane.setSelectedIndex(1));
        
        JMenuItem itemDokter = new JMenuItem("Data Dokter");
        itemDokter.setAccelerator(KeyStroke.getKeyStroke("ctrl 2"));
        itemDokter.addActionListener(e -> tabbedPane.setSelectedIndex(2));
        
        JMenuItem itemRekam = new JMenuItem("Rekam Medis");
        itemRekam.setAccelerator(KeyStroke.getKeyStroke("ctrl 3"));
        itemRekam.addActionListener(e -> tabbedPane.setSelectedIndex(3));
        
        menuNav.add(itemHome);
        menuNav.addSeparator();
        menuNav.add(itemPasien);
        menuNav.add(itemDokter);
        menuNav.add(itemRekam);
        
        // 3. Menu Bantuan
        JMenu menuHelp = new JMenu("Bantuan");
        JMenuItem itemAbout = new JMenuItem("Tentang");
        itemAbout.addActionListener(e -> JOptionPane.showMessageDialog(this, "Sistem Klinik v2.0\nCreated with Java Swing MVC"));
        menuHelp.add(itemAbout);
        
        menuBar.add(menuFile);
        menuBar.add(menuNav);
        menuBar.add(menuHelp);
        
        setJMenuBar(menuBar);
    }
    
    private void setupTabChangeListener() {
        tabbedPane.addChangeListener(e -> {
            int selectedIndex = tabbedPane.getSelectedIndex();
            // Refresh data saat tab dibuka
            switch (selectedIndex) {
                case 0: dashboardPanel.loadStats(); break;
                case 1: pasienController.refresh(); break;
                case 2: dokterController.refresh(); break;
                case 3: rekamMedisController.refresh(); break;
            }
        });
    }
    
    private JPanel createStatusBar() {
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        // Tampilkan siapa yang login di pojok kiri bawah
        statusBar.add(new JLabel("User Aktif: " + currentUser.getUsername() + " (" + currentUser.getRole() + ")"), BorderLayout.WEST);
        statusBar.add(new JLabel("© 2026 Klinik Kesehatan"), BorderLayout.EAST);
        return statusBar;
    }
    
    // ENTRY POINT UTAMA
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}
        
        SwingUtilities.invokeLater(() -> {
            // PENTING: Aplikasi dimulai dari LOGIN FRAME, bukan MainApp langsung
            new LoginFrame().setVisible(true);
        });
    }
}