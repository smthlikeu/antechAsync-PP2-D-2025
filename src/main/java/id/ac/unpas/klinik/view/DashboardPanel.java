package id.ac.unpas.klinik.view;

import id.ac.unpas.klinik.dao.DokterDAO;
import id.ac.unpas.klinik.dao.PasienDAO;
import id.ac.unpas.klinik.dao.RekamMedisDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;

public class DashboardPanel extends JPanel {

    private JLabel lblTotalPasien;
    private JLabel lblTotalDokter;
    private JLabel lblTotalRekamMedis;

    private PasienDAO pasienDAO;
    private DokterDAO dokterDAO;
    private RekamMedisDAO rekamDAO;

    public DashboardPanel() {
        this.pasienDAO = new PasienDAO();
        this.dokterDAO = new DokterDAO();
        this.rekamDAO = new RekamMedisDAO();

        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(30, 30, 30, 30)); // Margin lebih besar agar lega

        initComponents();
        loadStats();
    }

    private void initComponents() {
        // 1. Header Area
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel lblWelcome = new JLabel("Selamat Datang di Sistem Informasi Klinik");
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 28)); // Font lebih besar
        lblWelcome.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel lblSubtitle = new JLabel("Dashboard Monitoring Statistik & Aktivitas Klinik");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblSubtitle.setForeground(Color.GRAY);
        lblSubtitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblSubtitle.setBorder(new EmptyBorder(10, 0, 30, 0)); // Jarak ke kartu

        headerPanel.add(lblWelcome, BorderLayout.NORTH);
        headerPanel.add(lblSubtitle, BorderLayout.CENTER);

        // 2. Cards Panel
        JPanel cardsContainer = new JPanel(new GridLayout(1, 3, 30, 0)); // Gap 30px

        // Kartu 1: Pasien
        JPanel cardPasien = createCard("Total Pasien", new Color(66, 133, 244));
        lblTotalPasien = new JLabel("...");
        styleValueLabel(lblTotalPasien);
        cardPasien.add(lblTotalPasien, BorderLayout.CENTER);

        // Kartu 2: Dokter
        JPanel cardDokter = createCard("Total Dokter", new Color(15, 157, 88));
        lblTotalDokter = new JLabel("...");
        styleValueLabel(lblTotalDokter);
        cardDokter.add(lblTotalDokter, BorderLayout.CENTER);

        // Kartu 3: Rekam Medis
        JPanel cardRekam = createCard("Total Kunjungan", new Color(219, 68, 55));
        lblTotalRekamMedis = new JLabel("...");
        styleValueLabel(lblTotalRekamMedis);
        cardRekam.add(lblTotalRekamMedis, BorderLayout.CENTER);

        cardsContainer.add(cardPasien);
        cardsContainer.add(cardDokter);
        cardsContainer.add(cardRekam);

        JPanel bodyPanel = new JPanel(new BorderLayout());
        bodyPanel.add(cardsContainer, BorderLayout.NORTH);

        JLabel lblInfo = new JLabel("<html><center>Data di atas diperbarui secara real-time saat tab dibuka.<br>"
                + "Gunakan navigasi di atas untuk manajemen data.</center></html>");
        lblInfo.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        lblInfo.setForeground(Color.DARK_GRAY);
        lblInfo.setHorizontalAlignment(SwingConstants.CENTER);
        lblInfo.setBorder(new EmptyBorder(40, 0, 0, 0)); // Jarak dari kartu ke teks bawah

        bodyPanel.add(lblInfo, BorderLayout.CENTER);

        // Add to main layout
        add(headerPanel, BorderLayout.NORTH);
        add(bodyPanel, BorderLayout.CENTER);
    }

    private JPanel createCard(String title, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(color);
        // Padding dalam kartu
        card.setBorder(new EmptyBorder(20, 20, 20, 20));
        // Set Preferred Size agar kartu punya tinggi tetap (tidak gepeng, tidak terlalu tinggi)
        card.setPreferredSize(new Dimension(200, 150));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(new Color(255, 255, 255, 220)); // Putih agak transparan

        card.add(lblTitle, BorderLayout.NORTH);
        return card;
    }

    private void styleValueLabel(JLabel label) {
        label.setFont(new Font("Segoe UI", Font.BOLD, 60)); // Angka Besar
        label.setForeground(Color.WHITE);
        label.setHorizontalAlignment(SwingConstants.RIGHT);
    }

    public void loadStats() {
        try {
            lblTotalPasien.setText(String.valueOf(pasienDAO.getCount()));
            lblTotalDokter.setText(String.valueOf(dokterDAO.getCount()));
            lblTotalRekamMedis.setText(String.valueOf(rekamDAO.getCount()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
