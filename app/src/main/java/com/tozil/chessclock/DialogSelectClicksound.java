package com.tozil.chessclock;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class DialogSelectClicksound extends AppCompatDialogFragment{

    private RadioGroup radioGroup;
    private RadioButton[] radioButtons;
    private DialogListener listener;

    private int current_sound_id;
    private MediaPlayer mp;

    public DialogSelectClicksound(int current_sound_id){
        this.current_sound_id = current_sound_id;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_DARK);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_select_clicksound, null);

        radioGroup = view.findViewById(R.id.radioGroup);

        initialzeRadioButtons(view);

        if(current_sound_id == 0){radioGroup.check(R.id.radioButton1);}
        if(current_sound_id == 1){radioGroup.check(R.id.radioButton2);}
        if(current_sound_id == 2){radioGroup.check(R.id.radioButton3);}
        if(current_sound_id == 3){radioGroup.check(R.id.radioButton4);}

        builder.setView(view);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setPositiveButton("Apply", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int sound_id = -1;
                int checkedId = radioGroup.getCheckedRadioButtonId();
                if(checkedId == R.id.radioButton1){sound_id = 0;}
                if(checkedId == R.id.radioButton2){sound_id = 1;}
                if(checkedId == R.id.radioButton3){sound_id = 2;}
                if(checkedId == R.id.radioButton4){sound_id = 3;}

                listener.applySound(sound_id);
            }
        });

        return builder.create();
    }

    public void playSound(int id){
        try {
            mp.release(); // -> needed to free resources of old sound
        } catch (Exception e){
            e.printStackTrace();
        }
        mp = MediaPlayer.create(getContext(), id);
        mp.start();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (DialogListener) context;
        } catch (Exception e) {
            throw new ClassCastException((context.toString() + " must implement DialogListener"));
        }
    }

    public interface DialogListener{
        void applySound(int sound_id);
    }

    public void initialzeRadioButtons(View view){
        radioButtons = new RadioButton[4];
        radioButtons[0] = view.findViewById(R.id.radioButton1);
        radioButtons[1] = view.findViewById(R.id.radioButton2);
        radioButtons[2] = view.findViewById(R.id.radioButton3);
        radioButtons[3] = view.findViewById(R.id.radioButton4);

        radioButtons[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSound(R.raw.blob);
            }
        });
        radioButtons[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSound(R.raw.metronom_click);
            }
        });
        radioButtons[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSound(R.raw.normal_clack);
            }
        });
        radioButtons[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSound(R.raw.tongue_clack);
            }
        });
    }
}
