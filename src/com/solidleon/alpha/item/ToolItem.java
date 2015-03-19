/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solidleon.alpha.item;

import java.io.Serializable;
import org.newdawn.slick.Color;

/**
 *
 * @author Markus
 */
public class ToolItem extends Item implements Serializable{

    private static final Color []colors = {
        Color.darkGray,
        Color.red.darker(),
        Color.orange.darker(),
        Color.green.darker()
    };
    private static final Color []colorsSelected = {
        Color.lightGray,
        Color.red.brighter(),
        Color.orange.brighter(),
        Color.green.brighter()
    };
    
    public Material material;
    public ToolType type;
    public int durability;
    
    public ToolItem(Material material, ToolType type) {
        this.material = material;
        this.type = type;
        this.durability = material.resistance;
    }
    
    @Override
    public String getName() {
        return material.name + " " + type.name + "("+super.hashCode()+")";
    }

    @Override
    public boolean matches(Item item) {
        if (item instanceof ToolItem) {
            ToolItem tool = (ToolItem) item;
            return tool.type == type && tool.material.id == material.id; //== valid because ToolType is an ENUM
        }
        return false;
    }

    @Override
    public boolean isDepleted() {
        return durability <= 0;
    }

    @Override
    public int getSprite() {
        return type.sprite;
    }

    @Override
    public Color getColor() {
        
        float perc = (float)durability / (float) material.resistance;
        if (perc >= 0 && perc < 0.25f) return colors[0];
        if (perc >= 0.25 && perc < 0.50f) return colors[1];
        if (perc >= 0.50 && perc < 0.75f) return colors[2];
        if (perc >= 0.75 && perc <= 1f) return colors[3];
        
        return Color.white;
    }
    

    @Override
    public Color getColorSelected() {
        
        float perc = (float)durability / (float) material.resistance;
        if (perc >= 0 && perc < 0.25f) return colorsSelected[0];
        if (perc >= 0.25 && perc < 0.50f) return colorsSelected[1];
        if (perc >= 0.50 && perc < 0.75f) return colorsSelected[2];
        if (perc >= 0.75 && perc <= 1f) return colorsSelected[3];
        
        return Color.white;
    }
    
    
}
