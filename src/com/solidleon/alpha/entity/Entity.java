/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solidleon.alpha.entity;

import com.solidleon.alpha.InGameState;
import com.solidleon.alpha.game.world.Chunk;
import com.solidleon.alpha.game.world.ChunkManager;
import com.solidleon.alpha.item.Item;

import java.io.Serializable;
import java.util.*;

import org.newdawn.slick.Graphics;

/**
 *
 * @author Markus
 */
public class Entity implements Serializable{
    public static Map<Integer, Entity> entities = new HashMap<Integer, Entity>();
    public static int findFreeId() {
        if (entities.isEmpty()) return 0;
        int id = 0;
        while (entities.containsKey(id)) {
            id++;
            if (id < 0) return -1;//no key found
        }
        return id;
    }
    
    public static void clearEntityCache() {
        List<Integer> remove = new ArrayList<Integer>();
        for (Integer l : entities.keySet()) {
            if (entities.get(l).removed) remove.add(l);
        }
        for (int i = 0; i < remove.size(); i++) {
            entities.remove(remove.get(i));
        }
    }
    
    protected transient Random random;
    public int id;
    public int x;
    public int y;
    public int w = 1;
    public int h = 1;
    public transient Chunk area;
    public boolean removed;

    protected void remove() {
        removed = true;
    }
    
    public void init(Chunk area) {
        if (area == null) throw new RuntimeException("Init entity with null chunk not allowed!");
        this.area = area;
        if (random == null) random = new Random();
        this.id = findFreeId();
        if (this.id == -1) {
            clearEntityCache();
            this.id = findFreeId();
            if (this.id == -1) {
                throw new RuntimeException("Found no free id!");
            }
        }
        entities.put(this.id, this);
//        System.out.println("Added " +getClass().getSimpleName() + " @" + id );
    }
    
    
    public void update(int delta) {
        
    }
    
    public boolean move(int dx, int dy) {
        if (dx != 0 || dy != 0) {
            boolean stopped = true;
            if (dx != 0 && move2(dx, 0)) stopped = false;
            if (dy != 0 && move2(0, dy)) stopped = false;
            if (!stopped) {
                //if dx, dy > 1, we only "step on" the last tile!
                area.cm.getTile(x, y).steppedOn(area.cm, x, y, this);
            } 
            return !stopped;
        }
        return true;
    }
    
    private boolean move2(int dx, int dy) {
        if (dx != 0 && dy != 0) throw new IllegalArgumentException("Mov2 allows only single-axis movement.");
        ChunkManager cm = area.cm;
        //check if we hit any blocking tile on our way
        int xt0 = x;
        int yt0 = y;
        int xt1 = x;
        int yt1 = y;
        if (dx < 0) xt0 += dx;
        if (dx > 0) xt1 += dx;
        if (dy < 0) yt0 += dy;
        if (dy > 0) yt1 += dy;
        
        for (int yt = yt0; yt <= yt1; yt++) {
            for (int xt = xt0; xt <= xt1; xt++) {
                if (xt == x && yt == y) continue; //dont check our own tile
                if (!cm.getTile(xt, yt).mayPass(area.cm, xt, yt, this)) {
//                    System.out.println("STOP: " + area.getTile(xt, yt).getClass());
                    return false;
                }
            }
        }
        this.x += dx;
        this.y += dy;
        return true;
    }
    
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Entity other = (Entity) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.id;
        return hash;
    }
    
    public String getName() {
        return getClass().getSimpleName();
    }

    public void render(Graphics g) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void onSelect(InGameState game) {
        
    }
    public boolean interact(Chunk area, int xt, int yt, Player player, Item item) {
        return false;
    }

    /**
     * Ingame minute update method
     */
    public void gameUpdate() {
        
    }
    
    
}
