/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solidleon.alpha.item;

import com.solidleon.alpha.cursormode.KeybindListItem;
import com.solidleon.alpha.entity.Player;
import com.solidleon.alpha.game.world.Chunk;
import com.solidleon.alpha.game.world.tile.Tile;

import java.io.Serializable;

import org.newdawn.slick.Color;

/**
 *
 * @author Markus
 */
public class Item implements Serializable, KeybindListItem{
    
    public Material getMaterial() {
        return null;
    }
    
    @Override
    public String getName() {
        return "";
    }
    
    public void drawInventory(int x, int y) {
//        Font.drawString(getName(), x, y);
    }

    public boolean matches(Item item) {
        return item.getClass() == getClass();
    }
    
    public boolean isDepleted() {
        return false;
    }
    public boolean interactOn(Tile tile, Chunk area, int xt, int yt, Player player) {
        return false;
    }
            
    public int getSprite() {
        return 0;
    }
    
    public Color getColor() {
        return Color.gray;
    }
    public Color getColorSelected() {
        return Color.white;
    }
}
