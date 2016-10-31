package Utilities;

import android.util.Log;

/**
 * Created by Ankur Bansal on 2/10/2016.
 */
public class DataStoreFactory {

    private static DataStoreManager dataStoreManager;

    private static final String DBURL = "jdbc:mysql://10.27.113.185:3306/guesswho";
    private static final String USERNAME = "user1";
    private static final String PASSWORD = "123";

    /**
     * Create an object for database manager
     * @return database manager object
     */
    public static DataStoreManager createDataStoreManager(){
        if(dataStoreManager == null){
            Log.d("pplolo", "called");
            dataStoreManager = new MySQLImpl(DBURL, USERNAME, PASSWORD);
        }

        return dataStoreManager;
    }
}
