package com.example.shrey_000.guesswho.PersonalCollection;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.TreeMap;

import android.os.AsyncTask;

import android.os.Handler;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

// IBM Watson SDK
import com.example.shrey_000.guesswho.R;
import com.ibm.watson.developer_cloud.android.speech_to_text.v1.dto.SpeechConfiguration;
import com.ibm.watson.developer_cloud.android.speech_to_text.v1.ISpeechDelegate;
import com.ibm.watson.developer_cloud.android.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.android.speech_common.v1.TokenProvider;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Entities.Acquaintance;
import Utilities.DataStoreFactory;
import Utilities.DataStoreManager;

public class PersonalCollectionActivity extends AppCompatActivity {

    private String username;
    private static Acquaintance acquaintance = null;
    private static MediaPlayer player = null;

    ImageView ivAvatar;

    private static String fileName = "";
    private static TreeMap<String, Double[]> timeStampValues = new TreeMap<>();

    private DataStoreManager dataStoreManager = DataStoreFactory.createDataStoreManager();
    private android.support.v4.app.FragmentManager fragmentManager;

    FragmentRecordRecording fragmentRecordRecording = new FragmentRecordRecording();
    FragmentListenRecording fragmentListenRecording = new FragmentListenRecording();

    public static class FragmentRecordRecording extends Fragment implements ISpeechDelegate {

        private static final String STRING_ASKED = "I Suppose You Know Me. Let Us See If You Can Remember. " +
                "Will you join us at dinner. Have a good night. See You in the Morning";
        private static final String[] REQUIRED_VALUES = new String[]{"i", "me", "let", "remember", "will", "dinner", "have", "night", "see", "morning"};


        private enum ConnectionState {
            IDLE, CONNECTING, CONNECTED
        }

        ConnectionState mState = ConnectionState.IDLE;
        public View mView = null;
        public Context mContext = null;
        public JSONObject jsonModels = null;
        private Handler mHandler = null;

        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            mView = inflater.inflate(R.layout.record_fragment, container, false);
            mContext = getActivity().getApplicationContext();
            mHandler = new Handler();

            if (!initSTT()) {
                return mView;
            }

            if (jsonModels == null) {
                jsonModels = new STTCommands().doInBackground();
                if (jsonModels == null) {
                    return mView;
                }
            }

