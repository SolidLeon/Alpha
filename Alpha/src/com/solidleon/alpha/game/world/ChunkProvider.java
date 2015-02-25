/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solidleon.alpha.game.world;

import com.solidleon.alpha.game.world.biome.Biome;
import com.solidleon.alpha.game.world.tile.GrasTile;
import com.solidleon.alpha.game.world.tile.Tile;
import com.solidleon.alpha.game.world.tile.TreeTile;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;

/**
 *
 * @author Markus
 */
public class ChunkProvider {
    private static final Logger logger = Logger.getLogger(ChunkProvider.class.getName());
    private static PerlinNoise pn;
    private static Random random = new Random();
    private static long seed;

    public static long getSeed() {
        return seed;
    }
    
    public static void init(long seed) {
        pn = new PerlinNoise(0x10000, seed);
        ChunkProvider.seed = seed;
    }
    
    public static ChunkData generate(int chunkX, int chunkY, int w, int h) {
        logger.log(Level.INFO, "Generate Chunk @{0}/{1} [{2}/{3}]", new Object[]{chunkX, chunkY, w, h});
        
        //Step 1) Get biome for this Chunk X/Y
        Biome biome = Biome.forest;
        
        
        //Step 2) Create terrain
        //GOAL:
        // Continental maps
        // Rivers
        // Seas
        // Ocean between continents
        // Temperature specific biomes
        
        byte[] tiles = new byte[w*h];
        int[] data = new int[w*h];
        
        int ox = chunkX * w;
        int oy = chunkY * h;
        
        float bias = 0.05f;
        float biasContinental = 0.1f;
        float biasHeight = 0.05f;
        
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                float xx = (ox + x);
                float yy = (oy + y);
                
                float continental = pn.pn2d(xx*biasContinental,yy*biasContinental);
                float height = pn.pn2d(xx*biasHeight,yy*biasHeight);

                if (height < biome.waterLevel/*-0.3*/) {
                    tiles[x+y*w] = Tile.water.id;
                }
                if (height >= biome.waterLevel/*-0.3f*/) {
                    tiles[x+y*w] = Tile.grass.id;
                    data[x+y*w] = random.nextInt(GrasTile.TYPES);
                }
            }
        }
        //Step 3) Populate terrain with
        //          - Trees
        //          - Ores
        //          - Animals
        
        if (biome.treeTypes != null && biome.treeTypes.length > 0) {
            for (int i = 0; i < w*h/400; i++) {
                int x = random.nextInt(w);
                int y = random.nextInt(h);

                //TODO: mischwald / "type"-only etc
                int type = biome.treeTypes[random.nextInt(biome.treeTypes.length)].ordinal();

                for (int j = 0; j < biome.forestDensity/*50*/; j++) {
                    int xx = x + random.nextInt(15) - random.nextInt(15);
                    int yy = y + random.nextInt(15) - random.nextInt(15);
                    
                    if (xx >= 0 && yy >= 0 && xx < w && yy < h) {
                        if (tiles[xx+yy*w] == Tile.grass.id) {
                            tiles[xx+yy*w] = Tile.tree.id;
                            data[xx+yy*w] = type;
                        }
                    }
                }
            }
        }
        
        
        return new ChunkData(tiles, data, biome);
    }
    
    
    static ChunkProvider test = new ChunkProvider();
    static {
        test.init(0);
    }
    static ChunkData chunk = test.generate(0, 0, 256, 256);
    static int ox, oy;
    static int sz = 8;
    
    public static void main(String []args) {
        pn = new PerlinNoise(0x1000, 0);
        JFrame frame = new JFrame();
        Canvas c = new Canvas() {

            @Override
            public void paint(Graphics g) {
                int w = getWidth() / sz;
                int h = getHeight() / sz;
                
                g.setColor(Color.black);
                g.fillRect(0, 0, getWidth(), getHeight());
                
                for (int x = 0; x < w; x++) {
                    for (int y = 0; y < h; y++) {
                        
                        byte tileID = chunk.tiles[x + y * 256];
                        Tile tile = Tile.tiles[tileID];
                        
                        
                        Color col;
                        
                        if (tile == Tile.tree) {
                            col = new Color(0.2f, 0.7f, 0.3f);
                        } else if (tile == Tile.grass) {
                            col = new Color(0.2f, 0.5f, 0.3f);
                        } else if(tile == Tile.water) {
                            col = new Color(0.2f, 0.3f, 0.7f);
                        } else {
                            col = Color.white;
                        }
                        
                        g.setColor(col);
                        g.fillRect(x*sz, y*sz, sz, sz);
                    }
                }
            }
            
        };
        c.setMinimumSize(new Dimension(800, 600));
        c.setMaximumSize(new Dimension(800, 600));
        c.setPreferredSize(new Dimension(800, 600));
        c.addKeyListener(new KeyAdapter() {
            
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_UP) oy--;
                if (key == KeyEvent.VK_DOWN) oy++;
                if (key == KeyEvent.VK_LEFT) ox--;
                if (key == KeyEvent.VK_RIGHT) ox++;
                e.getComponent().repaint();
            }
            
        });
        frame.add(c);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

}
