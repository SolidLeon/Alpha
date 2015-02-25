/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solidleon.alpha.entity;

import com.solidleon.alpha.Config;
import com.solidleon.alpha.InGameState;
import com.solidleon.alpha.TileSet;
import com.solidleon.alpha.game.world.Chunk;
import com.solidleon.alpha.item.Item;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

/**
 *
 * @author Markus
 */
public class ItemEntity extends Entity {

    public Item item;
    private int time;
    
    public ItemEntity(Item item, int x, int y) {
        this.item = item;
        this.x = x;
        this.y = y;
    }

    @Override
    public void update(int delta) {
        time += delta;
        if (time >= 1500) {
            if (area.player.inventory.add(item)) {
                remove();
                return;
            }
            time = 1500;
        }
    }
    
    

    @Override
    public void onSelect(InGameState game) {
        game.player.inventory.add(item);
        remove();
    }

    @Override
    public String getName() {
        return item.getName();
    }

    @Override
    public void render(Graphics g) {
        int ts = Config.getInstance().tileSize;
        TileSet.draw(item.getSprite(), x*ts, y*ts, item.getColor());
    }
    
}
