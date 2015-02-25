/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solidleon.alpha.item.resource;

import com.solidleon.alpha.entity.Player;
import com.solidleon.alpha.game.world.Chunk;
import com.solidleon.alpha.game.world.tile.Tile;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.newdawn.slick.Color;

/**
 *
 * @author Markus
 */
public class PlantableResource extends Resource implements Serializable{
    public List<Tile> source;
    public Tile target;

    public PlantableResource(String name, int sprite, Color color, Tile target, Tile... source) {
        this(name, sprite, color, target, Arrays.asList(source));
    }
    
    public PlantableResource(String name, int sprite, Color color, Tile target, List<Tile> source) {
        super(name, sprite, color);
        this.source = source;
        this.target = target;
    }

    @Override
    public boolean interactOn(Tile tile, Chunk area, int xt, int yt, Player player) {
        if (source.contains(tile)) {
            area.cm.setTile(xt, yt, target, 0);
            return true;
        }
        return false;
    }
    
    
}
