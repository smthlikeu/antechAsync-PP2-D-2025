package id.ac.unpas.klinik.util; // <-- Sesuai dengan folder di gambar Anda

// Import library dari iText (Wajib update pom.xml agar ini tidak error)
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

// Import komponen Swing bawaan Java
import javax.swing.JTable;
import javax.swing.JOptionPane;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Class untuk export data JTable ke PDF
 * Lokasi: id.ac.unpas.klinik.util
 */
public class PdfExporter {
    
    public static void exportToPDF(JTable table, String title, String filePath) {
        // Membuat dokumen PDF baru (menggunakan library iText)
        Document document = new Document();
        
        try {
            // Menyiapkan writer untuk menulis ke file
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // 1. Menambahkan Judul
            Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph paragrafJudul = new Paragraph(title, fontTitle);
            paragrafJudul.setAlignment(Element.ALIGN_CENTER);
            paragrafJudul.setSpacingAfter(20);
            document.add(paragrafJudul);

            // 2. Membuat Tabel PDF
            PdfPTable pdfTable = new PdfPTable(table.getColumnCount());
            pdfTable.setWidthPercentage(100);
            
            // 3. Menambahkan Header Kolom
            Font fontHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            for (int i = 0; i < table.getColumnCount(); i++) {
                String colName = table.getColumnName(i);
                PdfPCell cell = new PdfPCell(new Phrase(colName, fontHeader));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfTable.addCell(cell);
            }
            
            // 4. Menambahkan Data Baris
            for (int rows = 0; rows < table.getRowCount(); rows++) {
                for (int cols = 0; cols < table.getColumnCount(); cols++) {
                    Object value = table.getModel().getValueAt(rows, cols);
                    String text = (value == null) ? "" : value.toString();
                    pdfTable.addCell(text);
                }
            }
            
            document.add(pdfTable);
            System.out.println("PDF Berhasil dibuat: " + filePath);
            
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
            // Tampilkan error ke user jika gagal
            JOptionPane.showMessageDialog(null, "Gagal export PDF: " + e.getMessage());
        } finally {
            // Pastikan dokumen selalu ditutup untuk mencegah file corrupt
            if (document.isOpen()) {
                document.close();
            }
        }
    }
}