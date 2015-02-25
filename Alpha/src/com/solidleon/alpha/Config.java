/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solidleon.alpha;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Markus
 */
public class Config implements Serializable{
    private static Config instance = new Config();

    public static Config getInstance() {
        return instance;
    }
    
    public static void save() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(instance.data + "config.xml");
            Element root = document.createElement("config");
            
            Element eData = document.createElement("data");
            eData.setTextContent(instance.data);
            root.appendChild(eData);
            Element eSound = document.createElement("sound");
            eSound.setTextContent(instance.sound);
            root.appendChild(eSound);
            Element eGfx = document.createElement("gfx");
            eGfx.setTextContent(instance.gfx);
            root.appendChild(eGfx);
            Element eSave = document.createElement("save");
            eSave.setTextContent(instance.save);
            root.appendChild(eSave);
            Element eMusic = document.createElement("music");
            eMusic.setTextContent("" + instance.music);
            root.appendChild(eMusic);
            Element eTilesize = document.createElement("tilesize");
            eTilesize.setTextContent("" + instance.tileSize);
            root.appendChild(eTilesize);
            
            document.appendChild(root);
            
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer t = tf.newTransformer();
            t.setOutputProperty(OutputKeys.INDENT, "yes");
            t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            t.transform(new DOMSource(root), new StreamResult(instance.data + "config.xml"));
            
            
        } catch (TransformerException ex) {
            Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void load() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(instance.data + "config.xml");
            Element root = document.getDocumentElement();
            
            NodeList nodeList = root.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node != null) {
                    if (node != null) {
                        if ("data".equals(node.getNodeName())) {
                            instance.data = node.getTextContent();
                        } else if ("sound".equals(node.getNodeName())) {
                            instance.sound = node.getTextContent();
                        } else if ("gfx".equals(node.getNodeName())) {
                            instance.gfx = node.getTextContent();
                        } else if ("save".equals(node.getNodeName())) {
                            instance.save = node.getTextContent();
                        } else if ("music".equals(node.getNodeName())) {
                            try {
                                System.out.println("MUSIC: " + node.getTextContent());
                                System.out.println(" --> " + Float.parseFloat(node.getTextContent()));
                                instance.music = Float.parseFloat(node.getTextContent());
                                System.out.println(" -->" + instance.music);
                                
                            } catch (NumberFormatException ex) {
                                instance.music = 1.0f;
                            }
                        } else if ("tilesize".equals(node.getNodeName())) {
                            instance.tileSize = Integer.parseInt(node.getTextContent());
                        }
                    }
//                    NodeList subNodeList = root.getChildNodes();
//                    for (int j = 0; j < subNodeList.getLength(); j++) {
//                        Node subNode = subNodeList.item(j);
//                    }
                }
            }
            
        } catch (SAXException ex) {
            Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String data = "data/";
    public String sound = "data/sound/";
    public String gfx = "data/gfx/";
    public String save = "data/sav.dat";
    public float music = 1.0f;
    public int tileSize = 16;
    public int tileMask=4;
    
    public void updateTileMask() {
        tileMask = (int) (Math.log(tileSize) / Math.log(2));
    }
    
}
