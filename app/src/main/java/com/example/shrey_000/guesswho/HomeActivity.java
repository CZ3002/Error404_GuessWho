package com.example.shrey_000.guesswho;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.shrey_000.guesswho.FaceGame.FaceGameActivity;
import com.example.shrey_000.guesswho.PersonalCollection.CameraActivity;
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
        Intent PCollection = new Intent(this,CameraActivity.class);
        startActivity(PCollection);
    }
}
