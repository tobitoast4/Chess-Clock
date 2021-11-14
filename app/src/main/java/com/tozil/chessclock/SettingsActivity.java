package com.tozil.chessclock;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity implements DialogSelectClicksound.DialogListener {


    SharedPreferences sharedPrefs;
    Switch switch_darkmode;

    boolean darkmode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sharedPrefs = this.getSharedPreferences("settings", Context.MODE_PRIVATE);

        switch_darkmode = findViewById(R.id.switch_darkmode);
        darkmode = (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES);
        switch_darkmode.setChecked(darkmode);

        switch_darkmode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switchDarkmode();
            }
        });
    }

    public void changeTime(View v){
        Intent intent = new Intent(getApplicationContext(), ChangeTimeActivity.class);
        startActivity(intent);
    }

    public void changeSound(View v){
        DialogSelectClicksound dialogSelectClicksound = new DialogSelectClicksound(sharedPrefs.getInt("sound_id", 0), darkmode);
        dialogSelectClicksound.show(getSupportFragmentManager(), "dialogSelectClicksound");
    }

    @Override
    public void applySound(int sound_id) {
        sharedPrefs.edit().putInt("sound_id", sound_id).apply();
    }


    public void changeDarkmode(View v){
        switchDarkmode();
    }

    public void switchDarkmode(){
        if(AppCompatDelegate.getDefaultNightMode() != AppCompatDelegate.MODE_NIGHT_NO){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
    }
}