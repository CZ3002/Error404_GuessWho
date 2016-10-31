package Utilities;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import Entities.Acquaintance;
import Entities.User;

/**
 * Created by Ankur Bansal on 2/10/2016.
 */
public interface DataStoreManager {

    /**
     * Getter for user acquaintances
     * @param username
     * @return
     */
    ArrayList<Acquaintance> getAllAcquaintance(String username);

    /**
     * Getter for user photos
     * @param username
     * @return
     */
    ArrayList<HashMap<String, String>> getPhotos(String username);

    /**
     * Getter for user's acquaintances' voices
     * @param username
     * @return
     */
    HashMap<String, HashMap<String, ArrayList<String>>> getVoice(String username);

    /**
     * To create a new user
     * @param user
     * @return
     */
    int createUser(User user);

    /**
     * Create a new acquaintance
     * @param acquaintance
     * @return
     */
    int insertPC(Acquaintance acquaintance);

    /**
     * Insert game scores to the database
     * @param username
     * @param gameName
     * @param score
     * @return
     */
    int insertScore(String username, String gameName, int score);

    /**
     * Getter for a user's game scores
     * @param username
     * @param gameName
     * @return
     */
    HashMap<String,String> getScores(String username, String gameName);

    /**
     * Delete a user's acquaintance
     * @param acquaintance
     * @return
     */
    int deletePC(Acquaintance acquaintance);

    /**
     * Validate a user's existence
     * @param username
     * @param password
     * @return
     */
    String validateUser(String username, String password);
}
