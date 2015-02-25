/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solidleon.alpha.item;

/**
 *
 * @author Markus
 */
public enum ToolType {
    AXE("Axe", 5*16);
    
    public final String name;
    public final int sprite;

    private ToolType(String name, int sprite) {
        this.name = name;
        this.sprite = sprite;
    }

    
    
}
