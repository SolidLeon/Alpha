/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solidleon.alpha.cursormode;

import com.solidleon.alpha.Config;
import com.solidleon.alpha.Font;
import com.solidleon.alpha.InGameState;
import com.solidleon.alpha.TileSet;
import com.solidleon.alpha.crafting.BuildPlan;
import com.solidleon.alpha.crafting.Recipe;
import com.solidleon.alpha.entity.ConstructionSite;
import com.solidleon.alpha.entity.Entity;
import com.solidleon.alpha.entity.Placeable;
import com.solidleon.alpha.entity.Player;
import com.solidleon.alpha.game.world.Chunk;
import com.solidleon.alpha.game.world.ChunkManager;
import com.solidleon.alpha.game.world.tile.Tile;

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
public class BuildCursor2 extends CursorMode {

    private Player player;
    private String title;
    private List<Recipe> recipes;
//    private BuildPlan selected;
    private boolean shift;
    
    private boolean canBuild;
    private Chunk area;
    
    public BuildCursor2(String title, List<Recipe> recipes, Player player, Chunk area) {
        this.title = title;
        this.recipes = recipes;
        this.player = player;
        this.area = area;
        
        for (int i = 0; i < recipes.size(); i++) {
            Recipe r = recipes.get(i);
            r.checkCanCraft(player);
        }
        assignKeybindings(recipes);
    }
    @Override
    public void keyReleased(InGameState game, Chunk area, int xCursor, int yCursor, int key, char c) {
        if (key == Input.KEY_LSHIFT || key == Input.KEY_RSHIFT) shift = false;
    }

    @Override
    public void keyPressed(InGameState game, Chunk area, int xCursor, int yCursor, int key, char c) {
        if ((c >= 'a' && c <= 'z' ) || (c >= 'A' && c <= 'Z')) {
            checkKeybindings(game, area, xCursor, yCursor, key, c);
        }
        if (key == Input.KEY_LSHIFT || key == Input.KEY_RSHIFT) shift = true;
        if (key == Input.KEY_ENTER) build(game, area, (BuildPlan)selected);
        if (key == Input.KEY_LEFT) game.xCursor -= shift?5:1;
        if (key == Input.KEY_RIGHT) game.xCursor += shift?5:1;
        if (key == Input.KEY_DOWN) game.yCursor += shift?5:1;
        if (key == Input.KEY_UP) game.yCursor -= shift?5:1;
        if (key == Input.KEY_ESCAPE) game.changeMode(new BuildSelectionCursor(), false);
    }
    
    private void build(InGameState game, Chunk area, Recipe r) {
        if (selected == null) return;
        BuildPlan plan = (BuildPlan) r;
        plan.checkCanCraft(area.player);
        if (!plan.canCraft) return;
//        Entity e = null;
//        try {
//            e = (Entity) plan.result.newInstance();
//        } catch (Exception ex) {
//            Logger.getLogger(InteractionMode.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        if (e == null) return;


        if (canBuildThere(game, game.chunkManager, game.xCursor, game.yCursor, plan.template)) {
//            e.x = game.xCursor;
//            e.y = game.yCursor;
            area.add(new ConstructionSite(game.xCursor,game.yCursor,plan));
            plan.deductCost(area.player);
            for (int i = 0; i < recipes.size(); i++) 
                recipes.get(i).checkCanCraft(area.player);
            area.player.addMsg("Build " + plan.template.getName() + "!");
        } else {
            area.player.addMsg("Can not build " + plan.template.getName() + " there!");
        }
    }
    
