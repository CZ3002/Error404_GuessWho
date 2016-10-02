package com.example.shrey_000.guesswho;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import Utilities.DataStoreFactory;
import Utilities.DataStoreManager;


public class LoginActivity extends AppCompatActivity {

    /**
     * DataManager used to call functions that communicate with the database
     */
    private DataStoreManager dataStoreManager = DataStoreFactory.createDataStoreManager();
    /**
     * userId used to store userId of user while logging in
     */
    private EditText userId;
    /**
     * password used to store password of user while logging in
     */
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        SharedPreferences prefs = getSharedPreferences("CZ3002Prefs", MODE_MULTI_PROCESS);
        String id = prefs.getString("loginId", "");

        if(!id.equals("")){
            userId = new EditText(this);
            userId.setText(id);
            doLogin("Success");
        }

        setContentView(R.layout.activity_login);

        Button loginButton = (Button) findViewById(R.id.buttonLogin),
                signUpButton = (Button) findViewById(R.id.buttonSignUp);
        userId = (EditText) findViewById(R.id.editTextUserId);
        password = (EditText) findViewById(R.id.editTextPassword);
        loginButton.setOnClickListener(new View.OnClickListener(	) {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String result = validate(userId.getText().toString(), password.getText().toString());
                password.setText("");
                doLogin(result);
            }
        });
        signUpButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent in = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(in);
            }
        });

    }

    /**
     * Function used to log into the respective user's homepage based on user's type
     * @param result String that stores the result of login
     */
    private void doLogin(String result){
        if(result.equals("Success")){
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            intent.putExtra("userName", userId.getText().toString());
            startActivityForResult(intent, 1);
        }else{
            Toast.makeText(LoginActivity.this, "Username or password is incorrect", Toast.LENGTH_LONG).show();
            password.setText("");
        }
        userId.setText("");
    }


    /**
     * Function used to validate username and password entered by user during Login
     */
    private String validate(String username, String password){

        String result = dataStoreManager.validateUser(username, password);
        if(result.equals(""))
            return "Error";
        else if(result.equals("Success")){
            SharedPreferences prefs = getSharedPreferences("CZ3002Prefs", MODE_MULTI_PROCESS);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("loginId", username);
            editor.commit();
            return "Success";
        }
        return "Error";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if(data == null || data.getBooleanExtra("shouldFinish", true))
            finish();
    }
}
