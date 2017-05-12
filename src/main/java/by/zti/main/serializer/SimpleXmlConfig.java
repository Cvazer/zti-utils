package by.zti.main.serializer;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

/**
 * This class is a simple implementation of native Java XML DOM functionality. This class should be used only to store
 * simple key-value String parameters in specified file. Invoke {@link #put(String, String)} method to save your
 * config parameter to file, and invoke {@link #get(String)} method to get parameter value from instance object.
 */
public class SimpleXmlConfig {
    /** Internal reference to cfg {@link java.io.File} instance. **/
    private File file;
    /** Internal reference to {@link org.w3c.dom.Document} object. **/
    private Document document;
    /** Root element for given {@link org.w3c.dom.Document} object. **/
    private Element root;
    /** Default file name string. **/
    private String defailtRoot = "config";

    /**
     * This constructor will use {@link #defailtRoot} string to reference file.
     */
    public SimpleXmlConfig() {
        this(new File("config.xml"));
    }

    /**
     * This constructor creates {@link File} reference from given.
     * @param path Path to config file.
     */
    public SimpleXmlConfig(String path) {
        this(new File(path));
    }

    /**
     * This constructor takes {@link File} reference to config file to work with. if given file is already exists -
     * it will load it, else it will create new file.
     * @param file Instance of file to work with/
     */
    public SimpleXmlConfig(File file) {
        try {
            this.file = file;
            if(!file.exists()) {
                file.createNewFile();
                document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
                document.appendChild(document.createElement(defailtRoot));
            } else {
                document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
            }
            root = document.getDocumentElement();
            write();
        } catch (IOException | ParserConfigurationException | SAXException | TransformerException e ) {
            e.printStackTrace();
        }
    }

    /**
     * Invoke this method when you want to get value of tag in your config file.
     * @param key Tag name.
     * @return Value of tag.
     */
    public String get(String key){
        read();
        NodeList list = root.getElementsByTagName(key);
        if(list.getLength()==0){return null;}
        return list.item(0).getChildNodes().item(0).getTextContent();
    }

    /**
     * Invoke this method when you want to store data as tag value in your xml config file.
     * If tag by given key is already exists in config file it will override it's current value, else it
     * will create new tag with given key as name.
     * @param key Tag name.
     * @param value Value for tag.
     */
    public void put(String key, String value){
        NodeList list = root.getElementsByTagName(key);
        if(list.getLength()==1){
            if(!(list.item(0).getNodeType()== Node.ELEMENT_NODE)){return;}
            Element element = (Element) list.item(0);
            element.getChildNodes().item(0).setNodeValue(value);
        } else {
            Element element = document.createElement(key);
            element.appendChild(document.createTextNode(value));
            root.appendChild(element);
        }
        try {
            write();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Internal method that provides read functionality.
     */
    private void read(){
        try {
            document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
            root = document.getDocumentElement();
            root.normalize();
        } catch (SAXException | ParserConfigurationException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Internal method that provides write functionality.
     */
    private void write() throws TransformerException {
        document.getDocumentElement().normalize();
        TransformerFactory factory = TransformerFactory.newInstance();
        factory.setAttribute("indent-number", 2);
        Transformer transformer = factory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        StreamResult result = new StreamResult(file);
        DOMSource source = new DOMSource(document);
        transformer.transform(source, result);
    }

    public String getDefailtRoot() {
        return defailtRoot;
    }

    public void setDefailtRoot(String defailtRoot) {
        this.defailtRoot = defailtRoot;
    }
}
