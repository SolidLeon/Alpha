/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solidleon.alpha.entity.creature;

import com.solidleon.alpha.Config;
import com.solidleon.alpha.entity.Entity;

import java.io.Serializable;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

/**
 *
 * @author markusmannel
 */
public class Creature extends Entity implements Serializable{
    
    protected int hp;
    protected int hpMax;
    private int xa, ya;
    
    public Creature(int x, int y) {
        this.x = x;
        this.y = y;
        w = h = 1;
    }

    @Override
    public void gameUpdate() {
        if (!move(xa, ya) || random.nextInt(40) == 0) {
            xa = random.nextInt(3)-1;
            ya = random.nextInt(3)-1;
        }
    }

    @Override
    public void render(Graphics g) {
        int ts = Config.getInstance().tileSize;
        g.setColor(Color.cyan);
        g.fillRect(x*ts, y*ts, w*ts, h*ts);
    }
    
    
    
    
}
