/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solidleon.alpha.game.world.tile;

import com.solidleon.alpha.entity.Entity;
import com.solidleon.alpha.game.world.Chunk;
import com.solidleon.alpha.game.world.ChunkManager;

/**
 *
 * @author Markus
 */
public class EmptyTile extends Tile{

    public EmptyTile(int id) {
        super(id);
    }

    @Override
    public boolean mayPass(ChunkManager cm, int xt, int yt, Entity e) {
        return false;
    }

    @Override
    public String getName(ChunkManager cm, int xt, int yt) {
        return "Void";
    }
    
}
