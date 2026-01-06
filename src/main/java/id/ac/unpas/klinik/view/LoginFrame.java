package id.ac.unpas.klinik.view;

import id.ac.unpas.klinik.MainApp;
import id.ac.unpas.klinik.dao.UserDAO;
import id.ac.unpas.klinik.model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI; // Import BasicButtonUI
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class LoginFrame extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private UserDAO userDAO;

    public LoginFrame() {
        this.userDAO = new UserDAO();
        
        setTitle("Login Sistem Klinik");
        setSize(450, 450); // Ukuran yang sudah diperbaiki
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setResizable(false);
        
        initComponents();
    }

    private void initComponents() {
        // 1. Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(66, 133, 244));
        headerPanel.setBorder(new EmptyBorder(25, 0, 25, 0));
        
        JLabel lblTitle = new JLabel("KLINIK SEHAT");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(Color.WHITE);
        headerPanel.add(lblTitle);
        
        // 2. Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(30, 50, 30, 50));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        
        // Username
        gbc.gridy = 0;
        JLabel lblUser = new JLabel("Username:");
        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(lblUser, gbc);
        
        gbc.gridy = 1;
        txtUsername = new JTextField();
        txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUsername.setPreferredSize(new Dimension(0, 35));
        formPanel.add(txtUsername, gbc);
        
        // Password
        gbc.gridy = 2;
        JLabel lblPass = new JLabel("Password:");
        lblPass.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(lblPass, gbc);
        
        gbc.gridy = 3;
        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPassword.setPreferredSize(new Dimension(0, 35));
        formPanel.add(txtPassword, gbc);
        
        // Tombol Login (FIX WARNA)
        gbc.gridy = 4;
        gbc.insets = new Insets(30, 0, 0, 0);
        btnLogin = new JButton("LOGIN SEKARANG");
        
        // --- PERBAIKAN UTAMA ---
        // Memaksa tombol menggunakan UI dasar agar warna background Biru mau muncul
        btnLogin.setUI(new BasicButtonUI()); 
        // -----------------------
        
        btnLogin.setBackground(new Color(66, 133, 244)); // Biru Google
        btnLogin.setForeground(Color.WHITE);             // Teks Putih
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setPreferredSize(new Dimension(0, 45));
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Efek Hover (Opsional: agar tombol sedikit berubah saat disorot)
        btnLogin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLogin.setBackground(new Color(50, 100, 200)); // Biru lebih gelap
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnLogin.setBackground(new Color(66, 133, 244)); // Kembali ke warna asal
            }
        });
        
        btnLogin.addActionListener(e -> handleLogin());
        formPanel.add(btnLogin, gbc);
        
        // Footer
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(Color.WHITE);
        JLabel lblFooter = new JLabel("© 2026 Klinik Kesehatan - Sistem Informasi");
        lblFooter.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblFooter.setForeground(Color.GRAY);
        footerPanel.setBorder(new EmptyBorder(10, 0, 15, 0));
        footerPanel.add(lblFooter);

        // Key Listeners
        KeyAdapter enterKeyAdapter = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) handleLogin();
            }
        };
        txtUsername.addKeyListener(enterKeyAdapter);
        txtPassword.addKeyListener(enterKeyAdapter);
        btnLogin.addKeyListener(enterKeyAdapter);

        add(headerPanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);
    }

    private void handleLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username dan Password harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            User user = userDAO.login(username, password);
            if (user != null) {
                new MainApp(user).setVisible(true);
                this.dispose(); 
            } else {
                JOptionPane.showMessageDialog(this, "Username atau Password salah!", "Login Gagal", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan database: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}