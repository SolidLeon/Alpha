/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solidleon.alpha.game.world.tile;

import com.solidleon.alpha.ColorCache;
import com.solidleon.alpha.Config;
import com.solidleon.alpha.TileSet;
import com.solidleon.alpha.game.world.ChunkManager;

import org.newdawn.slick.Color;

/**
 *
 * @author markusmannel
 */
public class PlaingroundTile extends Tile {

    public PlaingroundTile(int id) {
        super(id);
    }

    @Override
    public void draw(ChunkManager cm, int xt, int yt) {
        int ts = Config.getInstance().tileSize;
        TileSet.draw(15+4*16, xt*ts, yt*ts, ColorCache.get("Brown", 130, 100, 40, 255));
    }
    
}
