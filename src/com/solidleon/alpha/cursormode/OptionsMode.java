/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solidleon.alpha.cursormode;

import com.solidleon.alpha.*;
import com.solidleon.alpha.game.world.Chunk;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author Markus
 */
public class OptionsMode extends CursorMode {
    private CursorMode oldCursorMode;
    private int cursor;

    public OptionsMode(CursorMode cursorMode) {
        oldCursorMode = cursorMode;
    }

    @Override
    public void renderCustom(GameContainer container, StateBasedGame game, Graphics g, InGameState aThis) {
        int w = container.getWidth() - 16 - 32;
        int h = container.getHeight() - 24 - 32;

        int ox = 16;
        int oy = 16;

        g.setColor(Color.black);
        g.fillRect(0, 0, container.getWidth(), container.getHeight());
        TileSet.drawFrame(0, 0, container.getWidth() - 16, container.getHeight() - 24);
        
        
        Font.drawString("Tile Size: " + Config.getInstance().tileSize + "("+Config.getInstance().tileMask+")", 16, 16+0*16, cursor == 0 ? Color.red : Color.white);
        
        int len = SoundCache.bgm.size();
        if (len > 0) {
            Font.drawString("Music: " + Config.getInstance().music, 16, 16+1*16, cursor == 1 ? Color.red : Color.white);
            Font.drawString("Music: " + SoundCache.bgm.get(SoundCache.current), 16, 16+2*16, cursor == 2 ? Color.red : Color.white);
        }
        Font.drawString("To add music, put .ogg files into \"sound/\" directory!", 
                container.getWidth() / 2 - Font.getWidth("To add music, put .ogg files into \"sound/\" directory!") / 2, 
                16+3*16, 
                Color.white);
    }

    @Override
    public void keyPressed(InGameState game, Chunk area, int xCursor, int yCursor, int key, char c) {
        if (key == Input.KEY_ESCAPE) game.changeMode(oldCursorMode);
        if (key == Input.KEY_UP) cursor -= 1;
        if (key == Input.KEY_DOWN) cursor += 1;
        int len = 3;
        if (cursor < 0) cursor += len;
        if (cursor >= len) cursor -= len;
        
        if (cursor == 0) {
            if (key == Input.KEY_RIGHT) Config.getInstance().tileSize *= 2;
            if (key == Input.KEY_LEFT) Config.getInstance().tileSize /= 2;
            if (Config.getInstance().tileSize < 8) Config.getInstance().tileSize = 8;
            if (Config.getInstance().tileSize > 64) Config.getInstance().tileSize = 64;
            Config.getInstance().updateTileMask();
        }
        if (cursor == 1) {
            if (key == Input.KEY_RIGHT) Config.getInstance().music += 0.1f;
            if (key == Input.KEY_LEFT) Config.getInstance().music -= 0.1f;
            float oldVolume = Config.getInstance().music;
            if (Config.getInstance().music < 0.0f) Config.getInstance().music = 0.0f;
            if (Config.getInstance().music > 1.0f) Config.getInstance().music = 1.0f;
            if (Config.getInstance().music > 0.0f) {
                if (oldVolume == 0.0f) {
                    SoundCache.start();
                } else {
                    SoundCache.setVolume(Config.getInstance().music);
                }
            } else {
                SoundCache.stop();
            }
        }
        if (cursor == 2) {
            if (key == Input.KEY_RIGHT) SoundCache.next();
            if (key == Input.KEY_LEFT) SoundCache.previous();
        }
        
    }
    
    
    
}
