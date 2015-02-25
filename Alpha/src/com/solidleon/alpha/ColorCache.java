/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solidleon.alpha;

import java.util.HashMap;
import java.util.Map;
import org.newdawn.slick.Color;


/**
 *
 * @author markusmannel
 */
public class ColorCache {
    private static Color nullColor = Color.magenta;
    
    public static Color black = Color.black;
    public static Color blue = Color.blue;
    public static Color cyan = Color.cyan;
    public static Color darkGray = Color.darkGray;
    public static Color gray = Color.gray;
    public static Color green = Color.green;
    public static Color lightGray = Color.lightGray;
    public static Color magenta = Color.magenta;
    public static Color orange = Color.orange;
    public static Color pink = Color.pink;
    public static Color red = Color.red;
    public static Color white = Color.white;
    public static Color yellow = Color.yellow;
    
    public static Color []maple = {new Color(141, 15, 20)};
    public static Color []birch = {new Color(95, 117, 18)};
    public static Color []ash = {new Color(57, 145, 55)};
    public static Color []oak = {new Color(89, 105, 46)};
    public static Color []pine = {new Color(68, 122, 60)};
    public static Color []chestnut = {new Color(68, 116, 60)};
    
    private static Map<String, Color> table = new HashMap<String, Color>();
    
    public static Color get(String name, int r, int g, int b, int a) {
        if (table.containsKey(name)) {
            return table.get(name);
        }
        Color col = new Color(r,g,b,a);
        table.put(name, col);
        return col;
    }
    
    public static Color get(String name, float r, float g, float b, float a) {
        if (table.containsKey(name)) {
            return table.get(name);
        }
        Color col = new Color(r,g,b,a);
        table.put(name, col);
        return col;
    }
    
    public static Color get(String name) {
        if (table.containsKey(name)) {
            return table.get(name);
        }
        return nullColor;
    }
    
    
    private ColorCache() {
    }
    
}
