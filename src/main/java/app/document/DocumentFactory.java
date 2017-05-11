package app.document;

import java.util.Map;

/**
 * Created by maria on 5/9/2017.
 */
public class DocumentFactory {
    public static ExtDocument getDocument(String action) {
        switch(action) {
            case "pdf":
                return new PDF();
            case "csv":
                return new CSV();
            default:
                return null;
        }
    }
}
