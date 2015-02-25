/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solidleon.alpha.cursormode;

import com.solidleon.alpha.Font;
import com.solidleon.alpha.InGameState;
import com.solidleon.alpha.game.world.Chunk;
import com.solidleon.alpha.item.ResourceItem;
import com.solidleon.alpha.item.resource.Resource;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author markusmannel
 */
public class MarketMode extends CursorMode {

    private int []price;

    public MarketMode() {
        int len = 1;
        price = new int[len];
        
        
    }
    
    
    @Override
    public void renderCustom(GameContainer container, StateBasedGame game, Graphics g, InGameState aThis) {
        g.setColor(Color.black);
        g.fillRect(0, 0, customWidth, customHeight);
        Font.drawString("Test", 0, 0, Color.white);
        Font.drawString("END", customWidth-Font.getWidth("END"), customHeight-Font.getLineHeight(), Color.white);
    }

    
    
    @Override
    public void keyPressed(InGameState game, Chunk area, int xCursor, int yCursor, int key, char c) {
        game.changeMode(new ScrollMode(), false);
    }
    
}
