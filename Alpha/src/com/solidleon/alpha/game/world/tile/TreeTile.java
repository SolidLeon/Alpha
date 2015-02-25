/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solidleon.alpha.game.world.tile;

import com.solidleon.alpha.ColorCache;
import com.solidleon.alpha.Config;
import com.solidleon.alpha.InGameState;
import com.solidleon.alpha.TileSet;
import com.solidleon.alpha.entity.Entity;
import com.solidleon.alpha.entity.ItemEntity;
import com.solidleon.alpha.entity.Player;
import com.solidleon.alpha.game.world.ChunkManager;
import com.solidleon.alpha.item.Item;
import com.solidleon.alpha.item.ResourceItem;
import com.solidleon.alpha.item.ToolItem;
import com.solidleon.alpha.item.ToolType;
import com.solidleon.alpha.item.resource.Resource;

import org.newdawn.slick.Color;

/**
 *
 * @author Markus
 */
public class TreeTile extends Tile {
    
    public static enum TreeType {
        BIRCH("Birch", 5),
        ASH("Ash", 6),
        CHESTNUT("Chestnut", 6),
        MAPLE("Maple", 5),
        OAK("Oak", 6),
        PINE("Pine", 8+16);
        
        public String name;
        public int sprite;

        private TreeType(String name, int sprite) {
            this.name = name;
            this.sprite = sprite;
        }

        
        
    }

    public TreeTile(int id) {
        super(id);
    }

    @Override
    public void draw(ChunkManager cm, int xt, int yt) {
        int ts = Config.getInstance().tileSize;
        int type = cm.getData(xt, yt);
        TreeType t = TreeType.values()[type];
        int season = InGameState.getInstance().time.getSeason();
        Color color = null;
        switch (t) {
            case ASH: color = ColorCache.ash[season];
                break;
            case BIRCH: color = ColorCache.birch[season];
                break;
            case CHESTNUT: color = ColorCache.chestnut[season];
                break;
            case MAPLE: color = ColorCache.maple[season];
                break;
            case OAK: color = ColorCache.oak[season];
                break;
            case PINE: color = ColorCache.pine[season];
                break;
        }
        TileSet.draw(t.sprite, xt*ts, yt*ts, color);
    }
    
    @Override
    public boolean interact(ChunkManager cm, int xt, int yt, Player player, Item item) {
        if (item instanceof ToolItem) {
            ToolItem tool = (ToolItem) item;
            if (tool.type == ToolType.AXE) {
                cm.getChunk(xt,yt).add(new ItemEntity(new ResourceItem(Resource.wood, random.nextInt(4-2)+2), xt, yt));
                int n = random.nextInt(4);
                
                int type = cm.getData(xt, yt);
                TreeType t = TreeType.values()[type];
                Resource sapling = null;
                switch (t) {
                    case ASH: sapling = Resource.ashSapling;
                        break;
                    case BIRCH: sapling = Resource.birchSapling;
                        break;
                    case CHESTNUT: sapling = Resource.chestnutSapling;
                        break;
                    case MAPLE: sapling = Resource.mapleSapling;
                        break;
                    case OAK: sapling = Resource.oakSapling;
                        break;
                    case PINE: sapling = Resource.pineSapling;
                        break;
                }
                
                
                if (n > 0) cm.getChunk(xt,yt).add(new ItemEntity(new ResourceItem(sapling, n), xt, yt));
                cm.setTile(xt, yt, Tile.grass, random.nextInt(GrasTile.TYPES));
                tool.durability -= 1;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean mayPass(ChunkManager cm, int xt, int yt, Entity e) {
        return false;
    }
    
    @Override
    public String getName(ChunkManager cm, int xt, int yt) {
        return TreeType.values()[cm.getData(xt,yt)].name;
    }
    
}
