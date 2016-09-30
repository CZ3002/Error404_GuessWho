package com.example.shrey_000.guesswho;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Set;

/**
 * Created by SHREY_000 on 9/2/2016.
 */
public class HTTPUtility extends AsyncTask<Void, Void, JSONObject> {

    public static final String API_URL = "https://api.kairos.com/detect";
    public static final String APP_ID = "3a4472cc";
    public static final String APP_KEY = "0962a5979fb26c22e46bcdfea31fc4e4";

    public static final  String DBURL = "jdbc:mysql://10.27.201.184:3306/guesswho";
    public static final  String USERNAME = "pma";
    public static final  String PASSWORD = "";

    private CanvasView canvasView;
    private View view;


    RadioGroup options;
    LinkedHashSet<String> optionsList;


    private BitmapDrawable bd;
    private HttpURLConnection connectionDB=null;
    private HttpURLConnection connectionAPI = null;
    private DBManager dbm;

    public HTTPUtility(CanvasView canvasView, View view, RadioGroup options){
        this.canvasView = canvasView;
        this.view=view;
        this.options = options;
        optionsList = new LinkedHashSet<>();

        dbm = new DBManager(DBURL, USERNAME, PASSWORD);
    }


    protected JSONObject doInBackground(Void... nothing) {
        String responseStr = null;
        try {
            ArrayList<HashMap<String, String>> contacts = dbm.getPhotos("gupta");
            RandomGenerator rg = new RandomGenerator(contacts.size());
            int rowIndex = rg.getRandomPhotoIndex();

            String name = contacts.get(rowIndex).get("acqName");
            String base64 = contacts.get(rowIndex).get("base64");

            generateRandomOptions(name,rg,contacts);

//            optionsList.add(name);
//
//            while(optionsList.size() < 4){
//                int wrongOptIndex = rg.getRandomPhotoIndex();
//                String wrongOpt = contacts.get(wrongOptIndex).get("acqName");
//                optionsList.add(wrongOpt);
//            }

            convertBase64ToDrawable(base64);
            responseStr = getKairosResponse(base64);

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

            Object[] optionsArray = optionsList.toArray();
            for(int i = 0;i < 4;i++){
                ((RadioButton)options.getChildAt(i)).setText((String)optionsArray[i]);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void generateRandomOptions(String name, RandomGenerator rg, ArrayList<HashMap<String, String>> contacts){
        optionsList.add(name);

        while(optionsList.size() < 4){
            int wrongOptIndex = rg.getRandomPhotoIndex();
            String wrongOpt = contacts.get(wrongOptIndex).get("acqName");
            optionsList.add(wrongOpt);
        }
    }



    public JSONObject JSONProcessing(String responseText) throws JSONException {

        JSONObject jobj = new JSONObject(responseText);
        Log.d("JSON Object Type", ""+(jobj instanceof JSONObject));
        Log.d("JSON Object", ""+jobj);
        return jobj;
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
            StringBuilder response = new StringBuilder();
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
