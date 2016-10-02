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
import com.example.shrey_000.guesswho.PersonalCollection.ViewPersonalCollectionActivity;
//import com.example.shrey_000.guesswho.VoiceGame.VoiceGameActivity;

public class HomeActivity extends AppCompatActivity {

    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        username = getIntent().getStringExtra("username");
        setContentView(R.layout.activity_home);
    }

    public void onPlayFaceGame(View view) {
        Intent faceGame = new Intent(this,FaceGameActivity.class);
        faceGame.putExtra("userName", username);
        startActivity(faceGame);
    }

    public void onPlayVoiceGame(View view) {
//        Intent voiceGame = new Intent(this,VoiceGameActivity.class);
//        voiceGame.putExtra("userName", username);
//        startActivity(voiceGame);
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
            startActivity(intent);
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
