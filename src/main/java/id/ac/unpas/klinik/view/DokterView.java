package id.ac.unpas.klinik.view;

import id.ac.unpas.klinik.model.Dokter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class DokterView extends JPanel {
    
    private JTextField txtNama, txtSpesialisasi, txtJadwal;
    private JTextField txtCari;
    private JButton btnCari, btnResetCari;
    
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnTambah, btnEdit, btnHapus, btnBersih, btnExport;
    
    public DokterView() {
        setLayout(new BorderLayout());
        initComponents();
    }
    
    private void initComponents() {
        // 1. Form Panel dengan GridBagLayout
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Input Data Dokter"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        
        // Inisialisasi TextFields
        txtNama = new JTextField(20);
        txtSpesialisasi = new JTextField(20);
        txtJadwal = new JTextField(20);
        
        // Baris 0: Nama
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.0;
        formPanel.add(new JLabel("Nama Dokter:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        formPanel.add(txtNama, gbc);
        
        // Baris 1: Spesialisasi
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.0;
        formPanel.add(new JLabel("Spesialisasi:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        formPanel.add(txtSpesialisasi, gbc);
        
        // Baris 2: Jadwal
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.0;
        formPanel.add(new JLabel("Jadwal Praktek:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        formPanel.add(txtJadwal, gbc);
        
        // 2. Panel Pencarian
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBorder(new EmptyBorder(5, 5, 0, 5));
        
        txtCari = new JTextField(20);
        btnCari = new JButton("Cari");
        btnResetCari = new JButton("Reset");
        
        searchPanel.add(new JLabel("Cari Dokter:"));
        searchPanel.add(txtCari);
        searchPanel.add(btnCari);
        searchPanel.add(btnResetCari);
        
        // 3. Table Area
        tableModel = new DefaultTableModel(
            new String[]{"ID", "Nama", "Spesialisasi", "Jadwal"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
            // Logic agar kolom ID (index 0) diurutkan sebagai Angka, bukan Teks
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
        JScrollPane scrollPane = new JScrollPane(table);
        
        // Wrapper Center
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(searchPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        // 4. Button Panel
        JPanel mainButtonPanel = new JPanel(new BorderLayout());
        mainButtonPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        
        // Panel Kiri: CRUD
        JPanel crudPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnTambah = new JButton("Tambah");
        btnEdit = new JButton("Edit");
        btnHapus = new JButton("Hapus");
        btnBersih = new JButton("Bersihkan Form");
        
        crudPanel.add(btnTambah);
        crudPanel.add(btnEdit);
        crudPanel.add(btnHapus);
        crudPanel.add(Box.createHorizontalStrut(15));
        crudPanel.add(btnBersih);
        
        // Panel Kanan: Export
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
    
    public void setTableData(List<Dokter> data) {
        tableModel.setRowCount(0);
        for (Dokter d : data) {
            tableModel.addRow(new Object[]{
                d.getId(), d.getNama(), d.getSpesialisasi(), d.getJadwal()
            });
        }
    }
    
    public Dokter getFormData() {
        Dokter d = new Dokter();
        d.setNama(txtNama.getText().trim());
        d.setSpesialisasi(txtSpesialisasi.getText().trim());
        d.setJadwal(txtJadwal.getText().trim());
        return d;
    }
    
    public void setFormData(Dokter dokter) {
        if (dokter != null) {
            txtNama.setText(dokter.getNama());
            txtSpesialisasi.setText(dokter.getSpesialisasi());
            txtJadwal.setText(dokter.getJadwal());
        }
    }
    
    public void clearForm() {
        txtNama.setText("");
        txtSpesialisasi.setText("");
        txtJadwal.setText("");
        table.clearSelection();
    }
    
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
            int modelRow = table.convertRowIndexToModel(selectedRow); // Konversi Index
            return (int) tableModel.getValueAt(modelRow, 0);
        }
        return -1;
    }
    
    // Helper untuk mengambil Data yang benar saat sorting aktif
    public Dokter getSelectedRowData() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int modelRow = table.convertRowIndexToModel(selectedRow); // Konversi Index
            
            Dokter d = new Dokter();
            d.setId((int) tableModel.getValueAt(modelRow, 0));
            d.setNama(tableModel.getValueAt(modelRow, 1).toString());
            d.setSpesialisasi(tableModel.getValueAt(modelRow, 2).toString());
            d.setJadwal(tableModel.getValueAt(modelRow, 3).toString());
            return d;
        }
        return null;
    }
    
    public JTable getTable() {
        return table;
    }
    
    // ===== Event Listeners =====
    public void addTambahListener(ActionListener listener) { btnTambah.addActionListener(listener); }
    public void addEditListener(ActionListener listener) { btnEdit.addActionListener(listener); }
    public void addHapusListener(ActionListener listener) { btnHapus.addActionListener(listener); }
    public void addBersihListener(ActionListener listener) { btnBersih.addActionListener(listener); }
    public void addExportListener(ActionListener listener) { btnExport.addActionListener(listener); }
    public void addCariListener(ActionListener listener) { btnCari.addActionListener(listener); }
    public void addResetCariListener(ActionListener listener) { btnResetCari.addActionListener(listener); }
    
    public void addTableSelectionListener(Runnable callback) {
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                callback.run();
            }
        });
    }
    
    public void showMessage(String message) { JOptionPane.showMessageDialog(this, message, "Informasi", JOptionPane.INFORMATION_MESSAGE); }
    public void showError(String error) { JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE); }
    public boolean confirmDelete() {
        return JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }
    
    public void setEnableDelete(boolean enabled) {
        btnHapus.setEnabled(enabled);
    }
}