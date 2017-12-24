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
     * This method should be called on {@code Configurator} instance to read key-value pairs from given source
     * @return {@code Properties} map that contains fieldName-value pairs
     */
    public Properties read() {
        Properties params = new Properties();
        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xml);
            asList(document.getElementsByTagName("configuration")).forEach(node -> {
                if (!node.hasAttributes()){return;}
                try {node.getAttributes().getNamedItem("class").getNodeValue();} catch (Exception e) {return;}
                if (!node.getAttributes().getNamedItem("class").getNodeValue().contentEquals(clazz.getName())){return;}
                if (!node.hasChildNodes()) {return;}
                Arrays.asList(clazz.getDeclaredFields()).forEach(field -> {
                    asList(node.getChildNodes()).forEach(fieldNode -> {
                        if (!fieldNode.hasAttributes()){return;}
                        try {fieldNode.getAttributes().getNamedItem("name").getNodeValue(); fieldNode.getAttributes().getNamedItem("value").getNodeValue();} catch (Exception e) {return;}
                        if (!fieldNode.getAttributes().getNamedItem("name").getNodeValue().contentEquals(field.getName())) {return;}
                        params.put(field.getName(), fieldNode.getAttributes().getNamedItem("value").getNodeValue());
                    });
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
