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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class CanvasView extends View
{
    Context context;
    Path fillPath;
    CoordinateExtractor ce;


    Paint paint;
    Canvas canvas;

    public CanvasView(Context c, AttributeSet attrs) {
        super(c, attrs);
        context = c;
        fillPath = new Path();

    }

    public void getMaps(JSONObject responseObj) throws JSONException {
        ce = new CoordinateExtractor(responseObj);
//        ce.getLips();
//        ce.findEyes();
//        ce.getNose();
//        invalidate();
        setShapes();
    }

    public boolean setShapes() throws JSONException {

        if(ce==null)
            return false;
//        View view = new View(context);
//        view=findViewById(R.id.imageView);
//        if(view==null){
//            Log.d("view null","null");
//           //return false;
//        }

        HashMap<String, Double> eyesMap=new HashMap<String,Double>();
        eyesMap=ce.findEyes();

        float density = getContext().getResources().getDisplayMetrics().density;
        float leftEyeCenterX = Float.valueOf(eyesMap.get("leftEyeCenterX").toString());
        float leftEyeCenterY = Float.valueOf(eyesMap.get("leftEyeCenterY").toString());

        float rightEyeCenterX = Float.valueOf(eyesMap.get("rightEyeCenterX").toString());
        float rightEyeCenterY = Float.valueOf(eyesMap.get("rightEyeCenterY").toString());
        Log.d("hello","not null");
        int[] rand = new int[2];
        rand[0] = 0;
        rand[1] = 0;
       // view.getLocationOnScreen(rand);
        Log.d("rand",""+rand);

       // Compare();

      // fillPath.moveTo(getRelativeLeft(this), getRelativeTop(this)); // Your origin point
        fillPath.moveTo(100, 300);

        fillPath.lineTo(0, 800); // First point
        // Repeat above line for all points on your line graph
       // fillPath.lineTo(leftEyeCornerRightX, leftEyeCornerRightY); // Final point
        fillPath.lineTo(600, 200); // Draw from final point to the axis ++
        fillPath.lineTo(100, 300);
       // fillPath.lineTo(getRelativeLeft(this), getRelativeTop(this)); // Same origin point
//        HashMap<String,Double> eyesMap=ce.findEyes();
//
//        float leftEyeCornerLeftX,leftEyeCornerLeftY,leftEyeTopX,leftEyeTopY,leftEyeCornerRightX,leftEyeCornerRightY,leftEyeBottomX,leftEyeBottomY;
//        leftEyeCornerLeftX=Float.valueOf(eyesMap.get("leftEyeCornerLeftX").toString());
//        leftEyeCornerLeftY=Float.valueOf(eyesMap.get("leftEyeCornerLeftY").toString());
//        leftEyeTopX=Float.valueOf(eyesMap.get("leftEyeTopX").toString());
//        leftEyeTopY=Float.valueOf(eyesMap.get("leftEyeTopY").toString());
//        leftEyeCornerRightX=Float.valueOf(eyesMap.get("leftEyeCornerRightX").toString());
//        leftEyeCornerRightY=Float.valueOf(eyesMap.get("leftEyeCornerRightY").toString());
//        leftEyeBottomX=Float.valueOf(eyesMap.get("leftEyeBottomX").toString());
//        leftEyeBottomY=Float.valueOf(eyesMap.get("leftEyeBottomY").toString());
//
//        fillPath.moveTo(leftEyeCornerLeftX, leftEyeCornerLeftY); // Your origin point
//        fillPath.lineTo(leftEyeTopX, leftEyeTopY); // First point
//        // Repeat above line for all points on your line graph
//        fillPath.lineTo(leftEyeCornerRightX, leftEyeCornerRightY); // Final point
//        fillPath.lineTo(leftEyeBottomX, leftEyeBottomY); // Draw from final point to the axis ++
//        fillPath.lineTo(leftEyeCornerLeftX, leftEyeCornerLeftY); // Same origin point

        if (canvas==null)
            Log.d("Canvas null","");

        canvas.drawPath(fillPath, paint);

        return true;


    }

    ////////////////////////////// rel position////////////////////////////////////

    private float getRelativeLeft(View myView) throws JSONException {
        HashMap<String, Double> eyesMap=new HashMap<String,Double>();
        eyesMap=ce.findEyes();
        float leftEyeCenterX = Float.valueOf(eyesMap.get("leftEyeCenterX").toString());

        if (myView.getParent() == myView.getRootView())
            return leftEyeCenterX + myView.getLeft();
        else
            return  myView.getLeft() + getRelativeLeft((View) myView.getParent());
    }

    private float getRelativeTop(View myView) throws JSONException {
        HashMap<String, Double> eyesMap=new HashMap<String,Double>();
        eyesMap=ce.findEyes();
        float leftEyeCenterY = Float.valueOf(eyesMap.get("leftEyeCenterY").toString());
        if (myView.getParent() == myView.getRootView())
            return leftEyeCenterY + myView.getTop();
        else
            return myView.getTop() + getRelativeTop((View) myView.getParent());
    }
    //////////////////////////////////////////////////////////////////////////////

//    public void Compare()
//    {
//        View view = findViewById(R.id.imageView);
//        if(view == null){
//            Log.d("view null","true");
//        }
////        int width=
//        else {
//            Log.d("view null", "false");
//        }
//    }


    // override onDraw
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint=new Paint();
        paint.setColor(Color.MAGENTA);
        paint.setStyle(Paint.Style.FILL);

        this.canvas = canvas;


//        fillPath.moveTo(0, 0); // Your origin point
//        fillPath.lineTo(800, 0); // First point
//        // Repeat above line for all points on your line graph
//        fillPath.lineTo(800, 800); // Final point
//        fillPath.lineTo(0, 800); // Draw from final point to the axis ++
//        fillPath.lineTo(0, 0); // Same origin point


//        try {
//            if(!setShapes())
//                return;
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        canvas.drawPath(fillPath, paint);


//
//        Path fillPath2 = new Path();
//
//        fillPath.moveTo(500, 900); // Your origin point
//        fillPath.lineTo(900, 900); // First point
//        // Repeat above line for all points on your line graph
//        fillPath.lineTo(900, 1100); // Final point
//        fillPath.lineTo(500, 1100); // Draw from final point to the axis ++
//        fillPath.lineTo(500, 500); // Same origin point
//        canvas.drawPath(fillPath, paint);

    }



    public void clearCanvas() {

        invalidate();
    }

}



