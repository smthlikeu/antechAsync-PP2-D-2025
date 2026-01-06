package id.ac.unpas.klinik.view;

import id.ac.unpas.klinik.model.Pasien;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class PasienView extends JPanel {

    private JTextField txtNama, txtNIK, txtAlamat, txtTelp;
    private JTextField txtCari;
    private JButton btnCari, btnResetCari;
    
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnTambah, btnEdit, btnHapus, btnBersih, btnExport;

    public PasienView() {
        setLayout(new BorderLayout());
        initComponents();
    }

    private void initComponents() {
        // 1. Form Panel dengan GridBagLayout
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Input Data Pasien"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); 
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Inisialisasi TextFields
        txtNama = new JTextField(20);
        txtNIK = new JTextField(15);
        txtAlamat = new JTextField(25);
        txtTelp = new JTextField(15);

        // Baris 0: Nama
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.0;
        formPanel.add(new JLabel("Nama Pasien:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        formPanel.add(txtNama, gbc);

        // Baris 1: NIK
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.0;
        formPanel.add(new JLabel("NIK:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        formPanel.add(txtNIK, gbc);

        // Baris 2: Alamat
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.0;
        formPanel.add(new JLabel("Alamat:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        formPanel.add(txtAlamat, gbc);

        // Baris 3: Telepon
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.0;
        formPanel.add(new JLabel("No Telepon:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        formPanel.add(txtTelp, gbc);

        // 2. Panel Pencarian
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBorder(new EmptyBorder(5, 5, 0, 5));
        
        txtCari = new JTextField(20);
        btnCari = new JButton("Cari");
        btnResetCari = new JButton("Reset"); 
        
        searchPanel.add(new JLabel("Cari Nama:"));
        searchPanel.add(txtCari);
        searchPanel.add(btnCari);
        searchPanel.add(btnResetCari);

        // 3. Table Area
        tableModel = new DefaultTableModel(
                new String[]{"ID", "Nama", "NIK", "Alamat", "Telepon"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
            // Diperlukan agar sorting angka (ID) bekerja dengan benar (bukan urut abjad 1, 10, 2)
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) return Integer.class; // Kolom ID adalah Integer
                return String.class;
            }
        };
        table = new JTable(tableModel);
        
        // ===== FITUR SORTING =====
        table.setAutoCreateRowSorter(true); 
        // =========================
        
        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(table);

        // Wrapper Center (Search + Table)
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

    public void setTableData(List<Pasien> data) {
        tableModel.setRowCount(0);
        for (Pasien p : data) {
            tableModel.addRow(new Object[]{
                p.getId(), p.getNama(), p.getNik(), p.getAlamat(), p.getTelepon()
            });
        }
    }

    public Pasien getFormData() {
        Pasien p = new Pasien();
        p.setNama(txtNama.getText().trim());
        p.setNik(txtNIK.getText().trim());
        p.setAlamat(txtAlamat.getText().trim());
        p.setTelepon(txtTelp.getText().trim());
        return p;
    }

    public void setFormData(Pasien pasien) {
        if (pasien != null) {
            txtNama.setText(pasien.getNama());
            txtNIK.setText(pasien.getNik());
            txtAlamat.setText(pasien.getAlamat());
            txtTelp.setText(pasien.getTelepon());
        }
    }

    public void clearForm() {
        txtNama.setText("");
        txtNIK.setText("");
        txtAlamat.setText("");
        txtTelp.setText("");
        table.clearSelection();
    }
    
    public String getSearchKeyword() {
        return txtCari.getText().trim();
    }
    
    public void resetSearchField() {
        txtCari.setText("");
    }
    
    // Helper untuk mengambil ID yang benar meskipun tabel sedang di-sorting
    public int getSelectedRowId() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            // Konversi index baris view (yang ter-sort) ke index model yang asli
            int modelRow = table.convertRowIndexToModel(selectedRow);
            return (int) tableModel.getValueAt(modelRow, 0);
        }
        return -1;
    }

    public Pasien getSelectedRowData() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            // PENTING: Konversi index saat sorting aktif
            int modelRow = table.convertRowIndexToModel(selectedRow);
            
            Pasien p = new Pasien();
            p.setId((int) tableModel.getValueAt(modelRow, 0));
            p.setNama(tableModel.getValueAt(modelRow, 1).toString());
            p.setNik(tableModel.getValueAt(modelRow, 2).toString());
            p.setAlamat(tableModel.getValueAt(modelRow, 3).toString());
            p.setTelepon(tableModel.getValueAt(modelRow, 4).toString());
            return p;
        }
        return null;
    }

    public JTable getTable() { return table; }

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
            public void mouseClicked(MouseEvent e) { callback.run(); }
        });
    }

    // Feedback
    public void showMessage(String message) { JOptionPane.showMessageDialog(this, message, "Informasi", JOptionPane.INFORMATION_MESSAGE); }
    public void showError(String error) { JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE); }
    public boolean confirmDelete() {
        return JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus data ini?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION;
    }
    
    public void setEnableDelete(boolean enabled) {
        btnHapus.setEnabled(enabled);
    }
}