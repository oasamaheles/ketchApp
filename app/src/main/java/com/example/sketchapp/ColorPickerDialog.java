package com.example.sketchapp;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
public class ColorPickerDialog extends AlertDialog {

    private ColorPickerView colorPickerView;
    private OnColorSelectedListener onColorSelectedListener;

    protected ColorPickerDialog(Context context) {
        super(context);
        init(context);
    }

    protected ColorPickerDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    protected ColorPickerDialog(Context context, int themeResId) {
        super(context, themeResId);
        init(context);
    }

    private void init(Context context) {
        colorPickerView = new ColorPickerView(context);
        colorPickerView.setOnColorSelectedListener(new ColorPickerView.OnColorSelectedListener() {
            @Override
            public void onColorSelected(int color) {
                if (onColorSelectedListener != null) {
                    onColorSelectedListener.onColorSelected(color);
                }
                dismiss();
            }
        });

        setView(colorPickerView);
        setTitle("Select Color");
    }

    public void setOnColorSelectedListener(OnColorSelectedListener listener) {
        onColorSelectedListener = listener;
    }

    public interface OnColorSelectedListener {
        void onColorSelected(int color);
    }
}
