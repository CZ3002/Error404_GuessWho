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

//import static com.example.shrey_000.guesswho.R.*;

public class CanvasView extends View
{
    Context context;
//    Path fillPath;
    CoordinateExtractor ce;
    View view;
    HashMap<String, Path> pathMap;

    public CanvasView(Context c, AttributeSet attrs) {
        super(c, attrs);
        context = c;
//        fillPath = new Path();
        pathMap = new HashMap<>();

    }

    public void renderShapes(JSONObject responseObj, View view) throws JSONException {
        ce = new CoordinateExtractor(responseObj);
        this.view=view;
        setShapes();
        invalidate();
    }

    public boolean setShapes() throws JSONException {

        if(ce==null)
            return false;

//        HashMap<String,Double> dimensions = ce.getWidthandHeight();
//
//
//        float width = Float.valueOf(dimensions.get("width").toString());
//        float height = Float.valueOf(dimensions.get("height").toString());

//        widthRatio = view.getWidth()/width;
//        heightRatio = view.getHeight()/height;

        setEyes();
        setNose();
        setLips();

        //invalidate();
        return true;


    }


    public HashMap scaleCoordinates(HashMap<String, Double> map, float ratio) throws JSONException {
        HashMap<String,Double> dimensions = ce.getWidthandHeight();

        Double widthKairos = dimensions.get("width");
        Double heightKairos = dimensions.get("height");

        for(String key : map.keySet()){
            double newCoordinate;
            if(key.charAt(key.length()-1) == 'X'){
                newCoordinate = (map.get(key))*ratio + (view.getWidth()/2) - widthKairos*ratio/2;
            }
            else{
                newCoordinate = (map.get(key))*ratio + (view.getHeight()/2) - heightKairos*ratio/2;
            }
            map.put(key,newCoordinate);
        }

        return map;

    }

    public float getRatio() throws JSONException {
        float ratio;

        HashMap<String,Double> dimensions = ce.getWidthandHeight();

        float width = Float.valueOf(dimensions.get("width").toString());
        float height = Float.valueOf(dimensions.get("height").toString());

        float widthRatio = view.getWidth()/width;
        float heightRatio = view.getHeight()/height;

        if(widthRatio > heightRatio) {
            ratio = heightRatio;
        }
        else {
            ratio = widthRatio;
        }

        return ratio;
    }


    public void setEyes() throws JSONException {
        float ratio = getRatio();

        HashMap<String, Double> map=new HashMap<String,Double>();
        map=ce.findEyes();
        HashMap<String,Double> eyesMap=scaleCoordinates(map, ratio);

       // Log.d("eyesMap is",""+eyesMap);



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

        Path eyesPath = new Path();
//
        eyesPath.moveTo(leftEyeCornerLeftX , leftEyeCornerLeftY ); // Your origin point
        eyesPath.lineTo(leftEyeTopX , leftEyeTopY); // First point
        // Repeat above line for all points on your line graph
        eyesPath.lineTo(leftEyeCornerRightX , leftEyeCornerRightY ); // Final point
        eyesPath.lineTo(leftEyeBottomX , leftEyeBottomY ); // Draw from final point to the axis ++
        eyesPath.lineTo(leftEyeCornerLeftX , leftEyeCornerLeftY ); // Same origin point

        eyesPath.moveTo(rightEyeCornerLeftX, rightEyeCornerLeftY); // Your origin point
        eyesPath.lineTo(rightEyeTopX, rightEyeTopY); // First point
        // Repeat above line for all points on your line graph
        eyesPath.lineTo(rightEyeCornerRightX, rightEyeCornerRightY); // Final point
        eyesPath.lineTo(rightEyeBottomX, rightEyeBottomY); // Draw from final point to the axis ++
        eyesPath.lineTo(rightEyeCornerLeftX, rightEyeCornerLeftY);

        pathMap.put("eyesPath", eyesPath);
//
//        Log.d("eyes printed remove now","");
//
//        eyePath.reset();
//        Log.d("eyes removed","");


    }


