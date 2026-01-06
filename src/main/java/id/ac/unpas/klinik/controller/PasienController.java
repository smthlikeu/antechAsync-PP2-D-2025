package id.ac.unpas.klinik.controller;

import id.ac.unpas.klinik.dao.PasienDAO;
import id.ac.unpas.klinik.model.Pasien;
import id.ac.unpas.klinik.util.PdfExporter;
import id.ac.unpas.klinik.view.PasienView;

import java.sql.SQLException;
import java.util.List;

public class PasienController {
    
    private PasienView view;
    private PasienDAO dao;
    private Pasien selectedPasien;
    
    public PasienController(PasienView view) {
        this.view = view;
        this.dao = new PasienDAO();
        this.selectedPasien = null;
        
        initController();
        loadData();
    }
    
    private void initController() {
        view.addTambahListener(e -> handleTambah());
        view.addEditListener(e -> handleEdit());
        view.addHapusListener(e -> handleHapus());
        view.addBersihListener(e -> handleBersih());
        view.addExportListener(e -> handleExport());
        
        // Listener Pencarian
        view.addCariListener(e -> handleCari());
        view.addResetCariListener(e -> handleResetCari()); // Register Reset
        
        view.addTableSelectionListener(() -> handleTableSelection());
    }
    
    private void loadData() {
        try {
            List<Pasien> data = dao.getAll();
            view.setTableData(data);
        } catch (SQLException e) {
            view.showError("Gagal memuat data: " + e.getMessage());
        }
    }
    
    // ===== LOGIKA SEARCH & RESET =====
    
    private void handleCari() {
        String keyword = view.getSearchKeyword();
        
        try {
            List<Pasien> searchResults;
            
            if (keyword == null || keyword.isEmpty()) {
                searchResults = dao.getAll();
            } else {
                searchResults = dao.searchByName(keyword);
            }
            
            view.setTableData(searchResults);
            
            if (searchResults.isEmpty()) {
                view.showMessage("Data tidak ditemukan untuk: " + keyword);
            }
            
        } catch (SQLException e) {
            view.showError("Gagal mencari data: " + e.getMessage());
        }
    }
    
    private void handleResetCari() {
        view.resetSearchField(); // Kosongkan text field
        loadData(); // Load ulang semua data
    }
    
    // ================================
    
    private void handleTambah() {
        Pasien pasien = view.getFormData();
        
        String errorValidation = pasien.getValidationError();
        if (errorValidation != null) {
            view.showError(errorValidation);
            return;
        }
        
        try {
            dao.insert(pasien);
            view.showMessage("Data berhasil disimpan");
            loadData();
            handleBersih();
        } catch (SQLException e) {
            view.showError("Gagal menyimpan data: " + e.getMessage());
        }
    }
    
    private void handleEdit() {
        if (selectedPasien == null) {
            view.showError("Pilih data yang akan diedit!");
            return;
        }
        
        Pasien pasien = view.getFormData();
        pasien.setId(selectedPasien.getId());
        
        String errorValidation = pasien.getValidationError();
        if (errorValidation != null) {
            view.showError(errorValidation);
            return;
        }
        
        try {
            dao.update(pasien);
            view.showMessage("Data berhasil diupdate");
            loadData();
            handleBersih();
        } catch (SQLException e) {
            view.showError("Gagal mengupdate data: " + e.getMessage());
        }
    }
    
    private void handleHapus() {
        if (selectedPasien == null) {
            view.showError("Pilih data yang akan dihapus!");
            return;
        }
        
        if (!view.confirmDelete()) {
            return;
        }
        
        try {
            dao.delete(selectedPasien.getId());
            view.showMessage("Data berhasil dihapus");
            loadData();
            handleBersih();
        } catch (SQLException e) {
            view.showError("Gagal menghapus data: " + e.getMessage());
        }
    }
    
    private void handleBersih() {
        view.clearForm();
        selectedPasien = null;
    }
    
    private void handleTableSelection() {
        selectedPasien = view.getSelectedRowData();
        if (selectedPasien != null) {
            view.setFormData(selectedPasien);
        }
    }
    
    private void handleExport() {
        try {
            PdfExporter.exportToPDF(
                view.getTable(), 
                "Laporan Data Pasien", 
                "Laporan_Pasien.pdf"
            );
            view.showMessage("PDF berhasil disimpan: Laporan_Pasien.pdf");
        } catch (Exception e) {
            view.showError("Gagal export PDF: " + e.getMessage());
        }
    }
    
    public void refresh() {
        loadData();
    }
}