package com.example.myeyehealth.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

public class AmslerGridPlotView extends AmslerGridView {
    private HashMap<String, ArrayList<Float>> coordinates;
    private int plotGridSize;
    private int originalGridSize;
    private float lineSpacing;
    private static final int GRID_LINES = 40;

    public AmslerGridPlotView(Context context) {
        super(context);
        originalGridSize = 800;
        init();
    }

    public AmslerGridPlotView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        originalGridSize = 800;
        init();
    }

    public AmslerGridPlotView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        originalGridSize = 800;
        init();
    }

    public void setOriginalGridSize(int originalGridSize) {
        this.originalGridSize = originalGridSize;
    }

    private void init() {
        coordinates = new HashMap<>();
    }

    @Override

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint pointPaint = new Paint();
        pointPaint.setColor(0xFFFF0000);
        pointPaint.setStrokeWidth(12f);
        pointPaint.setStyle(Paint.Style.FILL);

        ArrayList<Float> xCoordinates = coordinates.get("x");
        ArrayList<Float> yCoordinates = coordinates.get("y");

        float scalingFactor = (float) plotGridSize / originalGridSize;

        if (xCoordinates != null && yCoordinates != null) {
            for (int i = 0; i < xCoordinates.size(); i++) {
                float x = xCoordinates.get(i);
                float y = yCoordinates.get(i);
                float xPos = getPaddingLeft() + x * scalingFactor;
                float yPos = getPaddingTop() + y * scalingFactor;

                canvas.drawCircle(xPos, yPos, 8f, pointPaint);
            }
        } else {
            Log.d("AmslerGridPlotView", "No coordinates to draw");
        }
    }


    public void setCoordinates(HashMap<String, ArrayList<Float>> coordinates) {
        this.coordinates = coordinates;
        Log.d("AmslerGridPlotView", "Received coordinates: " + coordinates.toString());
        invalidate();
        postInvalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        plotGridSize = 350; // Set the plot grid size to 300px
        updateLineSpacing(w, h);
    }

    public int getPlotGridSize() {
        return plotGridSize;
    }

    private void updateLineSpacing(int w, int h) {
        lineSpacing = Math.min(w, h) / (float) GRID_LINES;
    }
}
