/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solidleon.alpha.cursormode;

import com.solidleon.alpha.Font;
import com.solidleon.alpha.InGameState;
import com.solidleon.alpha.crafting.Crafting;
import com.solidleon.alpha.game.world.Chunk;

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
public class BuildSelectionCursor extends CursorMode {
    

    public BuildSelectionCursor() {
        addKeybinding(new Keybinding('w', "Workshops") {

            @Override
            public void action(InGameState game, Chunk area, int xCursor, int yCursor, int key, char c) {
                game.changeMode(new BuildCursor2("Workshops", Crafting.workshopPlans, area.player, area), false);
            }
            
        });
    }
    
    
    
    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g, InGameState ingame) {
        int yo = 0;
        Font.drawString("BUILD", 234.0f/2.0f - Font.getWidth("BUILD") / 2.0f, yo, Color.white);
        
        for (int i = 0; i < keybindings.size(); i++) {
            Keybinding kb = keybindings.get(i);
            Font.drawString("\\c[green]" + kb.c + "\\c[gray]: " + kb.object.toString(), 0,yo+ 24 + Font.getLineHeight()*i, Color.gray);
        }
    }

    @Override
    public void keyPressed(InGameState game, Chunk area, int xCursor, int yCursor, int key, char c) {
        for (int i = 0; i < keybindings.size(); i++) {
            Keybinding kb = keybindings.get(i);
            if (kb.c == c) {
                kb.action(game, area, xCursor, yCursor, key, c);
                return;
            }
        }
        if (key == Input.KEY_ESCAPE)game.changeMode(new ScrollMode(), false);
    }
    
    
}
