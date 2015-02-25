/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solidleon.alpha.crafting;

import com.solidleon.alpha.entity.Entity;
import com.solidleon.alpha.entity.Player;
import com.solidleon.alpha.item.Item;
import com.solidleon.alpha.item.resource.Resource;

import java.io.Serializable;

/**
 *
 * @author Markus
 */
public class BuildPlan extends Recipe implements Serializable {
    public Class<? extends Entity> result;
    public Entity template;

    /** Gametime in minutes till the building is completed */    
    public BuildPlan(Class<? extends Entity> result, int requiredGameTime) throws InstantiationException, IllegalAccessException {
        super(null, 0);
        template = result.newInstance();
        this.result = result;
        this.craftTime = requiredGameTime;
    }

    @Override
    public String getName() {
        return template.getName();
    }

    @Override
    public void craft(Player player) {
    }

    @Override
    public Item craft() {
        return null;
    }
    

    @Override
    public String toString() {
        return getName();
    }
    
    
}
