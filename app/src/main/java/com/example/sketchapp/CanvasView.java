package com.example.sketchapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class CanvasView extends View {
    private  Paint paint;
    private  Path path;
    private int previousStrokeWidth;

    private int paintColor;
    private Canvas canvas;

    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        // Initialize paint object
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);  // Set default color to black
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(10f);

        // Initialize path object
        path = new Path();
    }


    public void setPaintColor(int color) {
        paintColor = color;
        paint.setColor(paintColor);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(path, paint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            //todo Start a new line in the path
            case MotionEvent.ACTION_DOWN:
                path.moveTo(x, y);
                break;
                //todo Draws Line Between Last Line pint and this point
            case MotionEvent.ACTION_MOVE:
                path.lineTo(x, y);
                break;
            case MotionEvent.ACTION_UP:
                break;
            default: return false;
        }
        //todo if you need to drawing Again
        invalidate();
        return true;
    }

    public void setColor(int color) {
        paint.setColor(color);
    }

    public void setStrokeWidth(float width) {
        paint.setStrokeWidth(width);
    }

    public void clear() {
        path.reset();
        invalidate();
    }

    public Bitmap getBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        draw(canvas);
        return bitmap;
    }



    public void clearCanvas() {
        canvas.drawColor(Color.WHITE);
        invalidate();
    }

    public float getStrokeWidth() {
        return paint.getStrokeWidth();
    }

    public float getPreviousStrokeWidth() {
        return previousStrokeWidth;
    }


}
