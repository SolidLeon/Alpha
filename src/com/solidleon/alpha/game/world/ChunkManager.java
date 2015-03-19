/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solidleon.alpha.game.world;

import com.solidleon.alpha.entity.Entity;
import com.solidleon.alpha.entity.Player;
import com.solidleon.alpha.game.world.biome.Biome;
import com.solidleon.alpha.game.world.tile.Tile;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Markus
 */
public class ChunkManager {
    private static final Logger logger = Logger.getLogger(ChunkManager.class.getName());
    public static int toChunk(int i) {
        return i >> 5;
    }
    public static int toRelative(int i) {
        return i&31;
    }
    
    /** Active chunks that should be updated */
    public List<Chunk> loadedChunks = new ArrayList<Chunk>();
    /** List of chunks pending to write to disk */
    public List<Chunk> writeChunks = new ArrayList<Chunk>();
    
    public boolean newGame;
    private Random random = new Random();
    private long newGameTime;
    
    public Tile getTile(int x, int y) {
        Chunk chunk = getChunk(x, y);
        if (chunk == null) return Tile.empty; //throw new RuntimeException("Chunk not loaded!");
        
        int rx = toRelative(x);
        int ry = toRelative(y);
        return chunk.getTile(rx, ry);
    }
    
    public void setTile(int x, int y, Tile tile, int dataValue) {
        Chunk chunk = getChunk(x, y);
        if (chunk == null) return ; //throw new RuntimeException("Chunk not loaded!");
        
        int rx = toRelative(x);
        int ry = toRelative(y);
        chunk.setTile(rx, ry, tile, dataValue);
    }
    
    public void setData(int x, int y, int dataValue) {
        Chunk chunk = getChunk(x, y);
        if (chunk == null) return ; //throw new RuntimeException("Chunk not loaded!");
        
        int rx = toRelative(x);
        int ry = toRelative(y);
        chunk.setData(rx, ry, dataValue);
    }
    
    /**
     * 
     * @param x - abs x
     * @param y - abs y
     * @return 
     */
    public Chunk getChunk(int x, int y) {
        return getChunkByChunkCoordinates(x>>5, y>>5);
    }
    
    /**
     * Returns the chunk at the given chunk coordinates or null of the
     * chunk has not been loaded
     * @param cx
     * @param cy
     * @return 
     */
    public Chunk getChunkByChunkCoordinates(int cx, int cy) {
        for (int i = 0; i < loadedChunks.size(); i++) {
            Chunk chunk = loadedChunks.get(i);
            if (chunk.chunkX == cx && chunk.chunkY == cy) {
                return chunk;
            }
        }
        return null;
    }
    
    //TODO: BUG, entity not in specified rectangles chunk(s)
    //          Fixed by just checking every loaded chunk!
    // -> If the x/y of an entity lies within a chunk outside the
    // given corners, than the entity will not be returned, though being
    // inside the rectangle!
    public List<Entity> getEntities(int x0, int y0, int x1, int y1) {
        List<Entity> result = new ArrayList<Entity>();
        //1) get chunks for xy positions
//        List<Chunk> chunks = new ArrayList<Chunk>();
//        for (int x = x0; x <= x1; x++) {
//            for (int y = y0; y <= y1; y++) {
//                Chunk c = getChunk(x, y);
//                if (c == null) continue; /* not loaded chunk */
//                if (chunks.contains(c)) continue;
//                chunks.add(c);
//            }
//        }
        
//        for (Chunk c : chunks) {
        for (Chunk c : loadedChunks) {
            for (int i = 0; i < c.entities.size(); i++) {
                Entity e = c.entities.get(i);
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
        }
        return result;
    }
    
    /**
     * Removes all loaded chunks
     */
    public void clear() {
        loadedChunks.clear();
    }

    /**
     * Loads given chunk, if it was not loaded
     * @param chunkX - chunk x
     * @param chunkY - chunk y
     */
    public void loadChunk(Player player, int chunkX, int chunkY) {
        Chunk c = getChunkByChunkCoordinates(chunkX, chunkY);
        if (c != null) {
            //chunk already loaded
            return;
        }
//        logger.log(Level.INFO, "Chunk@{0}/{1}: {2}", new Object[]{chunkX, chunkY, c});
        Chunk chunk = loadChunk(chunkX, chunkY);
        if (chunk == null) {
            //error
            logger.log(Level.WARNING, "Unable to load chunk {0}/{1}", new Object[]{chunkX, chunkY});
            return;
        }
        logger.log(Level.INFO, "Loaded chunk: {0}/{1}", new Object[]{chunkX, chunkY});
        chunk.cm = this;
        chunk.player = player;
        if (loadedChunks.contains(chunk)) {
            logger.log(Level.WARNING, "Duplicate chunk entries {0}/{1}!", new Object[]{chunkX, chunkY});
        }
        loadedChunks.add(chunk);
    }
    /**
     * 
     * @param x chunk x
     * @param y chunk y
     * @return loaded chunk
     */
    private Chunk loadChunk(int chunkX, int chunkY) {
        //Check harddisk if we can load the chunk from there
        
        logger.log(Level.INFO, "Load Chunk {0}/{1}", new Object[]{chunkX, chunkY});
        try {
            Chunk chunk = loadChunkFromDrive(chunkX, chunkY);
            if(chunk != null) {
                logger.info("Loaded from HDD");
                return chunk;
            }
        } catch (Exception ex) {}
        
        Chunk chunk = new Chunk(null, chunkX, chunkY);
        ChunkData data = ChunkProvider.generate(chunkX, chunkY, 32, 32);
        chunk.tiles = data.tiles;
        chunk.data = data.data;
        chunk.biome = data.biome;
        chunk.timestamp = System.currentTimeMillis();
        logger.info("Generated new chunk");
        return chunk;
    }
    
    public Chunk loadChunkFromDrive(int chunkX, int chunkY) throws Exception {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(String.format("data/sav/c%d.%d", chunkX, chunkY)));
        try {
            long timestamp = in.readLong();
            if (timestamp < newGameTime) 
                return null;
            logger.log(Level.INFO, "HDD, load from {0}", new Date(timestamp));
            Biome _biome = Biome.biomes.get(in.readInt());
            int _chunkX = in.readInt();
            int _chunkY = in.readInt();
            if (_chunkX != chunkX || _chunkY != chunkY) 
                throw new RuntimeException("Invalid chunk position!");
            int w = in.readInt();
            int h = in.readInt();
            byte []tiles = new byte[w*h];
            in.readFully(tiles);
            int []data = new int[w*h];
            for (int i = 0; i  < w*h; i++) {
                data[i] = in.readInt();
            }
            List<Entity> entities = new ArrayList<Entity>();
            int entities_len = in.readInt();
            for (int i = 0; i <entities_len; i++) {
                Entity e = (Entity) in.readObject();
                entities.add(e);
            }
            
            Chunk chunk = new Chunk(_biome, _chunkX, _chunkY);
            chunk.timestamp = timestamp;
            chunk.w = w;
            chunk.h = h;
            chunk.tiles = tiles;
            chunk.data = data;
            for (Entity e : entities) {
                chunk.add(e);
            }
            
            return chunk;
        } finally {
            if (in != null)
                in.close();
        }
    }
    
