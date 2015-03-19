/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solidleon.alpha.game.world.biome;

import com.solidleon.alpha.game.world.tile.TreeTile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Markus
 */
public class Biome implements Serializable{
    public static final List<Biome> biomes = new ArrayList<Biome>();
    public static final Biome forest = new ForestBiome(0);
    public static final Biome plains = new PlainsBiome(1);
    
    public int id;
    public float waterLevel = 0.0f;
    public int forestDensity = 0;
    public TreeTile.TreeType []treeTypes;

    public Biome(int id) {
        this.id = id;
        biomes.add(id, this);
    }
    
    
}
