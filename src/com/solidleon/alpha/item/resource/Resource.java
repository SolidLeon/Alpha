/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solidleon.alpha.item.resource;

import com.solidleon.alpha.entity.Player;
import com.solidleon.alpha.game.world.Chunk;
import com.solidleon.alpha.game.world.tile.OakTreeSaplingTile;
import com.solidleon.alpha.game.world.tile.Tile;
import com.solidleon.alpha.game.world.tile.TreeTile;

import java.io.Serializable;

import org.newdawn.slick.Color;

/**
 *
 * @author Markus
 */
public class Resource implements Serializable{
    private static int autoIncrement = 0;
    public static final Resource wood = new Resource("Wood", 6+16, new Color(80, 100, 32));
    public static final Resource cutwood = new Resource("Cut Wood", 13+32, new Color(80, 100, 32));
    public static final Resource woodenstock = new Resource("Wooden Stock", 6+1*16, new Color(80, 100, 32));
    public static final Resource woodenaxehead = new Resource("Wooden Axe Head", 7, new Color(80, 100, 32));
    public static final Resource stone = new Resource("Stone", 7, Color.gray);
    public static final Resource oakSapling = new PlantableResource("Oak Sapling", 14+32, Color.green, Tile.oakTreeSapling, Tile.grass);
    public static final Resource birchSapling = new PlantableResource("Birch Sapling", 14+32, Color.green, Tile.birchTreeSapling, Tile.grass);
    public static final Resource ashSapling = new PlantableResource("Ash Sapling", 14+32, Color.green, Tile.ashTreeSapling, Tile.grass);
    public static final Resource chestnutSapling = new PlantableResource("Chestnut Sapling", 14+32, Color.green, Tile.chestnutTreeSapling, Tile.grass);
    public static final Resource mapleSapling = new PlantableResource("Maple Sapling", 14+32, Color.green, Tile.mapleTreeSapling, Tile.grass);
    public static final Resource pineSapling = new PlantableResource("Pine Sapling", 14+32, Color.green, Tile.pineTreeSapling, Tile.grass);
    
    public final int id;
    public final String name;
    public final int sprite;
    public final Color color;
        
    public Resource(String name, int sprite, Color color) {
        this.id = autoIncrement++;
        this.name = name;
        this.sprite = sprite;
        this.color = color;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Resource other = (Resource) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + this.id;
        return hash;
    }

    public boolean interactOn(Tile tile, Chunk area, int xt, int yt, Player player) {
        return false;
    }
    
    
}
