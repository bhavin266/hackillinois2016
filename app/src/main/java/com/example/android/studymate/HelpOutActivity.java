package com.example.android.studymate;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HelpOutActivity extends AppCompatActivity {
    String requestor;

    @Bind(R.id.question)TextView question;
    @Bind(R.id.reply)TextView reply;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_out);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Firebase ref = new Firebase(Constants.FIREBASE_URL);
        String uid=ref.getAuth().getUid();

        ref.child("/users/"+uid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getKey().toString().equals("request"))
                    getQuestionDetails(dataSnapshot.getValue().toString());
                System.out.print(dataSnapshot.getKey());

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

            reply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Firebase ref=new Firebase(Constants.FIREBASE_URL+"/users/");
                    System.out.println(ref.child(requestor+"/email").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            System.out.println(dataSnapshot.getKey() + " " + dataSnapshot.getValue());

                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.setType("text/plain");
                            intent.putExtra(Intent.EXTRA_EMAIL,dataSnapshot.getValue().toString());
                            intent.putExtra(Intent.EXTRA_SUBJECT, "Answer from your Study Mate");
                            intent.putExtra(Intent.EXTRA_TEXT, "Answer Below");
                            if (intent.resolveActivity(getPackageManager()) != null) {
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    }));

                }
            });
    }

    private void getQuestionDetails(String value) {
        final StringBuffer sb=new StringBuffer();
        Firebase ref=new Firebase(Constants.FIREBASE_URL);
        ref.child("unresolvedQuestions/"+value).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                System.out.println(dataSnapshot.getKey());
                if (!dataSnapshot.getKey().toString().equals("requestor"))
                    sb.append(dataSnapshot.getKey().toString() + ":" + dataSnapshot.getValue().toString() + "\n");
                else
                    requestor = dataSnapshot.getValue().toString();
                question.setText(sb.toString());
                reply.setText("Respond");
            }


            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.logout) {
            logout();
            return true;

        } else if (id == R.id.addInterest) {
            updateInterest();
            return true;
        }else{
            //Do Nothing
        }

        return super.onOptionsItemSelected(item);
    }
    public void updateInterest() {
        Intent intent = new Intent(HelpOutActivity.this, UpdateInterestActivity.class);
        intent.putExtra("uid", GeoActivity.current_user);
        startActivity(intent);
    }

    public void logout() {
        Firebase ref = new Firebase(Constants.FIREBASE_URL + "/activeUsers/");
        ref.unauth();
        Intent intent = new Intent(HelpOutActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
}
