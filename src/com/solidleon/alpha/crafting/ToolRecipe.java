/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solidleon.alpha.crafting;

import com.solidleon.alpha.entity.Player;
import com.solidleon.alpha.item.Item;
import com.solidleon.alpha.item.Material;
import com.solidleon.alpha.item.ToolItem;
import com.solidleon.alpha.item.ToolType;

/**
 *
 * @author Markus
 */
public class ToolRecipe extends Recipe {
    private ToolType type;
    private Material material;

    public ToolRecipe(ToolType type, Material material, int craftTime) {
        super(new ToolItem(material, type), craftTime);
        this.type = type;
        this.material = material;
    }

    @Override
    public void craft(Player player) {
        player.inventory.add(0, new ToolItem(material, type));
    }

    @Override
    public Item craft() {
        return new ToolItem(material, type);
    }

    @Override
    public String getExtendedString() {
        return material.name + " " + type.name;
    }

    @Override
    public String getName() {
        return type.name;
    }
    
    
    
}
