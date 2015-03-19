/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solidleon.alpha.cursormode;

import com.solidleon.alpha.Config;
import com.solidleon.alpha.Font;
import com.solidleon.alpha.InGameState;
import com.solidleon.alpha.TileSet;
import com.solidleon.alpha.entity.Entity;
import com.solidleon.alpha.game.world.Chunk;
import com.solidleon.alpha.game.world.tile.Tile;

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
public class LookMode extends CursorMode {

    private boolean shift = false;
    private Tile tile;
    private List<Entity> entities = new ArrayList<Entity>();

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g, InGameState ingame) {
        Font.drawString(tile.getName(ingame.getChunk().cm, ingame.xCursor, ingame.yCursor), 0, 24, Color.white);
        for (int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);
            Font.drawString(e.getName(), 0, 24+Font.getLineHeight()*(i+1), Color.white);
        }
    }
    
    @Override
    public void drawCursor(Graphics g, int xCursor, int yCursor) {
        int ts = Config.getInstance().tileSize;
        int tm = Config.getInstance().tileMask;
        TileSet.drawBackground(xCursor*ts, yCursor*ts, ts, ts, Color.black);
        TileSet.draw(8+5*16, xCursor*ts, yCursor*ts, Color.yellow);
    }

    @Override
    public void keyReleased(InGameState game, Chunk area, int xCursor, int yCursor, int key, char c) {
        if (key == Input.KEY_LSHIFT || key == Input.KEY_RSHIFT) shift = false;
    }
    
    

    @Override
    public void keyPressed(InGameState game, Chunk area, int xCursor, int yCursor, int key, char c) {
        if (key == Input.KEY_LSHIFT || key == Input.KEY_RSHIFT) shift = true;
        if (key == Input.KEY_ESCAPE) game.changeMode(new ScrollMode());
        int oldX = game.xCursor;
        int oldY = game.yCursor;
        if (key == Input.KEY_LEFT) game.xCursor -= shift ? 5: 1;
        if (key == Input.KEY_RIGHT) game.xCursor += shift ? 5 : 1;
        if (key == Input.KEY_DOWN) game.yCursor += shift ? 5 : 1;
        if (key == Input.KEY_UP) game.yCursor -= shift ? 5 : 1;
        int newX = game.xCursor;
        int newY = game.yCursor;
        if (newX != oldX ||newY != oldY) {
            entities.clear();
            entities.addAll(game.chunkManager.getEntities(newX, newY, newX, newY));
        }
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
        
        if (ingame.xCursor < x0) ingame.xScroll -= ts * (x0 - ingame.xCursor);
        if (ingame.yCursor < y0) ingame.yScroll -= ts * (y0 - ingame.yCursor);
        if (ingame.xCursor >= x0+w) ingame.xScroll += ts * (ingame.xCursor - (x0+w-1));
        if (ingame.yCursor >= y0+h) ingame.yScroll += ts * (ingame.yCursor - (y0+h-1));
        
        tile = ingame.chunkManager.getTile(ingame.xCursor, ingame.yCursor);
        
    }
    
    
    
    
    
}
