/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solidleon.alpha.entity;

import com.solidleon.alpha.Config;
import com.solidleon.alpha.InGameState;
import com.solidleon.alpha.TileSet;
import com.solidleon.alpha.cursormode.MarketMode;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

/**
 *
 * @author markusmannel
 */
public class Market extends Entity {
    private Color col = new Color(128, 128, 0);
    private int tile = 0;
    private int tileTimer;
    
    public Market() {
        w = 3;
        h = 3;
    }
    
    @Override
    public void update(int delta) {
    }   
    
    @Override
    public void render(Graphics g) {
        int ts = Config.getInstance().tileSize;
        
        TileSet.drawBackground(x*ts, y*ts, ts*3, ts*3, Color.black);
        TileSet.draw(2+2*16, (x+1)*ts, y*16, Color.gray); //"
        TileSet.draw(1+11*16, (x)*ts, (y+1)*ts, Color.gray); //
        TileSet.draw(1+11*16, (x)*ts, (y+2)*ts, Color.gray); //
        TileSet.draw(13+5*16, (x+1)*ts, (y+2)*ts, Color.gray); //
        TileSet.draw(1+11*16, (x+2)*ts, (y+2)*ts, Color.gray); //
    }

    @Override
    public void onSelect(InGameState game) {
        game.changeMode(new MarketMode());
    }
    
    
}
