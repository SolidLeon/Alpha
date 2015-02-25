/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solidleon.alpha.cursormode;

import com.solidleon.alpha.Font;
import com.solidleon.alpha.InGameState;
import com.solidleon.alpha.crafting.Recipe;
import com.solidleon.alpha.entity.Factory;
import com.solidleon.alpha.entity.Player;
import com.solidleon.alpha.entity.Factory.QueueItem;
import com.solidleon.alpha.game.world.Chunk;
import com.solidleon.alpha.item.Item;
import com.solidleon.alpha.item.ResourceItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
public class CraftingMode extends CursorMode {

    public List<Recipe> recipes;
    private int cursor;
    private boolean craftAll = false;
    private Factory factory;
    private Player player;
    
    public CraftingMode(List<Recipe> recipes, Player player, Factory factory) {
        this.player = player;
        this.recipes = recipes;
        this.factory = factory;
        for (int i = 0; i < recipes.size(); i++)
            recipes.get(i).checkCanCraft(player);
        
        List<Character> usedChars = new ArrayList<Character>();
        //Create keybindings
        for (int i = 0; i < recipes.size(); i++) {
            Recipe r = recipes.get(i);
            char c = r.getName().toLowerCase().charAt(0);
            if (usedChars.contains(c)) {
                c = r.getName().toUpperCase().charAt(0); 
                if (usedChars.contains(c)) {
                    //find ANY key
                    boolean found = false;
                    //find any occuring char in lower case
                    String lc = r.getName().toLowerCase();
                    for (int j = 0; j < lc.length(); j++) {
                        char ch = lc.charAt(j);
                        if (!usedChars.contains(ch)) {
                            found = true;
                            c = ch;
                            break;
                        }
                    }
                    if (!found) {
                        String uc = r.getName().toUpperCase();
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
            addKeybinding(new Keybinding(c, r) {

                @Override
                public void action(InGameState game, Chunk area, int xCursor, int yCursor, int key, char c) {
                    craft((Recipe)object);
                }
                
            });
            usedChars.add(c);
        }
        
        Collections.sort(recipes, new Comparator<Recipe>() {
            @Override
            public int compare(Recipe r1, Recipe r2) {
                if (r1.canCraft && !r2.canCraft) return -1;
                if (!r1.canCraft && r2.canCraft) return 1;
                return 0;
            }
        });
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g, InGameState ingame) {
        
        Font.drawString(factory.getName().toUpperCase(), width / 2 - Font.getWidth(factory.getName().toUpperCase()) / 2, 0, Color.white);
        if (craftAll)
            Font.drawString("ALL", 0, 0, Color.red);
        for (int i = 0; i < keybindings.size(); i++) {
            Color col = i == cursor ? Color.white : Color.gray;
            Keybinding kb = keybindings.get(i);
            Recipe r = (Recipe) kb.object;
            Font.drawString("\\c[green]" + kb.c +"\\c[gray]: " + r.getExtendedString(), 0, 24 + Font.getLineHeight()*i, col);
            
            if (!r.canCraft)
                Font.drawString("(\\c[red]na\\c[gray])", width - Font.getWidth("(\\c[red]na\\c[gray])"), 24 + Font.getLineHeight()*i, Color.gray);
        }
//        for (int i = 0; i <recipes.size(); i++) {
//            Color col = i == cursor ? Color.white : Color.gray;
//            
//            Font.drawString(recipes.get(i).getName(), 0, 24 + Font.getLineHeight()*i, col);
//            
//            if (!recipes.get(i).canCraft)
//                Font.drawString("(\\c[red]na\\c[gray])", width - Font.getWidth("(\\c[red]na\\c[gray])"), 24 + Font.getLineHeight()*i, Color.gray);
//        }
        
        int perc = (int)((1.0f - ( (float)factory.timer / factory.maxTime))*100.0f);
        int len = factory.queue.size();
        if (len == 0) perc = 0;
        Font.drawString("Queue: \\c[cyan]" + len + "/" + factory.getMaxQueue() + " \\c[gray]" + perc + "%", 0, height / 2, Color.white);
        
        g.setColor(Color.white);
//        g.drawRect(width - 100-1, height / 2 - 1, 100+2, 8+1);
        
        
        if (len > 5) len = 5;
        for (int i = 0; i < len; i++) {
            QueueItem queueItem = factory.queue.get(i);
            Item result = queueItem.item;
            int count = 1;
            if (result instanceof ResourceItem) {
                count = ((ResourceItem) result).count;
            }
            count *= queueItem.count;
            
            Font.drawString(String.format("%04d ", count), 0, height/2 + Font.getLineHeight()*(i+1), Color.white);
            Font.drawString(result.getName(), Font.getWidth(String.format("%04d ", count)), height/2 + Font.getLineHeight()*(i+1), result.getColor());
        }
        
    }

    @Override
    public void keyPressed(InGameState game, Chunk area, int xCursor, int yCursor, int key, char c) {
        if (key == Input.KEY_ESCAPE) game.changeMode(new SelectCursor(), false);
        if (key == Input.KEY_TAB) craftAll = !craftAll; 
//        if (key == Input.KEY_UP) cursor -= 1;
//        if (key == Input.KEY_DOWN) cursor += 1;
//        int len = recipes.size();
//        if (cursor < 0) cursor += len;
//        if (cursor >= len) cursor -= len;
        
        if ((c >= 'a' && c <= 'z' ) || (c >= 'A' && c <= 'Z')) {
            checkKeybindings(game, area, xCursor, yCursor, key, c);
        }
        
        
    }
    
    private void craft(Player player) {
        if (recipes.isEmpty()) return;
        Recipe r = recipes.get(cursor);
        if(factory.queue(r, craftAll, player)) {
            for (int i = 0; i < recipes.size(); i++)
                recipes.get(i).checkCanCraft(player);
        }
    }
    private void craft(Recipe recipe) {
        if (recipes.isEmpty()) return;
        if(factory.queue(recipe, craftAll, player)) {
            for (int i = 0; i < recipes.size(); i++)
                recipes.get(i).checkCanCraft(player);
        }
    }
    
    
    @Override
    public void notifyDirtyInventory(Player player) {
        for (int i = 0; i < recipes.size(); i++) {
            recipes.get(i).checkCanCraft(player);
        }
    }

    
}
