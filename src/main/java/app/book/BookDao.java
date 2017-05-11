package app.book;

import java.io.File;
import java.util.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class BookDao {
    private static final String BOOK_BUCKET = "D:\\Desktop\\Anul_III\\Semestrul_II\\PS\\Assignment2\\src\\main\\resources\\Data\\books.xml";
    private static XPath xpath = null;
    private static Document doc = null;
    private static List<Book> books = null;

    public BookDao() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            this.doc = builder.parse(BOOK_BUCKET);
            // Create XPathFactory object
            XPathFactory xpathFactory = XPathFactory.newInstance();
            // Create XPath object
            this.xpath = xpathFactory.newXPath();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        books = BookDao.getBooks();
    }

    private static List<String> generalQuery(String query) {
        List<String> list = new ArrayList<>();
        try {
            //create XPathExpression object
            XPathExpression expr = xpath.compile(query);
            //evaluate expression result on XML document
            NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            for (int i = 0; i < nodes.getLength(); i++)
                list.add(nodes.item(i).getNodeValue());
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<String> getGenres() {
        return generalQuery("Books/Book/genre/text()");
    }

    public static List<String> getTitles() {
        return generalQuery("/Books/Book/title/text()");
    }

    public static List<String> getAuthors() {
        return generalQuery("/Books/Book/author/text()");
    }

    public static List<String> getQuantities() { return generalQuery("/Books/Book/nr/text()"); }

    public static List<String> getPrices() {return generalQuery("/Books/Book/price/text()");}

    public static List<String> getISBNs() {
        return generalQuery("/Books/Book/isbn/text()");
    }

    public static List<String> getDescription() {
        return generalQuery("/Books/Book/description/text()");
    }

    public static List<String> getBooksByGenre(String bookGenre) {
        return generalQuery("/Books/Book[genre = bookGenre]/title/text()");
    }

    public static String getBookTitleById(int id) {
        String name = null;
        try {
            XPathExpression expr = xpath.compile("/Books/Book[@id='" + id + "']/title/text()");
            name = (String) expr.evaluate(doc, XPathConstants.STRING);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return name;
    }

    private static List<Book> getBooks() {
        List<String> bGenres = BookDao.getGenres();
        List<String> bTitles = BookDao.getTitles();
        List<String> bAuthors = BookDao.getAuthors();
        List<String> bISBNs = BookDao.getISBNs();
        List<String> bNrs = BookDao.getQuantities();
        List<String> bPrices = BookDao.getPrices();
        List<String> bDescriptions = BookDao.getDescription();
        List<Book> books = new ArrayList<>();
        for(int i=0;i<bTitles.size();i++) {
            books.add(new Book(bGenres.get(i),
                    bTitles.get(i),
                    bAuthors.get(i),
                    bISBNs.get(i),
                    Integer.parseInt(bNrs.get(i)),
                    Double.parseDouble(bPrices.get(i)),
                    bDescriptions.get(i)));
        }
        return books;
    }

    public Iterable<Book> getAllBooks() {
        return books;
    }

    public Book getBookByIsbn(String isbn) {
        return books.stream().filter(b -> b.getIsbn().equals(isbn)).findFirst().orElse(null);
    }

    public Book getRandomBook() {
        return books.get(new Random().nextInt(books.size()));
    }

    public static String setGenreByIsbn(String bookIsbn, String genre) {
        NodeList node = null;
        try {
            XPathExpression expr = xpath.compile("/Books/Book[isbn="+bookIsbn+"]/genre/text()");
            node = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            node.item(0).setNodeValue(genre);
            return "Modified genre";
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return "Could not edit the genre -> some problem appeared!";
    }

    private static void write() {
        try {
            doc.getDocumentElement().normalize();
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(BOOK_BUCKET));
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(source, result);
            System.out.println("XML file updated successfully");
        } catch(TransformerException e) {
            e.printStackTrace();
        }
    }

    public static void decreaseNrByIsbn(String bookIsbn) {
        NodeList nodeBooks = doc.getElementsByTagName("Book");
        Element nodeBook = null;
        Book bookValue = books.stream().filter(b -> b.getIsbn().equals(bookIsbn)).findFirst().orElse(null);
        Integer value = bookValue.getNr() - 1;
        //loop for each books
        for(int i=0; i<nodeBooks.getLength();i++){
            nodeBook = (Element) nodeBooks.item(i);
            Node nr = nodeBook.getElementsByTagName("nr").item(0).getFirstChild();
            Node isbn = nodeBook.getElementsByTagName("isbn").item(0).getFirstChild();
            if(isbn.getTextContent() == bookIsbn) {
                System.out.println("facem actualizarea la numarul de copii");
                nr.setNodeValue(value.toString());
                write();
            }
        }
    }

}
