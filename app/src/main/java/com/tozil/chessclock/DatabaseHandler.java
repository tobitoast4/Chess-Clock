package com.tozil.chessclock;


import static android.content.Context.MODE_ENABLE_WRITE_AHEAD_LOGGING;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.regex.Pattern;

//Class that handles all database connections. Handles new insertions and queries.
public class DatabaseHandler {
    private SQLiteDatabase database;
    private Cursor c;

    public DatabaseHandler(Context context) {
        this.database = context.openOrCreateDatabase("app_database", MODE_ENABLE_WRITE_AHEAD_LOGGING, null);
    }

    public void createTables() {
        try {
            database.execSQL("" +
                    "CREATE TABLE IF NOT EXISTS TIMERS (" +
                        "name VARCHAR NOT NULL PRIMARY KEY, " +
                        "timer_top_ms_total INT, " +
                        "timer_top_ms_delay INT, " +
                        "timer_top_ms_increment INT, " +
                        "timer_bottom_ms_total INT, " +
                        "timer_bottom_ms_delay INT, " +
                        "timer_bottom_ms_increment INT" +
                    ");");
        } catch (Exception e){
            e.printStackTrace();
        }
    }



    public void deleteTimer(String name){
        try {
            database.execSQL("DELETE FROM TIMERS WHERE name = '" + name + "';");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean timerAlreadyExists(String name){
        try {
            c = database.rawQuery("SELECT COUNT(*) FROM TIMERS WHERE name = '" + name + "';", null);
            int index = c.getColumnIndex("COUNT(*)");
            c.moveToFirst();
            int amount = c.getInt(index);;

            if(amount>0){
                return true;
            } else {
                return false;
            }
        } catch (Exception e){
            e.printStackTrace();
            return true;
        } finally {
            c.close();
        }
    }

    //Inserts a new player to the table 'players' with passed name and sex of the player.
    public void insertNewTimer(TimerItem timer){
        try {
            database.execSQL("  INSERT INTO TIMERS (" +
                        "name, " +
                        "timer_top_ms_total, " +
                        "timer_top_ms_delay," +
                        "timer_top_ms_increment, " +
                        "timer_bottom_ms_total, " +
                        "timer_bottom_ms_delay, " +
                        "timer_bottom_ms_increment " +
                    ") VALUES (" +
                        "'" + timer.getTimerName() + "', " +
                        timer.getTimerTopMilliSeconds() + ", " +
                        timer.getTimerTopMilliSecondsDelay() + ", " +
                        timer.getTimerTopMilliSecondsIncrement() + ", " +
                        timer.getTimerBottomMilliSeconds() + ", " +
                        timer.getTimerBottomMilliSecondsDelay() + ", " +
                        timer.getTimerBottomMilliSecondsIncrement() + "" +
                    ");");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public ArrayList<TimerItem> getAllTimers(){
        ArrayList<TimerItem> timerList = new ArrayList<>();

        try {
            c = database.rawQuery("SELECT * FROM TIMERS", null);
            int index_name = c.getColumnIndex("name");
            int index_timer_top_ms_total = c.getColumnIndex("timer_top_ms_total");
            int index_timer_top_ms_delay = c.getColumnIndex("timer_top_ms_delay");
            int index_timer_top_ms_increment = c.getColumnIndex("timer_top_ms_increment");
            int index_timer_bottom_ms_total = c.getColumnIndex("timer_bottom_ms_total");
            int index_timer_bottom_ms_delay = c.getColumnIndex("timer_bottom_ms_delay");
            int index_timer_bottom_ms_increment = c.getColumnIndex("timer_bottom_ms_increment");
            c.moveToFirst();
            while (c.isAfterLast() == false){
                String name = c.getString(index_name);
                int timer_top_ms_total = c.getInt(index_timer_top_ms_total);
                int timer_top_ms_delay = c.getInt(index_timer_top_ms_delay);
                int timer_top_ms_increment = c.getInt(index_timer_top_ms_increment);
                int timer_bottom_ms_total = c.getInt(index_timer_bottom_ms_total);
                int timer_bottom_ms_delay = c.getInt(index_timer_bottom_ms_delay);
                int timer_bottom_ms_increment = c.getInt(index_timer_bottom_ms_increment);

                TimerItem timerItem = new TimerItem(
                        name, timer_top_ms_total, timer_top_ms_delay, timer_top_ms_increment,
                        timer_bottom_ms_total, timer_bottom_ms_delay, timer_bottom_ms_increment
                );

                timerList.add(timerItem);

                c.moveToNext();
            }

            Collections.reverse(timerList);
            return timerList;

        } catch (Exception e){
            e.printStackTrace();
            return null;
        } finally {
            c.close();
        }
    }

    public void insertBasicTimers(){
        TimerItem timerItem;

        //Rapid
        timerItem = new TimerItem(
                "Rapid 60",
                3600000, 0, 0,
                3600000, 0, 0
        );
        insertNewTimer(timerItem);
        timerItem = new TimerItem(
                "Rapid 30",
                1800000, 0, 0,
                1800000, 0, 0
        );
        insertNewTimer(timerItem);
        timerItem = new TimerItem(
                "Rapid 20",
                1200000, 0, 0,
                1200000, 0, 0
        );
        insertNewTimer(timerItem);
        timerItem = new TimerItem(
                "Rapid 10",
                600000, 0, 0,
                600000, 0, 0
        );
        insertNewTimer(timerItem);

        //Blitz
        timerItem = new TimerItem(
                "Blitz 5|5",
                300000, 0, 5000,
                300000, 0, 5000
        );
        insertNewTimer(timerItem);
        timerItem = new TimerItem(
                "Blitz 5",
                300000, 0, 0,
                300000, 0, 0
        );
        insertNewTimer(timerItem);
        timerItem = new TimerItem(
                "Blitz 3|2",
                180000, 0, 2000,
                180000, 0, 2000
        );
        insertNewTimer(timerItem);
        timerItem = new TimerItem(
                "Blitz 3",
                180000, 0, 0,
                180000, 0, 0
        );
        insertNewTimer(timerItem);


        // Bullets
        timerItem = new TimerItem(
                "Bullet 2|1",
                120000, 0, 1000,
                120000, 0, 1000
        );
        insertNewTimer(timerItem);
        timerItem = new TimerItem(
                "Bullet 1|1",
                60000, 0, 1000,
                60000, 0, 1000
        );
        insertNewTimer(timerItem);
        timerItem = new TimerItem(
                "Bullet 1",
                60000, 0, 0,
                60000, 0, 0
        );
        insertNewTimer(timerItem);
    }
}
