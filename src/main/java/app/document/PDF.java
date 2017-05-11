package app.document;
import app.book.Book;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public class PDF implements ExtDocument {
    public static final String DEST = "D:\\Desktop\\Anul_III\\Semestrul_II\\PS\\Assignment2\\src\\main\\resources\\Data\\books.pdf";

    @Override
    public void create(List<Book> books) {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        PdfDocument pdfDoc = null;
        try {
            pdfDoc = new PdfDocument(new PdfWriter(DEST));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Document doc = new Document(pdfDoc);
        Table table = new Table(FILE_HEADER.length);
        for (Book b: books) {
            table.addCell(b.getIsbn());
            table.addCell(b.getTitle());
            table.addCell(b.getAuthor());
            table.addCell(b.getDescription());
            table.addCell(b.getGenre());
        }
        doc.add(table);

        doc.close();
    }

}
