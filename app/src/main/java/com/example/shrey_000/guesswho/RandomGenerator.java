package com.example.shrey_000.guesswho;

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
}
