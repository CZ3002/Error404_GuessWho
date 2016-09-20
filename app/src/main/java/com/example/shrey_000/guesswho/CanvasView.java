package com.example.shrey_000.guesswho;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.example.shrey_000.guesswho.R.*;

public class CanvasView extends View
{
    Context context;
    Path fillPath;
    CoordinateExtractor ce;
    View view;

    public CanvasView(Context c, AttributeSet attrs) {
        super(c, attrs);
        context = c;
        fillPath = new Path();

    }

    public void getMaps(JSONObject responseObj, View view) throws JSONException {
        ce = new CoordinateExtractor(responseObj);
        this.view=view;
        setShapes();
    }

    public boolean setShapes() throws JSONException {

        if(ce==null)
            return false;
//        View view = new View(context);
//        view=findViewById(R.id.imageView);
        if(view==null){
            Log.d("view null","null");
           return false;
        }
        else
        Log.d("view is not null","");

        //---------------changed--------------------------------------

        HashMap<String,Double> dimensions = ce.getWidthandHeight();


        float width = Float.valueOf(dimensions.get("width").toString());
        float height = Float.valueOf(dimensions.get("height").toString());

        float widthRatio = view.getWidth()/width;
        float heightRatio = view.getHeight()/height;

        //----------------------------------------------------------------

        HashMap<String, Double> eyesMap=new HashMap<String,Double>();
        eyesMap=ce.findEyes();
        Log.d("eyesMap is",""+eyesMap);

//


        float leftEyeCornerLeftX,leftEyeCornerLeftY,leftEyeTopX,leftEyeTopY,leftEyeCornerRightX,leftEyeCornerRightY,leftEyeBottomX,leftEyeBottomY,
                rightEyeCornerLeftX,rightEyeCornerLeftY,rightEyeTopX,rightEyeTopY,rightEyeCornerRightX,rightEyeCornerRightY,rightEyeBottomX,rightEyeBottomY;
        leftEyeCornerLeftX=Float.valueOf(eyesMap.get("leftEyeCornerLeftX").toString());
        leftEyeCornerLeftY=Float.valueOf(eyesMap.get("leftEyeCornerLeftY").toString());
        leftEyeTopX=Float.valueOf(eyesMap.get("leftEyeTopX").toString());
        leftEyeTopY=Float.valueOf(eyesMap.get("leftEyeTopY").toString());
        leftEyeCornerRightX=Float.valueOf(eyesMap.get("leftEyeCornerRightX").toString());
        leftEyeCornerRightY=Float.valueOf(eyesMap.get("leftEyeCornerRightY").toString());
        leftEyeBottomX=Float.valueOf(eyesMap.get("leftEyeBottomX").toString());
        leftEyeBottomY=Float.valueOf(eyesMap.get("leftEyeBottomY").toString());

        rightEyeCornerLeftX=Float.valueOf(eyesMap.get("rightEyeCornerLeftX").toString());
        rightEyeCornerLeftY=Float.valueOf(eyesMap.get("rightEyeCornerLeftY").toString());
        rightEyeTopX=Float.valueOf(eyesMap.get("rightEyeTopX").toString());
        rightEyeTopY=Float.valueOf(eyesMap.get("rightEyeTopY").toString());
        rightEyeCornerRightX=Float.valueOf(eyesMap.get("rightEyeCornerRightX").toString());
        rightEyeCornerRightY=Float.valueOf(eyesMap.get("rightEyeCornerRightY").toString());
        rightEyeBottomX=Float.valueOf(eyesMap.get("rightEyeBottomX").toString());
        rightEyeBottomY=Float.valueOf(eyesMap.get("rightEyeBottomY").toString());
//
        fillPath.moveTo(leftEyeCornerLeftX * widthRatio, leftEyeCornerLeftY * heightRatio); // Your origin point
        fillPath.lineTo(leftEyeTopX * widthRatio, leftEyeTopY * heightRatio); // First point
        // Repeat above line for all points on your line graph
        fillPath.lineTo(leftEyeCornerRightX * widthRatio, leftEyeCornerRightY * heightRatio); // Final point
        fillPath.lineTo(leftEyeBottomX * widthRatio, leftEyeBottomY * heightRatio); // Draw from final point to the axis ++
        fillPath.lineTo(leftEyeCornerLeftX * widthRatio, leftEyeCornerLeftY * heightRatio); // Same origin point

        fillPath.moveTo(rightEyeCornerLeftX, rightEyeCornerLeftY); // Your origin point
        fillPath.lineTo(rightEyeTopX, rightEyeTopY); // First point
        // Repeat above line for all points on your line graph
        fillPath.lineTo(rightEyeCornerRightX, rightEyeCornerRightY); // Final point
        fillPath.lineTo(rightEyeBottomX, rightEyeBottomY); // Draw from final point to the axis ++
        fillPath.lineTo(rightEyeCornerLeftX, rightEyeCornerLeftY);

        invalidate();
        return true;


    }

    ////////////////////////////// rel position////////////////////////////////////

//    private float getRelativeLeft(View myView) throws JSONException {
//        HashMap<String, Double> eyesMap=new HashMap<String,Double>();
//        eyesMap=ce.findEyes();
//        float leftEyeCenterX = Float.valueOf(eyesMap.get("leftEyeCenterX").toString());
//
//        if (myView.getParent() == myView.getRootView())
//            return leftEyeCenterX + myView.getLeft();
//        else
//            return  myView.getLeft() + getRelativeLeft((View) myView.getParent());
//    }
//
//    private float getRelativeTop(View myView) throws JSONException {
//        HashMap<String, Double> eyesMap=new HashMap<String,Double>();
//        eyesMap=ce.findEyes();
//        float leftEyeCenterY = Float.valueOf(eyesMap.get("leftEyeCenterY").toString());
//        if (myView.getParent() == myView.getRootView())
//            return leftEyeCenterY + myView.getTop();
//        else
//            return myView.getTop() + getRelativeTop((View) myView.getParent());
//    }
    //////////////////////////////////////////////////////////////////////////////

    // override onDraw
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//       // Drawable d = R.drawable.kairos;
//        Drawable d = getResources().getDrawable(drawable.kairos);
//        Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
//        int color = bitmap.getPixel(766, 1122);

        Paint paint=new Paint();
        paint.setColor(Color.argb(100,245,255,250));
        //paint.setAlpha(175);
        paint.setStyle(Paint.Style.FILL);

       // paint.setMaskFilter(new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL));

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
        canvas.drawPath(fillPath, paint);
    }



    public void clearCanvas() {

        invalidate();
    }

}



