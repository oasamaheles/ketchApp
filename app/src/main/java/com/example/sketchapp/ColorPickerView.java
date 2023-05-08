package com.example.sketchapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.provider.CalendarContract;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class ColorPickerView extends View {

    private Paint paint;
    private RectF rectF;
    private float hue, saturation, value;
    private int selectedColor;
    private OnColorSelectedListener onColorSelectedListener;

    public ColorPickerView(Context context) {
        super(context);
        init();
    }

    public ColorPickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ColorPickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        rectF = new RectF();

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        float x = event.getX();
                        float y = event.getY();
                        int color = getColorFromPosition(x, y);
                        setSelectedColor(color);
                        return true;
                }
                return false;
            }
        });
    }

    private int getColorFromPosition(float x, float y) {
        float hue = 360f * x / getWidth();
        float saturation = 1f - y / getHeight();
        float value = 1f;
        return Color.HSVToColor(new float[]{hue, saturation, value});
    }

    private void setSelectedColor(int color) {
        selectedColor = color;
        if (onColorSelectedListener != null) {
            onColorSelectedListener.onColorSelected(selectedColor);
        }
        invalidate();
    }

    public int getColor() {
        return selectedColor;
    }

    public void setColor(int color) {
        selectedColor = color;
        Color.colorToHSV(selectedColor, new float[]{hue, saturation, value});
        invalidate();
    }

    public void setOnColorSelectedListener(OnColorSelectedListener listener) {
        onColorSelectedListener = listener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();

        int[] colors = new int[]{Color.YELLOW, Color.GREEN, Color.CYAN, Color.BLUE, Color.MAGENTA, Color.RED,Color.GREEN};
        for (int i = 0; i < colors.length - 1; i++) {
            float left = i * width / (colors.length - 1);
            float right = (i + 1) * width / (colors.length - 1);
            rectF.set(left, 0, right, height);
            paint.setColor(colors[i]);
            canvas.drawRect(rectF, paint);
        }


        float x = hue * width / 360f;
        float y = (1f - saturation) * height;
        paint.setColor(Color.WHITE);
        canvas.drawCircle(x, y, 10, paint);

        paint.setColor(Color.BLACK);
        canvas.drawCircle(x, y, 11, paint);
    }

    public interface OnColorSelectedListener {
        void onColorSelected(int color);
    }
}
