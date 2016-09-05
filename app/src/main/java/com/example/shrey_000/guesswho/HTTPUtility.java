package com.example.shrey_000.guesswho;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by SHREY_000 on 9/2/2016.
 */
public class HTTPUtility extends AsyncTask<Void, Void, JSONObject> {

    public static final String API_URL = "https://api.kairos.com/detect";
    public static final String APP_ID = "3a4472cc";
    public static final String APP_KEY = "0962a5979fb26c22e46bcdfea31fc4e4";

    //    public static String executePost()
    protected JSONObject doInBackground(Void... nothing) {
        String responseStr = executePost();
        try {
            return JSONProcessing(responseStr);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }



    public String executePost()
    {
        HttpURLConnection connection = null;
//        String urlParam = "image=http://media.kairos.com/kairos-elizabeth.jpg&selector=SETPOSE";
        String urlParam = "{\n    \"image\":\" http://media.kairos.com/kairos-elizabeth.jpg \",\n    \"selector\":\"SETPOSE\"\r\n}";

        try {
            //Create connection
            URL url = new URL(API_URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");

            connection.setRequestProperty("app_id",APP_ID);
            connection.setRequestProperty("app_key",APP_KEY);

//            connection.setRequestProperty("Content-Length",
//                    Integer.toString(urlParam.getBytes().length));
//            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches(false);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream (
                    connection.getOutputStream());
            wr.writeBytes(urlParam);
            wr.close();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            Log.d("HTTP POST URL", response.toString());
            return response.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public JSONObject JSONProcessing(String responseText) throws JSONException {

        List<Float> valuesList = new ArrayList<Float>();


        JSONObject jobj = new JSONObject(responseText);
        Log.d("JSON Object Type", ""+(jobj instanceof JSONObject));
        Log.d("JSON Object", ""+jobj);
        return jobj;

        // JSON j=new JSON();
//        for(int i=0;i<keysList.size();i++)
//        {
//            key=keysList.get(i);
//            value=json.get()
//        }




      }



    }
