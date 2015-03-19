/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solidleon.alpha;

import com.solidleon.alpha.cursormode.*;
import com.solidleon.alpha.entity.Entity;
import com.solidleon.alpha.entity.Player;
import com.solidleon.alpha.game.time.FTS;
import com.solidleon.alpha.game.weather.FWS;
import com.solidleon.alpha.game.world.Chunk;
import com.solidleon.alpha.game.world.ChunkManager;
import com.solidleon.alpha.game.world.ChunkProvider;
import com.solidleon.alpha.game.world.tile.Tile;
import com.solidleon.alpha.item.Inventory;
import com.solidleon.alpha.item.Material;
import com.solidleon.alpha.item.ToolItem;
import com.solidleon.alpha.item.ToolType;

import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author Markus
 */
public class InGameState extends AbstractGameState {
    
    private static final Logger logger = Logger.getLogger(InGameState.class.getName());
    
    public static InGameState instance;

    public static InGameState getInstance() {
        return instance;
    }
    
    
    public static final int VERSION_MAJOR = 0;
    public static final int VERSION_MINOR = 15;

    /** Chunk manager */
    public ChunkManager chunkManager = new ChunkManager();
    /** Player reference */
    public Player player;

    /** scroll amount */
    public int xScroll, yScroll;
    /** current cursor position */
    public int xCursor, yCursor;
    //Rectangle Selection
    /** rectangle selection */
    public int sx0, sy0, sx1, sy1;
    /** should show cursor */
    private boolean cursorShow = true;
    /** cursor timer for blinking */
    private int cursorTimer;
    /** current cursor mode */
    public CursorMode cursorMode = new ScrollMode();
    /** pending cursor change */
    private CursorMode changeCursor;
    /** is in fullscreen ? */
    private boolean fullscreen = false;
    /** should change to fullscreen */
    private boolean pendingFullscreenToggle = false;
    /** should exit ? */
    private boolean pendingExit = false;
    /** should center cursor on next update ? */
    private boolean centerCursor;

    private int entityCacheTimeout = 60000; //one minute cache clear timeout

    /** show time per entity (multiple layered entities should be traversed) */
    private int entityTimer = 1500;
    /** delay timer */
    private int entityDelay;
    /** current entity to show, will be incremented infinite (modulo for correction) */
    private int entityCount;
    
    /** Game Alpha Time System */
    public FTS time = new FTS();
    /** Game Alpha Weather System */
    public FWS weatherSystem = new FWS();
    
    public InGameState(int id) {
        super(id);
        instance = this;
    }
    
    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        Config.load();
        Font.init();
        SoundCache.init();
        SoundCache.start();
        TileSet.init();
        
        //Load game, if loading fails, load() will automatically create a new game
        load();
        player.addMsg("Press [F11] for Help!");
        centerCursor(container.getWidth()-250, container.getHeight());
        
