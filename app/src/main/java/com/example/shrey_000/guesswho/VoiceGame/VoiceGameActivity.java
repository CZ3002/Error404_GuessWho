package com.example.shrey_000.guesswho.VoiceGame;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.RunnableFuture;

import android.app.FragmentTransaction;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.app.ActionBar;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;

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

public class VoiceGameActivity extends AppCompatActivity {

    FragmentTabSTT fragmentTabSTT = new FragmentTabSTT();
    // Media Recorder Instance
    public static MediaRecorder audioRecorder;
    // Path where the file is stored
    public static String outputFile = null;

    public static class FragmentTabSTT extends Fragment implements ISpeechDelegate {

        private static final String STRING_ASKED = "I Suppose You Know Me. Let Us See If You Can Remember. " +
                "Will you join us at dinner. Have a good night. See You in the Morning";
        private static final String[] REQUIRED_VALUES = new String[]{"i", "me", "let", "remember", "will", "dinner", "have", "night", "see", "morning"};
        private HashMap<String, Double[]> timeStampValues = new HashMap<>();

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

            mView = inflater.inflate(R.layout.tab_stt_fragment, container, false);
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
                        SpeechToText.sharedInstance().setModel(getString(R.string.modelDefault));
                        // start media recorder here
//                            audioRecorder.prepare();
//                            audioRecorder.start();

                        // start recognition
                        new AsyncTask<Void, Void, Void>(){
                            @Override
                            protected Void doInBackground(Void... none) {
                                SpeechToText.sharedInstance().recognize();
                                return null;
                            }
                        }.execute();
                        setButtonLabel(R.id.buttonRecord, "blue");
                    }
                    else if (mState == ConnectionState.CONNECTED) {
                        // end media recorder here

                        mState = ConnectionState.IDLE;
                        SpeechToText.sharedInstance().stopRecognition();
//                        audioRecorder.stop();
//                        audioRecorder.release();
//                        audioRecorder = null;
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
            String username = "4af4482f-0c5a-4402-b2c1-a2185e4180e9";
            String password = "7bhozArWiP7b";

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
        public void setButtonLabel(final int buttonId, final String colorVal) {
            final Runnable runnableUi = new Runnable(){
                @Override
                public void run() {
                    ImageButton button = (ImageButton)mView.findViewById(buttonId);
                    if(colorVal.equals("blue")){
                        final Animation animation = new AlphaAnimation(1.0f, 0.5f); // Change alpha from fully visible to invisible
                        animation.setDuration(500); // duration - half a second
                        animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
                        animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
                        animation.setRepeatMode(Animation.REVERSE);
                        button.startAnimation(animation);
                    }
                    else{
                        button.clearAnimation();
                    }
//                    button.setBackground(getResources().getDrawable(R.drawable.round_button));
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
            setButtonLabel(R.id.buttonRecord, "blue");
            mState = ConnectionState.CONNECTED;
        }

        public void onError(String error) {
            mState = ConnectionState.IDLE;
        }

        public void onClose(int code, String reason, boolean remote) {
            setButtonLabel(R.id.buttonRecord, "red");
            mState = ConnectionState.IDLE;
        }

        public void onMessage(String message) {

            try {
                JSONObject jObj = new JSONObject(message);
                //Log.d("thullu", message.toString());
                // state message
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
                                timeStampValues.put(key, new Double[]{startTime, endTime});
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

        //setContentView(R.layout.activity_home);
        setContentView(R.layout.activity_tab_text);

        // Create a directory GuessWho to store audio recordings
        File mediaStorage = new File(Environment.getExternalStorageDirectory(),"GuessWho");
        if (!mediaStorage.exists()) {
            mediaStorage.mkdirs();
        }
        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/GuessWho/recording.3gp";

        audioRecorder = new MediaRecorder();
        audioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        audioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        audioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        audioRecorder.setOutputFile(outputFile);

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragmentTabSTT).commit();

        //actionBar.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#B5C0D0")));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
