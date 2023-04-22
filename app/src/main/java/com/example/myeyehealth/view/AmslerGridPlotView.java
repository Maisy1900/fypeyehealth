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

    public AmslerGridPlotView(Context context) {
        super(context);
        init();
    }

    public AmslerGridPlotView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AmslerGridPlotView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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

        int gridSize = getGridSize();
        int lineSpacing = getLineSpacing();

        Paint pointPaint = new Paint();
        pointPaint.setColor(0xFFFF0000);
        pointPaint.setStrokeWidth(12f);
        pointPaint.setStyle(Paint.Style.FILL);

        ArrayList<Float> xCoordinates = coordinates.get("x");
        ArrayList<Float> yCoordinates = coordinates.get("y");

        if (xCoordinates != null && yCoordinates != null) {
            Log.d("AmslerGridPlotView", "Drawing " + xCoordinates.size() + " points");
            for (int i = 0; i < xCoordinates.size(); i++) {
                float x = xCoordinates.get(i);
                float y = yCoordinates.get(i);
                float xPos = (getWidth() - gridSize) / 2.0f + x * lineSpacing;
                float yPos = (getHeight() - gridSize) / 2.0f + y * lineSpacing;

                if (originalGridSize != 0) {
                    float scaleFactor = (float) plotGridSize / originalGridSize;
                    xPos *= scaleFactor;
                    yPos *= scaleFactor;
                }

                Log.d("AmslerGridPlotView", "Drawing point at (" + xPos + ", " + yPos + ")");
                canvas.drawCircle(xPos, yPos, 8f, pointPaint);
            }
        } else {
            Log.d("AmslerGridPlotView", "No coordinates to draw");
        }
    }

    public void setCoordinates(HashMap<String, ArrayList<Float>> coordinates) {
        this.coordinates = coordinates;
        invalidate();
        postInvalidate(); // Add this line to force the view to redraw itself
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        plotGridSize = Math.min(w, h); // Use the smaller dimension as the grid size
    }

    public int getPlotGridSize() {
        return plotGridSize;
    }

}
