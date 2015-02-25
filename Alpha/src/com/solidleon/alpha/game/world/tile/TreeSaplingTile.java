/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solidleon.alpha.game.world.tile;

import com.solidleon.alpha.Config;
import com.solidleon.alpha.TileSet;
import com.solidleon.alpha.game.time.FTS;
import com.solidleon.alpha.game.world.ChunkManager;
import com.solidleon.alpha.game.world.tile.TreeTile.TreeType;

import org.newdawn.slick.Color;

/**
 *
 * @author markusmannel
 */
public class TreeSaplingTile extends Tile {

    private TreeType type;
    private int t = FTS.getGameTime(1, 0, 0, 0, 0);
    
    public TreeSaplingTile(int id, TreeType type) {
        super(id);
        this.type = type;
    }
    
    @Override
    public void draw(ChunkManager cm, int xt, int yt) {
        int ts = Config.getInstance().tileSize;
        TileSet.draw(15+2*16, xt*ts, yt*ts, Color.yellow);
    }

    @Override
    public void gameUpdate(ChunkManager cm, int xt, int yt) {
        int age = cm.getData(xt, yt);
        
        if (age >= t) { //60 ingame minutes
            cm.setTile(xt, yt, Tile.tree, type.ordinal());
        } else {
            cm.setData(xt,yt,age+1);
        }
    }
    
    

    @Override
    public String getName(ChunkManager cm, int xt, int yt) {
        return type.name + " Tree Sapling";
    }

    
}
