package com.example.sketchapp;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import java.security.AccessControlContext;


public class SeekBarDialog extends AlertDialog.Builder {

    private final int minValue;
    private final int maxValue;
    private final TextView valueText;

    private final SeekBar seekBar;
    private OnSeekBarChangeListener seekBarChangeListener;
    private OnDialogButtonClickListener dialogButtonClickListener;

    public SeekBarDialog(Context context, String title, int minValue, int maxValue, int initialValue) {
        super(context);
        this.minValue = minValue;
        this.maxValue = maxValue;

        // Set the title of the dialog
        setTitle(title);

        // Inflate the layout for the dialog
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_seekbar, null);
        setView(view);

        // Get references to the SeekBar and TextView in the layout
        seekBar = view.findViewById(R.id.seekBar);
        valueText = view.findViewById(R.id.valueTextView);

        // Set the range and initial value of the SeekBar
        seekBar.setMax(maxValue - minValue);
        seekBar.setProgress(initialValue - minValue);

        seekBar.getProgressDrawable().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
        seekBar.getThumb().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);


        // Update the value displayed in the TextView to match the initial value of the SeekBar
        updateValueText(initialValue);

        // Set a listener to update the value displayed in the TextView when the user changes the value on the SeekBar
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Update the value displayed in the TextView
                int value = minValue + progress;
                updateValueText(value);

                // Notify the listener, if set
                if (seekBarChangeListener != null) {
                    seekBarChangeListener.onProgressChanged(seekBar, value, fromUser);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    public interface OnSeekBarChangeListener {
        void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser);
    }

    public void setOnSeekBarChangeListener(OnSeekBarChangeListener listener) {
        seekBarChangeListener = listener;
    }

    public interface OnDialogButtonClickListener {
        void onDialogButtonClick(DialogInterface dialog, int which);
    }

    public void setOnDialogButtonClickListener(OnDialogButtonClickListener listener) {
        dialogButtonClickListener = listener;
    }

    public void show(int positiveButtonTextResId, int negativeButtonTextResId) {
        // Set the positive and negative buttons and their listeners
        setPositiveButton(positiveButtonTextResId, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Notify the listener, if set
                if (dialogButtonClickListener != null) {
                    dialogButtonClickListener.onDialogButtonClick(dialog, which);
                }
            }
        });

        setNegativeButton(negativeButtonTextResId, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Notify the listener, if set
                if (dialogButtonClickListener != null) {
                    dialogButtonClickListener.onDialogButtonClick(dialog, which);
                }
            }
        });

        // Show the dialog
        super.show();
    }

    private void updateValueText(int value) {
        valueText.setText(String.valueOf(value));
    }
}