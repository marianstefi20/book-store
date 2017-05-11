package app.admin;

import app.book.Book;
import app.document.DocumentFactory;
import app.document.ExtDocument;
import app.document.PDF;
import app.login.LoginController;
import app.user.User;
import app.util.Path;
import app.util.ViewUtil;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static app.Application.bookDao;
import static app.Application.userDao;
import static app.document.DocumentFactory.getDocument;
import static app.util.JsonUtil.dataToJson;
import static app.util.RequestUtil.clientAcceptsHtml;
import static app.util.RequestUtil.clientAcceptsJson;
import static app.util.RequestUtil.getParamIsbn;

/**
 * Created by maria on 5/10/2017.
 */
public class AdminController {

    public static Route populateData = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        if (clientAcceptsHtml(request)) {
            HashMap<String, Object> model = new HashMap<>();
            model.put("books", bookDao.getAllBooks());
            return ViewUtil.render(request, model, Path.Template.ADMIN);
        }
        if (clientAcceptsJson(request)) {
            return dataToJson(bookDao.getBookByIsbn(getParamIsbn(request)));
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };

    public static Route pdforcsv = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        String pdf = request.queryParams("pdf");
        String csv = request.queryParams("csv");
        System.out.println("pdf: " +pdf + " si csv:" + csv);
        HashMap<String, Object> model = new HashMap<>();
        model.put("books", bookDao.getAllBooks());
        if (clientAcceptsHtml(request)) {
            Iterable<Book> books = bookDao.getAllBooks();
            List<Book> emptyBooks = new ArrayList<>();
            for (Book book : books) {
                Integer nr = book.getNr();
                if (nr == 0) {
                    emptyBooks.add(book);
                }
            }
            if(pdf.equals("1")) {
                ExtDocument doc1 = DocumentFactory.getDocument("pdf");
                doc1.create(emptyBooks);
                System.out.println("Creat document pdf");
            }
            if(csv.equals("1")) {
                ExtDocument doc2 = DocumentFactory.getDocument("csv");
                doc2.create(emptyBooks);
                System.out.println("Creat document csv");
            }
            return ViewUtil.render(request, model, Path.Template.ADMIN);
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };
}
