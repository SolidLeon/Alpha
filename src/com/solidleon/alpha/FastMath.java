/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solidleon.alpha;

/**
 *
 * @author markusmannel
 */
public class FastMath {
    
    public static int floor(double d) {
        return d < 0 ? (int) (d-1.0): (int)d;
    }
}
