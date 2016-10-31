package com.example.shrey_000.guesswho;

import android.app.DatePickerDialog;
import android.os.StrictMode;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import Entities.User;
import Utilities.DataStoreFactory;
import Utilities.DataStoreManager;


/**
 *
 * Class used by new users during Sign Up
 * @author aakashgupta1236
 *
 */
public class SignUpActivity extends AppCompatActivity {

    EditText dob;

    /**
     * userDOB used to store user's date of birth
     */
    private Calendar userDob = Calendar.getInstance();

    /**
     * DataManager used to call functions that communicate with the database
     */
    private DataStoreManager dataStoreManager = DataStoreFactory.createDataStoreManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_sign_up);

        Button signUp = (Button) findViewById(R.id.signUp);

        signUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                signUp();
            }
        });

        dob = (EditText) findViewById(R.id.userDOBSignUp);

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                showDateDialog();
            }
        });

        dob.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (hasFocus) {
                    showDateDialog();
                }
            }
        });
    }

    /**
     * Function used to input field values from patient during Sign Up
     */
    private void signUp(){
        User user = new User();
        user.setUsername(((EditText)findViewById(R.id.userIdSignUp)).getText().toString());
        user.setName(((EditText) findViewById(R.id.userNameSignUp)).getText().toString());
        user.setPassword(((EditText) findViewById(R.id.userPasswordSignUp)).getText().toString());
        user.setContact(((EditText) findViewById(R.id.userContactSignUp)).getText().toString());
        user.setDOB(((EditText) findViewById(R.id.userDOBSignUp)).getText().toString());

        dataStoreManager.createUser(user);
        Toast.makeText(SignUpActivity.this, "User successfully registered", Toast.LENGTH_LONG).show();
        this.finish();
    }

    private void showDateDialog(){
        Calendar cal = Calendar.getInstance();
        DatePickerDialog dateDialog = new DatePickerDialog(SignUpActivity.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                userDob.set(Calendar.YEAR, year);
                userDob.set(Calendar.MONTH, monthOfYear);
                userDob.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        dateDialog.show();
    }

    private void updateLabel() {

        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dob.setText(sdf.format(userDob.getTime()));
    }

}
