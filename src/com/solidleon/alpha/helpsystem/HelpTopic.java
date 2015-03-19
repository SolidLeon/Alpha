/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solidleon.alpha.helpsystem;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Markus
 */
public class HelpTopic {
    public String topicName;
    public List<String> content = new ArrayList<String>();

    public HelpTopic(String topicName) {
        this.topicName = topicName.toLowerCase();
    }

    
    
    
}
