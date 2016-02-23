package com.example.android.studymate;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
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

/**
 * Created by Bhavin on 20-02-2016.
 */
public class WelcomeActivity extends AppCompatActivity {
    private static final String LOG_TAG = WelcomeActivity.class.getSimpleName();

    @Bind(R.id.textView2)
    EditText _nameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);

        setContentView(R.layout.activity_welcome);

        ButterKnife.bind(this);

    }

}
