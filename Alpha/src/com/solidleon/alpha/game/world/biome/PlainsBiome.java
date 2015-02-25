/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solidleon.alpha.game.world.biome;

/**
 *
 * @author Markus
 */
public class PlainsBiome extends Biome {

    public PlainsBiome(int id) {
        super(id);
        forestDensity = 25;
        waterLevel = -0.25f;
    }
    
}
