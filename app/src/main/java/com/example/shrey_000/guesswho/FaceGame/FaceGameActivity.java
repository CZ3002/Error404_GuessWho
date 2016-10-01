package com.example.shrey_000.guesswho.FaceGame;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Path;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.shrey_000.guesswho.R;

import org.json.JSONException;
import org.json.JSONObject;

import Utilities.CoordinateExtractor;
import Utilities.HTTPUtility;
import Utilities.ScoreCalculatorFaceGame;


public class FaceGameActivity extends AppCompatActivity{
    private CanvasView cv;
    private CoordinateExtractor ce;
    private ScoreCalculatorFaceGame scoreCalc;
    String correctAns = new String();
//    private Drawable drawable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_game);

        findViewById(R.id.options).setVisibility(View.INVISIBLE);
        View view = findViewById(R.id.imageView);

        Intent intent = getIntent();
        int scoreTotal = intent.getIntExtra("score",0);
        scoreCalc = new ScoreCalculatorFaceGame();
        scoreCalc.setScoreTotal(scoreTotal);

        // Update the score
        updateScoreView(scoreCalc.getScoreTotal());

        cv = (CanvasView) findViewById(R.id.canvas);
        RadioGroup options = (RadioGroup)findViewById(R.id.options);

        HTTPUtility poster = new HTTPUtility(cv,view,options,this);
        poster.execute();
    }

    public void setCorrectAns(String correctAns){
        this.correctAns = correctAns;
    }


    public void clearCanvas(View v) {
        cv.clearCanvas();
    }




    public void getMaps(JSONObject responseObj) throws JSONException {
        ce = new CoordinateExtractor(responseObj);
        ce.getLips();
        ce.findEyes();
        ce.getNose();
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

    public void revealEyes(View view)
    {
        Path eyesPath=cv.getEyesPath();
        if(!eyesPath.isEmpty())
            scoreCalc.incrementFeaturesRevealed();
        eyesPath.reset();
        cv.invalidate();
    }
    public void revealNose(View view)
    {

        Path nosePath=cv.getNosePath();
        if(!nosePath.isEmpty())
            scoreCalc.incrementFeaturesRevealed();
        nosePath.reset();
        cv.invalidate();
    }
    public void revealLips(View view)
    {
        Path lipsPath=cv.getLipsPath();
        if(!lipsPath.isEmpty())
            scoreCalc.incrementFeaturesRevealed();
        lipsPath.reset();
        cv.invalidate();
    }

    public void goToNext(View view){
        RadioGroup rg = (RadioGroup)findViewById(R.id.options);
        String selectedAnswer = ((RadioButton)findViewById(rg.getCheckedRadioButtonId())).getText().toString();
        checkAnswer(selectedAnswer);

    }

//    public void goToMain(View view){
//        RadioGroup rg = (RadioGroup)findViewById(R.id.options);
//        String selectedAnswer = ((RadioButton)findViewById(rg.getCheckedRadioButtonId())).getText().toString();
//        checkAnswer(selectedAnswer);
//        Intent intentMain = new Intent(this,HomeActivity.class);
//        startActivity(intentMain);
//    }

    private void checkAnswer(String selectedAnswer) {
        String message;
        if(correctAns.equals(selectedAnswer)) {
            message = "Correct Answer.";
            scoreCalc.resetForNextQuestion(true);
        }
        else {
            message = "Wrong Answer. Correct Answer is " + correctAns;
            scoreCalc.resetForNextQuestion(false);
        }
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which){
                Intent intent = new Intent(getApplicationContext(),FaceGameActivity.class);
                intent.putExtra("score",scoreCalc.getScoreTotal());
                startActivity(intent);
            }
        });
        alertDialog.setTitle("Result");
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);
        alertDialog.create();
        alertDialog.show();
    }

    private void updateScoreView(int newScore) {
        TextView scoreView = (TextView) findViewById(R.id.scoreView);
        String scoreText = "Score : " + newScore;
        scoreView.setText(scoreText);
    }

    @Override
    public void onBackPressed(){
    }
}