        changeMode(new ScrollMode());
        //init scroll mode
//        cursorMode.initCustom(container.getWidth()-16, container.getHeight()-24);
//        cursorMode.init(250-32, container.getHeight()-24);
//        changeCursor.init(container, game, delta, this, getChunk(), cursorMode);
    }
    
    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        //GAME AREA;
        
        //Set game bounds, bounds for actual visible gameing parts
        int ts = Config.getInstance().tileSize;
        int tm = Config.getInstance().tileMask;
        
        //border offsets
        int xBorderOffset = 8;
        int yBorderOFfset = 12;
        
        int xt0 = (xScroll >> tm);
        int yt0 = (yScroll >> tm);
        //8 = one vertical border width (250 includes the right border)
        int w = (container.getWidth()-250-8);
        //24 = 12*2 -> 12 is one horizontal border height
        int h = (container.getHeight()-24);
        
        w = FastMath.floor((double) w  / ts);
        h = FastMath.floor((double) h  / ts);

        Globals.setTileBounds(ts,tm);
        Globals.setBounds(xt0, yt0, w, h);
        
        
        g.translate(-xScroll+xBorderOffset , -yScroll+yBorderOFfset);
        for (int x = xt0; x < xt0 + w; x++) {
            for (int y = yt0; y < yt0 + h; y++) {
                List<Entity> entities = chunkManager.getEntities(x, y, x, y);
                if (entities.size() > 0) {
                    if (entities.size() == 1) {
                        entities.get(0).render(g);
                    } else {
                        if (entityDelay > 0) {
                            int entityDelaySprite = 8-(entityDelay / 50);
    //                        System.out.println("" + entityDelay);
                            if (entityDelaySprite == 0)TileSet.draw(12+5*16, x*ts, y*ts, Color.gray); // \\
                            if (entityDelaySprite == 1)TileSet.draw(3+11*16, x*ts, y*ts, Color.gray); // |
                            if (entityDelaySprite == 2)TileSet.draw(15+2*16, x*ts, y*ts, Color.gray); // /
                            if (entityDelaySprite == 3)TileSet.draw(13+2*16, x*ts, y*ts, Color.gray); // =
                            if (entityDelaySprite == 4)TileSet.draw(12+5*16, x*ts, y*ts, Color.gray); // \\
                            if (entityDelaySprite == 5)TileSet.draw(3+11*16, x*ts, y*ts, Color.gray); // |
                            if (entityDelaySprite == 6)TileSet.draw(15+2*16, x*ts, y*ts, Color.gray); // /
                            if (entityDelaySprite == 7)TileSet.draw(13+2*16, x*ts, y*ts, Color.gray); // =
                            if (entityDelaySprite == 8)TileSet.draw(12+5*16, x*ts, y*ts, Color.gray); // \\
    //                        Font.drawString("" + entityDelaySprite, x*16, y*16, Color.red);
                        } else {
                            int c = entityCount / entities.size();
                            int i = entityCount - entities.size() * c;
                            entities.get(i).render(g);
                        }
                    }
                } else {
                    Tile t = chunkManager.getTile(x, y);
                    if (t != null)  {
                        t.draw(chunkManager, x, y);
                    }
                }
            }
        }
        
        if (cursorMode != null && cursorShow) {
            cursorMode.drawCursor(g, xCursor, yCursor); //CURSOR MODE CURSOR
        }
        g.translate(xScroll-xBorderOffset, yScroll-yBorderOFfset);
        
        //----------------------
        // GUI
        //----------------------
        
        g.setColor(Color.black);
        g.fillRect(container.getWidth() - 250, 0, 250, container.getHeight());
        g.translate(container.getWidth() - 250 + 16, 20);
        cursorMode.render(container, game, g, this);
        g.translate(-(container.getWidth() - 250 + 16), -20);
        
        
        
        //OVERALL BORDER
        {
            int ww = container.getWidth();
            int hh = container.getHeight();
            g.setColor(Color.gray);
            g.fillRect(ww-250, 0, 8, hh); //split
            
            g.fillRect(0, 0, ww, 12); //top
            g.fillRect(0, hh-12, ww, 12); //bottom
            g.fillRect(0, 0, 8, hh); //left
            g.fillRect(ww-8, 0, 8, hh); //right
        }
        //CURSOR MODE custom
        g.setClip(8, 12, container.getWidth()-16, container.getHeight()-24);
        g.translate(8, 12);
        cursorMode.renderCustom(container, game, g, this);       
        g.translate(-8, -12);
        g.clearClip();
        
        
        //--------
        //MESSAGES
        //--------
        int xo = (int) ((container.getWidth()-250)/2.0f);
        int yo = 32;
        int n = player.messages.size();
        if (n > 3) n = 3;
        if (n > 0) {
            int minWidth = 200;
            for (int i = 0; i < n; i++) {
                String msg = player.messages.get(i);
                int ww = Font.getWidth(msg)+32;
                if (ww > minWidth) minWidth = ww;
            }
            
            TileSet.drawFrame(xo-minWidth/2, 16, minWidth, 32 + Font.getLineHeight()*n, Color.white, Color.red);
            for (int i = 0; i < n; i++) {
                String msg = player.messages.get(i);
                int ww = Font.getWidth(msg);
                Font.drawString(msg, xo-ww/2.0f, yo + Font.getLineHeight()*i, Color.white);
            }
        }
        
        
        if (Game.DEBUG) {
            Chunk chunk = getChunk();
            String s1 = "Cursor: " + xCursor + "/" + yCursor;
            String s2 = " Chunk: " + (xCursor>>5) + "/" + (yCursor>>5);
            String s3 = " Rel: " + ChunkManager.toRelative(xCursor) + "/" + ChunkManager.toRelative(yCursor);
            String s4 = " CM: " + chunkManager.loadedChunks.size() + "|" + chunkManager.writeChunks.size();
            String s5 = " T: " + (chunk == null ? 0 : AlphaUtils.sdf.format(new Date(chunk.timestamp)));
            Font.drawString(s1, 8, 0, Color.green);
            Font.drawString(s2, 8+Font.getWidth(s1), 0, Color.green);
            Font.drawString(s3, 8+Font.getWidth(s1) + Font.getWidth(s2), 0, Color.green);
            Font.drawString(s4, 8+Font.getWidth(s1) + Font.getWidth(s2) +  Font.getWidth(s3), 0, Color.white);
            Font.drawString(s5, 8+Font.getWidth(s1) + Font.getWidth(s2) +  Font.getWidth(s3) + Font.getWidth(s4), 0, Color.white);
        }
        int t = time.getCycleTime();
