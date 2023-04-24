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
            float x = event.getX();
            float y = event.getY();

            int width = getWidth();
            int height = getHeight();
            int gridSize = getGridSize();

            float gridLeft = (width - gridSize) / 2.0f;
            float gridTop = (height - gridSize) / 2.0f;
            float gridRight = gridLeft + gridSize;
            float gridBottom = gridTop + gridSize;

            if (x >= gridLeft && x <= gridRight && y >= gridTop && y <= gridBottom) {
                coloredPoints.add(new PointF(x, y));
                System.out.println("Point added: (" + x + ", " + y + ")");
                invalidate();
                saveDistortionCoordinates();
                return true;
            }
        }

        return super.onTouchEvent(event);
    }



    public interface OnCompleteListener {
        void onComplete(ArrayList<ArrayList<Float>> distortionCoordinates);
    }


    private OnCompleteListener onCompleteListener;

    public void setOnCompleteListener(OnCompleteListener listener) {
        this.onCompleteListener = listener;
    }

    public void callOnComplete() {
        ArrayList<ArrayList<Float>> coordinates = saveDistortionCoordinates();

        if (onCompleteListener != null) {
            System.out.println("Calling onCompleteListener.onComplete()");
            onCompleteListener.onComplete(coordinates);
        }
    }



    public ArrayList<ArrayList<Float>> saveDistortionCoordinates() {
        distortionCoordinatesList = new ArrayList<>();

        for (PointF point : coloredPoints) {
            ArrayList<Float> coordinates = new ArrayList<>();
            coordinates.add(point.x);
            coordinates.add(point.y);
            distortionCoordinatesList.add(coordinates);
        }

        return distortionCoordinatesList;
    }


    public HashMap<String, Float> calculateDistortionPercentages(ArrayList<ArrayList<Float>> distortionCoordinates) {
        float gridWidth = getGridSize();
        float gridHeight = getGridSize();
        float totalArea = gridWidth * gridHeight;

        float distortedArea = 0f;
        List<ArrayList<Float>> processedPoints = new ArrayList<>();

        for (ArrayList<Float> coordinates : distortionCoordinates) {
            float x = coordinates.get(0);
            float y = coordinates.get(1);

            boolean overlap = false;
            for (ArrayList<Float> processedPoint : processedPoints) {
                float dx = x - processedPoint.get(0);
                float dy = y - processedPoint.get(1);
                float distance = (float) Math.sqrt(dx * dx + dy * dy);
                float minDistance = 10; // Define the minimum distance between two points to be considered not overlapping
                if (distance < minDistance) {
                    overlap = true;
                    break;
                }
            }

            float distortionRadius = 25; // Define the radius for each distortion point (e.g., 5 pixels)
            float currentDistortedArea = (float) (Math.PI * Math.pow(distortionRadius, 2));

            if (overlap) {
                currentDistortedArea /= 2; // Divide the distorted area by 2 if there is an overlap
            }

            distortedArea += currentDistortedArea;
            processedPoints.add(coordinates);
        }

        float distortionPercentage = (distortedArea / totalArea) * 100;

        HashMap<String, Float> distortionPercentages = new HashMap<>();
        distortionPercentages.put("total", distortionPercentage);

        System.out.println("Distorted Area: " + distortedArea);
        System.out.println("Distortion Percentage: " + distortionPercentage);

        return distortionPercentages;
    }

}
