/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solidleon.alpha.item;

import com.solidleon.alpha.entity.Player;
import com.solidleon.alpha.game.world.Chunk;
import com.solidleon.alpha.game.world.tile.Tile;
import com.solidleon.alpha.item.resource.Resource;

import java.io.Serializable;

import org.newdawn.slick.Color;

/**
 *
 * @author Markus
 */
public class ResourceItem extends Item  implements Serializable{
    public Resource res;
    public int count;

    public ResourceItem(Resource res, int count) {
        this.res = res;
        this.count = count;
    }

    public ResourceItem(Resource res) {
        this.res = res;
        count = 1;
    }

    @Override
    public String getName() {
        return res.name;
    }

    @Override
    public boolean interactOn(Tile tile, Chunk area, int xt, int yt, Player player) {
        if (res.interactOn(tile, area, xt, yt, player)) {
            count--;
            return true;
        }
        return false;
    }

    @Override
    public boolean isDepleted() {
        return count <= 0;
    }

    @Override
    public int getSprite() {
        return res.sprite;
    }

    @Override
    public Color getColor() {
        return res.color;
    }
    
    
    
    
    
    
}
