package com.example.android.studymate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity {

    @Bind(R.id.userCount)
    TextView userCount;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Bundle extras=getIntent().getExtras();
        String subject=extras.getString("subject");
        String topic=extras.getString("topic");
        String question=extras.getString("question");
        String uid=extras.getString("uid");


        HashMap<String,String> hmap=new HashMap<String,String>();
        hmap.put("subject",subject);
        hmap.put("topic",topic);
        hmap.put("question", question);
        hmap.put("requestor", uid);


        HashMap<String,String> request=new HashMap<String,String>();
        request.put(uid, subject);

        Firebase ref=new Firebase(Constants.FIREBASE_URL+"/unresolvedQuestions");

        Firebase nref=ref.push();
        nref.setValue(hmap);

        String key=nref.getKey();

        Set users=GeoActivity.avail_users.keySet();
        Iterator<String> it=users.iterator();
        int count=0;
        while( it.hasNext())
        {
            String cur_user=it.next();
            ArrayList<String> current=GeoActivity.avail_users.get(cur_user);
            for(int i=0; i<current.size();i++)
                if(current.get(i).equals(subject) && !cur_user.equals(uid))
                {
                    ref = new Firebase(Constants.FIREBASE_URL+"/users/"+cur_user+"/request");
                    ref.setValue(key);
                    count++;
                    System.out.print(count);
                    break;
                }
        }
        String str;
        if(count>0)
        str=count+" users around you have been notified. Please wait for their email response!";
        else
        str="Sorry, no users around you who can currently help you! Try Again later!";

        userCount.setText(str);

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
        Intent intent = new Intent(SearchActivity.this, UpdateInterestActivity.class);
        intent.putExtra("uid", GeoActivity.current_user);
        startActivity(intent);
    }

    public void logout() {
        Firebase ref = new Firebase(Constants.FIREBASE_URL + "/activeUsers/");
        ref.unauth();
        Intent intent = new Intent(SearchActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

}
