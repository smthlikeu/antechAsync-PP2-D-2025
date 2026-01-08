package id.ac.unpas.klinik.view;

import id.ac.unpas.klinik.model.Dokter;
import id.ac.unpas.klinik.model.Pasien;
import id.ac.unpas.klinik.model.RekamMedis;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class RekamMedisView extends JPanel {

    private JComboBox<Pasien> comboPasien;
    private JComboBox<Dokter> comboDokter;
    private JTextArea txtKeluhan, txtDiagnosa, txtResep;
    
    // Search Components
    private JTextField txtCari;
    private JButton btnCari, btnResetCari;
    
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnSimpan, btnHapus, btnBersih, btnExport;

    public RekamMedisView() {
        setLayout(new BorderLayout());
        initComponents();
    }

    private void initComponents() {
        // 1. Form Panel (GridBagLayout)
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Input Rekam Medis"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Init Components
        comboPasien = new JComboBox<>();
        comboDokter = new JComboBox<>();
        
        txtKeluhan = new JTextArea(3, 20);
        txtKeluhan.setLineWrap(true); txtKeluhan.setWrapStyleWord(true);
        
        txtDiagnosa = new JTextArea(3, 20);
        txtDiagnosa.setLineWrap(true); txtDiagnosa.setWrapStyleWord(true);
        
        txtResep = new JTextArea(3, 20);
        txtResep.setLineWrap(true); txtResep.setWrapStyleWord(true);

        // --- Baris 0: Pasien ---
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.0;
        formPanel.add(new JLabel("Pilih Pasien:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        formPanel.add(comboPasien, gbc);

        // --- Baris 1: Dokter ---
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.0;
        formPanel.add(new JLabel("Pilih Dokter:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        formPanel.add(comboDokter, gbc);

        // --- Baris 2: Keluhan ---
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.0; gbc.anchor = GridBagConstraints.NORTHWEST;
        formPanel.add(new JLabel("Keluhan:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.BOTH;
        formPanel.add(new JScrollPane(txtKeluhan), gbc);

        // --- Baris 3: Diagnosa ---
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(new JLabel("Diagnosa:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.BOTH;
        formPanel.add(new JScrollPane(txtDiagnosa), gbc);

        // --- Baris 4: Resep ---
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(new JLabel("Resep Obat:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.BOTH;
        formPanel.add(new JScrollPane(txtResep), gbc);

        // 2. Search Panel (Search by Diagnosa)
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBorder(new EmptyBorder(5, 5, 0, 5));
        
        txtCari = new JTextField(20);
        btnCari = new JButton("Cari");
        btnResetCari = new JButton("Reset");
        
        searchPanel.add(new JLabel("Cari Diagnosa:"));
        searchPanel.add(txtCari);
        searchPanel.add(btnCari);
        searchPanel.add(btnResetCari);

        // 3. Table Area
        tableModel = new DefaultTableModel(
                new String[]{"ID", "Pasien", "Dokter", "Tanggal", "Diagnosa", "Resep"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            // Logic agar kolom ID (index 0) diurutkan sebagai Angka
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) return Integer.class;
                return String.class;
            }
        };
        table = new JTable(tableModel);
        
        // ===== FITUR SORTING AKTIF =====
        table.setAutoCreateRowSorter(true);
        // ===============================
        
        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Adjust column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(40);
        table.getColumnModel().getColumn(1).setPreferredWidth(120);
        table.getColumnModel().getColumn(2).setPreferredWidth(120);
        table.getColumnModel().getColumn(3).setPreferredWidth(110);
        table.getColumnModel().getColumn(4).setPreferredWidth(150);
        table.getColumnModel().getColumn(5).setPreferredWidth(150);

        JScrollPane scrollPane = new JScrollPane(table);

        // Wrapper Center
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(searchPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        // 4. Button Panel
        JPanel mainButtonPanel = new JPanel(new BorderLayout());
        mainButtonPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        
        // Kiri: Operasional
        JPanel crudPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnSimpan = new JButton("Simpan Rekam Medis");
        btnHapus = new JButton("Hapus");
        btnBersih = new JButton("Bersihkan Form");
        
        crudPanel.add(btnSimpan);
        crudPanel.add(btnHapus);
        crudPanel.add(Box.createHorizontalStrut(10));
        crudPanel.add(btnBersih);
        
        // Kanan: Reporting
        JPanel reportPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnExport = new JButton("Export PDF");
        reportPanel.add(btnExport);
        
        mainButtonPanel.add(crudPanel, BorderLayout.WEST);
        mainButtonPanel.add(reportPanel, BorderLayout.EAST);

        // Layout Utama
        add(formPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(mainButtonPanel, BorderLayout.SOUTH);
    }

    // ===== Public API =====

    public void setPasienComboData(List<Pasien> data) {
        comboPasien.removeAllItems();
        for (Pasien p : data) {
            comboPasien.addItem(p);
        }
    }

    public void setDokterComboData(List<Dokter> data) {
        comboDokter.removeAllItems();
        for (Dokter d : data) {
            comboDokter.addItem(d);
        }
    }

    public void setTableData(List<RekamMedis> data) {
        tableModel.setRowCount(0);
        for (RekamMedis rm : data) {
            tableModel.addRow(new Object[]{
                rm.getId(), rm.getNamaPasien(), rm.getNamaDokter(),
                rm.getFormattedTanggal(), rm.getDiagnosa(), rm.getResep()
            });
        }
    }

    public RekamMedis getFormData() {
        RekamMedis rm = new RekamMedis();
        Pasien selectedPasien = (Pasien) comboPasien.getSelectedItem();
        Dokter selectedDokter = (Dokter) comboDokter.getSelectedItem();

        if (selectedPasien != null) rm.setIdPasien(selectedPasien.getId());
        if (selectedDokter != null) rm.setIdDokter(selectedDokter.getId());

        rm.setKeluhan(txtKeluhan.getText().trim());
        rm.setDiagnosa(txtDiagnosa.getText().trim());
        rm.setResep(txtResep.getText().trim());
        return rm;
    }

    public void clearForm() {
        txtKeluhan.setText("");
        txtDiagnosa.setText("");
        txtResep.setText("");
        if (comboPasien.getItemCount() > 0) comboPasien.setSelectedIndex(0);
        if (comboDokter.getItemCount() > 0) comboDokter.setSelectedIndex(0);
        table.clearSelection();
    }
    
    // --- Search Features ---
    public String getSearchKeyword() {
        return txtCari.getText().trim();
    }
    
    public void resetSearchField() {
        txtCari.setText("");
    }

    // Helper untuk mengambil ID yang benar saat sorting aktif
    public int getSelectedRowId() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            // PENTING: Konversi index visual ke index model
            int modelRow = table.convertRowIndexToModel(selectedRow);
            return (int) tableModel.getValueAt(modelRow, 0);
        }
        return -1;
    }
    
    public JTable getTable() { return table; }

    // ===== Listeners =====
    public void addSimpanListener(ActionListener listener) { btnSimpan.addActionListener(listener); }
    public void addHapusListener(ActionListener listener) { btnHapus.addActionListener(listener); }
    public void addBersihListener(ActionListener listener) { btnBersih.addActionListener(listener); }
    public void addExportListener(ActionListener listener) { btnExport.addActionListener(listener); }
    
    // Search Listeners
    public void addCariListener(ActionListener listener) { btnCari.addActionListener(listener); }
    public void addResetCariListener(ActionListener listener) { btnResetCari.addActionListener(listener); }

    public void addTableSelectionListener(Runnable callback) {
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { callback.run(); }
        });
    }

    // Feedback
    public void showMessage(String message) { JOptionPane.showMessageDialog(this, message, "Informasi", JOptionPane.INFORMATION_MESSAGE); }
    public void showError(String error) { JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE); }
    public boolean confirmDelete() {
        return JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus rekam medis ini?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION;
    }
    
    public void setEnableDelete(boolean enabled) {
        btnHapus.setEnabled(enabled);
    }
}