/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solidleon.alpha.cursormode;

import com.solidleon.alpha.*;
import com.solidleon.alpha.entity.Cat;
import com.solidleon.alpha.entity.Player;
import com.solidleon.alpha.game.world.Chunk;
import com.solidleon.alpha.game.world.ChunkManager;
import com.solidleon.alpha.item.Item;
import com.solidleon.alpha.item.ResourceItem;
import com.solidleon.alpha.item.ToolItem;

import java.util.ArrayList;
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
public class DesignationCursor extends CursorMode {
    private Player player;
    private List<Item> items;
    private int inventoryCursor;
    private int sx0, sx1, sy0, sy1;
    private boolean rect0, rect1;
    private boolean shift;
    /** 
     * In rectangle mode sx0 and sy0 are top left corner position.
     * sx1 and sy1 are width and height.
     */
    private boolean rectangleMode = false;
    
    private Keybinding kbAll, kbRes, kbTools;
    private Keybinding kbUsed;

    public DesignationCursor(Player player) {
        this.player = player;
        applyKeybindings();
    }

    @Override
    public void notifyDirtyInventory(Player player) {
        //donst give any benefit at all... we must update the list
        // of the keybindings (assignKeybindings) !!!
        //Removed due to bad gameplay
        //Resets the inventory cursor, making it annoying!
//        kbUsed.action(player.ingame, player.ingame.getChunk(), player.ingame.xCursor, player.ingame.yCursor, 0, ' ');
    }
    
    private void applyKeybindings() {
        clearKeybindings();
        
        kbAll = new Keybinding('a', "All") {

            @Override
            public void action(InGameState game, Chunk area, int xCursor, int yCursor, int key, char c) {
                items = area.player.inventory.items;
                inventoryCursor = 0;
                clearKeybindings();
                assignKeybindings(items);
                kbUsed = this;
            }
            
        };
        
        kbRes = new Keybinding('r', "Resources") {

            @Override
            public void action(InGameState game, Chunk area, int xCursor, int yCursor, int key, char c) {
                List<Item> result = new ArrayList<Item>();
                for (Item item : area.player.inventory.items) {
                    if (item instanceof ResourceItem)
                        result.add(item);
                }
                items = result;
                inventoryCursor = 0;
                clearKeybindings();
                assignKeybindings(items);
                kbUsed = this;
            }
            
        };
        kbTools = new Keybinding('t', "Tools") {

            @Override
            public void action(InGameState game, Chunk area, int xCursor, int yCursor, int key, char c) {
                List<Item> result = new ArrayList<Item>();
                for (Item item : area.player.inventory.items) {
                    if (item instanceof ToolItem)
                        result.add(item);
                }
                items = result;
                inventoryCursor = 0;
                
                clearKeybindings();
                assignKeybindings(items);
                kbUsed = this;
            }
            
        };
        addKeybinding(kbAll);
        addKeybinding(kbRes);
        addKeybinding(kbTools);
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g, InGameState ingame) {
    
        int yo = 0;
        
        Font.drawString("INVENTORY", width/2.0f - Font.getWidth("INVENTORY") / 2.0f, yo, Color.white);
        if (items != null) {

    //        List<Item> items = ingame.player.inventory.items;

            //-32 for borders, and 2times line height for heading "Inventory" and space between
            int maxItems = (container.getHeight()-32-Font.getLineHeight()*2) / Font.getLineHeight();

            int offset= inventoryCursor>=maxItems ? inventoryCursor-maxItems+1 : 0;
            for (int i = 0; i < maxItems; i++) {
                int idx = offset+i;
                if (idx >= items.size()) break;
//                Color col = (idx == inventoryCursor ? Color.white : Color.gray);
                Item item = items.get(idx);
                Color col = (idx == inventoryCursor ? item.getColorSelected() : item.getColor());
                if (item instanceof ResourceItem) {
                    Font.drawString(String.format("%04d", ((ResourceItem) item).count), 0, yo + 24 + Font.getLineHeight()*i, Color.gray);
                }
                Font.drawString(item.getName(), Font.getWidth("0000 "), yo+24 + Font.getLineHeight()*i, col);
            }
            
            /* THIS PART RENDERS ITEMS WITH KEYBINDINGS - until the keybindings work 100% properly
             * old style is used! (above)
             */
            /*
            for (int i = 0; i < keybindings.size(); i++) {
                Keybinding kb = keybindings.get(i);
                Item item = (Item) kb.object;
                String s;
                if (item instanceof ResourceItem) {
                    s = String.format("%04d", ((ResourceItem) item).count) + " " + item.getName();
                } else {
                    s = "     " + item.getName();
                }
                Font.drawString("\\c[green]"+ kb.c +"\\c[gray]: " + s, 0, yo+24+Font.getLineHeight()*i, Color.gray);
            }*/
        } else {
            //Render Categories
            for (int i = 0; i < keybindings.size(); i++) {
                Keybinding kb = keybindings.get(i);
                Font.drawString("\\c[green]"+ kb.c +"\\c[gray]: " + kb.object.toString(), 0, yo+24+Font.getLineHeight()*i, Color.gray);
            }
        }
    }
    
    
    @Override
    public void keyReleased(InGameState game, Chunk area, int xCursor, int yCursor, int key, char c) {
        if (key == Input.KEY_LSHIFT || key == Input.KEY_RSHIFT) shift = false;
    }

