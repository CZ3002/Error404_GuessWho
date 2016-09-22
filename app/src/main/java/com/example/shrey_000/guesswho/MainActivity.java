package com.example.shrey_000.guesswho;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


import java.net.URL;


import android.util.*;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.*;
import java.io.*;


public class MainActivity extends AppCompatActivity {
    private CanvasView cv;
    private CoordinateExtractor ce;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View view = findViewById(R.id.imageView);
        cv = (CanvasView) findViewById(R.id.canvas);
        HTTPUtility poster = new HTTPUtility(cv,view);
        poster.execute();


        //cv.clearCanvas();


//        if(view==null) {
//            Log.d("view @main","true");
//        }
//        else{
//            Log.d("view @main","false"); //view is not null !!! yayyy!
//        }
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
        eyesPath.reset();
        cv.invalidate();
    }
    public void revealNose(View view)
    {

        Path nosePath=cv.getNosePath();
        nosePath.reset();
        cv.invalidate();
    }
    public void revealLips(View view)
    {
        Path lipsPath=cv.getLipsPath();
        lipsPath.reset();
        cv.invalidate();
    }


}
