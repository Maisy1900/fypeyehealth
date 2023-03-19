package com.example.myeyehealth.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class AmslerGridView extends View {
    private Paint paint;

    public AmslerGridView(Context context) {
        super(context);
        init();
    }

    public AmslerGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AmslerGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        setBackgroundColor(Color.WHITE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();
        int gridSize = Math.min(width, height);
        int cellSize = gridSize / 20; // Divide the grid into 20x20 cells

        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(2);

        // Draw vertical lines
        for (int i = 0; i <= 20; i++) {
            int x = i * cellSize;
            canvas.drawLine(x, 0, x, gridSize, paint);
        }

        // Draw horizontal lines
        for (int i = 0; i <= 20; i++) {
            int y = i * cellSize;
            canvas.drawLine(0, y, gridSize, y, paint);
        }

        // Draw central dot
        paint.setColor(Color.RED);
        paint.setStrokeWidth(8);
        canvas.drawPoint(gridSize / 2, gridSize / 2, paint);
    }
}
