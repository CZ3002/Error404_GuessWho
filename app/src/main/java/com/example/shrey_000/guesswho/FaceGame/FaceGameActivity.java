package com.example.shrey_000.guesswho.FaceGame;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Path;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shrey_000.guesswho.HomeActivity;
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
    private String username;
    private String correctAns = new String();
    private String selectedAns = new String();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_game);

        username = getIntent().getStringExtra("username");

        Log.d("username",username);

        findViewById(R.id.choice1).setVisibility(View.INVISIBLE);
        findViewById(R.id.choice2).setVisibility(View.INVISIBLE);
        findViewById(R.id.choice3).setVisibility(View.INVISIBLE);
        findViewById(R.id.choice4).setVisibility(View.INVISIBLE);

        View view = findViewById(R.id.imageView);

        Intent intent = getIntent();
        int scoreTotal = intent.getIntExtra("score",0);
        scoreCalc = new ScoreCalculatorFaceGame();
        scoreCalc.setScoreTotal(scoreTotal);

        // Update the score
        updateScoreView(scoreCalc.getScoreTotal());

        cv = (CanvasView) findViewById(R.id.canvas);

        HTTPUtility poster = new HTTPUtility(username,cv,view,this);
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
        if (id == R.id.action_back) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which){
                    Toast.makeText(getApplicationContext(), "Total Score for this Session: " + scoreCalc.getScoreTotal(), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    intent.putExtra("username",username);
                    startActivity(intent);
                }
            });
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){

                };
            });
            alertDialog.setTitle("Confirm Exit?");
            alertDialog.setMessage("Are you sure you want to exit?");
            alertDialog.setCancelable(true);
            alertDialog.create();
            alertDialog.show();
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

    public void onChoice1(View view){
        selectedAns = ((Button)view).getText().toString();
        view.setBackgroundColor(Color.YELLOW);
        ((Button)findViewById(R.id.choice2)).setBackgroundResource(android.R.drawable.btn_default);
        ((Button)findViewById(R.id.choice3)).setBackgroundResource(android.R.drawable.btn_default);
        ((Button)findViewById(R.id.choice4)).setBackgroundResource(android.R.drawable.btn_default);
    }

    public void onChoice2(View view){
        selectedAns = ((Button)view).getText().toString();
        view.setBackgroundColor(Color.YELLOW);
        ((Button)findViewById(R.id.choice1)).setBackgroundResource(android.R.drawable.btn_default);
        ((Button)findViewById(R.id.choice3)).setBackgroundResource(android.R.drawable.btn_default);
        ((Button)findViewById(R.id.choice4)).setBackgroundResource(android.R.drawable.btn_default);
    }

    public void onChoice3(View view){
        selectedAns = ((Button)view).getText().toString();
        view.setBackgroundColor(Color.YELLOW);
        ((Button)findViewById(R.id.choice1)).setBackgroundResource(android.R.drawable.btn_default);
        ((Button)findViewById(R.id.choice2)).setBackgroundResource(android.R.drawable.btn_default);
        ((Button)findViewById(R.id.choice4)).setBackgroundResource(android.R.drawable.btn_default);
    }

    public void onChoice4(View view){
        selectedAns = ((Button)view).getText().toString();
        view.setBackgroundColor(Color.YELLOW);
        ((Button)findViewById(R.id.choice1)).setBackgroundResource(android.R.drawable.btn_default);
        ((Button)findViewById(R.id.choice2)).setBackgroundResource(android.R.drawable.btn_default);
        ((Button)findViewById(R.id.choice3)).setBackgroundResource(android.R.drawable.btn_default);
    }

    public void goToNext(View view){
        checkAnswer();
    }

//    public void goToView(View view){
//        RadioGroup rg = (RadioGroup)findViewById(R.id.options);
//        String selectedAnswer = ((RadioButton)findViewById(rg.getCheckedRadioButtonId())).getText().toString();
//        checkAnswer(selectedAnswer);
//        Intent intentMain = new Intent(this,HomeActivity.class);
//        startActivity(intentMain);
//    }

    private void checkAnswer() {
        String message;
        if(correctAns.equals(selectedAns)) {
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
                intent.putExtra("username",username);
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
