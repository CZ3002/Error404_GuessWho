package com.example.shrey_000.guesswho.PersonalCollection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.shrey_000.guesswho.R;

import java.util.ArrayList;
import java.util.HashMap;

import Entities.Acquaintance;
import Utilities.DataStoreFactory;
import Utilities.DataStoreManager;
import Utilities.HTTPUtility;

public class ViewPersonalCollectionActivity extends AppCompatActivity {

    //TODO intent se get username

    DataStoreManager dataStoreManager = DataStoreFactory.createDataStoreManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_personal_collection);
        updateViewPersonalCollection("gupta");
    }

    private void updateViewPersonalCollection(String userName) {
        ArrayList<Acquaintance> records = dataStoreManager.getAllAcquaintance(userName);
        TableLayout tableLayout = (TableLayout) findViewById(R.id.tableContainer);
        float scale = getResources().getDisplayMetrics().density;
        for(int i = 0; i < records.size(); i++) {
            if( i % 2 == 0) {
                TableRow tableRow = new TableRow(this);
                tableLayout.addView(tableRow);
                tableRow.setId(i+1);
                TableLayout tableLayout1 = new TableLayout(this);
                tableRow.addView(tableLayout1);
                tableLayout1.getLayoutParams().width = (int) (175 * scale + 0.5f);
                tableLayout1.setPadding((int)(10 *scale + 0.5f),0,0,0);
                TableRow tableRow1 = new TableRow(this);
                tableLayout1.addView(tableRow1);
                tableRow1.setGravity(Gravity.CENTER);
                ImageView imageView = new ImageView(this);
                tableRow1.addView(imageView);
                imageView.setImageDrawable(convertBase64ToDrawable(records.get(i).getBase64()));
                imageView.getLayoutParams().width = (int) (100 * scale + 0.5f);
                imageView.getLayoutParams().height = (int) (150 * scale + 0.5f);
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
            }

            else {
                TableRow tableRow = (TableRow) findViewById(i);
                TableLayout tableLayout1 = new TableLayout(this);
                tableRow.addView(tableLayout1);
                tableLayout1.setGravity(Gravity.CENTER);
                tableLayout1.getLayoutParams().width = (int) (160 * scale + 0.5f);
                tableLayout1.setPadding((int)(20 *scale + 0.5f),0,0,0);
                TableRow tableRow1 = new TableRow(this);
                tableLayout1.addView(tableRow1);
                ImageView imageView = new ImageView(this);
                tableRow1.addView(imageView);
                imageView.setImageDrawable(convertBase64ToDrawable(records.get(i).getBase64()));
                imageView.getLayoutParams().width = (int) (100 * scale + 0.5f);
                imageView.getLayoutParams().height = (int) (150 * scale + 0.5f);
                TableRow tableRow2 = new TableRow(this);
                tableLayout1.addView(tableRow2);
                TableRow tableRow3 = new TableRow(this);
                tableLayout1.addView(tableRow3);
                TextView textView = new TextView(this);
                tableRow2.addView(textView);
                TextView textView1 = new TextView(this);
                tableRow3.addView(textView1);
                textView.setText(records.get(i).getAcqName());
                textView1.setText(records.get(i).getRelationship());
            }
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
