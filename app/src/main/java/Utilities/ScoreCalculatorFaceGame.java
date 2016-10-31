package Utilities;

import android.util.Log;

/**
 * Created by SHREY_000 on 9/26/2016.
 */

public class ScoreCalculatorFaceGame {
    private int scoreTotal = 0;
    private int featuresRevealed = 0;
    private int numQuestions = 0;
    private static final int MAX_SCORE = 100;
    private static final int SCORE_PER_FEATURE_REVEALED = 25;

    /**
     * Reset the score calculator for next question
     * @param isCorrect
     */
    public void resetForNextQuestion(boolean isCorrect){
        calculateTotalScore(isCorrect);
        numQuestions++;
        featuresRevealed = 0;
    }

    /**
     * Calculate the score the current question
     * @param isCorrect
     * @return score
     */
    public int calculateScoreForCurrentQuestion(boolean isCorrect){
        Log.d("Score","" + (MAX_SCORE - featuresRevealed*SCORE_PER_FEATURE_REVEALED));
        if(!isCorrect)
            return 0;
        return MAX_SCORE - featuresRevealed*SCORE_PER_FEATURE_REVEALED;
    }

    /**
     * Calculate the total score for a game session
     * @param isCorrect
     */
    public void calculateTotalScore(boolean isCorrect){
        scoreTotal += calculateScoreForCurrentQuestion(isCorrect);
        Log.d("Total score","" + scoreTotal);
    }

    /**
     * Increment the features revealed
     */
    public void incrementFeaturesRevealed(){
        featuresRevealed++;
    }

    /**
     * Getter for total score
     * @return total score
     */
    public int getScoreTotal(){
        return scoreTotal;
    }

    /**
     * Setter for total score
     * @param scoreTotal
     */
    public void setScoreTotal(int scoreTotal){
        this.scoreTotal = scoreTotal;
    }
}