/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solidleon.alpha.game.world.tile;

import com.solidleon.alpha.Config;
import com.solidleon.alpha.TileSet;
import com.solidleon.alpha.game.world.ChunkManager;

import org.newdawn.slick.Color;

/**
 *
 * @author Markus
 */
public class GrasTile extends Tile {

    public static final int TYPES = 4;
    
    private Color col =  new Color(0.0f, 0.5f, 0.0f); 
    
    public GrasTile(int id) {
        super(id);
    }

    @Override
    public void draw(ChunkManager cm, int xt, int yt) {
        int ts = Config.getInstance().tileSize;
        int type = cm.getData(xt, yt);
        if (type == 0)TileSet.draw(12+2*16, xt*ts, yt*ts, col);
        if (type == 1)TileSet.draw(14+2*16, xt*ts, yt*ts, col);
        if (type == 2)TileSet.draw(0+6*16, xt*ts, yt*ts, col);
        if (type == 3)TileSet.draw(7+2*16, xt*ts, yt*ts, col);
    }

    @Override
    public String getName(ChunkManager cm, int xt, int yt) {
        return "Grass";
    }
    
    
    
    
}
