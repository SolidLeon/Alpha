/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solidleon.alpha.cursormode;

import com.solidleon.alpha.InGameState;
import com.solidleon.alpha.game.world.Chunk;

/**
 *
 * @author Markus
 */
public class Keybinding {

    public char c;
    /** This objects to string is used as display text */
    public Object object;

    public Keybinding(char c, Object object) {
        this.c = c;
        this.object = object;
    }

    public void action(InGameState game, Chunk area, int xCursor, int yCursor, int key, char c) {
    }
}
