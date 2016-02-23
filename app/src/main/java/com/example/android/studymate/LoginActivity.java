package com.example.android.studymate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    private static final String LOG_TAG = LoginActivity.class.getSimpleName();

    @Bind(R.id.input_email) EditText emailText;
    @Bind(R.id.input_password) EditText passwordText;
    @Bind(R.id.btn_login) Button btn_login;
    @Bind(R.id.link_signup) TextView signupLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);

        setContentView(R.layout.activity_login);
        final Firebase ref = new Firebase(Constants.FIREBASE_URL);
        ButterKnife.bind(this);
        try {
            if (ref.getAuth().getUid() != null) {
                Intent intent = new Intent(LoginActivity.this, GeoActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("uid", ref.getAuth().getUid());
                startActivity(intent);
            }
        } catch (Exception e)
        {
            System.out.println("Login exception:"+e);
        }



        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ref.authWithPassword(emailText.getText().toString(), passwordText.getText().toString(), new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        if (authData != null) {
                            //     com.example.android.studymate.SaveSharedPreferences.setUserId( authData.getUid());
                            Intent intent = new Intent(LoginActivity.this, GeoActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.putExtra("uid", authData.getUid());
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        Log.i(LOG_TAG, firebaseError.toString());
                        String error_msg;
                        switch (firebaseError.getCode()) {
                            case FirebaseError.USER_DOES_NOT_EXIST:
                                error_msg = "User does not exist! Please Sign up.";
                                break;
                            case FirebaseError.INVALID_PASSWORD:
                                error_msg = "Incorrect Username/Password";
                                break;
                            default:
                                // handle other errors
                                error_msg = "Login error";

                                break;
                        }
                        Toast.makeText(LoginActivity.this, error_msg, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signupIntent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(signupIntent);
            }
        });
    }
}
