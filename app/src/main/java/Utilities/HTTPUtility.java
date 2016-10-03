package Utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.example.shrey_000.guesswho.FaceGame.CanvasView;
import com.example.shrey_000.guesswho.FaceGame.FaceGameActivity;
import com.example.shrey_000.guesswho.R;

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

    Context context;

    LinkedHashSet<String> optionsList;
    String name;
    String username;

    private BitmapDrawable bd;
    private HttpURLConnection connectionDB=null;
    private HttpURLConnection connectionAPI = null;
    private DataStoreManager dbm;

    public HTTPUtility(String username, CanvasView canvasView, View view, Context context){
        this.username = username;
        this.canvasView = canvasView;
        this.view=view;
        optionsList = new LinkedHashSet<>();

        this.context = context;

        dbm = DataStoreFactory.createDataStoreManager();
    }


    protected JSONObject doInBackground(Void... nothing) {
        String responseStr = null;
        try {
            ArrayList<HashMap<String, String>> contacts = dbm.getPhotos(username);
//            Log.d("bansal", "" + contacts.size());
            RandomGenerator rg = new RandomGenerator(contacts.size());
            int rowIndex = rg.getRandomPhotoIndex();
            name = contacts.get(rowIndex).get("acqName");
            String base64 = contacts.get(rowIndex).get("base64");

            Log.d("correct ans",name);

            generateRandomOptions(name,rg,contacts);

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

//            options.setVisibility(View.VISIBLE);

            Object[] optionsArray = optionsList.toArray();
            RandomGenerator rg = new RandomGenerator(4);
            String[] shuffledOptions = rg.randomizeOptionOrder(optionsArray);

            Button choice1 = (Button)((FaceGameActivity)context).getWindow().getDecorView().findViewById(R.id.choice1);
            Button choice2 = (Button)((FaceGameActivity)context).getWindow().getDecorView().findViewById(R.id.choice2);
            Button choice3 = (Button)((FaceGameActivity)context).getWindow().getDecorView().findViewById(R.id.choice3);
            Button choice4 = (Button)((FaceGameActivity)context).getWindow().getDecorView().findViewById(R.id.choice4);
            Button[] choices = new Button[]{choice1,choice2,choice3,choice4};

            for(int i = 0;i < 4;i++){
                choices[i].setVisibility(View.VISIBLE);
                choices[i].setText(shuffledOptions[i]);
            }

            ((FaceGameActivity)context).setCorrectAns(name);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void generateRandomOptions(String name, RandomGenerator rg, ArrayList<HashMap<String, String>> contacts){
        optionsList.add(name);

        while(optionsList.size() < contacts.size()){
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


    //getting from database
    public void convertBase64ToDrawable(String base64)
    {
        Bitmap bm;
        byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
        bm = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        bd = new BitmapDrawable(null,bm);
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
