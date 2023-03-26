package com.example.myeyehealth.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InteractiveAmslerGridView extends AmslerGridView {

    private List<PointF> coloredPoints;
    private Paint coloredPointPaint;
    private ArrayList<ArrayList<Float>> distortionCoordinatesList;

    public InteractiveAmslerGridView(Context context) {
        super(context);
        init();
    }

    public InteractiveAmslerGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public InteractiveAmslerGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    public int getNumDistortions() {
        if (distortionCoordinatesList != null) {
            return distortionCoordinatesList.size();
        } else {
            return 0;
        }
    }

    public ArrayList<Float> getDistortionCoordinates(int index) {
        if (distortionCoordinatesList != null && index >= 0 && index < distortionCoordinatesList.size()) {
            return distortionCoordinatesList.get(index);
        } else {
            return null;
        }
    }
    public HashMap<String, Float> calculateDistortionPercentages() {
        int gridSize = getGridSize();
        int sections = 6; // Assuming 6 sections in total
        int[] sectionsDistortedPoints = new int[sections];

        for (PointF point : coloredPoints) {
            int section = getSectionFromCoordinates(point.x / getWidth(), point.y / getHeight());
            sectionsDistortedPoints[section]++;
        }

        HashMap<String, Float> distortionPercentages = new HashMap<>();
        for (int i = 0; i < sections; i++) {
            float percentage = (sectionsDistortedPoints[i] / (float) (gridSize * gridSize)) * 100;
            String sectionName = getSectionName(i);
            distortionPercentages.put(sectionName, percentage);
        }

        return distortionPercentages;
    }

    private int getSectionFromCoordinates(float x, float y) {
        int horizontalSection = x < 0.5f ? 0 : 1;
        int verticalSection = y < 0.333f ? 0 : y < 0.666f ? 1 : 2;

        return verticalSection * 2 + horizontalSection;
    }

    private String getSectionName(int index) {
        String[] sectionNames = {
                "Upper Left", "Upper Middle", "Upper Right",
                "Lower Left", "Lower Middle", "Lower Right"
        };

        return sectionNames[index];
    }

    private void init() {
        coloredPoints = new ArrayList<>();
        coloredPointPaint = new Paint();
        coloredPointPaint.setColor(Color.RED);
        coloredPointPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (PointF point : coloredPoints) {
            canvas.drawCircle(point.x, point.y, 10, coloredPointPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            coloredPoints.add(new PointF(event.getX(), event.getY()));
            invalidate();
            return true;
        }

        return super.onTouchEvent(event);
    }
    public interface OnCompleteListener {
        void onComplete();
    }

    private OnCompleteListener onCompleteListener;

    public void setOnCompleteListener(OnCompleteListener listener) {
        this.onCompleteListener = listener;
    }
}
