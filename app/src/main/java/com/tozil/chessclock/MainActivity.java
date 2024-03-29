package com.tozil.chessclock;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView textView_top;
    private TextView textView_bottom;
    private TextView textView_gamePaused_top;
    private TextView textView_gamePaused_bottom;

    ConstraintLayout button_top;
    ConstraintLayout button_bottom;

    CountDownTimerPausable timer_top;
    CountDownTimerPausable timer_bottom;

    Handler handler_top;
    Handler handler_bottom;

    private int state = 0; // indicates the state of the game
                           // 0: game hasnt started
                           // 1: game started

    SoundPool spool;
    int soundID;
    float volume;
    int[] sounds;

    SharedPreferences sharedPrefs;

    View decorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if(visibility == 0){
                    decorView.setSystemUiVisibility(hideSystemBars());
                }
            }
        });

        handler_top = new Handler();
        handler_bottom = new Handler();

        textView_top = findViewById(R.id.textView_top);
        textView_bottom = findViewById(R.id.textView_bottom);
        textView_gamePaused_top = findViewById(R.id.textView_gamePaused_top);
        textView_gamePaused_bottom = findViewById(R.id.textView_gamePaused_bottom);


        sharedPrefs = this.getSharedPreferences("settings", Context.MODE_PRIVATE);

        onFirstStart();

        startGame();

        initializeAudio();
    }

    public void startGame(){
        // (re)sets the textViews and the timers
        textView_top.setText(getTime(sharedPrefs.getLong("timer_top_milliSeconds", 0)));
        textView_bottom.setText(getTime(sharedPrefs.getLong("timer_bottom_milliSeconds", 0)));

        button_top = findViewById(R.id.constraint_top);
        button_bottom = findViewById(R.id.constraint_bottom);

        timer_top = initializeTimer(sharedPrefs.getLong("timer_top_milliSeconds", 0), textView_top);
        timer_bottom = initializeTimer(sharedPrefs.getLong("timer_bottom_milliSeconds", 0), textView_bottom);
    }

    public void onFirstStart(){
        String key = "first_run4";
        if (sharedPrefs.getBoolean(key, true)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            sharedPrefs.edit().putLong("timer_top_milliSeconds", 300000).apply();
            sharedPrefs.edit().putLong("timer_bottom_milliSeconds", 300000).apply();
            sharedPrefs.edit().putLong("timer_top_milliSecondsIncrement", 0).apply();
            sharedPrefs.edit().putLong("timer_bottom_milliSecondsIncrement", 0).apply();
            sharedPrefs.edit().putLong("timer_top_milliSecondsDelay", 0).apply();
            sharedPrefs.edit().putLong("timer_bottom_milliSecondsDelay", 0).apply();

            sharedPrefs.edit().putInt("sound_id", 1).apply();
            sharedPrefs.edit().putBoolean(key, false).apply();
        }
    }

    public CountDownTimerPausable initializeTimer(long timeInMilliSecs, TextView textView){
        CountDownTimerPausable timer =  new CountDownTimerPausable(timeInMilliSecs, 100) {

            public void onTick(long millisUntilFinished) {
                Log.i("TEst!!", "");
                textView.setText(getTime(millisUntilFinished));
                if(millisUntilFinished < 3050 && millisUntilFinished > 2950){
                    playFinishingSound(false);
                } else if(millisUntilFinished < 2050 && millisUntilFinished > 1950){
                    playFinishingSound(false);
                } else if(millisUntilFinished < 1050 && millisUntilFinished > 950){
                    playFinishingSound(false);
                }
            }

            public void onFinish() {
                textView.setText(getTime(0));
                timer_top.cancel();
                timer_bottom.cancel();
                playFinishingSound(true);
                button_top.setClickable(false);
                button_bottom.setClickable(false);
            }
        };
        return timer;
    }

    public void playFinishingSound(boolean game_over){
        // plays sound in each of the last three seconds to indicate that the users time will expire
        Thread streamThread = new Thread(new Runnable() {
            @Override
            public void run() {
                int soundID;
                if(game_over){
                    soundID = spool.load(getApplicationContext(), R.raw.game_over_sound, 1);
                } else {
                    soundID = spool.load(getApplicationContext(), R.raw.countdown_sound, 1);
                }

                spool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                    @Override
                    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                        // TODO Auto-generated method stub
                        if(state > 0){
                            spool.play(soundID, 1.0f, 1.0f, 1, 0, 1f);
                        }
                    }
                });

            }
        });

        streamThread.start();
    }

    public void initializeAudio(){
        // loads all audios
        sounds = new int[7];
        sounds[0] = R.raw.blob;
        sounds[1] = R.raw.closing_box_click;
        sounds[2] = R.raw.handgun_click;
        sounds[3] = R.raw.metronom_click;
        sounds[4] = R.raw.normal_clack;
        sounds[5] = R.raw.snear_drum_click;
        sounds[6] = R.raw.tongue_clack;

        // loads the onClick sound
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        spool = new SoundPool.Builder().setMaxStreams(10).build();
        soundID = spool.load(this, sounds[sharedPrefs.getInt("sound_id", 0)], 1);

        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        volume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    public String getTime(long milliSecs){
        // returns formatted string with time based ont milliSecs
        // example returns: "02:38:51.1" or "04:57.1"
        int hours = (int) (milliSecs / 3600000);
        milliSecs = milliSecs - (hours * 3600000);
        int minutes = (int) (milliSecs / 60000);
        milliSecs = milliSecs - (minutes * 60000);
        int seconds = (int) (milliSecs / 1000);
        milliSecs = milliSecs - (seconds * 1000);
        String time;
        if(hours > 0){
            time = String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds) + "." + String.format("%02d", milliSecs).charAt(0);
        } else {
            time = String.format("%02d", minutes) + ":" + String.format("%02d", seconds) + "." + String.format("%02d", milliSecs).charAt(0);
        }
        return time;
    }


    public void toggleTimerTop(View v) { // pauses timer on top, resumes timer on bottom
        if(state != 0){
            timer_top.addTime(sharedPrefs.getLong("timer_top_milliSecondsIncrement", 0));
            textView_top.setText(getTime(timer_top.millisRemaining));
        }
        toggleTimer(button_top, button_bottom);
        handler_top.postDelayed(new Runnable() {
            public void run() {
                timer_bottom.start();
            }
        }, sharedPrefs.getLong("timer_bottom_milliSecondsDelay", 0));
        try {
            timer_top.pause();
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }

    public void toggleTimerBottom(View v) { // pauses timer on bottom, resumes timer on top
        if(state != 0){
            timer_bottom.addTime(sharedPrefs.getLong("timer_bottom_milliSecondsIncrement", 0));
            textView_bottom.setText(getTime(timer_bottom.millisRemaining));
        }
        toggleTimer(button_bottom, button_top);
        handler_bottom.postDelayed(new Runnable() {
            public void run() {
                timer_top.start();
            }
        }, sharedPrefs.getLong("timer_top_milliSecondsDelay", 0));
        try {
            timer_bottom.pause();
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }

    public void toggleTimer(ConstraintLayout buttonToPause, ConstraintLayout buttonToStart){
        //stopping running handlers
        handler_top.removeCallbacksAndMessages(null);
        handler_bottom.removeCallbacksAndMessages(null);

        spool.play(soundID, volume, volume, 1, 0, 1f); // click sound

        textView_gamePaused_top.setVisibility(View.INVISIBLE);
        textView_gamePaused_bottom.setVisibility(View.INVISIBLE);

        buttonToPause.setBackground(getDrawable(R.drawable.button_main_inactive));
        buttonToStart.setBackground(getDrawable(R.drawable.button_main_active));

        buttonToPause.setClickable(false);
        buttonToStart.setClickable(true);

        state = 1;
    }

    public void settings(View v){
        pauseBothTimers();
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fadein1, R.anim.fadeout1);
    }

    public void pauseGame(View v){
        textView_gamePaused_top.setVisibility(View.VISIBLE);
        textView_gamePaused_bottom.setVisibility(View.VISIBLE);

        button_top.setClickable(true);
        button_bottom.setClickable(true);
        //stopping running handlers
        handler_top.removeCallbacksAndMessages(null);
        handler_bottom.removeCallbacksAndMessages(null);
        pauseBothTimers();
    }

    public void restart(View v){
        restartGame();
    }

    public void restartGame(){
        textView_gamePaused_top.setVisibility(View.INVISIBLE);
        textView_gamePaused_bottom.setVisibility(View.INVISIBLE);
        button_top.setBackground(getDrawable(R.drawable.button_main_inactive));
        button_bottom.setBackground(getDrawable(R.drawable.button_main_inactive));
        button_top.setClickable(true);
        button_bottom.setClickable(true);
        pauseBothTimers();
        startGame();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus){
        // hides system bars when activity is open
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            decorView.setSystemUiVisibility(hideSystemBars());
        }
    }

    private int hideSystemBars() {
        // returns int value to hide system bars
        return View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
    }

    @Override
    public void onResume() {
        restartGame();
        soundID = spool.load(this, sounds[sharedPrefs.getInt("sound_id", 0)], 1);
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        pauseBothTimers();
        moveTaskToBack(true);
        super.onBackPressed();
    }

    @Override
    public void onTrimMemory(int level) {
        // pauses both timer when app is in background
        // otherwise the timers will still run off and make sounds at the end
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_UI_HIDDEN) {
            moveTaskToBack(true);
            pauseBothTimers();
        }
    }

    public void pauseBothTimers(){
        //stopping running handlers
        handler_top.removeCallbacksAndMessages(null);
        handler_bottom.removeCallbacksAndMessages(null);
        try {
            timer_top.pause();
        } catch (Exception e) {
//            e.printStackTrace();
        }
        try {
            timer_bottom.pause();
        } catch (Exception e) {
//            e.printStackTrace();
        }
        state = 0;
    }





}