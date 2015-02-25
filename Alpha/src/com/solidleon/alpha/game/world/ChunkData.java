/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solidleon.alpha.game.world;

import com.solidleon.alpha.game.world.biome.Biome;

/**
 *
 * @author Markus
 */
public class ChunkData {
    public byte []tiles;
    public int []data;
    public Biome biome;

    public ChunkData(byte[] tiles, int[] data, Biome biome) {
        this.tiles = tiles;
        this.data = data;
        this.biome = biome;
    }


    
}
