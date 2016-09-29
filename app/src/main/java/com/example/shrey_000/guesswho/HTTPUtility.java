package com.example.shrey_000.guesswho;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
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
    private HttpURLConnection connectionDB=null;
    private HttpURLConnection connectionAPI = null;

    public HTTPUtility(CanvasView canvasView, View view){
        this.canvasView = canvasView;
        this.view=view;

    }


    protected JSONObject doInBackground(Void... nothing) {
        String responseStr = null;
        try {
           // responseStr = executePost();
            int numRows=getNumRowsFromDB();
            RandomGenerator rg=new RandomGenerator(numRows);
            int rowIndex = rg.getRandomPhotoIndex();
            String base64 = getContactDetails(rowIndex);
            convertBase64ToDrawable(base64);
            responseStr = getKairosResponse(base64);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
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
    }



    public JSONObject JSONProcessing(String responseText) throws JSONException {

        JSONObject jobj = new JSONObject(responseText);
        Log.d("JSON Object Type", ""+(jobj instanceof JSONObject));
        Log.d("JSON Object", ""+jobj);
        return jobj;

      }




    ////////////////////////////////// creating functions//////////////////////////////////////////


    public int getNumRowsFromDB() throws IOException, JSONException {
        HttpURLConnection connectionDB=null;


        URL urlGetNumRows = new URL("http://192.168.0.15/CZ3002/android_connect/get_num_rows.php");
        connectionDB = (HttpURLConnection) urlGetNumRows.openConnection();
        connectionDB.setRequestMethod("GET");

        connectionDB.setUseCaches(false);
        connectionDB.setDoOutput(true);
        connectionDB.connect();

        //Get Response
        InputStream is = connectionDB.getInputStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
        String line;
        while ((line = rd.readLine()) != null) {
            response.append(line);
            response.append('\r');
        }
        rd.close();

        String responseNumRows = response.toString(); //converting resp2 to string
        JSONObject numRowsObj = JSONProcessing(responseNumRows);
        String numRowsStr = numRowsObj.getString("numRows");

        return Integer.parseInt(numRowsStr);

    }



    public String getContactDetails(int contactID) throws IOException, JSONException {
        String urlParam = "{\"username\":\"gupta\"}";

        URL url = new URL("http://192.168.0.15/CZ3002/android_connect/get_contact_details.php?contactID=" + contactID);
        connectionDB = (HttpURLConnection) url.openConnection();
        connectionDB.setRequestMethod("GET");

        connectionDB.setUseCaches(false);
        connectionDB.setDoOutput(true);
        connectionDB.connect();

        //Get Response
        InputStream is = connectionDB.getInputStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
        String line;
        while ((line = rd.readLine()) != null) {
            response.append(line);
            response.append('\r');
        }
        rd.close();

        Log.d("HTTP GET RESPONSE", response.toString());

        String responseContactDetails = response.toString(); //converting resp2 to string

        JSONObject respObj = JSONProcessing(responseContactDetails);
        String base64 = respObj.getString("base64");

        return base64;
    }


    public void convertBase64ToDrawable(String base64)
    {
        Bitmap bm;
        byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
        bm = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        bd = new BitmapDrawable(null,bm);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object

    }

    public String getKairosResponse(String base64)
    {
        String urlParam = "{\"image\":\"" + base64 + "\",\"selector\":\"SETPOSE\"\r\n}";
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

    }