    public void writeChunk(Chunk chunk) throws IOException {
        File f =new File("data/sav/");
        if (!f.exists()) f.mkdirs();
        ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream(String.format("data/sav/c%d.%d", chunk.chunkX, chunk.chunkY)));
        try {
            out.writeLong(System.currentTimeMillis());
            out.writeInt(chunk.biome.id);
            out.writeInt(chunk.chunkX);
            out.writeInt(chunk.chunkY);
            out.writeInt(chunk.w);
            out.writeInt(chunk.h);
            out.write(chunk.tiles);
            for (int i = 0; i < (chunk.w*chunk.h); i++) {
                out.writeInt(chunk.data[i]);
            }

            int sizeEntities = chunk.entities.size();
            out.writeInt(sizeEntities);
            for (int i = 0; i < sizeEntities; i++) {
                Entity e = chunk.entities.get(i);
                out.writeObject(e);
            }
            out.flush();
        } finally {
            if (out != null)
                out.close();
        }
    }

    /**
     * Updates chunks
     * @param delta 
     */
    public void update(int delta) {
        if (writeChunks.size() > 0) {
            Chunk write = writeChunks.remove(0);
            try {
                writeChunk(write);
                logger.log(Level.INFO, "Wrote chunk {0}/{1}", new Object[]{write.chunkX, write.chunkY});
            } catch (IOException ex) {
                Logger.getLogger(ChunkManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        for (int i = 0; i < loadedChunks.size(); i++) {
            loadedChunks.get(i).update(delta);
        }
    }
    
    public void gameUpdate() {
        for (int i = 0; i < loadedChunks.size(); i++) {
            loadedChunks.get(i).gameUpdate();
        }
    }

    /**
     * Loads chunks for the given cursor position
     * x0,y0 must be top left
     * x1,y1 must be bottom right
     */
    public void loadChunks(Player player, int x0, int y0, int x1, int y1) {
        int cx0 = toChunk(x0);
        int cy0 = toChunk(y0);
        int cx1 = toChunk(x1);
        int cy1 = toChunk(y1);
        
        for (int x = cx0; x <= cx1; x++) {
            for (int y = cy0; y <= cy1; y++) {
                loadChunk(player, x,y);
            }
        }
    }

    /**
     * Unloads all chunks that are out of range.
     * x0,y0 must be top left
     * x1,y1 must be bottom right
     */
    public void unloadChunks(int x0, int y0, int x1, int y1) {
        int cx0 = toChunk(x0);
        int cy0 = toChunk(y0);
        int cx1 = toChunk(x1);
        int cy1 = toChunk(y1);
        
        List<Chunk> visibleChunks = new ArrayList<Chunk>();
        List<Chunk> removed = new ArrayList<Chunk>(loadedChunks);
        
        for (int x = cx0; x <= cx1; x++) {
            for (int y = cy0; y <= cy1; y++) {
                visibleChunks.add(getChunkByChunkCoordinates(x, y));
            }
        }
        removed.removeAll(visibleChunks);
        loadedChunks.retainAll(visibleChunks);
        writeChunks.addAll(removed);
    }

    /**
     * Returns the data for the tile on abs position x,y
     * @param x - abs x
     * @param y - abs y
     * @return data value
     */
    public int getData(int x, int y) {
        Chunk chunk = getChunk(x, y);
        if (chunk == null) return 0; //throw new RuntimeException("Chunk not loaded!");
        
        int rx = toRelative(x);
        int ry = toRelative(y);
        return chunk.getData(rx, ry);
    }

    /**
     * Marks new game, to not load old chunks from HDD
     */
    public void enableNewGame() {
        newGame = true;
        newGameTime = System.currentTimeMillis();
    }

}
