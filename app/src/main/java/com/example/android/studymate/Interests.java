package com.example.android.studymate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class Interests extends AppCompatActivity {
    private static final String LOG_TAG = Interests.class.getSimpleName();

    @Bind(R.id.searchSubjects)
    MultiAutoCompleteTextView mSearchSubjects;
    @Bind(R.id.submitSubjectsBtn)
    Button mSubmitSubjectsBtn;
    @Bind(R.id.link_login)
    TextView link_login;


    String textRefined ; //Remove the trailing comma
    String extraText;
    public static String[] subjects = {"Machine Learning", "Algorithms", "Data Structures", "Mobile App Development", "Software Engineering", "Calculus", "Theory of Computation", "Operating Systems", "Calculus", "Algebra",
            "Accounting","Acting/Theatre","Administrative Support certificate","Advanced Manufacturing","Anthropology	","Apprenticeships","Arts and Journalism","Astronomy","Atmospheric Science","Aviation","Biology","Botany	 ","Business Administration","Business Technology","Ceramics","Chemistry","Chiropractic Medicine","College in the High School","Composites","Computer Information Systems (CIS)	","Computer Science","Communication Studies (Speech)","Corporate and Continuing Education Center","Cosmetology","Creative Writing","Criminal Justice","Dental Hygiene","Dentistry","Diagnostic Ultrasound	","Digital Illustration","Drawing","Early Childhood Education","East County Campus","Economics","Education (Elementary and Secondary)","Emergency Medical Technician (EMT)	Engineering","Engineering Technology","English","Environmental Studies","Finance","Fire Science	","Foreign Language","Geography","Geoscience","Global Studies	","Graphic Design","Green Programs","","Healthcare Risk Management","History	","Humanities","","I-BEST","Information Technology	","Interactive Web Design","","Journalism	 ","","Learning Communities","Legal Office Assistant certificate	","Legal Office Support","","Management","Manufacturing Pre-Employment","Marketing","Mathematics","Occupational Therapy","Oceanography","Office Support certificate	","Optometry","ORCA (Ocean Research College Academy)","Painting","Pharmacy","Philosophy","Phlebotomy","Photography","Physical Education","Physical Therapy	","Physics","Political Science","Project Management","Precision Machining","Printmaking","Psychology","Radiologic Tech","Reading and Study Skills	","Running Start","Sculpture","Service Learning","Sociology	","Speech (Communication Studies)","Studio Arts"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interests);
        ButterKnife.bind(this);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, subjects);

        mSearchSubjects.setAdapter(adapter);
        mSearchSubjects.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());


        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                extraText = null;
            } else {
                extraText = extras.getString("uid");
            }
        }

        mSubmitSubjectsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSearchSubjects.getText().toString() == null) {
                    Toast.makeText(Interests.this, "Minimum one subject required"+extraText, Toast.LENGTH_SHORT).show();
                } else {
                    Firebase ref = new Firebase(Constants.FIREBASE_URL);
                    Firebase userRef = ref.child("users/" + extraText + "/Interests/");

                    textRefined = String.valueOf(mSearchSubjects.getText()).substring(0, String.valueOf(mSearchSubjects.getText()).length() - 2);

                    Map<String, Object> interestSubjects = new HashMap<String, Object>();
                    interestSubjects.put(extraText + "/Interests", subjects); //Make complete URL
                    userRef.setValue(textRefined);

                    Toast.makeText(getApplicationContext(), "Congratulations!! Successfully Registered", Toast.LENGTH_SHORT);
                    callGeoActivity(v);
                }
            }
        });

        link_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callGeoActivity(v);
            }
        });

    }

    public void callGeoActivity(View view) {
        Intent geoIntent = new Intent(this, GeoActivity.class);
        geoIntent.putExtra("uid",extraText);
        startActivity(geoIntent);
    }


}
