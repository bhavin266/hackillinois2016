package com.example.android.studymate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SignupActivity extends AppCompatActivity {

    private static final String LOG_TAG = SignupActivity.class.getSimpleName();

    @Bind(R.id.input_name) EditText _nameText;
    @Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.input_password) EditText _passwordText;
    @Bind(R.id.btn_signup) Button _signupButton;
    @Bind(R.id.link_login) TextView _loginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);

        setContentView(R.layout.activity_signup);

        ButterKnife.bind(this);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Firebase ref = new Firebase("https://study-hack.firebaseio.com/");

                ref.createUser(_emailText.getText().toString(), _passwordText.getText().toString(), new Firebase.ValueResultHandler<Map<String, Object>>() {
                    @Override
                    public void onSuccess(Map<String, Object> stringObjectMap) {
                        Intent interestsIntent = new Intent(SignupActivity.this,Interests.class);
                        interestsIntent.putExtra("name",_nameText.getText());
                        startActivity(interestsIntent);
                    }

                    @Override
                    public void onError(FirebaseError firebaseError) {
                        Log.i(LOG_TAG,firebaseError.toString());
                        Toast.makeText(SignupActivity.this, "User Registration failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(SignupActivity.this,LoginActivity.class);
                startActivity(loginIntent);
            }
        });
    }

}