    public void setNose() throws JSONException {
        float ratio = getRatio();

        HashMap<String, Double> map=new HashMap<String,Double>();
        map=ce.findNose();
        HashMap<String,Double> noseMap=scaleCoordinates(map, ratio);





        //Log.d("eyesMap is",""+eyesMap);

        float noseTipX,noseTipY,noseBtwEyesX,noseBtwEyesY,
        nostrilLeftSideX,nostrilLeftSideY,nostrilRightSideX,nostrilRightSideY,noseBottomX,noseBottomY;
        noseTipX=Float.valueOf(noseMap.get("noseTipX").toString());
        noseTipY=Float.valueOf(noseMap.get("noseTipY").toString());
        noseBtwEyesX=Float.valueOf(noseMap.get("noseBtwEyesX").toString());
        noseBtwEyesY=Float.valueOf(noseMap.get("noseBtwEyesY").toString());

        nostrilLeftSideX=Float.valueOf(noseMap.get("nostrilLeftSideX").toString());
        nostrilLeftSideY=Float.valueOf(noseMap.get("nostrilLeftSideY").toString());
        nostrilRightSideX=Float.valueOf(noseMap.get("nostrilRightSideX").toString());
        nostrilRightSideY=Float.valueOf(noseMap.get("nostrilRightSideY").toString());

        noseBottomX=Float.valueOf(noseMap.get("noseBottomX").toString());
        noseBottomY=Float.valueOf(noseMap.get("noseBottomY").toString());

//
        Path nosePath = new Path();
        nosePath.moveTo(noseBtwEyesX , noseBtwEyesY ); // Your origin point
        nosePath.lineTo(nostrilLeftSideX , nostrilLeftSideY ); // First point
        // Repeat above line for all points on your line graph
        nosePath.lineTo(noseBottomX , noseBottomY ); // Final point
        nosePath.lineTo(nostrilRightSideX , nostrilRightSideY ); // Draw from final point to the axis ++
        nosePath.lineTo(noseBtwEyesX , noseBtwEyesY ); // Same origin point

        pathMap.put("nosePath",nosePath);

    }



    public void setLips() throws JSONException {
        float ratio = getRatio();

        HashMap<String, Double> map=new HashMap<String,Double>();
        map=ce.findLips();
        HashMap<String,Double> lipsMap=scaleCoordinates(map, ratio);


        //Log.d("eyesMap is",""+eyesMap);

        float lipTopX,lipTopY,lipBottomX,lipBottomY,lipCornerLeftX,lipCornerLeftY,lipCornerRightX,lipCornerRightY;
        lipTopX=Float.valueOf(lipsMap.get("lipTopX").toString());
        lipTopY=Float.valueOf(lipsMap.get("lipTopY").toString());
        lipBottomX=Float.valueOf(lipsMap.get("lipBottomX").toString());
        lipBottomY=Float.valueOf(lipsMap.get("lipBottomY").toString());

        lipCornerLeftX=Float.valueOf(lipsMap.get("lipCornerLeftX").toString());
        lipCornerLeftY=Float.valueOf(lipsMap.get("lipCornerLeftY").toString());
        lipCornerRightX=Float.valueOf(lipsMap.get("lipCornerRightX").toString());
        lipCornerRightY=Float.valueOf(lipsMap.get("lipCornerRightY").toString());


//
        Path lipsPath = new Path();

        lipsPath.moveTo(lipTopX , lipTopY ); // Your origin point
        lipsPath.lineTo(lipCornerLeftX , lipCornerLeftY ); // First point
        // Repeat above line for all points on your line graph
        lipsPath.lineTo(lipBottomX , lipBottomY ); // Final point
        lipsPath.lineTo(lipCornerRightX , lipCornerRightY ); // Draw from final point to the axis ++
        lipsPath.lineTo(lipTopX , lipTopY ); // Same origin point

        pathMap.put("lipsPath",lipsPath);

    }




    // override onDraw
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//       // Drawable d = R.drawable.kairos;
//        Drawable d = getResources().getDrawable(drawable.kairos);
//        Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
//        int color = bitmap.getPixel(766, 1122);

        Paint paint=new Paint();
        //paint.setColor(Color.argb(255,245,255,250));
        paint.setColor(Color.BLACK);
        paint.setAlpha(200);
        paint.setStyle(Paint.Style.FILL);

        for(Path path:pathMap.values()){
            canvas.drawPath(path, paint);
        }
    }



    public Path getEyesPath()
    {
        return pathMap.get("eyesPath");
    }

    public Path getNosePath()
    {
        return pathMap.get("nosePath");
    }
    public Path getLipsPath()
    {
        return pathMap.get("lipsPath");
    }


    public void clearCanvas() {

        invalidate();
    }

}



