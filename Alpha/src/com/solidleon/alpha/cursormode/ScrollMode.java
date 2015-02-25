/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solidleon.alpha.cursormode;

import com.solidleon.alpha.Font;
import com.solidleon.alpha.InGameState;
import com.solidleon.alpha.eInteractionMode;
import com.solidleon.alpha.crafting.Crafting;
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
public class ScrollMode extends CursorMode {
    private boolean shift = false;

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g, InGameState ingame) {
        
        Font.drawString("\\c[green]d\\c[gray]: designation", 0, 24 + Font.getLineHeight(), Color.white);
        Font.drawString("\\c[green]b\\c[gray]: build", 0, 24 + Font.getLineHeight() * 2, Color.white);
        Font.drawString("\\c[green]s\\c[gray]: select", 0, 24 + Font.getLineHeight() * 3, Color.white);
        Font.drawString("\\c[green]k\\c[gray]: look", 0, 24 + Font.getLineHeight() * 4, Color.white);
        
    }

    
    
    @Override
    public void keyReleased(InGameState game, Chunk area, int xCursor, int yCursor, int key, char c) {
        if (key == Input.KEY_LSHIFT || key == Input.KEY_RSHIFT) shift = false;
    }
    
    @Override
    public void keyPressed(InGameState game, Chunk area, int xCursor, int yCursor, int key, char c) {
        if (key == Input.KEY_LSHIFT || key == Input.KEY_RSHIFT) shift = true;
//        if (key == Input.KEY_D) game.changeMode(new InteractionMode(eInteractionMode.USE), true);
        if (key == Input.KEY_D) game.changeMode(new DesignationCursor(area.player), false);
        if (key == Input.KEY_I) game.changeMode(new InventoryMode(), false);
        if (key == Input.KEY_B) game.changeMode(new BuildSelectionCursor(), false);
        if (key == Input.KEY_S) game.changeMode(new SelectCursor(), false);
        if (key == Input.KEY_K) game.changeMode(new LookMode(), false);
        if (key == Input.KEY_ESCAPE) game.changeMode(new OptionsMode(this), false);
        if (key == Input.KEY_A) game.changeMode(new CursorMode(){

            @Override
            public void render(GameContainer container, StateBasedGame game, Graphics g, InGameState ingame) {
                g.setColor(Color.yellow);
                g.fillRect(0, 0, this.width, this.height);
            }

            @Override
            public void keyPressed(InGameState game, Chunk area, int xCursor, int yCursor, int key, char c) {
                game.changeMode(new ScrollMode());
            }
            
            
            
        }, false);
        if (key == Input.KEY_LEFT) game.xScroll -= shift?256:80;
        if (key == Input.KEY_RIGHT) game.xScroll += shift?256:80;
        if (key == Input.KEY_DOWN) game.yScroll += shift?256:80;
        if (key == Input.KEY_UP) game.yScroll -= shift?256:80;
    }

    
    
    
}
