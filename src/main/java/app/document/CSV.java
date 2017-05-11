package app.document;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import app.book.Book;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
public class CSV implements ExtDocument {
    //Delimiter used in CSV file
    private static final String NEW_LINE_SEPARATOR = "\n";

    public static final String DEST = "D:\\Desktop\\Anul_III\\Semestrul_II\\PS\\Assignment2\\src\\main\\resources\\Data\\books.csv";


    @Override
    public void create(List<Book> books) {
        FileWriter fileWriter = null;
        CSVPrinter csvFilePrinter = null;
        //Create the CSVFormat object with "\n" as a record delimiter
        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator(NEW_LINE_SEPARATOR);

        try {
            //initialize FileWriter object
            fileWriter = new FileWriter(DEST);
            //initialize CSVPrinter object
            csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);
            //Create CSV file header
            csvFilePrinter.printRecord(FILE_HEADER);
            //Write a new student object list to the CSV file
            for (Book b : books) {
                List bookDataRecord = new ArrayList();
                bookDataRecord.add(b.getIsbn());
                bookDataRecord.add(b.getTitle());
                bookDataRecord.add(b.getAuthor());
                bookDataRecord.add(b.getDescription());
                bookDataRecord.add(b.getGenre());
                csvFilePrinter.printRecord(bookDataRecord);
            }
            System.out.println("CSV file was created successfully !!!");
        } catch (Exception e) {
            System.out.println("Error in CsvFileWriter !!!");
            e.printStackTrace();
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
                csvFilePrinter.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter/csvPrinter !!!");
                e.printStackTrace();
            }
        }
    }
}
