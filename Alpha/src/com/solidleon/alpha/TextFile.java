/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solidleon.alpha;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Markus
 */
public class TextFile {
    
    private String ref;
    public List<String> lines = new ArrayList<String>();
    
    public TextFile(String ref) {
        this.ref = ref;
        loadFile();
    }
    
    private void loadFile() {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(TextFile.class.getResourceAsStream(ref)));
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException ex) {
            
        }finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ex) {
                    Logger.getLogger(TextFile.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
