package com.example.shrey_000.guesswho;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class CanvasView extends View
{

    Context context;

    public CanvasView(Context c, AttributeSet attrs) {
        super(c, attrs);
        context = c;

    }



    // override onDraw
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint=new Paint();
        paint.setColor(Color.MAGENTA);
        paint.setStyle(Paint.Style.FILL);
        Path fillPath = new Path();

        fillPath.moveTo(0, 0); // Your origin point
        fillPath.lineTo(800, 0); // First point
        // Repeat above line for all points on your line graph
        fillPath.lineTo(800, 800); // Final point
        fillPath.lineTo(0, 800); // Draw from final point to the axis ++
        fillPath.lineTo(0, 0); // Same origin point
        canvas.drawPath(fillPath, paint);
    }



    public void clearCanvas() {

        invalidate();
    }

}



