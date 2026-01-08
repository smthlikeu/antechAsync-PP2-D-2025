package id.ac.unpas.klinik.controller;

import id.ac.unpas.klinik.dao.DokterDAO;
import id.ac.unpas.klinik.model.Dokter;
import id.ac.unpas.klinik.util.PdfExporter;
import id.ac.unpas.klinik.view.DokterView;

import java.sql.SQLException;
import java.util.List;

public class DokterController {

    private DokterView view;
    private DokterDAO dao;
    private Dokter selectedDokter;

    public DokterController(DokterView view) {
        this.view = view;
        this.dao = new DokterDAO();
        this.selectedDokter = null;

        initController();
        loadData();
    }

    private void initController() {
        view.addTambahListener(e -> handleTambah());
        view.addEditListener(e -> handleEdit());
        view.addHapusListener(e -> handleHapus());
        view.addBersihListener(e -> handleBersih()); // Register tombol bersih
        view.addExportListener(e -> handleExport());
        
        // Listener Pencarian
        view.addCariListener(e -> handleCari());
        view.addResetCariListener(e -> handleResetCari());
        
        view.addTableSelectionListener(() -> handleTableSelection());
    }

    private void loadData() {
        try {
            List<Dokter> data = dao.getAll();
            view.setTableData(data);
        } catch (SQLException e) {
            view.showError("Gagal memuat data: " + e.getMessage());
        }
    }
    
    // ===== LOGIKA SEARCH & RESET =====
    
    private void handleCari() {
        String keyword = view.getSearchKeyword();
        
        try {
            List<Dokter> searchResults;
            
            if (keyword == null || keyword.isEmpty()) {
                searchResults = dao.getAll();
            } else {
                searchResults = dao.searchByName(keyword); // Sudah ada di DAO
            }
            
            view.setTableData(searchResults);
            
            if (searchResults.isEmpty()) {
                view.showMessage("Data dokter tidak ditemukan untuk: " + keyword);
            }
            
        } catch (SQLException e) {
            view.showError("Gagal mencari data: " + e.getMessage());
        }
    }
    
    private void handleResetCari() {
        view.resetSearchField();
        loadData(); // Load ulang semua data
    }
    
    // ================================

    private void handleTambah() {
        Dokter dokter = view.getFormData();

        String errorValidation = dokter.getValidationError();
        if (errorValidation != null) {
            view.showError(errorValidation);
            return;
        }

        try {
            dao.insert(dokter);
            view.showMessage("Data berhasil disimpan");
            loadData();
            handleBersih();
        } catch (SQLException e) {
            view.showError("Gagal menyimpan data: " + e.getMessage());
        }
    }

    private void handleEdit() {
        if (selectedDokter == null) {
            view.showError("Pilih data yang akan diedit!");
            return;
        }

        Dokter dokter = view.getFormData();
        dokter.setId(selectedDokter.getId());

        String errorValidation = dokter.getValidationError();
        if (errorValidation != null) {
            view.showError(errorValidation);
            return;
        }

        try {
            dao.update(dokter);
            view.showMessage("Data berhasil diupdate");
            loadData();
            handleBersih();
        } catch (SQLException e) {
            view.showError("Gagal mengupdate data: " + e.getMessage());
        }
    }

    private void handleHapus() {
        if (selectedDokter == null) {
            view.showError("Pilih data yang akan dihapus!");
            return;
        }

        if (!view.confirmDelete()) {
            return;
        }

        try {
            if (dao.hasRekamMedis(selectedDokter.getId())) {
                view.showError("Tidak dapat menghapus dokter yang memiliki rekam medis!");
                return;
            }

            dao.delete(selectedDokter.getId());
            view.showMessage("Data berhasil dihapus");
            loadData();
            handleBersih();
        } catch (SQLException e) {
            view.showError("Gagal menghapus data: " + e.getMessage());
        }
    }
    
    private void handleBersih() {
        view.clearForm();
        selectedDokter = null;
    }
    
    private void handleExport() {
        try {
            PdfExporter.exportToPDF(
                view.getTable(), 
                "Laporan Data Dokter", 
                "Laporan_Dokter.pdf"
            );
            view.showMessage("PDF berhasil disimpan: Laporan_Dokter.pdf");
        } catch (Exception e) {
            view.showError("Gagal export PDF: " + e.getMessage());
        }
    }

    private void handleTableSelection() {
        selectedDokter = view.getSelectedRowData();
        if (selectedDokter != null) {
            view.setFormData(selectedDokter);
        }
    }

    public void refresh() {
        loadData();
    }
}