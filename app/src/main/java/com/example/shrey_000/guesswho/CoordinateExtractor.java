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
    private JSONObject responseText;
    public CoordinateExtractor(JSONObject responseText)
    {
        this.responseText=responseText;
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
