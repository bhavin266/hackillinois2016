package com.example.android.studymate;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.example.android.studymate.GeoActivity.*;


public class UpdateInterestActivity extends AppCompatActivity {


    @Bind(R.id.buttonAddInterests)
    Button addInterests;

    @Bind(R.id.searchSubjects)
    MultiAutoCompleteTextView searchSubjects;

    @Bind(R.id.back)
    Button back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_interest);
        ButterKnife.bind(this);

        Bundle extras=getIntent().getExtras();
        String current_user= extras.getString("uid");


        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, Interests.subjects);

        searchSubjects.setAdapter(adapter);
        searchSubjects.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        StringBuffer stringBuffer=new StringBuffer();
        Iterator<String> it=avail_users.get(current_user).iterator();
        while(it.hasNext())
        {
            stringBuffer.append(it.next()+",");
        }


        searchSubjects.setText(stringBuffer.toString());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void addInterests(View view){
        Firebase ref = new Firebase(Constants.FIREBASE_URL);
        Firebase userRef = ref.child("users/" + ref.getAuth().getUid() + "/Interests/");

        String textRefined = String.valueOf(searchSubjects.getText()).substring(0, String.valueOf(searchSubjects.getText()).length() - 2);
        userRef.setValue(textRefined);
        Toast.makeText(getApplicationContext(), "Congratulations!! Successfully Registered", Toast.LENGTH_SHORT);

    }

    public void goback(View view){
        Intent geoIntent = new Intent(this, GeoActivity.class);
        Firebase ref = new Firebase(Constants.FIREBASE_URL);
        geoIntent.putExtra("uid",ref.getAuth().getUid());

        startActivity(geoIntent);

    }

}
