/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solidleon.alpha;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

/**
 *
 * @author Markus
 */
public class Font {

    private final static String font =
            " !\"#$%&'()*+,-./"
            + "0123456789:;<=>?"
            + "@ABCDEFGHIJKLMNO"
            + "PQRSTUVWXYZ[\\]^_"
            + "`abcdefghijklmno"
            + "pqrstuvwxyz{|}~";
    private static int w = 10-2;
    private static int h = 12;
    private static org.newdawn.slick.UnicodeFont uFont;

    private static Color colLink = new Color(0x72, 0x91, 0xFF);
    
    public static void init() {
        try {
            uFont = new UnicodeFont(Config.getInstance().gfx + "font.ttf", 12, false, false);
            uFont.addAsciiGlyphs();   //Add Glyphs
            uFont.addGlyphs(400, 600); //Add Glyphs
            uFont.getEffects().add(new ColorEffect(java.awt.Color.WHITE)); //Add Effects
            uFont.loadGlyphs();  //Load Glyphs
        } catch (SlickException ex) {
            Logger.getLogger(Font.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static int getLineHeight() {
        return h;
    }

    public static int getWidth(String str) {
        int len = 0;
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (ch == '\\') {
                if (str.startsWith("\\c[", i)) {
                    i = str.indexOf(']', i);
                    continue;
                }
            }
            len++;
        }
        return w * len;
//        return uFont.getWidth(str);
    }

    public static void drawString(String str, int x, int y, Color col) {
        drawString(str, (float) x, (float) y, col);
    }

    public static void drawString(String str, float x, float y, Color col) {
        int c = 0;
        Color fgCol = col;
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (ch == '['&&i+1 < str.length() && str.charAt(i+1) == '[') {
                i+=1;
                fgCol = colLink;
                continue;
            } else if (ch == ']'&&i+1 < str.length() && str.charAt(i+1) == ']') {
                i+=1;
                fgCol = Color.white;
                continue;
            } else if (ch == '\\') {
                if (str.startsWith("\\c[", i)) {
                    int endIdx = str.indexOf("]", i);
                    String sCol = str.substring(i + 3, endIdx);
//                    System.out.println("NEW COL: " + sCol);
                    if ("green".equals(sCol)) {
                        fgCol = Color.green;
                    } else if ("lightGray".equals(sCol)) {
                        fgCol = Color.lightGray;
                    } else if ("gray".equals(sCol)) {
                        fgCol = Color.gray;
                    } else if ("darkGray".equals(sCol)) {
                        fgCol = Color.darkGray;
                    } else if ("black".equals(sCol)) {
                        fgCol = Color.black;
                    } else if ("blue".equals(sCol)) {
                        fgCol = Color.blue;
                    } else if ("cyan".equals(sCol)) {
                        fgCol = Color.cyan;
                    } else if ("magenta".equals(sCol)) {
                        fgCol = Color.magenta;
                    } else if ("orange".equals(sCol)) {
                        fgCol = Color.orange;
                    } else if ("pink".equals(sCol)) {
                        fgCol = Color.pink;
                    } else if ("red".equals(sCol)) {
                        fgCol = Color.red;
                    } else if ("white".equals(sCol)) {
                        fgCol = Color.white;
                    } else if ("yellow".equals(sCol)) {
                        fgCol = Color.yellow;
                    }
                    i = endIdx;
                    continue;
                }
            }
            int idx = font.indexOf(ch);
            if (idx == -1) {
                continue;
            }
            int xt = idx % 16;
            int yt = (idx / 16) + 2;
            TileSet.drawSprite10x12(xt, yt, x + c * w + 2, y + 2, Color.black);
            TileSet.drawSprite10x12(xt, yt, x + c * w, y, fgCol);
//    public static void draw(int tile, float x0, float y0, float x1, float y1, Color color) {
//            TileSet.draw(xt + yt * 16, //tile
//                    x + c * (w - 2),  //x0
//                    y, //y0 
//                    x + c * (w - 2) + w, // x1
//                    y + h,  //y1
//                    fgCol);
            c++;
        }
    }
}
