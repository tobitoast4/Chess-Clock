package com.tozil.chessclock;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class DialogTimePicker extends AppCompatDialogFragment{

    private DialogListener listener;
    private MyNumberPicker numberPickerHours;
    private MyNumberPicker numberPickerMinutes;
    private MyNumberPicker numberPickerSeconds;

    private boolean darkmode;


    private int hours, minutes, seconds, row;

    public DialogTimePicker(int hours, int minutes, int seconds, int row, boolean darkmode){
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
        this.row = row;
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
        View view = inflater.inflate(R.layout.dialog_timer_picker, null);

        numberPickerHours = view.findViewById(R.id.numberPickerHours);
        numberPickerMinutes = view.findViewById(R.id.numberPickerMinutes);
        numberPickerSeconds = view.findViewById(R.id.numberPickerSeconds);

        numberPickerHours.setValue(hours);
        numberPickerMinutes.setValue(minutes);
        numberPickerSeconds.setValue(seconds);


        builder.setView(view);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setPositiveButton("Apply", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.applyTime(numberPickerHours.getValue(), numberPickerMinutes.getValue(), numberPickerSeconds.getValue(), row);
            }
        });

        return builder.create();
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
        void applyTime(int hours, int minutes, int seconds, int row);
    }
}
