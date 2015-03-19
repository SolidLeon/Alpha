/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solidleon.alpha.helpsystem;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Markus
 */
public class HelpSystem {
    private static final HelpSystem instance = new HelpSystem();

    public static HelpSystem getInstance() {
        return instance;
    }

    private List<HelpTopic> topics = new ArrayList<HelpTopic>();
    
    private HelpSystem() {
    }
    
    public HelpTopic getMatchingTopic(String search) {
        search = search.toLowerCase();
        //Exact match
        for (int i = 0; i < topics.size(); i++) {
            HelpTopic topic = topics.get(i);
            if (topic.topicName.equalsIgnoreCase(search)) {
                return topic;
            }
        }
        //Start match
        for (int i = 0; i < topics.size(); i++) {
            HelpTopic topic = topics.get(i);
            if (topic.topicName.startsWith(search)) {
                return topic;
            }
        }
        //Contains match
        for (int i = 0; i < topics.size(); i++) {
            HelpTopic topic = topics.get(i);
            if (topic.topicName.contains(search)) {
                return topic;
            }
        }
        //No match
        return null;
    }
    
    public void load() throws FileNotFoundException, IOException {
        topics.clear();
        File dir = new File("data/help");
        if (!dir.exists()) return;//throw new RuntimeException("No help files available.");
        
        File []filesAndDirs = dir.listFiles();
        
        for (int i = 0; i  < filesAndDirs.length; i++) {
            File f = filesAndDirs[i];
            if (f.isFile() && f.getName().endsWith(".hlp")) {
                HelpTopic topic = readHelpFile(f);
                if (topic == null) {
                    return;//throw new RuntimeException("Error reading help file, topic is null.");
                }
                topics.add(topic);
            }
        }        
    }
    
    private HelpTopic readHelpFile(File f) throws FileNotFoundException, IOException {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(f));
            HelpTopic topic = null;
            
            String line;
            while ((line = in.readLine()) != null) {
                if (topic == null) {
                    //first line should be the topic name!
                    topic = new HelpTopic(line);
                } else {
                    //further lines are content
                    topic.content.add(line.trim());
                }
            }
            return topic;
        } finally {
            if (in != null) {
                in.close();
            }
        }
        
     
    }
    
    
}
