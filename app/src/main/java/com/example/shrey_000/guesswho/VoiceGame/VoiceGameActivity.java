package com.example.shrey_000.guesswho.VoiceGame;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.shrey_000.guesswho.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import Utilities.DataStoreFactory;
import Utilities.DataStoreManager;
import Utilities.RandomGenerator;
import Utilities.ScoreCalculatorFaceGame;


public class VoiceGameActivity extends AppCompatActivity{
    private String username;
    private HashMap<String, HashMap<String, ArrayList<String>>> availableFiles = null;
    private String correctAns = "";
    private int selectedID = 0;
    private int correctID = 0;
    private RandomGenerator rg;
    private Random random;
    private boolean playing;
    private DataStoreManager dataStoreManager = DataStoreFactory.createDataStoreManager();
    private MediaPlayer player;
    private Handler mHandler;
    private int playedNum;
    private int scoreTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_game);

        mHandler = new Handler();
        playedNum = 0;

        username = getIntent().getStringExtra("username");
        availableFiles = (HashMap<String, HashMap<String, ArrayList<String>>>) getIntent().getSerializableExtra("availableFiles");
        scoreTotal = getIntent().getIntExtra("score",0);

        displayScore(scoreTotal);

        random = new Random();

        if(availableFiles == null){
            availableFiles = dataStoreManager.getVoice(username);
        }

        rg =  new RandomGenerator(4);

        final HashMap<String, String> questionAndAnswer = getQuestionAndAnswer(availableFiles);
        String[] options = rg.randomizeOptionOrder(availableFiles.keySet().toArray());

        correctAns = questionAndAnswer.get("answer");

        options[random.nextInt(3)] = correctAns;

        // random button options
        ((Button)findViewById(R.id.choice1Voice)).setText(options[0]);
        ((Button)findViewById(R.id.choice2Voice)).setText(options[1]);
        ((Button)findViewById(R.id.choice3Voice)).setText(options[2]);
        ((Button)findViewById(R.id.choice4Voice)).setText(options[3]);

        correctID = ((Button)findViewById(R.id.choice1Voice)).getText().equals(correctAns) ? R.id.choice1Voice :
                ((Button)findViewById(R.id.choice2Voice)).getText().equals(correctAns) ? R.id.choice2Voice :
                        ((Button)findViewById(R.id.choice3Voice)).getText().equals(correctAns) ? R.id.choice3Voice :
                                R.id.choice4Voice;

