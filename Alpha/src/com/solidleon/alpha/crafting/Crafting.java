/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solidleon.alpha.crafting;

import com.solidleon.alpha.entity.Carpenter;
import com.solidleon.alpha.entity.Market;
import com.solidleon.alpha.entity.Sawmill;
import com.solidleon.alpha.item.Material;
import com.solidleon.alpha.item.ToolType;
import com.solidleon.alpha.item.resource.Resource;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Markus
 */
public class Crafting {
    public static final List<Recipe> buildPlans = new ArrayList<Recipe>();
    public static final List<Recipe> workshopPlans = new ArrayList<Recipe>();
    public static final List<Recipe> sawmillRecipes = new ArrayList<Recipe>();
    public static final List<Recipe> carpenterRecipes = new ArrayList<Recipe>();
    
    static {
        try {
            workshopPlans.add(new BuildPlan(Sawmill.class, 10).addCost(Resource.wood, 25));
            workshopPlans.add(new BuildPlan(Carpenter.class, 20).addCost(Resource.cutwood, 8));
            workshopPlans.add(new BuildPlan(Market.class, 20));
            
            sawmillRecipes.add(new ResourceRecipe(Resource.cutwood, 2, 15).addCost(Resource.wood, 1));
            carpenterRecipes.add(new ResourceRecipe(Resource.woodenstock, 1, 15).addCost(Resource.cutwood, 2));
            carpenterRecipes.add(new ResourceRecipe(Resource.woodenaxehead, 1, 30).addCost(Resource.cutwood, 1));
            carpenterRecipes.add(new ToolRecipe(ToolType.AXE, Material.wooden, 2*60).addCost(Resource.woodenaxehead, 1).addCost(Resource.woodenstock, 1));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    
}
