/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solidleon.alpha;

/**
 *
 * @author Markus
 */
public enum eInteractionMode {
    REMOVE("x","remove",8+5*16),
    USE("d", "use", 5+5*16),
    SELECT("s", "select", 3+5*16),;
    
    public String keybinding;
    public String name;
    public int sprite;

    private eInteractionMode(String keybinding, String name, int sprite) {
        this.keybinding = keybinding;
        this.name = name;
        this.sprite = sprite;
    }

    
}
