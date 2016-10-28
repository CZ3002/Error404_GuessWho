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

    ArrayList<Acquaintance> getAllAcquaintance(String username);

    ArrayList<HashMap<String, String>> getPhotos(String username);

    HashMap<String, HashMap<String, ArrayList<String>>> getVoice(String username);

    int createUser(User user);

    int insertPC(Acquaintance acquaintance);

    int insertScore(String username, String gameName, int score);

    HashMap<String,String> getScores(String username, String gameName);

    int deletePC(Acquaintance acquaintance);

    String validateUser(String username, String password);
}
