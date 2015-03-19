/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solidleon.alpha.item;

import java.io.Serializable;

/**
 *
 * @author Markus
 */
public class Material implements Serializable {
    public static final Material wooden = new Material(0, "Wooden", 36);
    public static final Material stone = new Material(1, "Stone", 90);
    
    public final int id;
    public final String name;
    /** Number of uses till broken */
    public final int resistance;

    public Material(int id, String name, int resistance) {
        this.id = id;
        this.name = name;
        this.resistance = resistance;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Material other = (Material) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + this.id;
        return hash;
    }

    
    
}
