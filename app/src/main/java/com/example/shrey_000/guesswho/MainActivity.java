package com.example.shrey_000.guesswho;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HTTPUtility poster = new HTTPUtility();
        poster.execute();
          cv = (CanvasView) findViewById(R.id.canvas);

    }


    public void clearCanvas(View v) {
        cv.clearCanvas();
    }




    public static void getMaps(JSONObject responseObj) throws JSONException {
        CoordinateExtractor ce = new CoordinateExtractor(responseObj);
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


}
