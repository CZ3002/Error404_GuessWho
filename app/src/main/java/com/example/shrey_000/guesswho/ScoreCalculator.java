package com.example.shrey_000.guesswho;

import android.util.Log;

/**
 * Created by SHREY_000 on 9/26/2016.
 */
public class ScoreCalculator {
    private int featuresRevealed = 0;
    private static final int MAX_SCORE = 100;
    private static final int SCORE_PER_FEATURE_REVEALED = 25;

    public int calculateScore(){
        Log.d("Score","" + (MAX_SCORE - featuresRevealed*SCORE_PER_FEATURE_REVEALED));
        return MAX_SCORE - featuresRevealed*SCORE_PER_FEATURE_REVEALED;
    }

    public void incrementFeaturesRevealed(){
        featuresRevealed++;
    }
}
