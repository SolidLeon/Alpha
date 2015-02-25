/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solidleon.alpha.crafting;

import com.solidleon.alpha.entity.Player;
import com.solidleon.alpha.item.Item;
import com.solidleon.alpha.item.ResourceItem;
import com.solidleon.alpha.item.resource.Resource;

/**
 *
 * @author Markus
 */
public class ResourceRecipe extends Recipe {

    private Resource res;
    private int count;

    public ResourceRecipe(Resource res, int count, int craftTime) {
        super(new ResourceItem(res, count), craftTime);
        this.res = res;
        this.count = count;
    }
    
    @Override
    public void craft(Player player) {
        player.inventory.add(0, new ResourceItem(res, count));
    }

    @Override
    public Item craft() {
        return new ResourceItem(res, count);
    }
    

    @Override
    public String getName() {
        return res.name;
    }

    
}
