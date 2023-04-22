package com.example.myeyehealth.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class AmslerGridPlotView extends AmslerGridView {
    private ArrayList<Float> coordinates;

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

    private void init() {
        coordinates = new ArrayList<>();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int gridSize = getGridSize();
        int lineSpacing = gridSize / 20; // 20 is the number of lines in the AmslerGridView

        Paint pointPaint = new Paint();
        pointPaint.setColor(0xFFFF0000); // red color
        pointPaint.setStrokeWidth(8f);
        pointPaint.setStyle(Paint.Style.FILL);

        if (coordinates != null) {
            for (int i = 0; i < coordinates.size(); i += 2) {
                float x = coordinates.get(i);
                float y = coordinates.get(i + 1);
                canvas.drawCircle((getWidth() - gridSize) / 2.0f + x * lineSpacing, (getHeight() - gridSize) / 2.0f + y * lineSpacing, 4f, pointPaint);
            }
        }
    }

    public void setCoordinates(ArrayList<Float> coordinates) {
        this.coordinates = coordinates;
        invalidate();
    }
}
