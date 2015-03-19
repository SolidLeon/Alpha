/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solidleon.alpha;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

/**
 *
 * @author Markus
 */
public class TileSet {
    private static SpriteSheet sheet;
    private static SpriteSheet sheet10x12;
    public static Image image, image10x12;
//    public static Image imageSmall;
    private static Color colorFilter = new Color(1.0f, 0.0f, 1.0f, 1.0f);
    
    private static Color[] colors = new Color[32];
    
    /* SPRITE SIZE */
    private static final int SX = 16;
    private static final int SY = 16;
    /* TILE SIZE */
//    private static final int Config.getInstance().tileSize = 16;
//    private static final int Config.getInstance().tileSize = 16;
    
    
    public static void init() {
        try {
//            image = new Image(
//                    TileSet.class.getResourceAsStream("/com/reddwarf/Alpha/res/curses_square_16x16.bmp"), 
//                    "/com/reddwarf/Alpha/res/curses_square_16x16.bmp", 
//                    false);
            image = new Image(
                    "data/gfx/curses_square_16x16.bmp", Color.magenta);
            image.setFilter(Image.FILTER_NEAREST);
            sheet = new SpriteSheet(image, SX, SY);
            
            image10x12 = new Image(
                    "data/gfx/curses_800x600.bmp", Color.magenta);
            image10x12.setFilter(Image.FILTER_NEAREST);
            sheet10x12 = new SpriteSheet(image10x12, 10, 12);
//            imageSmall = image.getScaledCopy(0.5f);
        } catch (SlickException ex) {
            Logger.getLogger(TileSet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
//    public static void draw(int tile, int x0, int y0, int x1, int y1, Color color) {
//        int sx = (tile%16) * 16;
//        int sy = (tile/16) * 16;
//        image.draw(x0, y0, x1, y1, sx, sy, sx+16, sy+16, color);
//    }

//    public static void draw(int tile, float x0, float y0, float x1, float y1, Color color) {
//        int sx = (tile%16) * 16;
//        int sy = (tile/16) * 16;
//        image.draw(x0, y0, x1, y1, sx, sy, sx+16, sy+16, color);
//        
//    }
    public static void draw(int tile, int x, int y, Color col) {
        sheet.getSprite(tile%16, tile/16).draw(x,y,Config.getInstance().tileSize,Config.getInstance().tileSize,col);
//        draw(tile, x, y, x+16, y+16, col);
    }
    public static void draw(int tile, float x, float y, Color col) {
        sheet.getSprite(tile%16, tile/16).draw(x,y,Config.getInstance().tileSize,Config.getInstance().tileSize,col);
//        draw(tile, x, y, x+16, y+16, col);
    }

    static void draw8x8(int tile, float x, float y, Color col) {
        sheet.getSprite(tile%16, tile/16).draw(x,y,12, 12,col);
//        draw(tile, x, y, x+12, y+12, col);
    }
    
    public static void drawFrame(int x, int y, int w, int h) {
        drawFrame(x, y, w, h, Color.white, Color.black);
    }
    public static void drawFrame(int x, int y, int w, int h, Color fg, Color bg) {
        drawBackground(x, y, w, h, bg);
        
        //corners
        sheet.getSprite(9, 12).draw(x, y, 16, 16, fg);
        sheet.getSprite(11, 11).draw(x+w-16, y, 16, 16, fg);
        sheet.getSprite(8, 12).draw(x, y+h-16, 16, 16, fg);
        sheet.getSprite(12, 11).draw(x+w-16, y+h-16, 16, 16, fg);
        
        //13+12*16
        sheet.getSprite(13, 12).draw(x+16, y, w-16-16, 16, fg);
        sheet.getSprite(13, 12).draw(x+16, y+h-16, w-16-16, 16, fg);
        sheet.getSprite(10, 11).draw(x, y+16, 16, h-16-16, fg);
        sheet.getSprite(10, 11).draw(x+w-16, y+16, 16, h-16-16, fg);
    }
    public static void drawBackground(int x, int y, int w, int h, Color col) {
        sheet.getSprite(11,13).draw(x,y,w,h,col);
        
    }

    static void drawSprite(int xt, int yt, float x, float y, Color col) {
        sheet.getSprite(xt, yt).draw(x, y, Config.getInstance().tileSize, Config.getInstance().tileSize, col);
    }
    static void drawSprite10x12(int xt, int yt, float x, float y, Color col) {
        sheet10x12.getSprite(xt, yt).draw(x, y, 10, 12, col);
    }
    static void drawSprite6x8(int xt, int yt, float x, float y, Color col) {
        sheet10x12.getSprite(xt, yt).draw(x, y, 6, 8, col);
    }
}
