package com.tozil.chessclock;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Arrays;

public class ChangeTimeActivity extends AppCompatActivity implements DialogTimePicker.DialogListener {


    private SharedPreferences sharedPrefs;
    private int[][] times;

    private boolean darkmode;

    /* times reperesents the following array:

                                 0         1        2
                               hours    minutes   seconds            (row)
        timer 1 time:           [0]       [0]      [0]                 0
        timer 1 delay:          [0]       [0]      [0]                 1
        timer 1 increment:      [0]       [0]      [0]                 2
        timer 2 time:           [0]       [0]      [0]                 3
        timer 2 delay:          [0]       [0]      [0]                 4
        timer 2 increment:      [0]       [0]      [0]                 5

     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_time);

        sharedPrefs = this.getSharedPreferences("settings", Context.MODE_PRIVATE);

        darkmode = (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES);

        initializeTimes();
    }

    public void initializeTimes(){
        times = new int[6][3];

        times[0] = generateTimeArray(sharedPrefs.getLong("timer_top_milliSeconds", 0));
        times[1] = generateTimeArray(sharedPrefs.getLong("timer_top_milliSecondsDelay", 0));
        times[2] = generateTimeArray(sharedPrefs.getLong("timer_top_milliSecondsIncrease", 0));
        times[3] = generateTimeArray(sharedPrefs.getLong("timer_bottom_milliSeconds", 0));
        times[4] = generateTimeArray(sharedPrefs.getLong("timer_bottom_milliSecondsDelay", 0));
        times[5] = generateTimeArray(sharedPrefs.getLong("timer_bottom_milliSecondsIncrease", 0));

        Button button = findViewById(R.id.button_timer1_total);
        changeButtonText(button, null, times[0][0], times[0][1], times[0][2]);
        button = findViewById(R.id.button_timer1_delay);
        changeButtonText(button, null, times[1][0], times[1][1], times[1][2]);
        button = findViewById(R.id.button_timer1_increment);
        changeButtonText(button, null, times[2][0], times[2][1], times[2][2]);
        button = findViewById(R.id.button_timer2_total);
        changeButtonText(button, null, times[3][0], times[3][1], times[3][2]);
        button = findViewById(R.id.button_timer2_delay);
        changeButtonText(button, null, times[4][0], times[4][1], times[4][2]);
        button = findViewById(R.id.button_timer2_increment);
        changeButtonText(button, null, times[5][0], times[5][1], times[5][2]);
    }

    public int[] generateTimeArray(long milliSecs){
        int hours = (int) (milliSecs / 3600000);
        milliSecs = milliSecs - (hours * 3600000L);
        int minutes = (int) (milliSecs / 60000);
        milliSecs = milliSecs - (minutes * 60000L);
        int seconds = (int) (milliSecs / 1000);
        return new int[]{hours, minutes, seconds};
    }

    public void changeTotalTimeTimer1(View v){
        DialogTimePicker dialogTimePicker = new DialogTimePicker(times[0][0], times[0][1], times[0][2], 0, darkmode);
        dialogTimePicker.show(getSupportFragmentManager(), "dialogTimePicker");
    }

    public void changeTotalDelayTimer1(View v){
        DialogTimePicker dialogTimePicker = new DialogTimePicker(times[1][0], times[1][1], times[1][2], 1, darkmode);
        dialogTimePicker.show(getSupportFragmentManager(), "dialogTimePicker");
    }

    public void changeTotalIncrementTimer1(View v){
        DialogTimePicker dialogTimePicker = new DialogTimePicker(times[2][0], times[2][1], times[2][2], 2, darkmode);
        dialogTimePicker.show(getSupportFragmentManager(), "dialogTimePicker");
    }

    public void changeTotalTimeTimer2(View v){
        DialogTimePicker dialogTimePicker = new DialogTimePicker(times[3][0], times[3][1], times[3][2], 3, darkmode);
        dialogTimePicker.show(getSupportFragmentManager(), "dialogTimePicker");
    }

    public void changeTotalDelayTimer2(View v){
        DialogTimePicker dialogTimePicker = new DialogTimePicker(times[4][0], times[4][1], times[4][2], 4, darkmode);
        dialogTimePicker.show(getSupportFragmentManager(), "dialogTimePicker");
    }

    public void changeTotalIncrementTimer2(View v){
        DialogTimePicker dialogTimePicker = new DialogTimePicker(times[5][0], times[5][1], times[5][2], 5, darkmode);
        dialogTimePicker.show(getSupportFragmentManager(), "dialogTimePicker");
    }

    public void applyTimer(View v){

        long timer_1_time = times[0][0] * 3600000L + times[0][1] * 60000L + times[0][2] * 1000L;
        long timer_1_delay = times[1][0] * 3600000L + times[1][1] * 60000L + times[1][2] * 1000L;
        long timer_1_increment = times[2][0] * 3600000L + times[2][1] * 60000L + times[2][2] * 1000L;
        long timer_2_time = times[3][0] * 3600000L + times[3][1] * 60000L + times[3][2] * 1000L;
        long timer_2_delay = times[4][0] * 3600000L + times[4][1] * 60000L + times[4][2] * 1000L;
        long timer_2_increment = times[5][0] * 3600000L + times[5][1] * 60000L + times[5][2] * 1000L;

        sharedPrefs.edit().putLong("timer_top_milliSeconds", timer_1_time).apply();
        sharedPrefs.edit().putLong("timer_top_milliSecondsDelay", timer_1_delay).apply();
        sharedPrefs.edit().putLong("timer_top_milliSecondsIncrease", timer_1_increment).apply();
        sharedPrefs.edit().putLong("timer_bottom_milliSeconds", timer_2_time).apply();
        sharedPrefs.edit().putLong("timer_bottom_milliSecondsDelay", timer_2_delay).apply();
        sharedPrefs.edit().putLong("timer_bottom_milliSecondsIncrease", timer_2_increment).apply();

        Toast.makeText(this, "New time set", Toast.LENGTH_SHORT).show();

//
//        String s = "";
//        for (int i = 0; i<6; i++){
//            s = s + Arrays.toString(times[i]);
//        }
//        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();

        finish();
    }



    @Override
    public void applyTime(int hours, int minutes, int seconds, int row) {
        times[row][0] = hours;
        times[row][1] = minutes;
        times[row][2] = seconds;

        if(row < 3){ // falls Timer 1 (links) geändert wird, wird Timer 2 auch geändert
            times[row + 3][0] = hours;
            times[row + 3][1] = minutes;
            times[row + 3][2] = seconds;
        }

        Button button1;
        Button button2; // falls Timer 1 (links) geändert wird, wird Timer 2 auch geändert
                        // -> button2 ist entsprechender rechter Button
        switch (row){
            case 0:
                button1 = findViewById(R.id.button_timer1_total);
                button2 = findViewById(R.id.button_timer2_total);
                break;
            case 1:
                button1 = findViewById(R.id.button_timer1_delay);
                button2 = findViewById(R.id.button_timer2_delay);
                break;
            case 2:
                button1 = findViewById(R.id.button_timer1_increment);
                button2 = findViewById(R.id.button_timer2_increment);
                break;
            case 3:
                button1 = findViewById(R.id.button_timer2_total);
                button2 = null;
                break;
            case 4:
                button1 = findViewById(R.id.button_timer2_delay);
                button2 = null;
            default:
                button1 = findViewById(R.id.button_timer2_increment);
                button2 = null;
                break;
        }

        changeButtonText(button1, button2, hours, minutes, seconds);

    }

    public void changeButtonText(Button button1, Button button2, int hours, int minutes, int seconds){
        String new_time;
        if(hours < 1){
            new_time = String.format("%02d", minutes) + ":" + String.format("%02d", seconds);
        } else {
            new_time = String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds);
        }

        button1.setText(new_time);
        if(button2 != null){
            button2.setText(new_time);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fadein2, R.anim.fadeout2);
    }
}