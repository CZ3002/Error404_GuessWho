package com.example.shrey_000.guesswho;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.shrey_000.guesswho.FaceGame.FaceGameActivity;
import com.example.shrey_000.guesswho.PersonalCollection.PersonalCollectionActivity;
import com.example.shrey_000.guesswho.VoiceGame.VoiceGameActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void onPlayFaceGame(View view) {
        Intent faceGame = new Intent(this,FaceGameActivity.class);
        startActivity(faceGame);
    }

    public void onPlayVoiceGame(View view) {
        Intent voiceGame = new Intent(this,VoiceGameActivity.class);
        startActivity(voiceGame);
    }

    public void onViewPersonalCollection(View view) {
        Intent PCollection = new Intent(this,PersonalCollectionActivity.class);
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
            startActivity(intent);
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
