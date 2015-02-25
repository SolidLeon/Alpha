/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solidleon.alpha.entity;

import com.solidleon.alpha.Config;
import com.solidleon.alpha.TileSet;

import java.io.Serializable;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

/**
 *
 * @author Markus
 */
public class Cat extends Entity implements Serializable{
    private int walkTime = 0;
    private int xa, ya;
    
    @Override
    public void render(Graphics g) {
        int ts = Config.getInstance().tileSize;
        
        TileSet.draw(3+6*16, x*ts, y*ts, Color.white);
    }

    @Override
    public void update(int delta) {
        walkTime -= delta;
        if (walkTime <= 0) {
            if (!move(xa, ya) || random.nextInt(40) == 0) {
                xa = random.nextInt(3)-1;
                ya = random.nextInt(3)-1;
            }
            walkTime += 250;
        }
    }
    
    
}
