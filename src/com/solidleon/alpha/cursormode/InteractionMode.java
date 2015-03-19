/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solidleon.alpha.cursormode;

import com.solidleon.alpha.Font;
import com.solidleon.alpha.InGameState;
import com.solidleon.alpha.TileSet;
import com.solidleon.alpha.eInteractionMode;
import com.solidleon.alpha.crafting.BuildPlan;
import com.solidleon.alpha.crafting.Crafting;
import com.solidleon.alpha.crafting.Recipe;
import com.solidleon.alpha.entity.Cat;
import com.solidleon.alpha.entity.Entity;
import com.solidleon.alpha.game.world.Chunk;
import com.solidleon.alpha.item.Item;
import com.solidleon.alpha.item.ResourceItem;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author Markus
 */
public class InteractionMode extends CursorMode{
    
    private int selectionCursor = 0;
    private int inventoryCursor = 0;
    private eInteractionMode interactionMode;
    private int sx0, sx1, sy0, sy1;
    private boolean rect0, rect1;
    private boolean shift;
    /** 
     * In rectangle mode sx0 and sy0 are top left corner position.
     * sx1 and sy1 are width and height.
     */
    private boolean rectangleMode = false;
    
    
    public InteractionMode(eInteractionMode interactionMode) {
        this.interactionMode = interactionMode;
    }
    
    

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g, InGameState ingame) {
        
        for (int i = 0; i < eInteractionMode.values().length; i++) {
            eInteractionMode mode = eInteractionMode.values()[i];
            Font.drawString("\\c[gray](\\c[green]"+mode.keybinding+"\\c[gray])", width-Font.getWidth("0000"), 24 + Font.getLineHeight() * i, Color.white);
            Font.drawString("\\c["+(mode == interactionMode ? "white" : "gray")+"]"+mode.name, 
                    0, 
                    24 + Font.getLineHeight() * i, 
                    Color.white);
        }
        
        int yo = container.getHeight() / 2;
        if (interactionMode == eInteractionMode.USE) {
            Font.drawString("INVENTORY", 234.0f/2.0f - Font.getWidth("INVENTORY") / 2.0f, yo, Color.white);

            List<Item> items = ingame.player.inventory.items;

            int offset= inventoryCursor>9 ? inventoryCursor-9 : 0;
            for (int i = 0; i < 10; i++) {
                int idx = offset+i;
                if (idx >= items.size()) break;
                Color col = (idx == inventoryCursor ? Color.white : Color.gray);
                Item item = items.get(idx);
                if (item instanceof ResourceItem) {
                    Font.drawString(String.format("%04d", ((ResourceItem) item).count), 0, yo + 24 + Font.getLineHeight()*i, Color.gray);
                }
                Font.drawString(item.getName(), Font.getWidth("0000 "), yo+24 + Font.getLineHeight()*i, col);
            }
        }
        if (interactionMode == eInteractionMode.SELECT) {
            Font.drawString("SELECT", 234.0f/2.0f - Font.getWidth("SELECT") / 2.0f, yo, Color.white);
            List<Entity> entities = ingame.chunkManager.getEntities(ingame.xCursor, ingame.yCursor, ingame.xCursor, ingame.yCursor);
            for (int i = 0; i <entities.size(); i++) {
                Font.drawString(entities.get(i).getName(), 0,yo+ 24 + Font.getLineHeight()*i, i == selectionCursor ? Color.white : Color.gray);
            }
        }
    }

    
    @Override
    public void keyReleased(InGameState game, Chunk area, int xCursor, int yCursor, int key, char c) {
        if (key == Input.KEY_LSHIFT || key == Input.KEY_RSHIFT) shift = false;
    }
    
    
    
    @Override
    public void keyPressed(InGameState game, Chunk area, int xCursor, int yCursor, int key, char c) {
        if (key == Input.KEY_LSHIFT || key == Input.KEY_RSHIFT) shift = true;
        if (key == Input.KEY_ESCAPE) game.changeMode(new ScrollMode());
        if (key == Input.KEY_Q) {
            Cat cat = new Cat();
            cat.setPosition(game.xCursor, game.yCursor);
            if (area.getTile(game.xCursor, game.yCursor).mayPass(area.cm, cat.x, cat.y, cat)) {
                area.add(cat);
                System.out.println("CREATED CAT: " + cat.x + ", " + cat.y);
            }
        }
        
        if (key == Input.KEY_LEFT) {game.xCursor -= shift?5:1;selectionCursor=0;}
        if (key == Input.KEY_RIGHT){ game.xCursor += shift?5:1;selectionCursor=0;}
        if (key == Input.KEY_DOWN) {game.yCursor += shift?5:1;selectionCursor=0;}
        if (key == Input.KEY_UP) {game.yCursor -= shift?5:1;selectionCursor=0;}
//        if (key == Input.KEY_X) interactionMode = eInteractionMode.REMOVE;
//        if (key == Input.KEY_U) interactionMode = eInteractionMode.USE;
//        if (key == Input.KEY_D) interactionMode = eInteractionMode.USE;
//        if (key == Input.KEY_S) interactionMode = eInteractionMode.SELECT;
//        if (key == Input.KEY_T) game.changeMode(new SelectionCursor());
//        if (key == Input.KEY_B) game.changeMode(new BuildCursor(area.player));
        
        
        if (interactionMode == eInteractionMode.SELECT) {
            if (key == Input.KEY_ENTER) selectEntity(game);
            if (c == '+') selectionCursor += 1;
            if (c == '-') selectionCursor -= 1;
            int len = area.getEntites(game.xCursor, game.yCursor, game.xCursor, game.yCursor).size();
            if (selectionCursor < 0) selectionCursor += len;
            if (selectionCursor >= len) selectionCursor -= len;
        }
        if (interactionMode == eInteractionMode.USE) {
            if (key == Input.KEY_ENTER) {
                if (rectangleMode) {
                    doAction(game, area, game.xCursor, game.yCursor, game.xCursor+sx1, game.yCursor+sy1);
                } else {
                    if (!rect0) {
                        sx0 = xCursor;
                        sy0 = yCursor;
                        rect0 = true;
                    } else {
                        if (!rect1) {
                            sx1 = xCursor;
                            sy1 = yCursor;
                            rect1 = true;

                            if (sy1 < sy0) {
                                int h = sy0;
                                sy0 = sy1;
                                sy1 = h;
                            }
                            if (sx1 < sx0) {
                                int h = sx0;
                                sx0 = sx1;
                                sx1 = h;
                            }

                            rect0 = rect1 = false;
                            doAction(game, area, sx0, sy0, sx1, sy1);
                        }
                    }
                }
            }
            if (key == Input.KEY_TAB) {
                rectangleMode = !rectangleMode;
                sx1 = 0;
                sy1 = 0;
                sx0 = 0;
                sy0 = 0;
                rect0 = false;
                rect1 = false;
            }
            if (rectangleMode) {
                if (key == Input.KEY_H) sx1 -= 1;
                if (key == Input.KEY_L) sx1 += 1;
                if (key == Input.KEY_J) sy1 -= 1;
                if (key == Input.KEY_K) sy1 += 1;
                if (sx1 < 0) sx1 = 0;
                if (sy1 < 0) sy1 = 0;
            }
            if (c == '+') inventoryCursor += 1;
            if (c == '-') inventoryCursor -= 1;
            int len = area.player.inventory.items.size();
            if (inventoryCursor < 0) inventoryCursor += len;
            if (inventoryCursor >= len) inventoryCursor -= len;
        }
    }
    
    private void selectEntity(InGameState game) {
        List<Entity> entities = game.chunkManager.getEntities(game.xCursor, game.yCursor, game.xCursor, game.yCursor);
        if (entities.size() > 0)
            entities.get(selectionCursor).onSelect(game);
        selectionCursor = 0;
    }
    
    //This only applies to actions on TILES
    private void doAction(InGameState game, Chunk area, int x0, int y0, int x1, int y1) {
        if (interactionMode == eInteractionMode.USE && area.player.inventory.items.size() > 0) {
            
        }
        if (interactionMode == eInteractionMode.USE && area.player.inventory.items.size() > 0) {
            Item activeItem = area.player.inventory.items.get(inventoryCursor);
            if (activeItem != null) {
                System.out.println("USE ITEM: " + activeItem.getName());
                for (int x = x0; x <= x1; x++) {
                    for (int y = y0; y <= y1; y++) {
                        if(!activeItem.interactOn(area.getTile(x,y), area, x, y, area.player)) {
                            area.getTile(x, y).interact(area.cm, x, y, area.player, activeItem);
                        }
                        if (activeItem.isDepleted()) {
                            area.player.inventory.items.remove(activeItem);
                            inventoryCursor = 0;
                            return;
                        }
                    }
                }
                //set cursor on activeItem
                for (int i = 0; i < area.player.inventory.items.size(); i++) {
                    if (activeItem.equals(area.player.inventory.items.get(i))) {
                        inventoryCursor = i;
                        break;
                    }
                }
            } else {
                System.out.println("NO ACTIVE ITEM");
            }
        }
    }
    
    @Override
    public void drawCursor(Graphics g, int xCursor, int yCursor) {
        if (interactionMode == eInteractionMode.SELECT) {
            TileSet.draw(8+5*16, xCursor*16, yCursor*16, Color.red);
        }  else {
            if (rectangleMode) {
                int x0 = xCursor;
                int y0 = yCursor;
                int x1 = x0 + sx1;
                int y1 = y0 + sy1;
                for (int x = x0; x <= x1; x++) {
                    for (int y = y0; y <= y1; y++) {
                        TileSet.draw(13+3*16, x*16, y*16, Color.red);
                    }
                }
                TileSet.draw(8+5*16, xCursor*16, yCursor*16, Color.red);
            } else {
                if (rect0) { 
                    int x0 = sx0;
                    int y0 = sy0;
                    int x1 = xCursor;
                    int y1 = yCursor;
                    if (y1 < y0) {
                        int h = y0;
                        y0 = y1;
                        y1 = h;
                    }
                    if (x1 < x0) {
                        int h = x0;
                        x0 = x1;
                        x1 = h;
                    }
                    for (int x = x0; x <= x1; x++) {
                        for (int y = y0; y <= y1; y++) {
                            TileSet.draw(13+3*16, x*16, y*16, Color.red);
                        }
                    }
                    if (!rect1) {
                        TileSet.draw(8+5*16, sx0*16, sy0*16, Color.red);
                    }
                }
            }
            TileSet.draw(interactionMode.sprite, xCursor*16, yCursor*16, Color.white);
        }
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta, InGameState ingame) {
        int x0 = ingame.xScroll>>4;
        int y0 = ingame.yScroll>>4;
        int w = ((container.getWidth()-250)>>4);
        int h = ((container.getHeight()-16)>>4);
        
        if (ingame.xCursor < x0) ingame.xCursor = x0;
        if (ingame.yCursor < y0) ingame.yCursor = y0;
        if (ingame.xCursor >= x0+w) ingame.xCursor = x0+w-1;
        if (ingame.yCursor >= y0+h) ingame.yCursor = y0+h-1;
    }
    
}
