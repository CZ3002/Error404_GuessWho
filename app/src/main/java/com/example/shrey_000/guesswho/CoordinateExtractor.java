package com.example.shrey_000.guesswho;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by New User on 9/5/2016.
 */
public class CoordinateExtractor {


    public JSONObject responseText;

    public CoordinateExtractor(JSONObject responseText)
    {

        this.responseText=responseText;
    }


    public HashMap getEyes() throws JSONException {

        JSONObject imagesObj = (JSONObject) responseText.getJSONArray("images").get(0);
        JSONObject faces = (JSONObject)imagesObj.getJSONArray("faces").get(0);
        HashMap<String,Double> eyesMap=new HashMap<String,Double>();
        String[] keys={"leftEyeCenterX","leftEyeCenterY","rightEyeCenterX","rightEyeCenterY",
                "leftEyeCornerLeftX","leftEyeCornerLeftY","leftEyeCornerRightX","leftEyeCornerRightY",
                "rightEyeCornerLeftX","rightEyeCornerLeftY","rightEyeCornerRightX","rightEyeCornerRightY"
        };

        for (String str : keys)
        {
            Double val=faces.getDouble(str);
            eyesMap.put(str,val);

        }
        //Log.d("eyes map",eyesMap.toString());
        return eyesMap;

    }


    public HashMap getEyeBrows() throws JSONException {

        JSONObject imagesObj = (JSONObject) responseText.getJSONArray("images").get(0);
        JSONObject faces = (JSONObject)imagesObj.getJSONArray("faces").get(0);
        HashMap<String,Double> eyeBrowsMap=new HashMap<String,Double>();
        String[] keys={"leftEyeBrowLeftX","leftEyeBrowLeftY","leftEyeBrowMiddleX","leftEyeBrowMiddleY",
                "leftEyeBrowRightX","leftEyeBrowRightY","rightEyeBrowLeftX","rightEyeBrowLeftY",
                "rightEyeBrowMiddleX","rightEyeBrowMiddleY","rightEyeBrowRightX","rightEyeBrowRightY"
        };

        for (String str : keys)
        {
            Double val=faces.getDouble(str);
            eyeBrowsMap.put(str,val);

        }
       //Log.d("eye brows map",eyeBrowsMap.toString());
        return eyeBrowsMap;

    }



    public HashMap getNose() throws JSONException {

        JSONObject imagesObj = (JSONObject) responseText.getJSONArray("images").get(0);
        JSONObject faces = (JSONObject)imagesObj.getJSONArray("faces").get(0);
        HashMap<String,Double> noseMap=new HashMap<String,Double>();
        String[] keys={"noseTipX","noseTipY","noseBtwEyesX","noseBtwEyesY",
                "nostrilLeftHoleBottomX","nostrilLeftHoleBottomY","nostrilRightHoleBottomX","nostrilRightHoleBottomY",
                "nostrilLeftSideX","nostrilLeftSideY","nostrilRightSideX","nostrilRightSideY"
        };

        for (String str : keys)
        {
            Double val=faces.getDouble(str);
            noseMap.put(str,val);

        }
        //Log.d("nose map: ",noseMap.toString());
        return noseMap;

    }



    public HashMap<String, Double> getLips () throws JSONException {
        HashMap<String, Double> lipsMap = new HashMap<>();
        JSONObject imagesObj = (JSONObject) responseText.getJSONArray("images").get(0);
        JSONObject facesObj = (JSONObject)imagesObj.getJSONArray("faces").get(0);

        String[] keys = {"lipCornerLeftX", "lipCornerLeftY", "lipLineMiddleX", "lipLineMiddleY", "lipCornerRightX", "lipCornerRightY"};

        for(String key : keys) {
            double value = facesObj.getDouble(key);
            lipsMap.put(key, value);
        }

        //Log.d("lipsMap", lipsMap.toString());
        return lipsMap;
    }


