package Utilities;

/**
 * Created by aakashgupa1236 on 30/9/2016.
 */

import android.util.Log;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import Entities.Acquaintance;
import Entities.User;

//import Entities.Acquaintance;
//import Entities.User;

public class MySQLImpl implements DataStoreManager{
    private static Connection con;
    private static Statement stmt;
    private static ResultSet resultSet;

    private static String query;

    public MySQLImpl(final String databaseURL, final String username, final String password) {
        // TODO Auto-generated constructor stub
        Thread t = new Thread() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    con = (Connection) DriverManager.getConnection(databaseURL,
                            username, password);
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        };

        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public ArrayList<Acquaintance> getAllAcquaintance(String username){
        ArrayList<Acquaintance> arrayList = new ArrayList<>();
        try {
            query = "SELECT * FROM personal_collection WHERE username = " + quotify(username) + terminate();
            stmt = con.createStatement();
            resultSet = stmt.executeQuery(query);

            while(resultSet.next()){
                Acquaintance acquaintance = new Acquaintance();
                HashMap<String, String> hashMap = new HashMap<>();
                String name = resultSet.getString("acqName");
                String base64 = resultSet.getString("base64");
                String contact = resultSet.getString("contact");
                String relation = resultSet.getString("relationship");
                String voiceFilename = resultSet.getString("soundFile");
                String notes = resultSet.getString("notes");
                acquaintance.setUsername(username);
                acquaintance.setAcqName(name);
                acquaintance.setBase64(base64);
                acquaintance.setContact(contact);
                acquaintance.setRelationship(relation);
                acquaintance.setSoundFile(voiceFilename);
                acquaintance.setNotes(notes);
                arrayList.add(acquaintance);
            }
        }catch (Exception e){
            Log.d("error", e.getMessage());
        }
        return arrayList;
    }

    public ArrayList<HashMap<String, String>> getPhotos(String username){
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        try {
            query = "SELECT acqName, base64 FROM personal_collection WHERE username = " + quotify(username) + terminate();
            stmt = con.createStatement();
            resultSet = stmt.executeQuery(query);

            while(resultSet.next()){
                HashMap<String, String> hashMap = new HashMap<>();
                String name = resultSet.getString("acqName");
                String base64 = resultSet.getString("base64");
                hashMap.put("acqName", name);
                hashMap.put("base64", base64);
                arrayList.add(hashMap);
            }
        }catch (Exception e){
            Log.d("error", e.getMessage());
        }
        return arrayList;
    }

    public HashMap<String, HashMap<String, ArrayList<String>>> getVoice(String username){
        HashMap<String, HashMap<String, ArrayList<String>>> arrayList = new HashMap<>();
        try{
            query = "SELECT acqName, soundFile, pos1, pos2, pos3, pos4, pos5 FROM personal_collection WHERE username = " + quotify(username) + terminate();
            stmt = con.createStatement();
            resultSet = stmt.executeQuery(query);
            while(resultSet.next()){
                String acqName = resultSet.getString("acqName");
                String soundFile = resultSet.getString("soundFile");
                arrayList.put(acqName, new HashMap<String, ArrayList<String>>());
                arrayList.get(acqName).put(soundFile, new ArrayList<String>());
                arrayList.get(acqName).get(soundFile).add(resultSet.getString("pos1"));
                arrayList.get(acqName).get(soundFile).add(resultSet.getString("pos2"));
                arrayList.get(acqName).get(soundFile).add(resultSet.getString("pos3"));
                arrayList.get(acqName).get(soundFile).add(resultSet.getString("pos4"));
                arrayList.get(acqName).get(soundFile).add(resultSet.getString("pos5"));
            }
        }catch(Exception e){
            Log.d("error", e.getMessage());
        }
        return arrayList;
    }

    public int createUser(User user){
        try {
            query = "INSERT INTO user VALUES("
                    + quotify(user.getUsername()) + ", "
                    + quotify(user.getName()) + ", md5("
                    + quotify(user.getPassword()) + "), "
                    + quotify(user.getContact()) + ", "
                    + quotify(user.getDOB()) + ") "
                    + terminate()
            ;

            stmt = con.createStatement();

            return stmt.executeUpdate(query);

        }catch (Exception e){
            Log.d("error", e.getMessage());
            return -1;
        }
    }

    public int insertPC(Acquaintance acquaintance){
        try {
            query = "INSERT INTO personal_collection VALUES("
                    + quotify(acquaintance.getUsername()) + ", "
                    + quotify(acquaintance.getAcqName()) + ", "
                    + quotify(acquaintance.getRelationship()) + ", "
                    + quotify(acquaintance.getContact()) + ", "
                    + quotify(acquaintance.getNotes()) + ", "
                    + quotify(acquaintance.getBase64()) + ", "
                    + quotify(acquaintance.getSoundFile()) + ", "
                    + quotify(acquaintance.getPos1()) + ", "
                    + quotify(acquaintance.getPos2()) + ", "
                    + quotify(acquaintance.getPos3()) + ", "
                    + quotify(acquaintance.getPos4()) + ", "
                    + quotify(acquaintance.getPos5()) + ")"
                    + terminate()
            ;

            stmt = con.createStatement();

            return stmt.executeUpdate(query);

        }catch (Exception e){
            Log.d("error", e.getMessage());
            return -1;
        }
    }

    public int deletePC(Acquaintance acquaintance){
        try{
            query = "DELETE FROM personal_collection WHERE username =" + quotify(acquaintance.getUsername()) +
                    " AND acqName = " + quotify(acquaintance.getAcqName()) + terminate();

            stmt = con.createStatement();

            return stmt.executeUpdate(query);
        }catch(Exception e){
            Log.d("error", e.getMessage());
        }
        return -1;
    }

    public String validateUser(String username, String password){
        try{
            query = "SELECT * FROM user WHERE username =" + quotify(username) + " " +
                    "AND password = md5(" + quotify(password) + ")" + terminate();

            stmt = con.createStatement();

            resultSet = stmt.executeQuery(query);

            if(resultSet.next())
                return "Success";

        }catch(Exception e){
            Log.d("error", e.getMessage());
        }
        return "Error";
    }

    private String terminate(){
        return ";";
    }

    private String quotify(String val){
        return "\"" + val + "\"";
    }
}