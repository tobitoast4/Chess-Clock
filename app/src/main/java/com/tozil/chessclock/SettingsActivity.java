package com.tozil.chessclock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity implements DialogSelectClicksound.DialogListener {


    SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sharedPrefs = this.getSharedPreferences("settings", Context.MODE_PRIVATE);
    }

    public void changeTime(View v){

    }

    public void changeSound(View v){
        DialogSelectClicksound dialogSelectClicksound = new DialogSelectClicksound(sharedPrefs.getInt("sound_id", 0));
        dialogSelectClicksound.show(getSupportFragmentManager(), "dialogSelectClicksound");
    }

    @Override
    public void applySound(int sound_id) {
        sharedPrefs.edit().putInt("sound_id", sound_id).apply();
    }
}