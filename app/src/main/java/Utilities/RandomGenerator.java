package Utilities;

import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Manjeera on 30/9/2016.
 */
public class RandomGenerator {

    private int numRows;
    private String[] fixedNames;

    /**
     * Constructor for class RandomGenerator
     * @param numRows
     */
    public RandomGenerator(int numRows)
    {
        this.numRows=numRows;
        this.fixedNames = new String[]{"Naruto", "Sasuke", "Pablo", "Berna", "Zheng Jie", "Zhang Jie", "Kelly", "Qui Zhi", "Qing Mei", "Tzoo Lai"};
    }

    /**
     * Get a random photo index
     * @return index of the photo
     */
    public int getRandomPhotoIndex()
    {
        Random r = new Random();
        int i = r.nextInt(numRows);
        return i;
    }

    /**
     * Randomize the order of options for a question
     * @param options
     * @return string array with shuffled options
     */
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

        ArrayList<String> names = new ArrayList<>();

        for(int i = 0; i < options.length; i++){
            names.add(options[i].toString());
        }

        while (names.size() < 4){
            int random = r.nextInt(fixedNames.length);
            if(!names.contains(fixedNames[random])){
                names.add(fixedNames[random]);
            }
        }


        for(int i = 0; i < numRows; i++){
            shuffledOpt[i] = (String)(names.get(selectedIndices.get(i)));
        }

        return shuffledOpt;
    }
}