    private boolean canBuildThere(InGameState game, ChunkManager cm, int xt, int yt, Entity e) {
        int x0 = xt;
        int y0 = yt;
        int x1 = x0 + e.w;
        int y1 = y0 + e.h;
        
        Placeable p = null;
        if (e instanceof Placeable) {
            p = (Placeable) e;
        }
        
        for (int y = y0; y < y1; y++) {
            for (int x = x0; x < x1; x++) {
                Tile t = cm.getTile(x, y);
                //there must be a valid tile
                if (!t.mayPass(area.cm, x, y, e)) return false;
                if (p != null && !p.canPlaceOn(t)) return false;
            }
        }
        //there must be no entities
        List<Entity> entities = cm.getEntities(x0, y0, x1-1, y1-1); //-1 because an entity with w/h of 1, does NOT overlap to the next TILE
        if (entities.size() > 0) return false;
        return true;
    }
    
    
    @Override
    public void update(GameContainer container, StateBasedGame game, int delta, InGameState ingame) {
        super.update(container, game, delta, ingame);        
        

        int ts = Config.getInstance().tileSize;
        int tm = Config.getInstance().tileMask;
        
        int px = ts / 2;
        int py = px + (px/2);
        
        int x0 = ingame.xScroll>>tm;
        int y0 = ingame.yScroll>>tm;
        int w = ((container.getWidth()-250-px)>>tm);
        int h = ((container.getHeight()-py)>>tm);
        
        BuildPlan plan = (BuildPlan)selected;
        if (plan != null) {
            Entity e = plan.template;
            w -= e.w;
            h -= e.h;
            canBuild = canBuildThere(ingame, ingame.chunkManager, ingame.xCursor, ingame.yCursor, e);
        }
        
        if (ingame.xCursor < x0) ingame.xScroll -= ts * (x0 - ingame.xCursor);
        if (ingame.yCursor < y0) ingame.yScroll -= ts * (y0 - ingame.yCursor);
        if (ingame.xCursor >= x0+w) ingame.xScroll += ts * (ingame.xCursor - (x0+w-1));
        if (ingame.yCursor >= y0+h) ingame.yScroll += ts * (ingame.yCursor - (y0+h-1));
//        if (ingame.xCursor < x0) ingame.xCursor = x0;
//        if (ingame.yCursor < y0) ingame.yCursor = y0;
//        if (ingame.xCursor >= x0+w) ingame.xCursor = x0+w;
//        if (ingame.yCursor >= y0+h) ingame.yCursor = y0+h;
    }

    
    @Override
    public void drawCursor(Graphics g, int xCursor, int yCursor) {
        if (selected != null) {
            Entity e = ((BuildPlan)selected).template;
            int w = e.w;
            int h = e.h;
            e.x = xCursor;
            e.y = yCursor;
            e.render(g);
            /*
             * Green = OK
             * Purple = OK (but parts are invalid)
             * Red = Invalid
             */
            
        int ts = Config.getInstance().tileSize;
//        int tm = Config.getInstance().tileMask;
            for (int y = yCursor; y  < yCursor + h; y++) {
                for (int x = xCursor; x < xCursor + w; x++) {
                    boolean ok = area.cm.getTile(x, y).mayPass(area.cm, x, y, e);
                    List<Entity> entities = area.cm.getEntities(x,y,x,y); 
                    if (entities.size() > 0) ok = false;
                    TileSet.drawBackground(x*ts, y*ts, ts, ts, Color.black);
                    
                    Color col;
                    if (ok) {
                        col = canBuild ? Color.green : Color.pink;
                    } else {
                        col = Color.red;
                    }
                    
                    TileSet.draw(8+5*16, x*ts, y*ts, col);
                }
            }
            
        }
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g, InGameState ingame) {
        
        Font.drawString(title, width / 2 - Font.getWidth(title) / 2, 0, Color.white);

        for (int i = 0; i < keybindings.size(); i++) {
            Keybinding kb = keybindings.get(i);
            Recipe r = (Recipe) kb.object;
            Font.drawString("\\c[green]" + kb.c +"\\c[gray]: " + r.getExtendedString(), 0, 24 + Font.getLineHeight()*i, Color.gray);
            
            if (!r.canCraft)
                Font.drawString("(\\c[red]na\\c[gray])", width - Font.getWidth("(\\c[red]na\\c[gray])"), 24 + Font.getLineHeight()*i, Color.gray);
        }
    }

    @Override
    public void notifyDirtyInventory(Player player) {
        for (int i = 0; i < recipes.size(); i++) {
            Recipe r = recipes.get(i);
            r.checkCanCraft(player);
        }
    }
    
    
}
