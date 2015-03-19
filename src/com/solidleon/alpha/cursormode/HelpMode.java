/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solidleon.alpha.cursormode;

import com.solidleon.alpha.Font;
import com.solidleon.alpha.InGameState;
import com.solidleon.alpha.TextFile;
import com.solidleon.alpha.TileSet;
import com.solidleon.alpha.game.world.Chunk;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author Markus
 */
public class HelpMode extends CursorMode {

    private TextFile gameManual;
    private CursorMode oldMode;
    private int scroll;
    
    public HelpMode(CursorMode oldMode) {
        this.oldMode = oldMode;
        gameManual = new TextFile("/com/reddwarf/alpha/game_manual.txt");
    }

    @Override
    public void renderCustom(GameContainer container, StateBasedGame game, Graphics g, InGameState aThis) {
        g.setColor(Color.black);
        g.fillRect(0, 0, container.getWidth(), container.getHeight());
        
        TileSet.drawFrame(0, 0, container.getWidth(), container.getHeight());
        int maxLines = (container.getHeight() - 16-16) / Font.getLineHeight();
        int c = 0;
        for (int i = scroll; i < gameManual.lines.size(); i++) {
            if (c >= maxLines) break;
            String line = gameManual.lines.get(i);
            Font.drawString(line, 16, 16+c*Font.getLineHeight(), Color.white);
            c++;
        }
    }

    @Override
    public void keyPressed(InGameState game, Chunk area, int xCursor, int yCursor, int key, char c) {
        if (key == Input.KEY_ESCAPE) game.changeMode(oldMode);
        if (key == Input.KEY_UP) scroll-=1;
        if (key == Input.KEY_DOWN) scroll+=1;
        int len = gameManual.lines.size();
        if (scroll < 0) scroll += len;
        if (scroll >= len) scroll -= len;
    }
    
    
    
    
    
}
