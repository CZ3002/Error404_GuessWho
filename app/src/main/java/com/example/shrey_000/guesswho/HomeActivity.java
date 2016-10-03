package com.example.shrey_000.guesswho;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.shrey_000.guesswho.FaceGame.FaceGameActivity;
import com.example.shrey_000.guesswho.PersonalCollection.PersonalCollectionActivity;
import com.example.shrey_000.guesswho.PersonalCollection.ViewPersonalCollectionActivity;
import com.example.shrey_000.guesswho.VoiceGame.VoiceGameActivity;
//import com.example.shrey_000.guesswho.VoiceGame.VoiceGameActivity;

public class HomeActivity extends AppCompatActivity {

    private String username;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        username = getIntent().getStringExtra("username");
        setContentView(R.layout.activity_home);
    }

    public void onPlayFaceGame(View view) {
        Intent faceGame = new Intent(this,FaceGameActivity.class);
        faceGame.putExtra("username", username);
        startActivity(faceGame);
    }

    public void onPlayVoiceGame(View view) {
        Intent voiceGame = new Intent(this,VoiceGameActivity.class);
        voiceGame.putExtra("username", username);
        startActivity(voiceGame);
    }

    public void onViewPersonalCollection(View view) {
        Intent PCollection = new Intent(this,ViewPersonalCollectionActivity.class);
        PCollection.putExtra("username", username);
        startActivity(PCollection);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_logoutHome) {
            SharedPreferences prefs = getSharedPreferences("CZ3002Prefs", MODE_MULTI_PROCESS);
            SharedPreferences.Editor editor = prefs.edit();
            editor.remove("loginId");
            editor.commit();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                homeIntent.addCategory( Intent.CATEGORY_HOME );
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
            }
        }, 2000);
    }
}
