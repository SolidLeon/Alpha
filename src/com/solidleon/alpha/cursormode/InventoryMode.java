/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solidleon.alpha.cursormode;

import com.solidleon.alpha.Font;
import com.solidleon.alpha.InGameState;
import com.solidleon.alpha.eInteractionMode;
import com.solidleon.alpha.game.world.Chunk;
import com.solidleon.alpha.item.Item;
import com.solidleon.alpha.item.ResourceItem;

import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author Markus
 */
public class InventoryMode extends CursorMode {

    private int itemCursor;

    @Override
    public void init(GameContainer container, StateBasedGame game, int delta, InGameState ingame, Chunk area) {
        area.player.deselectItem();
    }
    
    
    
    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g, InGameState ingame) {
        Font.drawString("INVENTORY", 250.0f/2.0f - Font.getWidth("INVENTORY") / 2.0f, 0, Color.red);
        
        List<Item> items = ingame.player.inventory.items;
        
        for (int i = 0; i < items.size(); i++) {
            Color col = (i == itemCursor ? Color.red : Color.white);
            Item item = items.get(i);
            if (item instanceof ResourceItem) {
                Font.drawString(String.format("%04d", ((ResourceItem) item).count), 0, 24 + Font.getLineHeight()*i, col);
            }
            Font.drawString(item.getName(), Font.getWidth("0000 "), 24 + Font.getLineHeight()*i, col);
        }
        
    }

    
    
    @Override
    public void keyPressed(InGameState game, Chunk area, int xCursor, int yCursor, int key, char c) {
        if (key == Input.KEY_ESCAPE) game.changeMode(new ScrollMode());
        if (key == Input.KEY_ENTER) {
            area.player.selectItem(itemCursor);
            game.changeMode(new InteractionMode(eInteractionMode.USE));
        }
        if (key == Input.KEY_UP) itemCursor -= 1;
        if (key == Input.KEY_DOWN) itemCursor += 1;
        int len = area.player.inventory.items.size();
        if (itemCursor < 0) itemCursor += len;
        if (itemCursor >= len) itemCursor -= len;
    }
    
}
