
package ioc.informaviescat.Controller;


import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author Pau Cors Bardolet
 */
public class UserSaver {
    private static final String XMLFILE = "data.xml"; 

    /**
     * Guarda les dades d'entrada de l'usuari (falta encriptar dades)
     *
     * @param username username del usuari
     * @param password Clau de pass.
     */
    public static void saveCredentials(String username, String password) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc;

            File file = new File(XMLFILE);
            if (file.exists()) {
                doc = dBuilder.parse(file);
            } else {
                doc = dBuilder.newDocument();
                doc.appendChild(doc.createElement("data"));
            }

            Element dataElement = doc.getDocumentElement();

            NodeList usernameNodes = dataElement.getElementsByTagName("username");
            NodeList passwordNodes = dataElement.getElementsByTagName("password");
            if (usernameNodes.getLength() > 0) {
                dataElement.removeChild(usernameNodes.item(0));
            }
            if (passwordNodes.getLength() > 0) {
                dataElement.removeChild(passwordNodes.item(0));
            }

            Element usernameElement = doc.createElement("username");
            usernameElement.appendChild(doc.createTextNode(username));
            Element passwordElement = doc.createElement("password");
            passwordElement.appendChild(doc.createTextNode(password));

            dataElement.appendChild(usernameElement);
            dataElement.appendChild(passwordElement);

            javax.xml.transform.TransformerFactory transformerFactory = javax.xml.transform.TransformerFactory.newInstance();
            javax.xml.transform.Transformer transformer = transformerFactory.newTransformer();
            javax.xml.transform.dom.DOMSource source = new javax.xml.transform.dom.DOMSource(doc);
            javax.xml.transform.stream.StreamResult result = new javax.xml.transform.stream.StreamResult(new File(XMLFILE));
            transformer.transform(source, result);
        } catch (ParserConfigurationException | org.xml.sax.SAXException | IOException | javax.xml.transform.TransformerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Recupera les dades d'entrada de l'usuari (falta desencriptació)
     *
     * @return dades d'entrada per a carregar
     */
    public static String[] restoreCredentials() {
        try {
            File file = new File(XMLFILE);
            if (!file.exists()) {
                return new String[]{"", ""};
            }

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);

            Element dataElement = doc.getDocumentElement();
            NodeList usernameNodes = dataElement.getElementsByTagName("username");
            NodeList passwordNodes = dataElement.getElementsByTagName("password");

            String username = (usernameNodes.getLength() > 0) ? usernameNodes.item(0).getTextContent() : "";
            String password = (passwordNodes.getLength() > 0) ? passwordNodes.item(0).getTextContent() : "";

            return new String[]{username, password};
        } catch (ParserConfigurationException | org.xml.sax.SAXException | IOException e) {
            e.printStackTrace();
        }

        return new String[]{"", ""};
    }
}
