/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solidleon.alpha;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.Music;
import org.newdawn.slick.MusicListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.openal.SoundStore;

/**
 *
 * @author Markus
 */
public class SoundCache {
    
    public static List<String> bgm = new ArrayList<String>();
    public static int current = 0;
    private static Music currentMusic;
    
    public static void init() {
        File f = new File(Config.getInstance().sound);
        if (f.isDirectory()) {
            String []filesAndDirs = f.list();
            for (int i = 0; i < filesAndDirs.length; i++) {
                String fs = filesAndDirs[i];
                if (fs.endsWith(".ogg")) {
                    bgm.add(Config.getInstance().sound + fs);
                }
            }
        }
    }
    
    private static void play(int idx) {
        if (idx < 0 || bgm.size() <= idx) return;
        try {
            if (currentMusic != null) {
                currentMusic.fade(1000, 0, true);
            }
            if (Config.getInstance().music <= 0f) return;
            currentMusic = new Music(bgm.get(idx), true);
            currentMusic.setVolume(0.0f);
            currentMusic.play();
            currentMusic.fade(1000, Config.getInstance().music, false);
//            currentMusic.setVolume(Config.getInstance().music);
        } catch (SlickException ex) {
            Logger.getLogger(SoundCache.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void start() {
        current = 0;
        play(current);
    }
    
    public static void update() {
        if (Config.getInstance().music == 0.0f) return;
        
        if (currentMusic != null) {
            if (!currentMusic.playing()) {
                current+=1;
                if (current >= bgm.size()) current = 0;
                play(current);
            }
        }
    }

    public static void stop() {
        if (currentMusic != null) currentMusic.stop();
    }

    public static void setVolume(float volume) {
        if (currentMusic != null) 
        {
//            currentMusic.setVolume(volume);
            currentMusic.fade(1000, volume, false);
        }
    }

    public static void previous() {
        current -= 1;
        if (current < 0) current = bgm.size() - 1;
        play(current);
    }
    public static void next() {
        current += 1;
        if (current >= bgm.size()) current = 0;
        play(current);
    }
    
}
