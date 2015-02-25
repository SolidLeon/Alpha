/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solidleon.alpha.cursormode;

import com.solidleon.alpha.Font;
import com.solidleon.alpha.InGameState;
import com.solidleon.alpha.item.Inventory;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author markusmannel
 */
public class ContainerMode extends CursorMode {
    private Inventory container;

    public ContainerMode(Inventory container) {
        this.container = container;
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g, InGameState ingame) {
        Font.drawString("Tset", 0, 0, Color.red);
    }
    
    
}
