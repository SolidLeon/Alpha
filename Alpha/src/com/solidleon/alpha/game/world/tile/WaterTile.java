/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solidleon.alpha.game.world.tile;

import com.solidleon.alpha.Config;
import com.solidleon.alpha.TileSet;
import com.solidleon.alpha.entity.Entity;
import com.solidleon.alpha.game.world.Chunk;
import com.solidleon.alpha.game.world.ChunkManager;

import org.newdawn.slick.Color;

/**
 *
 * @author Markus
 */
public class WaterTile extends Tile{
    
    private int time;

    public WaterTile(int id) {
        super(id);
    }
    
    @Override
    public void draw(ChunkManager cm, int xt, int yt) {
        int ts = Config.getInstance().tileSize;
        int t = cm.getData(xt, yt);
        TileSet.draw(t+11*16, xt*ts, yt*ts, Color.blue);
    }

    @Override
    public void update(ChunkManager cm, int xt, int yt, int delta) {
        time -= delta;
        
        if (time <= 0) {
            time += 20;
            Tile t = cm.getTile(xt, yt-1);
            if (t == Tile.water) {
                int p = cm.getData(xt, yt-1);
                if (p < 0 || p > 1) p = 0;
                cm.setData(xt, yt, p);
            } else {
                int p = cm.getData(xt, yt);
                p += 1;
                if (p > 1) p = 0;
                cm.setData(xt, yt, p);
            }
        }
    }

    @Override
    public boolean mayPass(ChunkManager cm, int xt, int yt, Entity e) {
        return false;
    }

    @Override
    public String getName(ChunkManager cm, int xt, int yt) {
        return "Water";
    }
    
    
    
}
