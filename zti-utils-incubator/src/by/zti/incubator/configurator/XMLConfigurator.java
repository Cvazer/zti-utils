package by.zti.incubator.configurator;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * This is real implementation of {@code Configurator} interface based on XML parsing from existing file
 *
 * @author Yan Frankovski
 * @since ZTIU 1.1.0
 * @see by.zti.incubator.configurator.Configurator
 */
public class XMLConfigurator implements Configurator {
    /** Reference to the class file that will be used as a source of match points to look for in xml **/
    private Class clazz;
    /** Reference to the XML file in the file system **/
    private File xml;

    /**
     * Default setter constructor that wraps xmlPath string as {@code File} object
     *
     * @param clazz - {@code Class} Reference to the end point class
     * @param xmlPath - {@code String} path to the source XML file
     */
    public XMLConfigurator(Class clazz, String xmlPath) {
        this(clazz, new File(xmlPath));
    }

    /**
     * Default setter constructor
     *
     * @param clazz - {@code Class} Reference to the end point class
     * @param xml - {@code File} reference to the source XML file
     */
    public XMLConfigurator(Class clazz, File xml) {
        this.clazz = clazz;
        this.xml = xml;
    }

    /**
     * This method should be called on {@code Configurator} instance to read key-value pairs from given source by id
     * @return {@code Properties} map that contains fieldName-value pairs
     */
    public Properties read(String id) {
        Properties params = new Properties();
        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xml);
            asList(document.getElementsByTagName("configuration")).forEach(node -> {
                if (!node.hasAttributes()){return;}
                try {node.getAttributes().getNamedItem("class").getNodeValue();} catch (Exception e) {return;}
                try {node.getAttributes().getNamedItem("id").getNodeValue();} catch (Exception e) {return;}
                if (!node.getAttributes().getNamedItem("id").getNodeValue().contentEquals(id)){return;}
                if (!node.getAttributes().getNamedItem("class").getNodeValue().contentEquals(clazz.getName())){return;}
                if (!node.hasChildNodes()) {return;}
                Arrays.asList(clazz.getDeclaredFields()).forEach(field -> asList(node.getChildNodes()).forEach(fieldNode -> {
                    if (!fieldNode.hasAttributes()){return;}
                    try {fieldNode.getAttributes().getNamedItem("name").getNodeValue();} catch (Exception e) {return;}
                    try {fieldNode.getAttributes().getNamedItem("value").getNodeValue();} catch (Exception e) {return;}
                    if (!fieldNode.getAttributes().getNamedItem("name").getNodeValue().contentEquals(field.getName())) {return;}
                    params.put(field.getName(), fieldNode.getAttributes().getNamedItem("value").getNodeValue());
                }));
            });
        } catch (SAXException | IOException | ParserConfigurationException e) {
            e.printStackTrace();
        }
        return params;
    }

    public Map readMap(String id) {
        return read(id);
    }

    /**
     * This method should be called on {@code Configurator} instance to read key-value pairs from given source by id
     * @param id - config id
     * @return {@code Properties} map that contains fieldName-value pairs
     */

    public Map<String, String> readFlat(String id){
        return readFlat(id, xml.getPath());
    }

    public static Map<String, String> readFlat(String id, String xmlPath) {
        Map<String, String> params = new HashMap<>();
        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(xmlPath));
            asList(document.getElementsByTagName("configuration")).forEach(node -> {
                if (!node.hasAttributes()){return;}
                if (!node.hasChildNodes()) {return;}
                try {Objects.requireNonNull(node.getAttributes().getNamedItem("id").getNodeValue());} catch (Exception e) {return;}
                if (!node.getAttributes().getNamedItem("id").getNodeValue().contentEquals(id)){return;}
                asList(node.getChildNodes()).forEach(field -> {
                    if(!field.hasAttributes()) {return;}
                    if(field.hasChildNodes()) {return;}
                    if(!field.getNodeName().contentEquals("field")){return;}
                    try {Objects.requireNonNull(field.getAttributes().getNamedItem("name").getNodeValue());} catch (Exception e) {return;}
                    try {Objects.requireNonNull(field.getAttributes().getNamedItem("value").getNodeValue());} catch (Exception e) {return;}
                    params.put(field.getAttributes().getNamedItem("name").getNodeValue(), field.getAttributes().getNamedItem("value").getNodeValue());
                });
            });
        } catch (SAXException | IOException | ParserConfigurationException e) {
            e.printStackTrace();
        }
        return params;
    }

    /**
     * Inner utility method to represent {@code NodeList} object as normal {@code List<Node>} one
     * @param nodeList - {@code NodeList} reference to convert from
     * @return {@code List<Node>} object containing all the nodes from nodeList
     */
    private static List<Node> asList(NodeList nodeList){
        List<Node> list = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            list.add(nodeList.item(i));
        }
        return list;
    }

}
