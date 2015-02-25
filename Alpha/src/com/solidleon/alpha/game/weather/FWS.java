/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solidleon.alpha.game.weather;

import com.solidleon.alpha.InGameState;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Alpha Weather System
 * @author Markus
 */
public class FWS implements Serializable {
    public static enum WeatherCondition {
        SUNNY,
        RAINY,
        SNOWING,
        HAILING
    }
    
    
    private Random random = new Random();
    /** List of forecasts */
    public List<WeatherForecast> forecasts = new ArrayList<WeatherForecast>();
    public WeatherCondition weatherCondition;
    public int temperature;

    
    
    public void update(InGameState game, int delta) {
        if (weatherCondition == null || random.nextInt(1000) == 0) {
            generateForecast(game);
        }
    }
    
    public void generateForecast(InGameState game) {
        WeatherForecast f = null;
        
        WeatherForecast lastForecast = null;
        if (forecasts.size() > 0) {
            lastForecast = forecasts.get(forecasts.size() - 1);
        }
        
        int time = lastForecast != null ? lastForecast.time : game.time.getTime();
        time += random.nextInt((24*60)-30)+30; //1/2 hour to 1 day
        WeatherCondition wc = WeatherCondition.values()[random.nextInt(WeatherCondition.values().length)];
        int min = random.nextInt(20 - (-20)) +  (-20); //-20 to +20
        int max = min + random.nextInt(15); //15 on MIN
        
        f = new WeatherForecast(time, wc, min, max);
        forecasts.add(f);
    }

    
}
