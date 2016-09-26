package com.example.shrey_000.guesswho;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

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

    private CanvasView canvasView;
    private View view;
    private BitmapDrawable bd;

    public HTTPUtility(CanvasView canvasView, View view){
        this.canvasView = canvasView;
        this.view=view;

    }


    protected JSONObject doInBackground(Void... nothing) {
        String responseStr = null;
        try {
            responseStr = executePost();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return JSONProcessing(responseStr);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

    protected void onPostExecute(JSONObject responseObj) {

        try {
            ImageView iv = (ImageView)view;
            iv.setImageDrawable(bd);
            canvasView.renderShapes(responseObj,view);

        } catch (JSONException e) {
            e.printStackTrace();
        }
       // view.invalidate();
    }



    public String executePost() throws IOException {
        HttpURLConnection connectionAPI = null;
        HttpURLConnection connectionDB=null;

        Bitmap bm;

        connectionDB = (HttpURLConnection) new URL("http://10.27.193.98/guesswho/kat.jpg").openConnection();
        connectionDB.connect();
        InputStream input = connectionDB.getInputStream();
        if(input==null)
            Log.d("input null","");

        bm = BitmapFactory.decodeStream(input);



        bd = new BitmapDrawable(null,bm);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);




       String urlParam = "{\"image\":\"" + encodedImage + "\",\"selector\":\"SETPOSE\"\r\n}";
//       String urlParam = "{\n    \"image\":\" http://media.kairos.com/kairos-elizabeth.jpg \",\n    \"selector\":\"SETPOSE\"\r\n}";

        try {
            //Create connectionAPI
            URL url = new URL(API_URL);
            connectionAPI = (HttpURLConnection) url.openConnection();
            connectionAPI.setRequestMethod("POST");
            connectionAPI.setRequestProperty("Content-Type", "application/json");

            connectionAPI.setRequestProperty("app_id",APP_ID);
            connectionAPI.setRequestProperty("app_key",APP_KEY);

            connectionAPI.setRequestProperty("Content-Language", "en-US");

            connectionAPI.setUseCaches(false);
            connectionAPI.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream (
                    connectionAPI.getOutputStream());
            wr.writeBytes(urlParam);
            wr.close();

            //Get Response
            InputStream is = connectionAPI.getInputStream();
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
            if (connectionAPI != null) {
                connectionAPI.disconnect();
            }
        }
    }

    public JSONObject JSONProcessing(String responseText) throws JSONException {

        List<Float> valuesList = new ArrayList<Float>();


        JSONObject jobj = new JSONObject(responseText);
        Log.d("JSON Object Type", ""+(jobj instanceof JSONObject));
        Log.d("JSON Object", ""+jobj);
        return jobj;

      }



    }
