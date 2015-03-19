/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solidleon.alpha.entity;

import com.solidleon.alpha.Config;
import com.solidleon.alpha.InGameState;
import com.solidleon.alpha.TileSet;
import com.solidleon.alpha.crafting.BuildPlan;
import com.solidleon.alpha.cursormode.ContainerMode;
import com.solidleon.alpha.item.Inventory;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

/**
 *
 * @author Markus
 */
public class ConstructionSite extends Entity {

    private BuildPlan plan;
    private int timeLeft;
    private Inventory container;
    
    public ConstructionSite(int x, int y, BuildPlan plan) {
        this.x = x;
        this.y = y;
        this.plan = plan;
        w = plan.template.w;
        h = plan.template.h;
        timeLeft = plan.craftTime;
        container = new Inventory();
    }

    @Override
    public void render(Graphics g) {
        int ts = Config.getInstance().tileSize;
        for (int xt = 0; xt < w; xt++) {
            for (int yt = 0; yt < h; yt++) {
                TileSet.draw(3+2*16, (x+xt)*ts, (y+yt)*ts, Color.gray);
            }
        }
    }

    @Override
    public String getName() {
        return "Construction Site";
    }

    
    
    @Override
    public void gameUpdate() {
        timeLeft -= 1;
        if (timeLeft <= 0) {
            Entity e = null;
            try {
                e = plan.result.newInstance();
            } catch (InstantiationException ex) {
                Logger.getLogger(ConstructionSite.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(ConstructionSite.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (e == null) throw new RuntimeException("Entity instance is null!");
            e.x = x;
            e.y = y;
            remove();
            area.add(e);
        }
    }
    
    
    
}
