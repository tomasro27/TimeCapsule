package com.rodrigueztomas.timecapsule;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class SignUpActivity extends Activity {

    private EditText etEmail, etPassword, etReenterPassword, name;
    private Button createAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etEmail = (EditText)  findViewById(R.id.email);
        etPassword = (EditText) findViewById(R.id.password);
        etReenterPassword = (EditText) findViewById(R.id.reenterPassword);
        name = (EditText) findViewById(R.id.name);
        createAccount = (Button) findViewById(R.id.createAccount);

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String email = etEmail.getText().toString();
                if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    etEmail.setError("Invalid etEmail address.");
                    return;
                }
                String password = etPassword.getText().toString();
                String reenterPassword = etReenterPassword.getText().toString();
                if(password.length() < 8)
                {
                    etPassword.setError("Password must be at least 8 characters long.");
                    return;
                }
                if(!password.equals(reenterPassword))
                {
                    etReenterPassword.setError("Passwords don't match!");
                    return;
                }
                if(etEmail.getText().toString().length() == 0)
                {
                    etEmail.setError("Email address can't be empty.");
                    return;
                }
                if(name.getText().toString().length() == 0)
                {
                    name.setError("Name can't be empty!");
                    return;
                }


                    ParseUser user = new ParseUser();
                    user.setUsername(email);
                    user.setEmail(email);
                    user.setPassword(password);
                    user.put("name", name.getText().toString());

                    user.signUpInBackground(new SignUpCallback() {

                        @Override
                        public void done(com.parse.ParseException e) {
                            if (e == null) {

                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(i);

                            } else {

                                Toast.makeText(getApplicationContext(), "Error: " + e.toString(), Toast.LENGTH_LONG).show();
                                // Sign up didn't succeed. Look at the ParseException
                                // to figure out what went wrong
                            }
                        }
                    });
                }



        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sign_up, menu);
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
