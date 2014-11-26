package com.rodrigueztomas.timecapsule;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.parse.ParseException;

import java.util.ArrayList;


public class LoginActivity extends Activity {

    private EditText et_username;
    private EditText et_password;
    public static ParseUser user;
    private Button login;
    private Button signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Parse.initialize(this, "nrS90SuBR6GFB35zv2LrPfId25VPx56dJGUf3W3D", "POqL1wwxbss78rBch1a1CVLBIEbvEBFJmhBwu266");

        et_username = (EditText) findViewById(R.id.username);
        et_password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        signUp = (Button) findViewById(R.id.signUp);

        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                if(et_username.getText().toString().length() < 6)
                {
                    et_username.setError("User name must be at least 6 characters long.");
                    // Toast.makeText(getApplicationContext(), "Username must be at least 6 characters.", Toast.LENGTH_LONG).show();
                }
                else if(et_password.getText().toString().length() < 6)
                {
                    et_password.setError("Password must be at least 6 characters long.");
                    //Toast.makeText(getApplicationContext(), "Password must be at least 6 characters.", Toast.LENGTH_LONG).show();

                }
                else {
                    Log.d("ParseLogIn", "Logging in as: " + et_username.getText().toString());
                    ParseUser.logInInBackground(et_username.getText().toString(), et_password.getText().toString(), new LogInCallback() {
                        public void done(ParseUser user, ParseException e) {
                            if (user != null) {
                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(i);
                                // Hooray! The user is logged in.
                            } else {

                                Toast.makeText(getApplicationContext(), "Invalid username/password.", Toast.LENGTH_LONG).show();
                                // Signup failed. Look at the ParseException to see what happened.
                            }
                        }
                    });
                }
            }

        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.user = new ParseUser();
                if(et_username.getText().toString().length() < 6)
                {
                    Toast.makeText(getApplicationContext(), "Username must be at least 6 characters.", Toast.LENGTH_LONG).show();
                }
                else if(et_password.getText().toString().length() < 6)
                {
                    Toast.makeText(getApplicationContext(), "Password must be at least 6 characters.", Toast.LENGTH_LONG).show();

                }
                else
                {
                    LoginActivity.user.setUsername(et_username.getText().toString());
                    LoginActivity.user.setPassword(et_password.getText().toString());




                    LoginActivity.user.signUpInBackground(new SignUpCallback() {

                        @Override
                        public void done(com.parse.ParseException e) {
                            if (e == null) {

                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(i);

                            } else {
                                Toast.makeText(getApplicationContext(), "User Already Exists.", Toast.LENGTH_LONG).show();
                                // Sign up didn't succeed. Look at the ParseException
                                // to figure out what went wrong
                            }
                        }
                    });
                }

            }

        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
