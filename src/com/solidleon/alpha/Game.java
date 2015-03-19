/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solidleon.alpha;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author Markus
 */
public class Game extends StateBasedGame {

    public static final int MENU = 0;
    public static final int PLAY = 1;
    public static final boolean DEBUG = true;
    
    public static void main(String []args) {
        try {
            AppGameContainer app = new AppGameContainer(new Game("Alpha"));
//            app.setDisplayMode(1024, 768, false);
//            app.setDisplayMode(800, 600, false);
            app.setDisplayMode(50*16 + 2, 392, false);
            app.setAlwaysRender(true); //otherwise, if the game is in background, it would not run ... maybe change it back
            app.setShowFPS(false);
            app.setSmoothDeltas(true);
            app.setTargetFrameRate(60);
            Keyboard.enableRepeatEvents(true);
            app.start();
        } catch (SlickException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Throwable t) {
            JOptionPane.showMessageDialog(null, "Alpha has encountered a problem!\n"+t.getMessage(), "Alpha | Error", JOptionPane.ERROR_MESSAGE);
            try {
                PrintWriter pw = new PrintWriter("err.log");
                t.printStackTrace(pw);
                pw.flush();
                pw.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public Game(String name) {
        super(name);
    }

    @Override
    public void initStatesList(GameContainer container) throws SlickException {
        addState(new InGameState(PLAY));
    }
    
}
