/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solidleon.alpha.game.world;

import com.solidleon.alpha.entity.Entity;
import com.solidleon.alpha.entity.Player;
import com.solidleon.alpha.game.world.biome.Biome;
import com.solidleon.alpha.game.world.tile.Tile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Markus
 */
public class Chunk {
    public ChunkManager cm;
    public int chunkX;
    public int chunkY;
    public byte []tiles;
    public int []data;

    public int w, h;
    
    private Random random = new Random();
    public Player player;
    
    public List<Entity> entities = new ArrayList<Entity>(); 
    public long timestamp;
    
    public Biome biome;
    
    public Chunk(Biome biome, int x, int y) {
        this.biome = biome;
        this.chunkX = x;
        this.chunkY = y;
        this.w = 32;
        this.h = 32;
    }

    public Chunk(int w, int h, byte[] tiles, int[] data) {
        this.w = w;
        this.h = h;
        this.tiles = tiles;
        this.data = data;
    }

    public void add(Entity e) {
        entities.add(e);
        e.init(this);
    }
    
    public Tile getTile(int xt, int yt) {
        if (xt < 0 || yt < 0 || xt >= w || yt >= h) return Tile.empty;
        return Tile.tiles[tiles[xt+yt*w]];
    }
    
    
    public void setTile(int xt, int yt, Tile tile, int dataValue) {
        if (xt < 0 || yt < 0 || xt >= w || yt >= h) return;
        tiles[xt+yt*w] = tile.id;
        data[xt+yt*w] = dataValue;
    }

    public void update(int delta) {
        Tile.tileTime += delta;
        for (int i = 0; i < w*h/50; i++) {
            int xt = random.nextInt(w);
            int yt = random.nextInt(h);
            getTile(xt,yt).update(cm, chunkX*32 + xt, chunkY*32 + yt, delta);
        }
        
        for (int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);
            
            e.update(delta);
            
            if (e.removed) {
                entities.remove(i--);
            }
        }
    }

    public int getData(int xt, int yt) {
        if (xt < 0 || yt < 0 || xt >= w || yt >= h) return 0;
        return data[xt+yt*w];
    }

    public void setData(int xt, int yt, int dataValue) {
        if (xt < 0 || yt < 0 || xt >= w || yt >= h) return;
        data[xt+yt*w] = dataValue;
    }

    public List<Entity> getEntites(int x0, int y0, int x1, int y1) {
        List<Entity> result = new ArrayList<Entity>();
        for (int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);
            int xe0 = e.x;
            int ye0 = e.y;
            int xe1 = e.x + e.w-1;
            int ye1 = e.y + e.h-1;
            
            if (xe0 >= x0 && xe0 <= x1 && ye0 >= y0 && ye0 <= y1) {
                result.add(e);
            } else if (xe1 >= x0 && xe1 <= x1 && ye0 >= y0 && ye0 <= y1) {
                result.add(e);
            } else if (xe1 >= x0 && xe1 <= x1 && ye1 >= y0 && ye1 <= y1) {
                result.add(e);
            } else if (xe0 >= x0 && xe0 <= x1 && ye1 >= y0 && ye1 <= y1) {
                result.add(e);
            } else if (x0 >= xe0 && x1 <= xe1 && y0 >= ye0 && y1 <= ye1) {
                result.add(e);
            }
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Chunk other = (Chunk) obj;
        if (this.chunkX != other.chunkX) {
            return false;
        }
        if (this.chunkY != other.chunkY) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + this.chunkX;
        hash = 53 * hash + this.chunkY;
        return hash;
    }

    /**
     * Update emthod for ingame minute updates
     */
    public void gameUpdate() {
        for (int x = 0; x < w; x++)
            for (int y = 0; y < h; y++)
                getTile(x, y).gameUpdate(cm, chunkX*32 + x, chunkY*32 + y);
        
        for (int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);
            
            e.gameUpdate();
            
            if (e.removed) {
                entities.remove(i--);
            }
        }
    }
    
    
}
