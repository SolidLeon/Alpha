/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solidleon.alpha.game.time;

import com.solidleon.alpha.InGameState;

import java.text.SimpleDateFormat;

/**
 * Alpha Time System
 * 1hour  = 60 minutes
 * 1day   = 24 hours
 * 1week  = 5 days
 * 1month = 6 weeks
 * 1year  = 4 months
 * 
 * 
 * Game Time        Real Time
 * 1min             1sec
 * 1h               1min
 * 1d               24min
 * 1w               2h
 * 1m               12h      
 * 1y               48h
 * 
 * in32 : (2^31)-1 = 2 147 483 647 seconds = 68.0511039 years
 * 
 * 
 * The overall game time is stored as minutes.
 * Time splitting into minutes, hours, days, years:
 * 
 * Minutes  Hours   Days     Weeks   Years
 * 60       1       0.041667
 * 
 * 
 * 
 * @author Markus
 */
public class FTS {

    public static class TimeStruct {
        public static final int MINUTE = 0;
        public static final int HOUR = 1;
        public static final int DAY = 2;
        public static final int WEEK = 3;
        public static final int MONTH = 4;
        public static final int YEAR = 5;
        public static final int SEASON = 6;
        public static final int NUM_PARTS = 7;
        public int []parts;
        public boolean []has;

        public TimeStruct(int[] parts, boolean[] has) {
            this.parts = parts;
            this.has = has;
        }

        public TimeStruct() {
            this(new int[NUM_PARTS], new boolean[NUM_PARTS]);
        }
        
        
        
        public boolean has(int part) {
            if (part < 0 || part >= parts.length) return false;
            return has[part];
        }
        
        public int get(int part) {
            if (part < 0 || part >= parts.length) return 0;
            return parts[part];
        }
        
        public void set(int part, int value) {
            if (part < 0 || part >= parts.length) return;
            parts[part] = value;
            has[part] = true;
        }

        private void reset() {
            for (int i = 0; i < parts.length; i++) {
                parts[i] = 0;
                has[i] = false;
            }
        }
    }

    public static final int HOUR = 60;
    public static final int DAY = 24*HOUR;
    public static final int WEEK = 5*DAY;
    public static final int MONTH = 6*WEEK;
    public static final int YEAR = 4 * MONTH;
    
    public static final int SEASON_SPRING = 0;
    public static final int SEASON_SUMMER = 1;
    public static final int SEASON_AUTUMN = 2;
    public static final int SEASON_WINTER = 3;
    
    public static final String []SEASON_NAME = {
        "Spring",
        "Summer",
        "Autumn",
        "Winter"
    };
    
    public static final String []MONTH_NAMES = {
        "Arasj",
        "Barksh",
        "Ujkasd",
        "Larth"
    };
    
    public static int getGameTime(int year, int month, int day, int hour, int minute) {
        int t = year * YEAR;
        t += month * MONTH;
        t += day * DAY;
        t += hour * HOUR;
        t += minute;
        return t;
    }
     
    public static boolean hasYear(int time) {
        return time >= YEAR;
    }
    public static boolean hasMonth(int time) {
        return time >= MONTH;
    }
    public static boolean hasWeek(int time) {
        return time >= WEEK;
    }
    public static boolean hasDay(int time) {
        return time >= DAY;
    }
    public static boolean hasHour(int time) {
        return time >= HOUR;
    }
    
    private static TimeStruct timeStruct = new TimeStruct();
    
    public static TimeStruct getParts(int time) {
        int year=0;
        int month=0;
        int week=0;
        int day=0;
        int hour=0;
        int minute=0;

        timeStruct.reset();
        
        if (hasYear(time)) {
            year = time / YEAR;
            time -= year * YEAR;
            timeStruct.set(TimeStruct.YEAR, year);
        }
        if (hasMonth(time)) {
            month = time / MONTH;
            time -= month * MONTH;
            timeStruct.set(TimeStruct.MONTH, month);
        }
        if (hasDay(time)) {
            day = time / DAY;
            time -= day * DAY;
            timeStruct.set(TimeStruct.DAY, day);
        }
        if (hasHour(time)) {
            hour = time / HOUR;
            time -= hour * HOUR;
            timeStruct.set(TimeStruct.HOUR, hour);
        }
        minute = time;
        timeStruct.set(TimeStruct.MINUTE, minute);
        

        timeStruct.set(TimeStruct.SEASON, month);
        
        return timeStruct;
    }
    
    
    public static String toString(int minutes) {
        boolean hasHours = minutes >= 60;
        boolean hasDays = minutes >= (60*24);
        
        int d = minutes / 24 / 60;
        minutes -= d*24*60;
        int h = minutes / 60;
        minutes -= h*60;
        
        return String.format("%02d:%02d:%02d", d, h, minutes);
    }


    public enum DayTime {
        DAY,
        NIGHT
    };
    /** Current game time in minutes (1 gtm = 1s) */
    private int time;
    private int timer;
    public boolean minutePassed;
    
    public void reset() {
        time = 0;
        timer = 0;
        minutePassed = false;
    }
    
    /**
     * 
     * @param delta  ms delta
     */
    public void update(int delta) {
        timer += delta;
        if (timer >= 1000) {
            timer -= 1000;
            time++;
            minutePassed = true;
        }
        getParts(time);
    }

    /**
     * Returns time in minutes
     * @return game time minutes
     */
    public int getTime() {
        return time;
    }
    
    public void setTime(int time) {
        this.time = time;
    }
    
    /**
     * Returns the time of the day in a day/night cycle.
     * Time stores minutes since begin, this returns 
     * the time for the day instead.
     * @return minute of the day
     */
    public int getCycleTime() {
        return time % 1440; //1440 is 24h in minutes
    }
    
    public DayTime getCurrentDayTime() {
        int t = getCycleTime();
        int h = t / 60;
        //7.00 - 18.00
        if (h >= 7 && h < 18) return DayTime.DAY;
        return DayTime.NIGHT;
    }
    
    /**
     * Converts real time milliseconds to
     * game time minutes.
     * @param ms
     * @return 
     */
    public static int rms2gm(int ms) {
        if (ms < 1000) return 0;
        return ms / 1000;
    }
    /**
     * Converts game time minutes to real time milliseconds.
     * @param gm
     * @return 
     */
    public static int gm2rms(int gm) {
        return gm * 1000;
    }

    @Override
    public String toString() {
        TimeStruct ts = timeStruct;
        
        StringBuilder sb = new StringBuilder();
        
        sb.append(SEASON_NAME[ts.get(TimeStruct.SEASON)]).append(' ');
        
        if (ts.get(TimeStruct.YEAR) < 1000) sb.append("000");
        else if (ts.get(TimeStruct.YEAR) < 100) sb.append("00");
        else if (ts.get(TimeStruct.YEAR) < 10) sb.append("0");
        sb.append(ts.get(TimeStruct.YEAR));
        sb.append('/');
        sb.append(MONTH_NAMES[ts.get(TimeStruct.MONTH)]);
        sb.append('/');
        if (ts.get(TimeStruct.DAY) < 10) sb.append('0');
        sb.append(ts.get(TimeStruct.DAY));
        sb.append(' ');
        if (ts.get(TimeStruct.HOUR) < 10) sb.append('0');
        sb.append(ts.get(TimeStruct.HOUR));
        sb.append(':');
        if (ts.get(TimeStruct.MINUTE) < 10) sb.append('0');
        sb.append(ts.get(TimeStruct.MINUTE));
        
        
        
        return sb.toString();
    }
    
    
    public int getSeason() {
        return timeStruct.get(TimeStruct.SEASON);
    }
    
    
}
