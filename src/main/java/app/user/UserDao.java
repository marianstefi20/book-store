package app.user;

import java.util.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class UserDao {
    private static final String USERS_BUCKET = "D:\\Desktop\\Anul_III\\Semestrul_II\\PS\\Assignment2\\src\\main\\resources\\Data\\users.xml";
    private static XPath xpath = null;
    private static Document doc = null;
    private static List<User> users = null;

    public UserDao() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            this.doc = builder.parse(USERS_BUCKET);
            // Create XPathFactory object
            XPathFactory xpathFactory = XPathFactory.newInstance();
            // Create XPath object
            this.xpath = xpathFactory.newXPath();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        users = UserDao.getUsers();
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

    public static List<String> getNames() {
        List<String> list = new ArrayList<>();
        try {
            //create XPathExpression object
            XPathExpression expr = xpath.compile("/Users/User/name/text()");
            //evaluate expression result on XML document
            NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            for (int i = 0; i < nodes.getLength(); i++)
                list.add(nodes.item(i).getNodeValue());
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<String> getUsernames() {
        return generalQuery("/Users/User/username/text()");
    }

    private static List<String> getPasswords() {
        return generalQuery("/Users/User/password/text()");
    }

    private static List<String> getSalts() {
        return generalQuery("/Users/User/salt/text()");
    }

    private static List<String> getRoles() {
        return generalQuery("/Users/User/role/text()");
    }

    private static List<User> getUsers() {
        List<String> uNames = UserDao.getNames();
        List<String> uUsernames = UserDao.getUsernames();
        List<String> uPasswords = UserDao.getPasswords();
        List<String> uSalts = UserDao.getSalts();
        List<String> uRoles = UserDao.getRoles();
        List<User> users = new ArrayList<>();
        for(int i=0;i<uNames.size();i++) {
            users.add(new User(uNames.get(i), uUsernames.get(i), uPasswords.get(i), uSalts.get(i), uRoles.get(i)));
        }
        return users;
    }

    public User getUserByUsername(String username) {
        return users.stream().filter(b -> b.getUsername().equals(username)).findFirst().orElse(null);
    }

    public Iterable<String> getAllUserNames() {
        return users.stream().map(User::getUsername).collect(Collectors.toList());
    }

    public static String getRole(String username) {
        User temp = users.stream().filter(b->b.getUsername().equals(username)).findFirst().orElse(null);
        if(temp!=null)
            return temp.getRole();
        return null;
    }

}
