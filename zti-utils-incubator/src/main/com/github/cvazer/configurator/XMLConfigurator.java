package com.github.cvazer.configurator;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is real implementation of {@code Configurator} interface based on XML parsing from existing file
 *
 * @author Yan Frankovski
 * @since ZTIU 1.1.0
 * @see Configurator
 */
public class XMLConfigurator implements Configurator {
    /** Reference to the XML file in the file system **/
    private File xml;

    /**
     * Default setter constructor that wraps xmlPath string as {@code File} object
     *
     * @param xml - {@code File} representing configurations
     */
    public XMLConfigurator(File xml) {
        this.xml = xml;
    }

    /**
     * This method should be called on {@code Configurator} instance to read key-value pairs from given source by id
     * @return {@code Map<String, String>} map that contains name-value pairs
     */
    public Map<String, String> read(String id) {
        Map<String, String> params = new HashMap<>();
        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xml);
            asList(document.getElementsByTagName("configuration")).forEach(configuration -> {
                if (!configuration.getAttributes().getNamedItem("id").getNodeValue().contentEquals(id)){return;}
                asList(configuration.getChildNodes()).forEach(param -> {
                    if (!param.getNodeName().contentEquals("param")){return;}
                    params.put(
                            param.getAttributes().getNamedItem("name").getNodeValue(),
                            param.getAttributes().getNamedItem("value").getNodeValue()
                    );
                });
            });
        } catch (SAXException | IOException | ParserConfigurationException ignored) {}
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
