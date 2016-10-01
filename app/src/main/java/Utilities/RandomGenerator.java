package Utilities;

import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Manjeera on 30/9/2016.
 */
public class RandomGenerator {

    private int numRows;

    public RandomGenerator(int numRows)
    {
        this.numRows=numRows;
    }

    public int getRandomPhotoIndex()
    {
        Random r = new Random();
        int i = r.nextInt(numRows);
        return i;
    }

    public String[] randomizeOptionOrder(Object[] options){
        String[] shuffledOpt = new String[numRows];
        ArrayList<Integer> selectedIndices = new ArrayList<>();
        Random r = new Random();

        while (selectedIndices.size() < numRows){
            int random = r.nextInt(numRows);
            if(!selectedIndices.contains(random)){
                selectedIndices.add(random);
            }
        }

        for(int i = 0; i < numRows; i++){
            shuffledOpt[i] = (String)(options[selectedIndices.get(i)]);
        }

        return shuffledOpt;
    }
}
