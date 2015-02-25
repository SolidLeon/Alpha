/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solidleon.alpha.entity;

import com.solidleon.alpha.crafting.Recipe;
import com.solidleon.alpha.item.Inventory;
import com.solidleon.alpha.item.Item;
import com.solidleon.alpha.item.ResourceItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author Markus
 */
public class Factory extends Entity {
    
    private static final Logger logger = Logger.getLogger(Factory.class.getName());

    
    public static class QueueItem implements Serializable{
        public Item item;
        public int count;
        public int craftTime;

        public QueueItem(Item item, int count, int craftTime) {
            this.item = item;
            this.count = count;
            this.craftTime = craftTime;
        }

    }
    
    public List<QueueItem> queue = new ArrayList<QueueItem>();
    protected int maxQueue = 3;
    public int timer, maxTime;
    
    public int getMaxQueue() {
        return maxQueue;
    }
    
    
    @Override
    public void gameUpdate() {
        if (queue.size() > 0 && timer <= 0) {
            QueueItem qi = queue.get(0);
            
            if (qi.item instanceof ResourceItem) {
                ResourceItem ri = (ResourceItem) qi.item;
                area.player.inventory.add(new ResourceItem(ri.res, ri.count));
            } else {
                area.player.inventory.add(qi.item);
            }
            
            qi.count -= 1;
            if (qi.count <= 0) {
                queue.remove(0);
            }
            if (queue.size() > 0) {
                timer = queue.get(0).craftTime;
                maxTime = timer;
            }
            area.player.ingame.cursorMode.notifyDirtyInventory(area.player);
        }
        if (timer > 0) timer -= 1;
    }
    
    public boolean queue(Recipe r, boolean craftAll, Player player) {
        boolean crafted = false;
        for(;;) {
            r.checkCanCraft(player);
            if (!r.canCraft) break;

            int size = queue.size();
            if (queueRecipe(r, 1)) {
                r.deductCost(player);
                if (size == 0) {
                    timer = r.craftTime;
                    maxTime = timer;
                }
                crafted = true;
                if (!craftAll) break;
            } else {
                //could not queue
                break;
            }
        }
        return crafted;
    }
    
    private boolean queueRecipe(Recipe r, int count) {
        Item result = r.resultTemplate;
        
        QueueItem has = findQueueItem(r);
        
        if (result instanceof ResourceItem) {
            ResourceItem ri = (ResourceItem) result;
            if (has == null) {
                if (queue.size() < maxQueue) {
                    queue.add(new QueueItem(ri, count, r.craftTime));
                    return true;
                }
                return false;
            } else {
                has.count += count;
                return true;
            }
        } else {
            boolean added = false;
            while (queue.size() < maxQueue) {
                queue.add(new QueueItem(r.craft(), 1, r.craftTime));
                added = true;
                count--;
                if (count <= 0) break;
            }
            return added;
        }
    }
    
    private QueueItem findQueueItem(Recipe r) {
        if (r.resultTemplate instanceof ResourceItem) {
            ResourceItem ri = (ResourceItem) r.resultTemplate;
            for (int i = 0; i < queue.size(); i++) {
                QueueItem qi = queue.get(i);
                if (qi.item instanceof ResourceItem) {
                    ResourceItem has = (ResourceItem) qi.item;
                    if (has.res.equals(ri.res)) {
                        return qi;
                    }
                }
            }
        } else {
            return null;
        }
        return null;
    }
    
}