    @Override
    public void keyPressed(InGameState game, Chunk area, int xCursor, int yCursor, int key, char c) {if (key == Input.KEY_LSHIFT || key == Input.KEY_RSHIFT) shift = true;
        checkKeybindings(game, area, xCursor, yCursor, key, c);
        if (key == Input.KEY_ESCAPE) {
            if (items != null) {
                if (rect0) {
                    rect0 = false;
                    rect1 = false;
                } else {
                    items = null;
                    clearKeybindings();
                    addKeybinding(kbAll);
                    addKeybinding(kbRes);
                    addKeybinding(kbTools);
                }
            } else {
                game.changeMode(new ScrollMode());
            }
        }
        if (key == Input.KEY_Q) {
            Cat cat = new Cat();
            cat.setPosition(game.xCursor, game.yCursor);
            if (area.getTile(game.xCursor, game.yCursor).mayPass(area.cm, cat.x, cat.y, cat)) {
                area.add(cat);
                System.out.println("CREATED CAT: " + cat.x + ", " + cat.y);
            }
        }
        
        if (key == Input.KEY_LEFT) game.xCursor -= shift?5:1;
        if (key == Input.KEY_RIGHT) game.xCursor += shift?5:1;
        if (key == Input.KEY_DOWN) game.yCursor += shift?5:1;
        if (key == Input.KEY_UP) game.yCursor -= shift?5:1;
        if (key == Input.KEY_ENTER) {
            if (rectangleMode) {
                doAction(game, game.chunkManager, game.xCursor, game.yCursor, game.xCursor+sx1, game.yCursor+sy1);
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
                        doAction(game, game.chunkManager, sx0, sy0, sx1, sy1);
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
        if (items != null) {
            if (c == '+') inventoryCursor += 1;
            if (c == '-') inventoryCursor -= 1;
            int len = items.size();
            if (inventoryCursor < 0) inventoryCursor += len;
            if (inventoryCursor >= len) inventoryCursor -= len;
        }
    }
    private void doAction(InGameState game, ChunkManager cm, int x0, int y0, int x1, int y1) {
        if (items != null && items.size() > 0) {
            Item activeItem = items.get(inventoryCursor);
            if (activeItem != null) {
                System.out.println("USE ITEM: " + activeItem.getName());
                System.out.println("Coords: " + x0 + "/" + y0 + " to " + x1+ "/" + y1);
                System.out.println("IC: " + inventoryCursor);
                
                for (int x = x0; x <= x1; x++) {
                    for (int y = y0; y <= y1; y++) {
                        Chunk chunk = cm.getChunk(x, y);
                        int rx = ChunkManager.toRelative(x);
                        int ry = ChunkManager.toRelative(y);
                        if(!activeItem.interactOn(cm.getTile(x,y), chunk, x, y, chunk.player)) {
                            chunk.getTile(rx, ry).interact(chunk.cm, x, y, chunk.player, activeItem);
                        }
                        
                        if (activeItem.isDepleted()) {
                            System.out.println("REMOVING DEPLETED ITEM:");
                            System.out.println("  " + activeItem.getName());
                            
                            if(!chunk.player.inventory.items.remove(activeItem))
                                System.out.println("ERR: Could not remove activeItem from player inventory");
                            
                            if (items != chunk.player.inventory.items) {
                                System.out.println(" Remove from sub list");
                                if(!items.remove(activeItem)) {
                                    System.out.println("ERR: Could not remove activeItem from subList");
                                }
                            }
                            
                            System.out.println("Reset inventory cursor");
                            inventoryCursor = 0;
                            if (items.isEmpty()) {
                                game.changeMode(new ScrollMode());
                                System.out.println("change game mode");
                            }
                            return;
                        }
                    }
                }
                //set cursor on activeItem
                for (int i = 0; i < items.size(); i++) {
                    if (activeItem == items.get(i)) { //activeItem.equals(items.get(i))) {
                        inventoryCursor = i;
                        break;
                    }
                }
            } else {
                System.out.println("NO ACTIVE ITEM");
                game.changeMode(new ScrollMode());
            }
        }
    }
    
    
    @Override
    public void drawCursor(Graphics g, int xCursor, int yCursor) {
        
        int ts = Config.getInstance().tileSize;
        int tm = Config.getInstance().tileMask;
        
        if (rectangleMode) {
            int x0 = xCursor;
            int y0 = yCursor;
            int x1 = x0 + sx1;
            int y1 = y0 + sy1;
            for (int x = x0; x <= x1; x++) {
                for (int y = y0; y <= y1; y++) {
                    TileSet.drawBackground(xCursor*ts, yCursor*ts, ts, ts, Color.black);
                    TileSet.draw(8+5*16, xCursor*ts, yCursor*ts, Color.blue);
                }
            }
            TileSet.drawBackground(xCursor*ts, yCursor*ts, ts, ts, Color.black);
            TileSet.draw(11+2*16, xCursor*ts, yCursor*ts, Color.blue);
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
                        if (x == x0&& y == y0) continue;
                        TileSet.drawBackground(x*ts, y*ts, ts, ts, Color.black);
                        TileSet.draw(13+3*16, x*ts, y*ts, Color.cyan); //=
                    }
                }
                if (!rect1) {
                    TileSet.drawBackground(x0*ts, y0*ts, ts, ts, Color.black);
                    TileSet.draw(11+2*16, x0*ts, y0*ts, Color.cyan);
                }
            }
        }
        TileSet.drawBackground(xCursor*ts, yCursor*ts, ts, ts, Color.black);
        TileSet.draw(8+5*16, xCursor*ts, yCursor*ts, Color.yellow);
        
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta, InGameState ingame) {
        int ts = Config.getInstance().tileSize;
        int tm = Config.getInstance().tileMask;
        
        int px = ts / 2;
        int py = px + (px/2);
        
        
        int x0 = ingame.xScroll>>tm;
        int y0 = ingame.yScroll>>tm;
        int w = ((container.getWidth()-250-px)>>tm);
        int h = ((container.getHeight()-py)>>tm);
        
        x0 = Globals.x;
        y0 = Globals.y;
        w = Globals.w;
        h = Globals.h;
        
        if (ingame.xCursor < x0) ingame.xScroll -= ts * (x0 - ingame.xCursor);
        if (ingame.yCursor < y0) ingame.yScroll -= ts * (y0 - ingame.yCursor);
        if (ingame.xCursor >= x0+w) ingame.xScroll += ts * (ingame.xCursor - (x0+w-1));
        if (ingame.yCursor >= y0+h) ingame.yScroll += ts * (ingame.yCursor - (y0+h-1));
        
    }
    
}
