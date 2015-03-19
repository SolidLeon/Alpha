/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solidleon.alpha.cursormode;

import com.solidleon.alpha.Config;
import com.solidleon.alpha.Font;
import com.solidleon.alpha.InGameState;
import com.solidleon.alpha.TileSet;
import com.solidleon.alpha.game.world.Chunk;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Selection cursor is a cursor that extends moveable cursor,
 * by adding
 * @author Markus
 */
public class SelectionCursor extends CommandCursor{

    public class ListItem {
        public Object object;

        public ListItem(Object o) {
            this.object = o;
        }
        
        public void render(int x, int y, boolean selected) {
            Font.drawString(object.toString(), x, y, selected ? Color.red : Color.white);
        }
        
    }
    
    private List<ListItem> list = new ArrayList<ListItem>();
    private int cursor;
    
    public SelectionCursor() {
        addCommand(new Command("ESC", "back", Input.KEY_ESCAPE) {
            @Override
            public void performAction(InGameState game, Chunk area, int xCursor, int yCursor) {
                game.changeMode(oldCursorMode);
            }
        });
        addCommand(new Command("+", "cursor down", '+') {

            @Override
            public void performAction(InGameState game, Chunk area, int xCursor, int yCursor) {
                cursor += 1;
                if (cursor >= list.size()) cursor -= list.size();
            }
            
        });
        addCommand(new Command("-", "cursor up", '-') {

            @Override
            public void performAction(InGameState game, Chunk area, int xCursor, int yCursor) {
                cursor -= 1;
                if (cursor < 0) cursor += list.size();
            }
            
        });
    }
    
    
    protected Object getSelection() {
        return list.get(cursor).object;
    }

    protected void addListItem(ListItem listItem) {
        list.add(listItem);
    }
    
    @Override
    public void renderContent(GameContainer container, StateBasedGame game, Graphics g, InGameState ingame) {
        for (int i = 0; i < list.size(); i++) {
            ListItem item = list.get(i);
            item.render(0, Font.getLineHeight() * i, i == cursor);
        }
    }

    @Override
    public void drawCursor(Graphics g, int xCursor, int yCursor) {
        int ts = Config.getInstance().tileSize;
        int tm = Config.getInstance().tileMask;
        TileSet.draw(8+5*16, xCursor*ts, yCursor*ts, Color.red);
    }
    
    
    
}
