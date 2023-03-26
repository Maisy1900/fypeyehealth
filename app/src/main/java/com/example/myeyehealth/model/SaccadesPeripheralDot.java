package com.example.myeyehealth.model;

import android.graphics.Paint;
import android.graphics.PointF;

public class SaccadesPeripheralDot {
    private PointF position;
    private Paint paint;
    private boolean isActive;

    public SaccadesPeripheralDot(float x, float y, int color) {
        position = new PointF(x, y);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
    }

    public PointF getPosition() {
        return position;
    }

    public void setPosition(PointF position) {
        this.position = position;
    }

    public Paint getPaint() {
        return paint;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
