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

    public HashMap<String, Float> calculateQuadrantDistortions(ArrayList<ArrayList<Float>> distortionCoordinates) {
        float gridWidth = getGridSize();
        float gridHeight = getGridSize();
        float quadrantWidth = gridWidth / 2;
        float quadrantHeight = gridHeight / 2;
        float quadrantArea = quadrantWidth * quadrantHeight;

        HashMap<String, Float> distortedAreas = new HashMap<>();
        distortedAreas.put("upperLeft", 0f);
        distortedAreas.put("upperRight", 0f);
        distortedAreas.put("lowerLeft", 0f);
        distortedAreas.put("lowerRight", 0f);

        HashMap<String, List<ArrayList<Float>>> pointsInQuadrant = new HashMap<>();
        pointsInQuadrant.put("upperLeft", new ArrayList<>());
        pointsInQuadrant.put("upperRight", new ArrayList<>());
        pointsInQuadrant.put("lowerLeft", new ArrayList<>());
        pointsInQuadrant.put("lowerRight", new ArrayList<>());

        for (ArrayList<Float> coordinates : distortionCoordinates) {
            float x = coordinates.get(0);
            float y = coordinates.get(1);

            String quadrant;
            if (x <= quadrantWidth && y <= quadrantHeight) {
                quadrant = "upperLeft";
            } else if (x > quadrantWidth && y <= quadrantHeight) {
                quadrant = "upperRight";
            } else if (x <= quadrantWidth && y > quadrantHeight) {
                quadrant = "lowerLeft";
            } else {
                quadrant = "lowerRight";
            }

            System.out.println("Coordinates (x, y): (" + x + ", " + y + "), Quadrant: " + quadrant);

            List<ArrayList<Float>> pointsInSameQuadrant = pointsInQuadrant.get(quadrant);
            boolean overlap = false;
            for (ArrayList<Float> point : pointsInSameQuadrant) {
                float dx = x - point.get(0);
                float dy = y - point.get(1);
                float distance = (float) Math.sqrt(dx * dx + dy * dy);
                float minDistance = 10; // Define the minimum distance between two points to be considered not overlapping
                if (distance < minDistance) {
                    overlap = true;
                    break;
                }
            }

            float distortionRadius = 5; // Define the radius for each distortion point (e.g., 5 pixels)
            float distortedArea = (float) (Math.PI * Math.pow(distortionRadius, 2));
            if (overlap) {
                distortedArea /= 2; // Divide the distorted area by 2 if there is an overlap
            }

            System.out.println("Overlap: " + overlap + ", Distorted Area: " + distortedArea);

            distortedAreas.put(quadrant, distortedAreas.get(quadrant) + distortedArea);
            pointsInSameQuadrant.add(coordinates);

            System.out.println("Updated distortedAreas: " + distortedAreas);
        }

        // Calculate distortion percentages
        HashMap<String, Float> distortionPercentages = new HashMap<>();
        for (String quadrant : distortedAreas.keySet()) {
            float distortedArea = distortedAreas.get(quadrant);
            float distortionPercentage = (distortedArea / quadrantArea) * 100;
            distortionPercentages.put(quadrant, distortionPercentage);
        }

        System.out.println("Distorted Areas: " + distortedAreas);
        System.out.println("Distortion Percentages: " + distortionPercentages);

        return distortionPercentages;
    }
    public HashMap<String, Float> getAverageDistortionPercentages(HashMap<Integer, List<HashMap<String, String>>> pastFiveTests) {
        HashMap<String, Float> sumDistortionPercentages = new HashMap<>();
        sumDistortionPercentages.put("upperLeft", 0f);
        sumDistortionPercentages.put("upperRight", 0f);
        sumDistortionPercentages.put("lowerLeft", 0f);
        sumDistortionPercentages.put("lowerRight", 0f);

        int testCount = 0;

        for (int testId : pastFiveTests.keySet()) {
            List<HashMap<String, String>> testResults = pastFiveTests.get(testId);
            ArrayList<ArrayList<Float>> distortionCoordinates = new ArrayList<>();

            for (HashMap<String, String> result : testResults) {
                float xCoord = Float.parseFloat(result.get("xCoord"));
                float yCoord = Float.parseFloat(result.get("yCoord"));
                ArrayList<Float> coordinates = new ArrayList<>();
                coordinates.add(xCoord);
                coordinates.add(yCoord);
                distortionCoordinates.add(coordinates);
            }

            HashMap<String, Float> distortionPercentages = calculateQuadrantDistortions(distortionCoordinates);

            for (String quadrant : distortionPercentages.keySet()) {
                float percentage = distortionPercentages.get(quadrant);
                sumDistortionPercentages.put(quadrant, sumDistortionPercentages.get(quadrant) + percentage);
            }

            testCount++;
        }

        HashMap<String, Float> averageDistortionPercentages = new HashMap<>();
        for (String quadrant : sumDistortionPercentages.keySet()) {
            float sumPercentage = sumDistortionPercentages.get(quadrant);
            averageDistortionPercentages.put(quadrant, sumPercentage / testCount);
        }

        return averageDistortionPercentages;
    }
    public HashMap<String, Float> calculateLeftRightDistortionPercentages(ArrayList<ArrayList<Float>> distortionCoordinates) {
        HashMap<String, Float> quadrantDistortions = calculateQuadrantDistortions(distortionCoordinates);
        HashMap<String, Float> leftRightDistortionPercentages = new HashMap<>();

        float leftDistortion = quadrantDistortions.get("upperLeft") + quadrantDistortions.get("lowerLeft");
        float rightDistortion = quadrantDistortions.get("upperRight") + quadrantDistortions.get("lowerRight");

        leftRightDistortionPercentages.put("left", leftDistortion);
        leftRightDistortionPercentages.put("right", rightDistortion);

        return leftRightDistortionPercentages;
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
