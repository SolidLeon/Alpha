/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solidleon.alpha.cursormode;

import com.solidleon.alpha.Font;
import com.solidleon.alpha.InGameState;
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
public class CommandCursor extends CursorMode {

    protected class Command {
        public String keybindingText;
        public String text;
        public int key = -1;
        public char c;

        public Command(String keybindingText, String text, char c) {
            this.keybindingText = keybindingText;
            this.text = text;
            this.c = c;
        }
        
        public Command(String keybindingText, String text, int key) {
            this.keybindingText = keybindingText;
            this.text = text;
            this.key = key;
            this.c = ' ';
        }
        
        public boolean isKeyPressed(int key, char c) {
            if (this.key != -1) {
                return this.key == key;
            } else {
                return this.c == c;
            }
        }

        public void performAction(InGameState game, Chunk area, int xCursor, int yCursor) {
            
        }
        
    }
    
    private List<Command> commands = new ArrayList<Command>();
    protected boolean shift = false;
    
    protected void addCommand(Command c) {
        commands.add(c);
    }
    
    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g, InGameState ingame) {
        //upper half commands
        for (int i = 0; i < commands.size(); i++) {
            Command c = commands.get(i);
            Font.drawString(c.text, 0, 0 + Font.getLineHeight()*i, Color.gray);
            Font.drawString("(\\c[green]"+c.keybindingText+"\\c[gray])", width-Font.getWidth("("+c.keybindingText+")"), 0 + Font.getLineHeight()*i, Color.gray);
        }
        //lower half custom content
        g.translate(0, container.getHeight()/2);
        renderContent(container, game, g, ingame);
        g.translate(0, -(container.getHeight()/2));
    }
    public void renderContent(GameContainer container, StateBasedGame game, Graphics g, InGameState ingame) {}

    @Override
    public void keyReleased(InGameState game, Chunk area, int xCursor, int yCursor, int key, char c) {
        if (key == Input.KEY_LSHIFT || key == Input.KEY_RSHIFT) shift = false;
    }

    
    @Override
    public void keyPressed(InGameState game, Chunk area, int xCursor, int yCursor, int key, char c) {
        if (key == Input.KEY_LSHIFT || key == Input.KEY_RSHIFT) shift = true;
        for (int i = 0; i < commands.size(); i++) {
            Command cmd = commands.get(i);
            if (cmd.isKeyPressed(key, c)) {
                cmd.performAction(game, area, xCursor, yCursor);
                return;
            }
        }
    
    }
    
    protected void addMovementKeys() {
        
        addCommand(new Command("RIGHT", "move right", Input.KEY_RIGHT) {

            @Override
            public void performAction(InGameState game, Chunk area, int xCursor, int yCursor) {
                game.xCursor += shift ? 5 : 1;
            }
            
        });
        addCommand(new Command("LEFT", "move left", Input.KEY_LEFT) {

            @Override
            public void performAction(InGameState game, Chunk area, int xCursor, int yCursor) {
                game.xCursor -= shift ? 5 : 1;
            }
            
        });
        addCommand(new Command("UP", "move up", Input.KEY_UP) {

            @Override
            public void performAction(InGameState game, Chunk area, int xCursor, int yCursor) {
                game.yCursor -= shift ? 5 : 1;
            }
            
        });
        addCommand(new Command("DOWN", "move down", Input.KEY_DOWN) {

            @Override
            public void performAction(InGameState game, Chunk area, int xCursor, int yCursor) {
                game.yCursor += shift ? 5 : 1;
            }
            
        });
    }
    
    
    
}
