/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solidleon.alpha.cursormode;

import com.solidleon.alpha.Config;
import com.solidleon.alpha.Font;
import com.solidleon.alpha.InGameState;
import com.solidleon.alpha.crafting.BuildPlan;
import com.solidleon.alpha.crafting.Crafting;
import com.solidleon.alpha.crafting.Recipe;
import com.solidleon.alpha.entity.Entity;
import com.solidleon.alpha.entity.Player;
import com.solidleon.alpha.game.world.Chunk;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author Markus
 */
public class BuildCursor extends SelectionCursor {
    
    private List<Recipe> plans;
    
    public BuildCursor(Player player, List<Recipe> plans) {
        this.plans = plans;
        
        for (int i = 0; i < plans.size(); i++) {
            BuildPlan plan = (BuildPlan) plans.get(i);
            plan.checkCanCraft(player);
            addListItem(new ListItem(plan) {

                @Override
                public void render(int x, int y, boolean selected) {
                    Color col = selected ? Color.white : Color.gray;
                    BuildPlan plan = (BuildPlan) object;
                    Font.drawString(object.toString(), x, y, col);
                    if (!plan.canCraft)
                        Font.drawString("(\\c[red]na\\c[gray])", width-Font.getWidth("(na)"), y, Color.gray);
                }
                
            });
        }
        
        addCommand(new Command("ENTER", "build!", Input.KEY_ENTER) {

            @Override
            public void performAction(InGameState game, Chunk area, int xCursor, int yCursor) {
                buildPerformAction(game, area);
            }
            
        });
        
        addMovementKeys();
    }

    @Override
    public void drawCursor(Graphics g, int xCursor, int yCursor) {
        BuildPlan plan = (BuildPlan) getSelection();
        if (plan != null) {
            Entity e = plan.template;
            e.x = xCursor;
            e.y = yCursor;
            e.render(g);
        }
    }
    
    private void buildPerformAction(InGameState game, Chunk area) {
        BuildPlan plan = (BuildPlan) getSelection();
        plan.checkCanCraft(area.player);
        if (!plan.canCraft) return;
        Entity e = null;
        try {
            e = (Entity) plan.result.newInstance();
        } catch (Exception ex) {
            Logger.getLogger(InteractionMode.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (e == null) return;
        
        
        if (canBuildThere(game, area, game.xCursor, game.yCursor, e)) {
            e.x = game.xCursor;
            e.y = game.yCursor;
            area.add(e);
            plan.deductCost(area.player);
            for (int i = 0; i < plans.size(); i++) 
                plans.get(i).checkCanCraft(area.player);
            area.player.addMsg("Build " + e.getName() + "!");
        } else {
            area.player.addMsg("Can not build " + e.getName() + " there!");
        }
    }
    
    private boolean canBuildThere(InGameState game, Chunk area, int xt, int yt, Entity e) {
        int x0 = xt;
        int y0 = yt;
        int x1 = x0 + e.w;
        int y1 = y0 + e.h;
        
        for (int y = y0; y < y1; y++) {
            for (int x = x0; x < x1; x++) {
                //there must be a valid tile
                if (!area.getTile(x, y).mayPass(area.cm, x, y, e)) return false;
            }
        }
        //there must be no entities
        List<Entity> entities = area.getEntites(x0, y0, x1-1, y1-1); //-1 because an entity with w/h of 1, does NOT overlap to the next TILE
        if (entities.size() > 0) return false;
        return true;
    }

    @Override
    public void notifyDirtyInventory(Player player) {
        for (int i = 0; i < Crafting.buildPlans.size(); i++) {
            Crafting.buildPlans.get(i).checkCanCraft(player);
        }
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta, InGameState ingame) {
        super.update(container, game, delta, ingame);
        
        
        int ts = Config.getInstance().tileSize;
        int tm = Config.getInstance().tileMask;
        
        int x0 = ingame.xScroll>>tm;
        int y0 = ingame.yScroll>>tm;
        int w = ((container.getWidth()-250)>>tm);
        int h = ((container.getHeight()-16)>>tm);
        
        BuildPlan plan = (BuildPlan) getSelection();
        if (plan != null) {
            Entity e = plan.template;
            w -= e.w;
            h -= e.h;
        }
        
        if (ingame.xCursor < x0) ingame.xCursor = x0;
        if (ingame.yCursor < y0) ingame.yCursor = y0;
        if (ingame.xCursor >= x0+w) ingame.xCursor = x0+w;
        if (ingame.yCursor >= y0+h) ingame.yCursor = y0+h;
    }

    
    
}
