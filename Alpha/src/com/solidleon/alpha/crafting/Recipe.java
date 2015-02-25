/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solidleon.alpha.crafting;

import com.solidleon.alpha.cursormode.KeybindListItem;
import com.solidleon.alpha.entity.Player;
import com.solidleon.alpha.item.Item;
import com.solidleon.alpha.item.ResourceItem;
import com.solidleon.alpha.item.resource.Resource;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Recipes are plans for crafting items, inside buildings.
 * @author Markus
 */
public abstract class Recipe implements Serializable, KeybindListItem{
    public Item resultTemplate;
    public List<Item> costs = new ArrayList<Item>();
    public boolean canCraft = false;
    /** Ingame craft time in minutes */
    public int craftTime;

    public Recipe(Item resultTemplate, int craftTime) {
        this.resultTemplate = resultTemplate;
        this.craftTime = craftTime;
    }
    
    public Recipe addCost(Resource resource, int count) {
        costs.add(new ResourceItem(resource, count));
        return this;
    }
    
    public String getExtendedString() {
        return getName();
    }
    
    @Override
    public String getName() {
        return "";
    }
            
    public void checkCanCraft(Player player) {
        for (int i = 0; i < costs.size(); i++) {
            Item item = costs.get(i);
            if (item instanceof ResourceItem) {
                ResourceItem ri = (ResourceItem) item;
                if (!player.inventory.hasResources(ri.res, ri.count)) {
                    canCraft = false;
                    return;
                }
            }
        }
        canCraft = true;
    }
    
    abstract public void craft(Player player);
    abstract public Item craft();
    
    public void deductCost(Player player) {
        for (int i = 0; i < costs.size(); i++) {
            Item item = costs.get(i);
            if (item instanceof ResourceItem) {
                ResourceItem ri = (ResourceItem) item;
                player.inventory.removeResource(ri.res, ri.count);
            }
        }
    }
}
