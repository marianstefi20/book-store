package app.document;

import app.book.Book;

import java.util.List;

/**
 * Created by maria on 5/9/2017.
 */
public interface ExtDocument {
    public static final Object [] FILE_HEADER = {"isbn","title","author","description","genre"};
    public void create(List<Book> books);
}
