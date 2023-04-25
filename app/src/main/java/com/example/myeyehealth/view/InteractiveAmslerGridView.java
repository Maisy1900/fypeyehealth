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
//basically sets the view of the amsler grid and this is incharge of actually plotting the points on clikc

public class InteractiveAmslerGridView extends AmslerGridView {

    private List<PointF> coloredPoints;//point f i s used bc it has the x and y functionality
    private Paint coloredPointPaint;
    private ArrayList<ArrayList<Float>> distortionCoordinatesList;//dist of the coords pressed on ther gird

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
    //returns the number of coord points on the grid
    public int getNumDistortions() {
        if (distortionCoordinatesList != null) {
            return distortionCoordinatesList.size();
        } else {
            return 0;
        }
    }

//colours in the point on press click
    private void init() {
        coloredPoints = new ArrayList<>();
        coloredPointPaint = new Paint();
        coloredPointPaint.setColor(Color.RED);
        coloredPointPaint.setStyle(Paint.Style.FILL);
    }
//draws the colour points
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (PointF point : coloredPoints) {
            canvas.drawCircle(point.x, point.y, 10, coloredPointPaint);
        }
    }
//this is incharge of the  touch events so adds the coloured point when the screen is tapped
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float x= event.getX();//current x coord
            float y = event.getY();//current y coord
            //of the current view
            int width= getWidth();
            int height = getHeight();
            int gridSize = getGridSize();//from amslrview

            //checks if in the boundaries of the view so that coloured dot isnt added outside
            float gridLeft = (width - gridSize) / 2.0f;//boundary
            float gridTop = (height - gridSize) / 2.0f;
            float gridRight = gridLeft + gridSize ;
            float gridBottom = gridTop + gridSize;

            if (x >= gridLeft && x <= gridRight && y >= gridTop && y <= gridBottom) {
                coloredPoints.add(new PointF(x, y)) ;
                System.out.println("Point added: (" + x + ", " + y + ")");
                invalidate();
                saveDistortionCoordinates();
                return true;
            }
        }

        return super.onTouchEvent(event);
    }


//notifies when the user isdone with the view
    public interface OnCompleteListener {
        void onComplete(ArrayList<ArrayList<Float>> distortionCoordinates);//to be sent to the next activity
    }

    //to be called when the user is finished with the amsler grid for that eye, saves all the built up coords to the next intent
    private OnCompleteListener onCompleteListener ;//refers to the listener

    public void setOnCompleteListener(OnCompleteListener listener) {//custom listener
        this.onCompleteListener = listener;
    }
//when finished button clicked
    public void callOnComplete() {
        ArrayList<ArrayList<Float>> coordinates = saveDistortionCoordinates();

        if (onCompleteListener != null) {
            System.out.println("Calling onCompleteListener.onComplete()");
            onCompleteListener.onComplete(coordinates) ;
        }
    }


//saves the distortion
    public ArrayList<ArrayList<Float>> saveDistortionCoordinates() {
        distortionCoordinatesList = new ArrayList<>();

        for (PointF point : coloredPoints) {
            ArrayList<Float> coordinates = new ArrayList<>();
            coordinates.add(point.x);
            coordinates.add(point.y) ;
            distortionCoordinatesList.add(coordinates);
        }

        return distortionCoordinatesList;
    }


    public HashMap<String, Float> calculateDistortionPercentages(ArrayList<ArrayList<Float>> distortionCoordinates) {
        //grid size calc area or grid
        float gridWidth = getGridSize();
        float gridHeight = getGridSize();
        float totalArea = gridWidth * gridHeight;

        float distortedArea = 0f;
        //tracks processed points avoids double counting overlapping points
        List<ArrayList<Float>> processedPoints = new ArrayList<>() ;

        for (ArrayList<Float> coordinates : distortionCoordinates) {
            float x =  coordinates.get(0);
            float y=  coordinates.get(1);

            boolean overlap = false;
            //checks overlap
            for (ArrayList<Float> processedPoint : processedPoints) {
                float dx = x -processedPoint.get(0) ;
                float dy = y  - processedPoint.get(1);
                float distance =(float)Math.sqrt(dx * dx + dy * dy) ;
                float minDistance = 10; // the minimum distance between two points not overlapping
                if (distance < minDistance) {
                    overlap = true;
                    break;
                }
            }

            float distortionRadius = 25; // the radius for each distortion point
            float currentDistortedArea = (float)(Math.PI * Math.pow(distortionRadius, 2)) ;

            if (overlap) {
                currentDistortedArea /= 2; //ivide the distorted area by 2 if overlap
            }

            distortedArea += currentDistortedArea;
            processedPoints.add(coordinates);
        }
//working out distortion percentage from clean coords
        float distortionPercentage = (distortedArea / totalArea) * 100;

        HashMap<String, Float> distortionPercentages = new HashMap<>();
        distortionPercentages.put("total", distortionPercentage);

        return distortionPercentages;
    }

}