    public HashMap<String,Double>  findEyes() throws JSONException {
        HashMap<String,Double> eyesMap=getEyes();
        HashMap<String,Double> eyeBrowsMap=getEyeBrows();

        // need to find top and bottom of eyes
        //  for top: y take avg of two eyebrow sides, x remains same as middle of the eye
        //for bottom: y take dist bw the eye top and eye centre, x remains same as middle of eye

        //right eye top
        Double rightEyeTopX=eyesMap.get("rightEyeCenterX");
        Double rightEyeTopY=(eyeBrowsMap.get("rightEyeBrowRightY")+eyeBrowsMap.get("rightEyeBrowLeftY"))/2;

        //right eye bottom
        Double rightEyeBottomX=eyesMap.get("rightEyeCenterX");
        Double rightEyeBottomY= 2*(eyesMap.get("rightEyeCenterY")) - (rightEyeTopY);

        //left eye top
        Double leftEyeTopX=eyesMap.get("leftEyeCenterX");
        Double leftEyeTopY=(eyeBrowsMap.get("leftEyeBrowRightY")+eyeBrowsMap.get("leftEyeBrowLeftY"))/2;

        //left eye bottom
        Double leftEyeBottomX=eyesMap.get("leftEyeCenterX");
        Double leftEyeBottomY= 2*(eyesMap.get("leftEyeCenterY")) - (leftEyeTopY);

        //add all to hashmap
        eyesMap.put("rightEyeTopX",rightEyeTopX);
        eyesMap.put("rightEyeTopY",rightEyeTopY);
        eyesMap.put("rightEyeBottomX",rightEyeBottomX);
        eyesMap.put("rightEyeBottomY",rightEyeBottomY);
        eyesMap.put("leftEyeTopX",leftEyeTopX);
        eyesMap.put("leftEyeTopY",leftEyeTopY);
        eyesMap.put("leftEyeBottomX",leftEyeBottomX);
        eyesMap.put("leftEyeBottomY",leftEyeBottomY);

       // Log.d("find eyesMap: ",eyesMap.toString());

        return eyesMap;


    }


    public HashMap<String,Double>  findNose() throws JSONException {
        HashMap<String, Double> lipsMap = findLips();
        HashMap<String, Double> noseMap = getNose();

        //Double noseBottomX=(noseMap.get("nostrilLeftSideX")+noseMap.get("nostrilRightSideX"))/2;
        Double noseBottomX=lipsMap.get("lipTopX");
        Double noseBottomY=(lipsMap.get("lipLineMiddleY")+noseMap.get("noseTipY"))/2;
       // Double noseBottomY=(lipsMap.get("lipTopY")+noseMap.get("noseTipY"))/2;

        noseMap.put("noseBottomX",noseBottomX);
        noseMap.put("noseBottomY",noseBottomY);
        return noseMap;
    }


    public HashMap<String,Double>  findLips() throws JSONException {
        HashMap<String, Double> lipsMap = getLips();
        HashMap<String, Double> noseMap = getNose();

        //lip top^M
        Double lipTopX = lipsMap.get("lipLineMiddleX");
        Double lipTopY = (noseMap.get("noseTipY") + lipsMap.get("lipLineMiddleY")) / 2;

        //lip bottom^M
        Double lipBottomX = lipsMap.get("lipLineMiddleX");
        Double lipBottomY = 2 * (lipsMap.get("lipLineMiddleY")) - (lipTopY);

        lipsMap.put("lipTopX", lipTopX);
        lipsMap.put("lipTopY", lipTopY);
        lipsMap.put("lipBottomX", lipBottomX);
        lipsMap.put("lipBottomY", lipBottomY);

        return lipsMap;
    }

    public HashMap getWidthandHeight() throws JSONException {

        JSONObject imagesObj = (JSONObject) responseText.getJSONArray("images").get(0);

        HashMap<String,Double> dimensionsMap=new HashMap<String,Double>();

        Double width = imagesObj.getDouble("width");
        Double height = imagesObj.getDouble("height");
        dimensionsMap.put("width",width);
        dimensionsMap.put("height",height);

        //Log.d("eyes map",eyesMap.toString());
        return dimensionsMap;

    }







}
