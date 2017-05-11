package app.book;

import app.login.*;
import app.util.*;
import spark.*;
import java.util.*;
import static app.Application.bookDao;
import static app.util.JsonUtil.*;
import static app.util.RequestUtil.*;

public class BookController {

    public static Route fetchAllBooks = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        if (clientAcceptsHtml(request)) {
            HashMap<String, Object> model = new HashMap<>();
            model.put("books", bookDao.getAllBooks());
            return ViewUtil.render(request, model, Path.Template.BOOKS_ALL);
        }
        if (clientAcceptsJson(request)) {
            return dataToJson(bookDao.getAllBooks());
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };

    public static Route fetchOneBook = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        if (clientAcceptsHtml(request)) {
            HashMap<String, Object> model = new HashMap<>();
            Book book = bookDao.getBookByIsbn(getParamIsbn(request));
            model.put("book", book);
            return ViewUtil.render(request, model, Path.Template.BOOKS_ONE);
        }
        if (clientAcceptsJson(request)) {
            return dataToJson(bookDao.getBookByIsbn(getParamIsbn(request)));
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };

    public static Route handleOrder = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        if (clientAcceptsHtml(request)) {
            HashMap<String, Object> model = new HashMap<>();
            Book book = bookDao.getBookByIsbn(getParamIsbn(request));

            ArrayList<String> bookProp = new ArrayList<>();
            bookProp.add(book.getTitle());
            bookProp.add(book.getAuthor());
            bookProp.add(book.getDescription());
            bookProp.add(book.getIsbn());
            bookProp.add(book.getPrice().toString());

            ArrayList<ArrayList<String>> currentOrders = request.session().attribute("orders");
            ArrayList<ArrayList<String>> orders = (currentOrders == null) ? new ArrayList<>() : currentOrders;
            orders.add(bookProp);

            request.session().attribute("orders",orders);
            int orderNr = request.session().attribute("orderNr");
            request.session().attribute("orderNr", orderNr+1);

            model.put("book",book);
            return ViewUtil.render(request, model, Path.Template.BOOKS_ONE);
        }
        if (clientAcceptsJson(request)) {
            return dataToJson(bookDao.getBookByIsbn(getParamIsbn(request)));
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };
}
