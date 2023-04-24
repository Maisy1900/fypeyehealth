package com.example.myeyehealth.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.myeyehealth.utils.SessionManager;

public class AmslerGridView extends View {

    private Paint gridPaint;
    private Paint crossPaint;

    private int gridSize;
    private int numLines;
    private int lineSpacing;
    private SessionManager sessionManager;

    public AmslerGridView(Context context) {
        super(context);
        init();
    }

    public AmslerGridView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AmslerGridView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public int getGridSize() {
        return gridSize;
    }
    private float getGridThicknessValue() {
        String gridThickness = sessionManager.getGridThickness();
        float thicknessValue;

        switch (gridThickness) {
            case "S":
                thicknessValue = 2.0f;
                break;
            case "M":
                thicknessValue = 4.0f;
                break;
            case "L":
                thicknessValue = 6.0f;
                break;
            default:
                thicknessValue = 4.0f; // Default to medium thickness
                break;
        }

        return thicknessValue;
    }


    private void init() {
        sessionManager = SessionManager.getInstance(getContext());

        gridPaint = new Paint();
        gridPaint.setColor(0xFF000000); // black color

        float thickness = getGridThicknessValue();
        gridPaint.setStrokeWidth(thickness);

        // Initialize the crossPaint object
        crossPaint = new Paint();
        crossPaint.setColor(0xFF000000); // black color
        crossPaint.setStrokeWidth(thickness * 2); // Set the cross width to double the grid line width

        gridSize = 800; // set the grid size
        numLines = 20; // set the number of lines
        lineSpacing = gridSize / numLines; // set the spacing between the lines
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        int height = resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec);

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();
        int gridSize = getGridSize();

        float gridLeft = (width - gridSize) / 2.0f;
        float gridTop = (height - gridSize) / 2.0f;
        Rect gridRect = new Rect((int) gridLeft, (int) gridTop, (int) (gridLeft + gridSize), (int) (gridTop + gridSize));

        // Draw horizontal lines
        for (int i = 0; i <= numLines; i++) {
            float yPos = gridTop + i * lineSpacing;
            canvas.drawLine(gridRect.left, yPos, gridRect.right, yPos, gridPaint);
        }

        // Draw vertical lines
        for (int i = 0; i <= numLines; i++) {
            float xPos = gridLeft + i * lineSpacing;
            canvas.drawLine(xPos, gridRect.top, xPos, gridRect.bottom, gridPaint);
        }
        // Draw the central cross
        float centerX = gridLeft + gridSize / 2.0f;
        float centerY = gridTop + gridSize / 2.0f;
        float crossLength = lineSpacing;

        // Draw the horizontal line of the cross
        canvas.drawLine(centerX - crossLength / 2.0f, centerY, centerX + crossLength / 2.0f, centerY, crossPaint);

        // Draw the vertical line of the cross
        canvas.drawLine(centerX, centerY - crossLength / 2.0f, centerX, centerY + crossLength / 2.0f, crossPaint);
    }

}
