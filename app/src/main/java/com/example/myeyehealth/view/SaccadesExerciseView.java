package com.example.myeyehealth.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.myeyehealth.utils.SessionManager;
import com.example.myeyehealth.model.SaccadesPeripheralDot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
//custom view for the saccades
public class SaccadesExerciseView extends View {
    private Paint centralDotPaint;
    private List<SaccadesPeripheralDot> peripheralDots;
    private int currentActiveDotIndex;
    private List<Long> tapTimes;
    private List<Float> tapDistances;
    private long lastTapTime;
    private SessionManager sessionManager;

    public interface OnExerciseCompleteListener {
        void onExerciseComplete();
    }

    private OnExerciseCompleteListener onExerciseCompleteListener;

    public SaccadesExerciseView(Context context) {
        super(context);
        sessionManager = SessionManager.getInstance(context);
        init();
    }

    public SaccadesExerciseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        sessionManager = SessionManager.getInstance(context);
        init();
    }

    public SaccadesExerciseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        sessionManager = SessionManager.getInstance(context);
        init();
    }

//initiates the paint objects tap times distances that will be store d in the database
    private void init() {
        centralDotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        centralDotPaint.setColor(Color.BLUE);
        peripheralDots = new ArrayList<>();
        currentActiveDotIndex = 0;
        tapTimes = new ArrayList<>();
        tapDistances = new ArrayList<>();
        lastTapTime = System.currentTimeMillis();

        generatePeripheralDotSequence();

    }

//draws central dot and perhiperal dots
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // draw the central dot
        canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, 20, centralDotPaint) ;

        // draw peripheral dots
        for (SaccadesPeripheralDot dot : peripheralDots) {
            PointF position = dot.getPosition();
            canvas.drawCircle(position.x, position.y, 15, dot.getPaint()) ;
        }
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        generatePeripheralDotSequence() ;
    }
    private void generatePeripheralDotSequence() {
        // generatesthe sequence of peripheral dots

        Random random = new Random();
        int width = getWidth();
        int height = getHeight() ;
        int padding = 40;

        if (width <= 0 || height <= 0) {
            return;
        }
        peripheralDots.clear();

        for (int i = 0; i < 8; i++) {
            boolean validDot = false ;
            SaccadesPeripheralDot dot = null;

            while (!validDot) {
                float x = padding + random.nextInt(width - 2 * padding);
                float y = padding + random.nextInt(height - 2 * padding);
                dot = new SaccadesPeripheralDot(x, y, Color.BLACK) ;

                if (isValidDot(dot, peripheralDots, 60)) {
                    validDot = true;
                }
            }

            peripheralDots.add(dot);
        }
        peripheralDots.get(currentActiveDotIndex).setActive(true);
        updateActiveDotColor();
    }

    private boolean isValidDot(SaccadesPeripheralDot newDot, List<SaccadesPeripheralDot> existingDots, int minDistance) {
        PointF newDotPosition = newDot.getPosition();
        PointF center = new PointF(getWidth() / 2f, getHeight() / 2f) ;

        // if the new dot is too close to the central dot
        if (distanceBetween(newDotPosition, center) < minDistance) {
            return false;
        }

        // if the new dot is too close to existing peripheral dots
        for (SaccadesPeripheralDot existingDot : existingDots) {
            PointF existingDotPosition = existingDot.getPosition() ;
            if (distanceBetween(newDotPosition, existingDotPosition) < minDistance) {
                return false;
            }
        }

        //  dot is not too close it's valid
        return true;
    }

    //this is incharge of the  touch events so changes the selected dot when tapped and adds the appropriate paramerets
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() ==MotionEvent.ACTION_DOWN) {
            PointF tapPosition= new PointF(event.getX(), event.getY()) ;
            SaccadesPeripheralDot activeDot = peripheralDots.get(currentActiveDotIndex);
            PointF activeDotPosition = activeDot.getPosition();

            // check if the user tapped within a certain radius around the active dot
            if (distanceBetween(tapPosition, activeDotPosition) <= 30) {
                // Record tap time
                long currentTime=System.currentTimeMillis();
                tapTimes.add(currentTime -lastTapTime);
                lastTapTime = currentTime;


                // record tap distance
                if (currentActiveDotIndex>0) {
                    SaccadesPeripheralDot lastDot = peripheralDots.get(currentActiveDotIndex - 1) ;
                    PointF lastDotPosition =lastDot.getPosition();
                    float distance = distanceBetween(activeDotPosition, lastDotPosition);
                     tapDistances.add(distance);
                }

                // update the active dot
                activeDot.setActive(false) ;
                 activeDot.getPaint().setColor(Color.BLACK);
                currentActiveDotIndex++;

                if (currentActiveDotIndex== peripheralDots.size()) {
                    if (onExerciseCompleteListener!= null) {
                        onExerciseCompleteListener.onExerciseComplete();
                    }
                } else {
                    SaccadesPeripheralDot nextActiveDot =peripheralDots.get(currentActiveDotIndex) ;
                    nextActiveDot.setActive(true);
                    updateActiveDotColor();
                    invalidate();
                }
            } else {
                //when the user taps outside of the vciew
                return true;
            }
        }

        return true;
    }
//updates the new dot
    private void updateActiveDotColor() {
        String color = sessionManager.getSaccadesColor();
        int parsedColor =Color.parseColor(color);
        SaccadesPeripheralDot activeDot= peripheralDots.get(currentActiveDotIndex) ;
        activeDot.getPaint().setColor(parsedColor);
        invalidate();
    }



    private float distanceBetween(PointF p1, PointF p2) {
        return (float) Math.sqrt(Math.pow(p2.x-p1.x,2) + Math.pow(p2.y - p1.y, 2));
    }

    public void setOnExerciseCompleteListener(OnExerciseCompleteListener listener) {
        this.onExerciseCompleteListener =listener ;
    }
//complete list
    public List<Long> getTapTimes() {
        return tapTimes ;
    }
    //complete list
    public List<Float> getTapDistances() {
        return tapDistances;
    }
}