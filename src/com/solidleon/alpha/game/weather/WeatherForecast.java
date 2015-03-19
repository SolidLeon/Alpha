/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solidleon.alpha.game.weather;

import com.solidleon.alpha.game.weather.FWS.WeatherCondition;

import java.io.Serializable;

/**
 *
 * @author markusmannel
 */
public class WeatherForecast implements  Serializable {

    public int time;
    public FWS.WeatherCondition condition;
    public int minTemperature, maxTemperature;

    public WeatherForecast(int time, WeatherCondition condition, int minTemperature, int maxTemperature) {
        this.time = time;
        this.condition = condition;
        this.minTemperature = minTemperature;
        this.maxTemperature = maxTemperature;
    }

    
}
