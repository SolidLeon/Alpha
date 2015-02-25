/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solidleon.alpha;

/**
 *
 * @author markusmannel
 */
public class Globals {
    public static int x;
    public static int y;
    public static int w;
    public static int h;
    /** tilesize */
    public static int ts;
    /** tile mask for shifts */
    public static int tm;
    
    public static void setBounds(int x, int y, int w, int h) {
        Globals.x = x;
        Globals.y = y;
        Globals.w = w;
        Globals.h = h;
    }

    static void setTileBounds(int ts, int tm) {
        Globals.ts = ts;
        Globals.tm = tm;
    }
}
