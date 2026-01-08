package id.ac.unpas.klinik.controller;

import id.ac.unpas.klinik.dao.DokterDAO;
import id.ac.unpas.klinik.dao.PasienDAO;
import id.ac.unpas.klinik.dao.RekamMedisDAO;
import id.ac.unpas.klinik.model.Dokter;
import id.ac.unpas.klinik.model.Pasien;
import id.ac.unpas.klinik.model.RekamMedis;
import id.ac.unpas.klinik.util.PdfExporter;
import id.ac.unpas.klinik.view.RekamMedisView;

import java.sql.SQLException;
import java.util.List;

public class RekamMedisController {

    private RekamMedisView view;
    private RekamMedisDAO rekamDAO;
    private PasienDAO pasienDAO;
    private DokterDAO dokterDAO;
    private int selectedId = -1;

    public RekamMedisController(RekamMedisView view) {
        this.view = view;
        this.rekamDAO = new RekamMedisDAO();
        this.pasienDAO = new PasienDAO();
        this.dokterDAO = new DokterDAO();

        initController();
        loadComboBoxData();
        loadTableData();
    }

    private void initController() {
        view.addSimpanListener(e -> handleSimpan());
        view.addHapusListener(e -> handleHapus());
        view.addBersihListener(e -> handleBersih());
        view.addExportListener(e -> handleExport());
        
        // Listener Search & Reset
        view.addCariListener(e -> handleCari());
        view.addResetCariListener(e -> handleResetCari());
        
        view.addTableSelectionListener(() -> handleTableSelection());
    }

    private void loadComboBoxData() {
        try {
            List<Pasien> pasienList = pasienDAO.getAll();
            view.setPasienComboData(pasienList);

            List<Dokter> dokterList = dokterDAO.getAll();
            view.setDokterComboData(dokterList);
        } catch (SQLException e) {
            view.showError("Gagal memuat data pasien/dokter: " + e.getMessage());
        }
    }

    private void loadTableData() {
        try {
            List<RekamMedis> data = rekamDAO.getAll();
            view.setTableData(data);
        } catch (SQLException e) {
            view.showError("Gagal memuat data: " + e.getMessage());
        }
    }
    
    // ===== LOGIKA SEARCH (By Diagnosa) =====
    
    private void handleCari() {
        String keyword = view.getSearchKeyword();
        
        try {
            List<RekamMedis> searchResults;
            
            if (keyword == null || keyword.isEmpty()) {
                searchResults = rekamDAO.getAll();
            } else {
                // Menggunakan DAO searchByDiagnosa
                searchResults = rekamDAO.searchByDiagnosa(keyword);
            }
            
            view.setTableData(searchResults);
            
            if (searchResults.isEmpty()) {
                view.showMessage("Tidak ditemukan rekam medis dengan diagnosa: " + keyword);
            }
            
        } catch (SQLException e) {
            view.showError("Gagal mencari data: " + e.getMessage());
        }
    }
    
    private void handleResetCari() {
        view.resetSearchField();
        loadTableData(); // Reset ke semua data
    }
    
    // =======================================

    private void handleSimpan() {
        RekamMedis rekamMedis = view.getFormData();

        String errorValidation = rekamMedis.getValidationError();
        if (errorValidation != null) {
            view.showError(errorValidation);
            return;
        }

        try {
            int newId = rekamDAO.insert(rekamMedis);
            view.showMessage("Rekam medis berhasil disimpan dengan ID: " + newId);
            loadTableData();
            handleBersih();
        } catch (SQLException e) {
            view.showError("Gagal menyimpan rekam medis: " + e.getMessage());
        }
    }

    private void handleHapus() {
        selectedId = view.getSelectedRowId();

        if (selectedId < 0) {
            view.showError("Pilih data yang akan dihapus!");
            return;
        }

        if (!view.confirmDelete()) {
            return;
        }

        try {
            rekamDAO.delete(selectedId);
            view.showMessage("Data berhasil dihapus");
            loadTableData();
            handleBersih();
        } catch (SQLException e) {
            view.showError("Gagal menghapus data: " + e.getMessage());
        }
    }

    private void handleBersih() {
        view.clearForm();
        selectedId = -1;
    }
    
    private void handleExport() {
        try {
            PdfExporter.exportToPDF(
                view.getTable(), 
                "Laporan Rekam Medis", 
                "Laporan_RekamMedis.pdf"
            );
            view.showMessage("PDF berhasil disimpan: Laporan_RekamMedis.pdf");
        } catch (Exception e) {
            view.showError("Gagal export PDF: " + e.getMessage());
        }
    }

    private void handleTableSelection() {
        selectedId = view.getSelectedRowId();
    }

    public void refresh() {
        loadComboBoxData();
        loadTableData();
    }
}