/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solidleon.alpha.cursormode;

import com.solidleon.alpha.Config;
import com.solidleon.alpha.Font;
import com.solidleon.alpha.InGameState;
import com.solidleon.alpha.TileSet;
import com.solidleon.alpha.entity.Cat;
import com.solidleon.alpha.entity.Entity;
import com.solidleon.alpha.game.world.Chunk;

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
public class SelectCursor extends CursorMode {
    private int selectionCursor;
    private boolean shift;
    
    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g, InGameState ingame) {
        int yo = 0;
        Font.drawString("SELECT", 234.0f/2.0f - Font.getWidth("SELECT") / 2.0f, yo, Color.white);
        List<Entity> entities = ingame.chunkManager.getEntities(ingame.xCursor, ingame.yCursor, ingame.xCursor, ingame.yCursor);
        for (int i = 0; i <entities.size(); i++) {
            Font.drawString(entities.get(i).getName(), 0,yo+ 24 + Font.getLineHeight()*i, i == selectionCursor ? Color.white : Color.gray);
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
        if (key == Input.KEY_ENTER) selectEntity(game);
        if (c == '+') selectionCursor += 1;
        if (c == '-') selectionCursor -= 1;
        int len = area.getEntites(game.xCursor, game.yCursor, game.xCursor, game.yCursor).size();
        if (selectionCursor < 0) selectionCursor += len;
        if (selectionCursor >= len) selectionCursor -= len;
    }
    private void selectEntity(InGameState game) {
        List<Entity> entities = game.chunkManager.getEntities(game.xCursor, game.yCursor, game.xCursor, game.yCursor);
        if (entities.size() > 0)
            entities.get(selectionCursor).onSelect(game);
        selectionCursor = 0;
    }
    
    @Override
    public void drawCursor(Graphics g, int xCursor, int yCursor) {
        int ts = Config.getInstance().tileSize;
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
        
//        if (ingame.xCursor < x0) ingame.xCursor = x0;
//        if (ingame.yCursor < y0) ingame.yCursor = y0;
//        if (ingame.xCursor >= x0+w) ingame.xCursor = x0+w-1;
//        if (ingame.yCursor >= y0+h) ingame.yCursor = y0+h-1;
        if (ingame.xCursor < x0) ingame.xScroll -= ts * (x0 - ingame.xCursor);
        if (ingame.yCursor < y0) ingame.yScroll -= ts * (y0 - ingame.yCursor);
        if (ingame.xCursor >= x0+w) ingame.xScroll += ts * (ingame.xCursor - (x0+w-1));
        if (ingame.yCursor >= y0+h) ingame.yScroll += ts * (ingame.yCursor - (y0+h-1));
    }
}
