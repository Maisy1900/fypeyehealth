package com.example.myeyehealth.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.myeyehealth.model.SaccadesPeripheralDot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SaccadesExerciseView extends View {
    private Paint centralDotPaint;
    private List<SaccadesPeripheralDot> peripheralDots;
    private int currentActiveDotIndex;
    private List<Long> tapTimes;
    private List<Float> tapDistances;
    private long lastTapTime;

    public interface OnExerciseCompleteListener {
        void onExerciseComplete();
    }

    private OnExerciseCompleteListener onExerciseCompleteListener;

    public SaccadesExerciseView(Context context) {
        super(context);
        init();
    }

    public SaccadesExerciseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SaccadesExerciseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // Initialize paint objects for the central dot
        centralDotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        centralDotPaint.setColor(Color.BLUE);

        // Initialize peripheral dots list and active dot index
        peripheralDots = new ArrayList<>();
        currentActiveDotIndex = 0;
        tapTimes = new ArrayList<>();
        tapDistances = new ArrayList<>();
        lastTapTime = System.currentTimeMillis();

        generatePeripheralDotSequence();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw the central dot
        canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, 20, centralDotPaint);

        // Draw peripheral dots
        for (SaccadesPeripheralDot dot : peripheralDots) {
            PointF position = dot.getPosition();
            canvas.drawCircle(position.x, position.y, 15, dot.getPaint());
        }
    }

    private void generatePeripheralDotSequence() {
        // Generate the sequence of peripheral dots and their positions
        // You can use random positions or follow a specific pattern
        // In this example, we create 8 random peripheral dots

        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            float x = random.nextInt(getWidth());
            float y = random.nextInt(getHeight());
            SaccadesPeripheralDot dot = new SaccadesPeripheralDot(x, y, Color.BLACK);
            peripheralDots.add(dot);
        }
        peripheralDots.get(currentActiveDotIndex).setActive(true);
        peripheralDots.get(currentActiveDotIndex).getPaint().setColor(Color.RED);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            PointF tapPosition = new PointF(event.getX(), event.getY());
            SaccadesPeripheralDot activeDot = peripheralDots.get(currentActiveDotIndex);
            PointF activeDotPosition = activeDot.getPosition();

            // Check if the user tapped within a certain radius around the active dot
            if (distanceBetween(tapPosition, activeDotPosition) <= 30) {
                // Record tap time
                long currentTime = System.currentTimeMillis();
                tapTimes.add(currentTime - lastTapTime);
                lastTapTime = currentTime;

                // Record tap distance
                if (currentActiveDotIndex > 0) {
                    SaccadesPeripheralDot lastDot = peripheralDots.get(currentActiveDotIndex - 1);
                    PointF lastDotPosition = lastDot.getPosition();
                    tapDistances.add(distanceBetween(activeDotPosition, lastDotPosition));
                }

                // Update the active dot
                activeDot.setActive(false);
                activeDot.getPaint().setColor(Color.BLACK);
                currentActiveDotIndex++;

                if (currentActiveDotIndex == peripheralDots.size()) {
                    if (onExerciseCompleteListener != null) {
                        onExerciseCompleteListener.onExerciseComplete();
                    }
                } else {
                    SaccadesPeripheralDot nextActiveDot = peripheralDots.get(currentActiveDotIndex);
                    nextActiveDot.setActive(true);
                    nextActiveDot.getPaint().setColor(Color.RED);
                    invalidate();
                }
            }
        }

        return true;
    }

    private float distanceBetween(PointF p1, PointF p2) {
        return (float) Math.sqrt(Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2));
    }

    public void setOnExerciseCompleteListener(OnExerciseCompleteListener listener) {
        this.onExerciseCompleteListener = listener;
    }

    public List<Long> getTapTimes() {
        return tapTimes;
    }

    public List<Float> getTapDistances() {
        return tapDistances;
    }
}