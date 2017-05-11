package app.store;

import app.book.BookDao;
import app.login.*;
import app.util.*;
import org.apache.commons.lang.ObjectUtils;
import spark.*;
import java.util.*;
import static app.Application.bookDao;
import static app.util.JsonUtil.*;
import static app.util.RequestUtil.*;

public class StoreController {
    private static Double totalSum(ArrayList<ArrayList<String>> orders) {
        Double suma = 0.0;
        try {
            for (ArrayList<String> order : orders) {
                suma += Double.parseDouble(order.get(4));
            }
            return suma;
        } catch(NumberFormatException | NullPointerException e) {
            return 0.0;
        }
    }

    public static Route fetchCart = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        if (clientAcceptsHtml(request)) {
            HashMap<String, Object> model = new HashMap<>();
            model.put("orders", request.session().attribute("orders"));
            model.put("totalSum", String.format("%.2f", totalSum(request.session().attribute("orders"))));
            return ViewUtil.render(request, model, Path.Template.CART);
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };

    public static Route checkout = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        if (clientAcceptsHtml(request)) {
            HashMap<String, Object> model = new HashMap<>();
            ArrayList<ArrayList<String>> orders = request.session().attribute("orders");
            for(ArrayList<String> order: orders) {
                String isbn = order.get(3);
                System.out.println(isbn);
                BookDao.decreaseNrByIsbn(isbn);
            }
            request.session().removeAttribute("orders");
            request.session().attribute("orderNr", 0);
            model.put("orders", new ArrayList<>());
            model.put("totalSum", String.format("%.2f", totalSum(request.session().attribute("orders"))));
            return ViewUtil.render(request, model, Path.Template.CART);
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };
}
