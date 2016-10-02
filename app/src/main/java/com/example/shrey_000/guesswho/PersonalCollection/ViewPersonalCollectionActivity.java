package com.example.shrey_000.guesswho.PersonalCollection;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.shrey_000.guesswho.R;

import java.util.ArrayList;

import Entities.Acquaintance;
import Utilities.DataStoreFactory;
import Utilities.DataStoreManager;

public class ViewPersonalCollectionActivity extends AppCompatActivity {

    //TODO intent se get username

    TableLayout tableLayout;
    float scale;

    DataStoreManager dataStoreManager = DataStoreFactory.createDataStoreManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_personal_collection);
        tableLayout = (TableLayout) findViewById(R.id.tableContainer);
        scale = getResources().getDisplayMetrics().density;
        updateViewPersonalCollection("gupta");
    }

    private void updateViewPersonalCollection(String userName) {
        final ArrayList<Acquaintance> records = dataStoreManager.getAllAcquaintance(userName);
        int i = 0;
        for(i = 0; i < records.size(); i++) {
            final int val = i;
            if( i % 2 == 0) {
                TableRow tableRow = new TableRow(this);
                tableLayout.addView(tableRow);
                tableRow.setId(i+1);
                TableLayout tableLayout1 = new TableLayout(this);
                tableRow.addView(tableLayout1);
                tableLayout1.setBackgroundResource(R.drawable.border);
                tableLayout1.getLayoutParams().width = (int) (180 * scale + 0.5f);
                tableLayout1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(),PersonalCollectionActivity.class);
                        intent.putExtra("acquaintance", records.get(val));
                        startActivity(intent);
                    }
                });
                TableRow tableRow1 = new TableRow(this);
                tableLayout1.addView(tableRow1);
                tableRow1.setGravity(Gravity.CENTER);
                ImageView imageView = new ImageView(this);
                tableRow1.addView(imageView);
                imageView.setImageDrawable(convertBase64ToDrawable(records.get(i).getBase64()));
                imageView.getLayoutParams().width = (int) (100 * scale + 0.5f);
                imageView.getLayoutParams().height = (int) (160 * scale + 0.5f);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(0,(int) (10 * scale + 0.5f),0,0);
                TableRow tableRow2 = new TableRow(this);
                tableLayout1.addView(tableRow2);
                tableRow2.setGravity(Gravity.CENTER);
                TableRow tableRow3 = new TableRow(this);
                tableLayout1.addView(tableRow3);
                tableRow3.setGravity(Gravity.CENTER);
                TextView textView = new TextView(this);
                tableRow2.addView(textView);
                TextView textView1 = new TextView(this);
                tableRow3.addView(textView1);
                textView.setGravity(Gravity.CENTER);
                textView1.setGravity(Gravity.CENTER);
                textView.setText(records.get(i).getAcqName());
                textView1.setText(records.get(i).getRelationship());
                textView.setTextSize(16);
                textView1.setTextSize(12);
                textView.setTypeface(null, Typeface.BOLD);
                textView1.setPadding(0,0,0,(int) (10 * scale + 0.5f));
            }

            else {
                TableRow tableRow = (TableRow) findViewById(i);
                TableLayout tableLayout1 = new TableLayout(this);
                tableRow.addView(tableLayout1);
                tableLayout1.setGravity(Gravity.CENTER);
                tableLayout1.getLayoutParams().width = (int) (180 * scale + 0.5f);
                tableLayout1.setBackgroundResource(R.drawable.border);
                tableLayout1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(),PersonalCollectionActivity.class);
                        intent.putExtra("acquaintance", records.get(val));
                        startActivity(intent);
                    }
                });
                TableRow tableRow1 = new TableRow(this);
                tableLayout1.addView(tableRow1);
                ImageView imageView = new ImageView(this);
                tableRow1.addView(imageView);
                tableRow1.setGravity(Gravity.CENTER);
                imageView.setImageDrawable(convertBase64ToDrawable(records.get(i).getBase64()));
                imageView.getLayoutParams().width = (int) (100 * scale + 0.5f);
                imageView.getLayoutParams().height = (int) (160 * scale + 0.5f);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(0,(int) (10 * scale + 0.5f),0,0);
                TableRow tableRow2 = new TableRow(this);
                tableLayout1.addView(tableRow2);
                tableRow2.setGravity(Gravity.CENTER);
                TableRow tableRow3 = new TableRow(this);
                tableLayout1.addView(tableRow3);
                tableRow3.setGravity(Gravity.CENTER);
                TextView textView = new TextView(this);
                tableRow2.addView(textView);
                TextView textView1 = new TextView(this);
                tableRow3.addView(textView1);
                textView.setGravity(Gravity.CENTER);
                textView1.setGravity(Gravity.CENTER);
                textView.setText(records.get(i).getAcqName());
                textView1.setText(records.get(i).getRelationship());
                textView.setTextSize(16);
                textView1.setTextSize(12);
                textView.setTypeface(null, Typeface.BOLD);
                textView1.setPadding(0,0,0,(int) (10 * scale + 0.5f));
            }
        }
        addAcquaintanceButton(i);
    }

    public void addAcquaintanceButton(int i){
        if(i%2 == 0) {
            TableRow tableRow = new TableRow(this);
            tableLayout.addView(tableRow);
            tableRow.setId(i+1);
            TableLayout tableLayout1 = new TableLayout(this);
            tableRow.addView(tableLayout1);
            tableLayout1.setGravity(Gravity.CENTER);
            tableLayout1.getLayoutParams().width = (int) (180 * scale + 0.5f);
            tableLayout1.getLayoutParams().height = (int) (208 * scale + 0.5f);
            tableLayout1.setBackgroundResource(R.drawable.addpersonalcollection);
            tableLayout1.setScaleX(0 * scale + 0.3f);
            tableLayout1.setScaleY(0 * scale + 0.3f);
            tableLayout1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(),PersonalCollectionActivity.class);
                    startActivity(intent);
                }
            });
        }
        else {
            TableRow tableRow = (TableRow) findViewById(i);
            TableLayout tableLayout1 = new TableLayout(this);
            tableRow.addView(tableLayout1);
            tableLayout1.setGravity(Gravity.CENTER);
            tableLayout1.getLayoutParams().width = (int) (180 * scale + 0.5f);
            tableLayout1.getLayoutParams().height = (int) (208 * scale + 0.5f);
            tableLayout1.setBackgroundResource(R.drawable.addpersonalcollection);
            tableLayout1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(),PersonalCollectionActivity.class);
                    startActivity(intent);
                }
            });
        }

    }

    private BitmapDrawable convertBase64ToDrawable(String base64)
    {
        Bitmap bm;
        byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
        bm = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return new BitmapDrawable(null,bm);
    }
}