//        String sTime = String.format("%02d:%02d", time.getHourPart(t), time.getMinutePart(t));
        String sTime = time.toString();
        Font.drawString(sTime, container.getWidth() - Font.getWidth(sTime) - 8, 0, Color.yellow);
        
        
//        g.setAntiAlias(false);
//        g.setColor(Color.black);
//        g.fillRect(0,0,container.getWidth(),container.getHeight());
//        int ww = 12;
//        int hh = 12;
//        int xx = 50;
//        int yy = 50;
//        TileSet.draw(4+6*16, xx-8, yy, Color.white);
//        TileSet.draw(4+6*16, xx+ww, yy, xx+ww+ww, yy+hh, Color.white);
//        g.drawImage(TileSet.image, xx+ww+ww+ww, yy, xx+ww+ww+ww+ww, yy+ww, 4*16, 6*16, 4*16+16, 6*16+16);
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        if (pendingExit) { //we exit the game, so we save our game
            //SAVE
            Config.save();
            save();
            container.exit();
            return;
        }
        
        int ts = Config.getInstance().tileSize;
        int tm = Config.getInstance().tileMask;
        
        int px = ts / 2;
        int py = px + (px/2);
        
        SoundCache.update();
        entityCacheTimeout -= delta;
        if (entityCacheTimeout <= 0) {
            Entity.clearEntityCache();
            entityCacheTimeout += 60000;
        }
        
        time.update(delta);
        
         weatherSystem.update(this, delta);
        
        cursorTimer -= delta;
        if (cursorTimer <= 0) {
            cursorShow = !cursorShow;
            cursorTimer += 250;
        }
        //entity timer
        if (entityTimer > 0) {
            entityTimer -= delta;
            if (entityTimer <= 0) {
                entityDelay = 450;
            }
        }
        if (entityDelay > 0) {
            entityDelay -= delta;
            if (entityDelay <= 0) {
                entityCount++;
                entityTimer = 1500;
            }
        }
        
        
        if (centerCursor) {
            centerCursor(container.getWidth()-250, container.getHeight());
            this.centerCursor = false;
        }
        
        if (changeCursor != null) {
            changeCursor.initCustom(container.getWidth()-16, container.getHeight()-24);
            changeCursor.init(250-32, container.getHeight() - 40);
            changeCursor.init(container, game, delta, this, getChunk(), cursorMode);
            
            cursorMode = changeCursor;
            changeCursor = null;
            if (!centerCursor) {
                int xt0 = ((xScroll) >> tm) - 1;
                int yt0 = ((yScroll) >> tm) - 1;
                int w = ((container.getWidth()-250-px) >> tm) + 1;
                int h = ((container.getHeight()-py) >> tm) + 1;
                if (xCursor < xt0 || yCursor < yt0 || xCursor >= (xt0+w) || yCursor >= (yt0+h)) centerCursor = true;
            }

            if (centerCursor) {
                centerCursor(container.getWidth()-250, container.getHeight());
            }
        }
        if (cursorMode != null) {
            cursorMode.update(container, game, delta, this);
        }
        
        
        int xt0 = ((xScroll) >> tm) - 1;
        int yt0 = ((yScroll) >> tm) - 1;
        int w = ((container.getWidth()-250-px) >> tm) + 1;
        int h = ((container.getHeight()-py) >> tm) + 1;
        chunkManager.loadChunks(player, xt0, yt0, xt0+w, yt0+h);
        chunkManager.unloadChunks(xt0, yt0, xt0+w, yt0+h);
        chunkManager.update(delta);
        if (time.minutePassed)
            chunkManager.gameUpdate();
        player.update(delta);
        
        if (player.inventory.dirty) {
            player.inventory.dirty = false;
            if (cursorMode != null) {
                cursorMode.notifyDirtyInventory(player);
            }
        }

        if (pendingFullscreenToggle) {
            fullscreen = !fullscreen;
            container.setFullscreen(fullscreen);
            pendingFullscreenToggle = false;
        }
        
        if (time.minutePassed) {
            time.minutePassed = false;
        }
        //if the cursor is outside the view area, center it
        
        {
//            int xt0 = ((xScroll) >> 4) - 1;
//            int yt0 = ((yScroll) >> 4) - 1;
//            int w = ((container.getWidth()-250-8) >> 4) + 1;
//            int h = ((container.getHeight()-8) >> 4) + 1;
            
            if (xCursor < xt0 || yCursor < yt0 || xCursor > (xt0+w) || yCursor > (yt0+h))
                centerCursor = true;
        }
    }

    public void changeMode(CursorMode newMode, boolean centerCursor) {
        changeCursor = newMode;
        this.centerCursor = centerCursor;
    }
    public void changeMode(CursorMode newMode) {
        changeMode(newMode, false);
    }

    public void centerCursor(int w, int h) {
        
        int ts = Config.getInstance().tileSize;
        int tm = Config.getInstance().tileMask;
        
        int xt = (xScroll >> tm) + ((w >> tm) / 2);
        int yt = (yScroll >> tm) + ((h >> tm) / 2);
        xCursor = xt;
        yCursor = yt;
    }

    @Override
    public void keyPressed(int key, char c) {
        if (cursorMode != null) {
            cursorMode.keyPressed(this, getChunk(), xCursor, yCursor, key, c);
        } 
        if (key == Input.KEY_F1) {
            changeMode(new ScrollMode());
//            pendingFullscreenToggle = true;
        }
        if (key == Input.KEY_F2) {
            stop();
        }
        if (key == Input.KEY_F5) {
            save();
        }
        if (key == Input.KEY_F9) {
            load();
        }
        if (key == Input.KEY_F3) {
            resetGame();
        }
        if (key == Input.KEY_F10) changeMode(new OptionsMode(cursorMode));
        if (key == Input.KEY_F11) changeMode(new IngameHelpCursor());
        if (c == '?') changeMode(new IngameHelpCursor());
    }
    
    public void stop() {
        pendingExit = true;
    }

    @Override
    public void keyReleased(int key, char c) {
        if (cursorMode != null) {
            cursorMode.keyReleased(this, getChunk(), xCursor, yCursor, key, c);
        }
    }

    public void resetGame() {
        ChunkProvider.init(System.currentTimeMillis());
        player = new Player(this);
        chunkManager.clear();
        chunkManager.enableNewGame();
        chunkManager.loadChunk(player, 0, 0);
        chunkManager.loadChunk(player, -1, -1);
        chunkManager.loadChunk(player, -1, 0);
        chunkManager.loadChunk(player, -1, +1);
        chunkManager.loadChunk(player,  1, -1);
        chunkManager.loadChunk(player,  1, 0);
        chunkManager.loadChunk(player,  1, +1);
        chunkManager.loadChunk(player,  0, -1);
        chunkManager.loadChunk(player,  0, +1);
        time.reset();
        time.setTime(720); //12:00
        player.inventory.add(0, new ToolItem(Material.wooden, ToolType.AXE));
        player.inventory.add(0, new ToolItem(Material.wooden, ToolType.AXE));
        player.inventory.add(0, new ToolItem(Material.wooden, ToolType.AXE));
        logger.info("Started new game");
        player.addMsg("NEW GAME STARTED");
        xScroll = yScroll = 0;
        xCursor = yCursor = 0;
        centerCursor = true;
    }
    
    public void load() {
        logger.info("Loading game");
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(new FileInputStream(Config.getInstance().save));

            int versionMajor = in.readInt();
            int versionMinor = in.readInt();
            if (versionMajor != VERSION_MAJOR || versionMinor != VERSION_MINOR) {
                resetGame();
                return;
            }
            
            long seed = in.readLong();
            xScroll = in.readInt();
            yScroll = in.readInt();
            
            weatherSystem = (FWS)in.readObject();
            
            //Player
            player = new Player(this);
            try {
                player.inventory = (Inventory) in.readObject();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(InGameState.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            time.setTime(in.readInt());
            
            ChunkProvider.init(seed);
            logger.info("Loaded game");
        } catch (Exception ex) {
            //new game ...
            logger.log(Level.WARNING, "Could not load game: {0}", ex);
            resetGame();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                    Logger.getLogger(InGameState.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public void save() {
        Config.save();
        player.deselectItem();
//        DataOutputStream out = null;
        ObjectOutputStream out = null;
        try {
//            out = new DataOutputStream(new FileOutputStream("sav.dat"));
            out = new ObjectOutputStream(new FileOutputStream(Config.getInstance().save));

            out.writeInt(VERSION_MAJOR); //Version
            out.writeInt(VERSION_MINOR); //Version
            
            out.writeLong(ChunkProvider.getSeed());
            out.writeInt(xScroll);
            out.writeInt(yScroll);
            
            out.writeObject(weatherSystem);
            
            out.writeObject(player.inventory);
            
            out.writeInt(time.getTime());
            
            // WRITE CHUNKS
            for (Chunk chunk : chunkManager.loadedChunks) {
                chunkManager.writeChunk(chunk);
            }

            
            out.flush();
            player.addMsg("GAME SAVED");
        } catch (IOException ex) {
            Logger.getLogger(InGameState.class.getName()).log(Level.SEVERE, null, ex);
            player.addMsg("ERROR SAVING GAME");
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ex) {
                    Logger.getLogger(InGameState.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    /**
     * Returns the chunk under the current x/y Scroll value.
     * @return 
     */
    public Chunk getChunk() {
        return chunkManager.getChunk(xCursor, yCursor);
    }
}
