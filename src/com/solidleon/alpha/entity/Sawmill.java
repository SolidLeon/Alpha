/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solidleon.alpha.entity;

import com.solidleon.alpha.Config;
import com.solidleon.alpha.InGameState;
import com.solidleon.alpha.TileSet;
import com.solidleon.alpha.crafting.Crafting;
import com.solidleon.alpha.cursormode.CraftingMode;
import com.solidleon.alpha.item.Item;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

/**
 *
 * @author Markus
 */
public class Sawmill extends Factory {

    private Color col = new Color(128, 128, 0);
    private int tile = 0;
    private int tileTimer;
    
    public Sawmill() {
        w = 3;
        h = 3;
    }
    
    @Override
    public void update(int delta) {
        super.update(delta);
        if (queue.size() > 0) {
            if (tileTimer<=0) {
                tile+=1;
                if (tile > 3) tile = 0;
                tileTimer = 100;
            }
            if (tileTimer > 0) {
                tileTimer-=delta;
            }
        }
    }   
    
    @Override
    public void render(Graphics g) {
        int ts = Config.getInstance().tileSize;
        
        TileSet.drawBackground(x*ts, y*ts, ts*3, ts*3, Color.black);
        TileSet.draw(2+2*16, (x+1)*ts, y*ts, Color.gray); //"
        if (tile == 0)TileSet.draw(13+2*16, (x+2)*ts, y*ts, col); // =
        if (tile == 1)TileSet.draw(12+5*16, (x+2)*ts, y*ts, col); // \\
        if (tile == 2)TileSet.draw(3+11*16, (x+2)*ts, y*ts, col); // |
        if (tile == 3)TileSet.draw(15+2*16, (x+2)*ts, y*ts, col); // /
        TileSet.draw(1+11*16, (x)*ts, (y+1)*ts, Color.gray); //
        TileSet.draw(1+11*16, (x)*ts, (y+2)*ts, Color.gray); //
        TileSet.draw(13+5*16, (x+1)*ts, (y+2)*ts, Color.gray); //
        TileSet.draw(1+11*16, (x+2)*ts, (y+2)*ts, Color.gray); //
    }

    @Override
    public void onSelect(InGameState game) {
        game.changeMode(new CraftingMode(Crafting.sawmillRecipes, game.player, this));
    }
    
    
    
}
