package com.example.shrey_000.guesswho;

import android.util.Log;
import org.json.JSONArray;
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
            //Float val=Float.valueOf(faces.getJSONObject(str).toString());
            Double val=faces.getDouble(str);
            eyesMap.put(str,val);

        }
        Log.d("eyes map",eyesMap.toString());
        return eyesMap;

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
            //Float val=Float.valueOf(faces.getJSONObject(str).toString());
            Double val=faces.getDouble(str);
            noseMap.put(str,val);

        }
        Log.d("nose map: ",noseMap.toString());
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

        Log.d("lipsMap", lipsMap.toString());
        return lipsMap;
    }









}