            ImageButton buttonRecord = (ImageButton)mView.findViewById(R.id.buttonRecord);
            buttonRecord.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {

                    if (mState == ConnectionState.IDLE) {
                        mState = ConnectionState.CONNECTING;

                        fileName += System.currentTimeMillis()/10000;

                        SpeechToText.sharedInstance().setModel(getString(R.string.modelDefault));
                        // start recognition
                        new AsyncTask<Void, Void, Void>(){
                            @Override
                            protected Void doInBackground(Void... none) {
                                SpeechToText.sharedInstance().recognize();
                                return null;
                            }
                        }.execute();
                        setRecordButton(R.id.buttonRecord, "blue");
                    }
                    else if (mState == ConnectionState.CONNECTED) {
                        // end media recorder here

                        mState = ConnectionState.IDLE;
                        SpeechToText.sharedInstance().stopRecognition();
                    }
                }
            });

            return mView;
        }

        public URI getHost(String url){
            try {
                return new URI(url);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            return null;
        }

        // initialize the connection to the Watson STT service
        private boolean initSTT() {

            // DISCLAIMER: please enter your credentials or token factory in the lines below
            String username = "2dc12809-26e8-4805-b4cb-15fa01e90025";
            String password = "eZZboToVDvrp";

            String tokenFactoryURL = getString(R.string.defaultTokenFactory);
            String serviceURL = "wss://stream.watsonplatform.net/speech-to-text/api";

            SpeechConfiguration sConfig = new SpeechConfiguration(SpeechConfiguration.AUDIO_FORMAT_OGGOPUS);
            sConfig.learningOptOut = false; // Change to true to opt-out

            SpeechToText.sharedInstance().initWithContext(this.getHost(serviceURL), getActivity().getApplicationContext(), sConfig);

            // token factory is the preferred authentication method (service credentials are not distributed in the client app)
            if (!tokenFactoryURL.equals(getString(R.string.defaultTokenFactory))) {
                SpeechToText.sharedInstance().setTokenProvider(new MyTokenProvider(tokenFactoryURL));
            }
            // Basic Authentication
            else if (!username.equals(getString(R.string.defaultUsername))) {
                SpeechToText.sharedInstance().setCredentials(username, password);
            } else {
                // no authentication method available
                return false;
            }

            SpeechToText.sharedInstance().setModel(getString(R.string.modelDefault));
            SpeechToText.sharedInstance().setDelegate(this);

            return true;
        }

        /**
         * Change the button's label
         */
        public void setRecordButton(final int buttonId, final String colorVal) {
            final Runnable runnableUi = new Runnable(){
                @Override
                public void run() {
                    ImageButton button = (ImageButton)mView.findViewById(buttonId);
                    if(colorVal.equals("blue")){
                        button.setBackground(getResources().getDrawable(R.drawable.round_button_blue));
                        final Animation animation = new AlphaAnimation(1.0f, 0.5f); // Change alpha from fully visible to invisible
                        animation.setDuration(500); // duration - half a second
                        animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
                        animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
                        animation.setRepeatMode(Animation.REVERSE);
                        button.startAnimation(animation);
                    }
                    else{
                        button.setBackground(getResources().getDrawable(R.drawable.round_button));
                        button.clearAnimation();
                    }
                }
            };
            new Thread(){
                public void run(){
                    mHandler.post(runnableUi);
                }
            }.start();
        }

        // delegages ----------------------------------------------

        public void onOpen() {
            setRecordButton(R.id.buttonRecord, "blue");
            mState = ConnectionState.CONNECTED;
        }

        public void onError(String error) {
            mState = ConnectionState.IDLE;
        }

        public void onClose(int code, String reason, boolean remote) {
            setRecordButton(R.id.buttonRecord, "red");
            mState = ConnectionState.IDLE;
        }

        public void onMessage(String message) {

            try {
                JSONObject jObj = new JSONObject(message);
                // playing message
                if (jObj.has("results")) {
                    //if has result
                    if(jObj.getJSONArray("results").getJSONObject(0).getBoolean("final")) {
                        JSONArray jArr = jObj.getJSONArray("results").getJSONObject(0).getJSONArray("alternatives");
                        int i = 0;
                        for (i = 0; i < jArr.length(); i++) {
                            if (jArr.getJSONObject(i).getString("transcript").replaceAll(".", " ").replaceAll(" ", "").equalsIgnoreCase(STRING_ASKED.replaceAll(".", "").replaceAll(" ", ""))) {
                                break;
                            }
                        }
                        JSONArray timeStampArray = jArr.getJSONObject(i).optJSONArray("timestamps");
                        for(int j = 0; j < timeStampArray.length(); j ++){
                            String key = timeStampArray.getJSONArray(j).getString(0);
                            Double startTime = timeStampArray.getJSONArray(j).getDouble(1);
                            Double endTime = timeStampArray.getJSONArray(j).getDouble(2);
                            if(Arrays.asList(REQUIRED_VALUES).contains(key.toLowerCase())){
                                timeStampValues.put(key.toLowerCase(), new Double[]{startTime, endTime});
                            }
                        }

                        for(String val : REQUIRED_VALUES){
                            if(!timeStampValues.containsKey(val)){
                                timeStampValues.put(val.toLowerCase(), new Double[]{0.0, 8.0});
                            }
                        }
                        for(String keyVal : timeStampValues.keySet())
                            Log.d("amigos", keyVal + " " + timeStampValues.get(keyVal)[0] + " " + timeStampValues.get(keyVal)[1]);
                    }
                } else {
                    //displayResult("unexpected data coming from stt server: \n" + message);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public void onAmplitude(double amplitude, double volume) {
            //Logger.e(TAG, "amplitude=" + amplitude + ", volume=" + volume);
        }
    }

    public static class FragmentListenRecording extends Fragment{
        public View mView = null;
        public Context mContext = null;
        private Handler mHandler = null;

        boolean playing;

        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            mView = inflater.inflate(R.layout.play_fragment, container, false);
            mContext = getActivity().getApplicationContext();
            mHandler = new Handler();

            final ImageButton playButton = (ImageButton) mView.findViewById(R.id.buttonPlay);
            playing = false;

            playButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!playing){

                        try {
                            if(acquaintance != null) {
                                player = new MediaPlayer();
                                player.setDataSource(Environment.getExternalStorageDirectory().getAbsolutePath() + "/GuessWho/recording/" + acquaintance.getSoundFile() + ".wav");
                                player.prepare();
                                player.start();
                                player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                    @Override
                                    public void onCompletion(MediaPlayer mp) {
                                        playButton.performClick();
                                    }
                                });
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else{
                        player.stop();
                    }
                    setPlayButton(playButton.getId(), playing);
                    playing = !playing;
                }
            });

            return mView;
        }


        public void setPlayButton(final int buttonId, final boolean playing) {
            final Runnable runnableUi = new Runnable(){
                @Override
                public void run() {
                    ImageButton button = (ImageButton)mView.findViewById(buttonId);
                    if(!playing){
                        button.setImageResource(R.drawable.ic_stop);
                    }
                    else{
                        button.setImageResource(R.drawable.ic_play);
                    }
                }
            };
            new Thread(){
                public void run(){
                    mHandler.post(runnableUi);
                }
            }.start();
        }
    }

    public static class STTCommands extends AsyncTask<Void, Void, JSONObject> {

        protected JSONObject doInBackground(Void... none) {

            return SpeechToText.sharedInstance().getModels();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Strictmode needed to run the http/wss request for devices > Gingerbread
        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.GINGERBREAD) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        setContentView(R.layout.activity_personal_collection);

        username = getIntent().getStringExtra("username");
        acquaintance = (Acquaintance) getIntent().getSerializableExtra("acquaintance");

        ivAvatar =(ImageView) findViewById(R.id.iv_avatar);

        fragmentManager = getSupportFragmentManager();

        if(acquaintance == null) {
            ivAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 0);
                }
            });

            fragmentManager.beginTransaction().replace(R.id.fragment_container, fragmentRecordRecording).commit();

        } else{
//            fragmentManager.beginTransaction().replace(R.id.fragment_container, fragmentRecordRecording).commit();
            Button addButton = (Button)findViewById(R.id.addPCButton);
            TextView textView = (TextView) findViewById(R.id.et_defaultText);
            addButton.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);
            setTitle(acquaintance.getAcqName());
            populateData();
            fragmentManager.beginTransaction().replace(R.id.fragment_container, fragmentListenRecording).commit();
        }


        //actionBar.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#B5C0D0")));
    }

    private void populateData(){
        findViewById(R.id.et_name).setEnabled(false);
        findViewById(R.id.et_contact).setEnabled(false);
        findViewById(R.id.et_relationship).setEnabled(false);
        findViewById(R.id.et_note).setEnabled(false);

        Bitmap bp = convertBase64ToBitmap(acquaintance.getBase64());
        ivAvatar.setImageBitmap(bp);
        ((EditText)findViewById(R.id.et_name)).setText(acquaintance.getAcqName());
        ((EditText)findViewById(R.id.et_contact)).setText(acquaintance.getContact());
        ((EditText)findViewById(R.id.et_relationship)).setText(acquaintance.getRelationship());
        ((EditText)findViewById(R.id.et_note)).setText(acquaintance.getNotes());
    }

    public void addToPC(View view){
        Bitmap bm = ((BitmapDrawable) ivAvatar.getDrawable()).getBitmap();
        String name = ((EditText)findViewById(R.id.et_name)).getText().toString();
        String contact = ((EditText)findViewById(R.id.et_contact)).getText().toString();
        String relationship = ((EditText)findViewById(R.id.et_relationship)).getText().toString();
        String note = ((EditText)findViewById(R.id.et_note)).getText().toString();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        String base64 = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);

        Acquaintance acq = new Acquaintance(username, name, relationship, contact, note, base64, fileName,
                timeStampValues.get("i")[0] + "-" + timeStampValues.get("me")[1],
                timeStampValues.get("let")[0] + "-" + timeStampValues.get("remember")[1],
                timeStampValues.get("will")[0] + "-" + timeStampValues.get("dinner")[1],
                timeStampValues.get("have")[0] + "-" + timeStampValues.get("night")[1],
                timeStampValues.get("see")[0] + "-" + timeStampValues.get("morning")[1]);

        dataStoreManager.insertPC(acq);
        Toast.makeText(PersonalCollectionActivity.this, name + " added to Personal Collection", Toast.LENGTH_LONG).show();

        goToView();
    }

    static class MyTokenProvider implements TokenProvider {

        String m_strTokenFactoryURL = null;

        public MyTokenProvider(String strTokenFactoryURL) {
            m_strTokenFactoryURL = strTokenFactoryURL;
        }

        public String getToken() {
            try {
                // DISCLAIMER: the application developer should implement an authentication mechanism from the mobile app to the
                // server side app so the token factory in the server only provides tokens to authenticated clients
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(m_strTokenFactoryURL);
                HttpResponse executed = httpClient.execute(httpGet);
                InputStream is = executed.getEntity().getContent();
                StringWriter writer = new StringWriter();
                IOUtils.copy(is, writer, "UTF-8");
                String strToken = writer.toString();
                return strToken;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    private Bitmap convertBase64ToBitmap(String base64) {
        Bitmap bm;
        byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
        bm = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return bm;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != 0) {
            Bitmap bp = (Bitmap) data.getExtras().get("data");
            ivAvatar.setImageBitmap(bp);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(acquaintance != null){
            getMenuInflater().inflate(R.menu.menu_pc, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete_PC && acquaintance != null) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which){
                    dataStoreManager.deletePC(acquaintance);
                    Toast.makeText(PersonalCollectionActivity.this, acquaintance.getAcqName() + " deleted successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),ViewPersonalCollectionActivity.class);
                    intent.putExtra("username",username);
                    startActivity(intent);
                }
            });
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){

                };
            });
            alertDialog.setTitle("Confirm Delete?");
            alertDialog.setMessage("Are you sure you want to remove " + acquaintance.getAcqName() + " from your personal collection?");
            alertDialog.setCancelable(false);
            alertDialog.create();
            alertDialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void goToView(){
        Intent intent = new Intent(this,ViewPersonalCollectionActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if(player != null && player.isPlaying()){
            player.stop();
        }
        Intent intent = new Intent(this, ViewPersonalCollectionActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
        this.finish();
    }

}
