/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solidleon.alpha.game.world.biome;

import com.solidleon.alpha.game.world.tile.TreeTile;

/**
 *
 * @author Markus
 */
public class ForestBiome extends Biome {

    public ForestBiome(int id) {
        super(id);
        waterLevel = -0.25f;
        forestDensity = 100;
        treeTypes = new TreeTile.TreeType[]{
            TreeTile.TreeType.MAPLE
        };
    }
   
}
