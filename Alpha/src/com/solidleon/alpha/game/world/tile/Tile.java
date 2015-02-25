/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solidleon.alpha.game.world.tile;

import com.solidleon.alpha.InGameState;
import com.solidleon.alpha.entity.Entity;
import com.solidleon.alpha.entity.Player;
import com.solidleon.alpha.game.world.ChunkManager;
import com.solidleon.alpha.item.Item;

import java.io.Serializable;
import java.util.Random;

/**
 *
 * @author Markus
 */
public class Tile implements Serializable {
    public static int tileTime;
    public static final Tile[] tiles = new Tile[256];
    public static final Tile empty = new EmptyTile(0);
    public static final Tile grass = new GrasTile(1);
    public static final Tile water = new WaterTile(2);
//    public static final Tile farm = new FarmTile(3);
    public static final Tile tree = new TreeTile(4);
    public static final Tile oakTreeSapling = new OakTreeSaplingTile(5);
    public static final Tile ashTreeSapling = new AshTreeSaplingTile(6);
    public static final Tile birchTreeSapling = new BirchTreeSaplingTile(7);
    public static final Tile pineTreeSapling = new PineTreeSaplingTile(8);
    public static final Tile chestnutTreeSapling = new ChestnutTreeSaplingTile(9);
    public static final Tile mapleTreeSapling = new MapleTreeSaplingTile(10);
//    public static final Tile wheat = new WheatTile(6);
    
    public final byte id;
    protected Random random = new Random();
    
    public Tile(int id) {
        this.id = (byte) id;
        tiles[id] = this;
    }
    
    public void draw(ChunkManager cm, int xt, int yt) {}

    
    public boolean interact(ChunkManager cm, int xt, int yt, Player player, Item item) {
        return false;
    }
    

    public void update(ChunkManager cm, int xt, int yt, int delta) {
        
    }
    
    /**
     * This update method gets called every ingame minute
     * @param cm
     * @param xt
     * @param yt 
     */
    public void gameUpdate(ChunkManager cm, int xt, int yt) {
        
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Tile other = (Tile) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + this.id;
        return hash;
    }

    public boolean mayPass(ChunkManager cm, int xt, int yt, Entity e) {
        return true;
    }

    public void steppedOn(ChunkManager cm, int x, int y, Entity e) {
        
    }
    
    public String getName(ChunkManager cm, int xt, int yt) {
        return "MISSING TILE NAME";
    }
    
}
