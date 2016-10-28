package com.example.shrey_000.guesswho;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import Utilities.DataStoreFactory;
import Utilities.DataStoreManager;

public class ScoreActivity extends AppCompatActivity {
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        username = getIntent().getStringExtra("username");
        String game = getIntent().getStringExtra("game");

        DataStoreManager dbm = DataStoreFactory.createDataStoreManager();
        createScoreView(dbm.getScores(username,game));
    }

    private void createScoreView(HashMap<String,String> scoreList) {
        TableLayout tableLayout = (TableLayout) findViewById(R.id.scoreContainer);
        Log.d("Size", "" + scoreList.size());
        for(String key : scoreList.keySet()) {
            String date = key.split(" ")[0];
            String time = key.split(" ")[1];
            String score = scoreList.get(key);
            TableRow tableRow = new TableRow(this);
            TextView dateView = new TextView(this);
            TextView timeView = new TextView(this);
            TextView scoreView = new TextView(this);
            tableLayout.addView(tableRow);
            tableRow.addView(dateView);
            tableRow.addView(timeView);
            tableRow.addView(scoreView);
            tableRow.setGravity(Gravity.CENTER);
            dateView.setText(date);
            dateView.setTextSize(20);
            dateView.setPadding(15,15,15,15);
            timeView.setText(time);
            timeView.setTextSize(20);
            timeView.setPadding(15,15,15,15);
            scoreView.setText(score);
            scoreView.setTextSize(20);
            scoreView.setPadding(15,15,15,15);
        }
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
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            intent.putExtra("username",username);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
    }
}
