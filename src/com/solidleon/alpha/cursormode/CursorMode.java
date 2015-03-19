/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solidleon.alpha.cursormode;

import com.solidleon.alpha.InGameState;
import com.solidleon.alpha.crafting.BuildPlan;
import com.solidleon.alpha.crafting.Recipe;
import com.solidleon.alpha.entity.Player;
import com.solidleon.alpha.game.world.Chunk;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author Markus
 */
public class CursorMode {
    
    public CursorMode oldCursorMode;
    protected int width = 250-32;
    protected int height = 600;
    protected int customWidth = 250-32;
    protected int customHeight = 600;
    
    protected Object selected;
    protected List<Keybinding> keybindings = new ArrayList<Keybinding>();
    
    public void init(int w, int h) {
        this.width = w;
        this.height = h;
    }
    /**
     * Initializes width and height, for the custom render method
     * @param w
     * @param h 
     */
    public void initCustom(int w, int h) {
        this.customWidth = w;
        this.customHeight = h;
    }
    
    protected void addKeybinding(Keybinding kb) {
        keybindings.add(kb);
    }
    
    protected void checkKeybindings(InGameState game, Chunk area, int xCursor, int yCursor, int key, char c) {
        for (int i = 0; i < keybindings.size(); i++) {
            Keybinding kb = keybindings.get(i);
            if (kb.c == c) {
                kb.action(game, area, xCursor, yCursor, key, c);
                return;
            }
        }
    }
    
    protected void clearKeybindings() {
        keybindings.clear();
    }
    
    protected void assignKeybindings(List<? extends KeybindListItem> list) {
        List<Character> usedChars = new ArrayList<Character>();
        //Create keybindings
        for (int i = 0; i < list.size(); i++) {
            KeybindListItem o = list.get(i);
            char c = o.getName().toLowerCase().charAt(0);
            if (usedChars.contains(c)) {
                c = o.getName().toUpperCase().charAt(0); 
                if (usedChars.contains(c)) {
                    //find ANY key
                    boolean found = false;
                    //find any occuring char in lower case
                    String lc = o.getName().toLowerCase();
                    for (int j = 0; j < lc.length(); j++) {
                        char ch = lc.charAt(j);
                        if (!usedChars.contains(ch)) {
                            found = true;
                            c = ch;
                            break;
                        }
                    }
                    if (!found) {
                        String uc = o.getName().toUpperCase();
                        for (int j = 0; j < uc.length(); j++) {
                            char ch = lc.charAt(j);
                            if (!usedChars.contains(ch)) {
                                found = true;
                                c = ch;
                                break;
                            }
                        }
                    }
                    if (!found) {
                        for (int j = 0; j < 26; j++) {
                            char ch = (char)('a' + j);
                            if (!usedChars.contains(ch)) {
                                found = true;
                                c = ch;
                                break;
                            }
                        }
                    }
                    if (!found) {
                        for (int j = 0; j < 26; j++) {
                            char ch = (char)('A' + j);
                            if (!usedChars.contains(ch)) {
                                found = true;
                                c = ch;
                                break;
                            }
                        }
                    }
                    if (!found)
                        throw new RuntimeException("Double use keybinding, " + c);
                }
            }
            addKeybinding(new Keybinding(c, o) {
                @Override
                public void action(InGameState game, Chunk area, int xCursor, int yCursor, int key, char c) {
                    selected = object;
                }
            });
            usedChars.add(c);
        }
    }
    
    public void notifyDirtyInventory(Player player) {
        
    }
    
    public void drawCursor(Graphics g, int xCursor, int yCursor) {
    }

    public void keyPressed(InGameState game, Chunk area, int xCursor, int yCursor, int key, char c) {
        
    }

    public void update(GameContainer container, StateBasedGame game, int delta, InGameState ingame) {
        
    }

    public void keyReleased(InGameState game, Chunk area, int xCursor, int yCursor, int key, char c) {
        
    }

    public void render(GameContainer container, StateBasedGame game, Graphics g, InGameState ingame) {
    }

    public void renderCustom(GameContainer container, StateBasedGame game, Graphics g, InGameState aThis) {
        
    }

    public void init(GameContainer container, StateBasedGame game, int delta, InGameState ingame, Chunk area, CursorMode oldCursorMode) {
        this.oldCursorMode = oldCursorMode;
        init(container, game, delta, ingame, area);
    }
    
    public void init(GameContainer container, StateBasedGame game, int delta, InGameState ingame, Chunk area) {
    }
}
