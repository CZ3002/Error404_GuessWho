package Utilities;

/**
 * Created by Ankur Bansal on 2/10/2016.
 */
public class DataStoreFactory {

    private static DataStoreManager dataStoreManager;

    private static final String DBURL = "jdbc:mysql://10.27.232.155:3306/guesswho";
    private static final String USERNAME = "pma";
    private static final String PASSWORD = "";

    public static DataStoreManager createDataStoreManager(){
        if(dataStoreManager == null){
            dataStoreManager = new MySQLImpl(DBURL, USERNAME, PASSWORD);
        }

        return dataStoreManager;
    }
}