//        findViewById(R.id.choice1).setVisibility(View.INVISIBLE);
//        findViewById(R.id.choice2).setVisibility(View.INVISIBLE);
//        findViewById(R.id.choice3).setVisibility(View.INVISIBLE);
//        findViewById(R.id.choice4).setVisibility(View.INVISIBLE);

        final ImageButton playButton = (ImageButton) findViewById(R.id.buttonPlay);
        playing = false;

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int startTime = (int)(Double.parseDouble(questionAndAnswer.get("timing").split("-")[0])*1000);
                int endTime = (int)(Double.parseDouble(questionAndAnswer.get("timing").split("-")[1])*1000);
                if(!playing){
                    try {
                        player = new MediaPlayer();
                        player.setDataSource(Environment.getExternalStorageDirectory().getAbsolutePath() + "/GuessWho/recording/" + questionAndAnswer.get("filename") + ".wav");
                        player.seekTo(startTime);
                        player.prepare();
                        player.start();
                        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                playButton.performClick();
                            }
                        });

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else{
                    final Runnable mStopAction = new Runnable() {
                        @Override
                        public void run() {
                            player.stop();
                        }
                    };
                    mHandler.postDelayed(mStopAction, endTime - startTime);
                }
                setPlayButton(playButton.getId(), playing);
                playing = !playing;
                playedNum++;
            }
        });
    }

    public void setPlayButton(final int buttonId, final boolean playing) {
        final Runnable runnableUi = new Runnable(){
            @Override
            public void run() {
                ImageButton button = (ImageButton) findViewById(buttonId);
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

    public HashMap<String, String> getQuestionAndAnswer(HashMap<String, HashMap<String, ArrayList<String>>> dataSet){
        HashMap<String, String> output = new HashMap<>();
        int total = dataSet.size();
        int num = random.nextInt(total);
        int ques = random.nextInt(5);
        int i = 0;

        for(String name : dataSet.keySet()){
            if(i++ == num){
                String fileName = (String) dataSet.get(name).keySet().toArray()[0];
                output.put("answer", name);
                output.put("filename", fileName);
                output.put("timing", dataSet.get(name).get(fileName).get(ques));
                break;
            }
        }
        return output;
    }

    public void setCorrectAns(String correctAns){
        this.correctAns = correctAns;
    }

    public void onChoice1(View view){
        selectedID = R.id.choice1;
        view.setBackgroundColor(Color.DKGRAY);
        findViewById(R.id.choice2).setBackgroundColor(Color.GRAY);
        findViewById(R.id.choice3).setBackgroundColor(Color.GRAY);
        findViewById(R.id.choice4).setBackgroundColor(Color.GRAY);
    }

    public void onChoice2(View view){
        selectedID = R.id.choice2;
        view.setBackgroundColor(Color.DKGRAY);
        findViewById(R.id.choice1).setBackgroundColor(Color.GRAY);
        findViewById(R.id.choice3).setBackgroundColor(Color.GRAY);
        findViewById(R.id.choice4).setBackgroundColor(Color.GRAY);
    }

    public void onChoice3(View view){
        selectedID = R.id.choice3;
        view.setBackgroundColor(Color.DKGRAY);
        findViewById(R.id.choice1).setBackgroundColor(Color.GRAY);
        findViewById(R.id.choice2).setBackgroundColor(Color.GRAY);
        findViewById(R.id.choice4).setBackgroundColor(Color.GRAY);
    }

    public void onChoice4(View view){
        selectedID = R.id.choice4;
        view.setBackgroundColor(Color.DKGRAY);
        findViewById(R.id.choice1).setBackgroundColor(Color.GRAY);
        findViewById(R.id.choice2).setBackgroundColor(Color.GRAY);
        findViewById(R.id.choice3).setBackgroundColor(Color.GRAY);
    }

    public void goToNext(View view) throws InterruptedException {
        checkAnswer();
        Thread.sleep(5000);
        Intent intent = new Intent(this, VoiceGameActivity.class);
        intent.putExtra("username", username);
        intent.putExtra("availableFiles", availableFiles);
        scoreTotal += (int) 100/playedNum;
        intent.putExtra("score", scoreTotal);
    }

//    public void goToView(View view){
//        RadioGroup rg = (RadioGroup)findViewById(R.id.options);
//        String selectedAnswer = ((RadioButton)findViewById(rg.getCheckedRadioButtonId())).getText().toString();
//        checkAnswer(selectedAnswer);
//        Intent intentMain = new Intent(this,HomeActivity.class);
//        startActivity(intentMain);
//    }

    private void checkAnswer() {
        if(correctID == selectedID) {
            findViewById(selectedID).setBackgroundColor(Color.GREEN);
            ((Button)findViewById(selectedID)).setTextColor(Color.WHITE);
        }
        else {
            findViewById(selectedID).setBackgroundColor(Color.RED);
            ((Button)findViewById(selectedID)).setTextColor(Color.WHITE);
            findViewById(correctID).setBackgroundColor(Color.GREEN);
            ((Button)findViewById(correctID)).setTextColor(Color.WHITE);
            final Animation animation = new AlphaAnimation(1.0f, 0.5f); // Change alpha from fully visible to invisible
            animation.setDuration(800); // duration - half a second
            animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
            animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
            animation.setRepeatMode(Animation.REVERSE);
            findViewById(correctID).startAnimation(animation);
        }
    }

    private void displayScore(int newScore) {
        TextView scoreView = (TextView) findViewById(R.id.scoreView);
        String scoreText = "Score : " + newScore;
        scoreView.setText(scoreText);
    }

    @Override
    public void onBackPressed(){
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
