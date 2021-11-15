package com.tozil.chessclock;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class DialogSelectClicksound extends AppCompatDialogFragment{

    private RadioGroup radioGroup;
    private RadioButton[] radioButtons;
    private DialogListener listener;

    private int current_sound_id;
    private MediaPlayer mp;

    private boolean darkmode;

    public DialogSelectClicksound(int current_sound_id, boolean darkmode){
        this.current_sound_id = current_sound_id;
        this.darkmode = darkmode;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder;
        if(darkmode){
            builder= new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_DARK);
        } else {

            builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
        }

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_select_clicksound, null);

        radioGroup = view.findViewById(R.id.radioGroup);

        initialzeRadioButtons(view);

        if(current_sound_id == 0){radioGroup.check(R.id.radioButton0);}
        if(current_sound_id == 1){radioGroup.check(R.id.radioButton1);}
        if(current_sound_id == 2){radioGroup.check(R.id.radioButton2);}
        if(current_sound_id == 3){radioGroup.check(R.id.radioButton3);}
        if(current_sound_id == 4){radioGroup.check(R.id.radioButton4);}
        if(current_sound_id == 5){radioGroup.check(R.id.radioButton5);}
        if(current_sound_id == 6){radioGroup.check(R.id.radioButton6);}

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
                if(checkedId == R.id.radioButton0){sound_id = 0;}
                if(checkedId == R.id.radioButton1){sound_id = 1;}
                if(checkedId == R.id.radioButton2){sound_id = 2;}
                if(checkedId == R.id.radioButton3){sound_id = 3;}
                if(checkedId == R.id.radioButton4){sound_id = 4;}
                if(checkedId == R.id.radioButton5){sound_id = 5;}
                if(checkedId == R.id.radioButton6){sound_id = 6;}

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
        radioButtons = new RadioButton[7];
        radioButtons[0] = view.findViewById(R.id.radioButton0);
        radioButtons[1] = view.findViewById(R.id.radioButton1);
        radioButtons[2] = view.findViewById(R.id.radioButton2);
        radioButtons[3] = view.findViewById(R.id.radioButton3);
        radioButtons[4] = view.findViewById(R.id.radioButton4);
        radioButtons[5] = view.findViewById(R.id.radioButton5);
        radioButtons[6] = view.findViewById(R.id.radioButton6);

        int[] sound_ids = new int[7];
        sound_ids[0] = R.raw.blob;
        sound_ids[1] = R.raw.closing_box_click;
        sound_ids[2] = R.raw.handgun_click;
        sound_ids[3] = R.raw.metronom_click;
        sound_ids[4] = R.raw.normal_clack;
        sound_ids[5] = R.raw.snear_drum_click;
        sound_ids[6] = R.raw.tongue_clack;

        for(int i = 0; i < radioButtons.length; i++){
            int finalI = i;
            radioButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playSound(sound_ids[finalI]);
                }
            });
        }
    }
}
