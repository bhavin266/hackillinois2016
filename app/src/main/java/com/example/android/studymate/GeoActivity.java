package com.example.android.studymate;

import android.Manifest.permission;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GeoActivity extends AppCompatActivity implements LocationListener {


    LocationManager locationManager;

    String provider;
    static String current_user;
    static HashMap<String, ArrayList<String>> avail_users;
    static ArrayList<String> requests;

    @Bind(R.id.searchSubjects)
    AutoCompleteTextView searchSubjects;
    @Bind(R.id.searchTopics)
    EditText editTextTopic;
    @Bind(R.id.questionTextView)
    EditText editTextQuestion;

    //List<String> interests = new ArrayList<>();
    @Nullable
    @Bind(R.id.searchButton)
    Button searchButton;

    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, Interests.subjects);
        searchSubjects.setAdapter(adapter);
        //searchSubjects.se;


        avail_users = new HashMap<String, ArrayList<String>>();
        Bundle extras = getIntent().getExtras();
        current_user = extras.getString("uid");


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

        if (ActivityCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        Location location = locationManager.getLastKnownLocation(provider);

        Firebase ref = new Firebase(Constants.FIREBASE_URL + "/users/" + current_user);
       // System.out.println(ref + "::::::");

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getChildrenCount() > 0)
                    System.out.print(dataSnapshot.getValue());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                System.out.print("NEW REQUEST FOR HELP!!!");
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

     //   onLocationChanged(location);

    }

    @Override
    public void onLocationChanged(Location location) {
        //   Log.i("LatLng", location.getLatitude() + "" + location.getLongitude());

        final Firebase ref = new Firebase(Constants.FIREBASE_URL);
        if (ref.getAuth() != null) {
            uid = ref.getAuth().getUid();
            GeoFire geoFire = new GeoFire(ref.child("activeUsers/"));
            geoFire.setLocation(uid, new GeoLocation(location.getLatitude(), location.getLongitude()), new GeoFire.CompletionListener() {
                @Override
                public void onComplete(String key, FirebaseError error) {
                    if (error != null)
                        System.out.print(error);
                    else
                        System.out.println("Successfully updated location");
                }
            });
            final GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(location.getLatitude(), location.getLongitude()), 0.3);

            geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                @Override
                public void onKeyEntered(final String key, GeoLocation location) {
                    ref.child("users/" + key + "/Interests/").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            // do some stuff once
                            ArrayList<String> Interests = new ArrayList<String>();

                            try {
                                String str[] = snapshot.getValue().toString().split(",");
                                for (String s : str)
                                    Interests.add(s);

                                avail_users.put(key, Interests);
                            } catch (Exception e) {
                             //   System.out.println("EXCEPTION IN generating interests" + e + " for " + key);
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                        }
                    });

                }

                @Override
                public void onKeyExited(String key) {
                    System.out.println(String.format("Key %s is no longer in the search area", key));
                }

                @Override
                public void onKeyMoved(String key, GeoLocation location) {
                    System.out.println(String.format("Key %s moved within the search area to [%f,%f]", key, location.latitude, location.longitude));
                }

                @Override
                public void onGeoQueryReady() {
                    System.out.println("All initial data has been loaded and events have been fired!");
                }

                @Override
                public void onGeoQueryError(FirebaseError error) {
                    System.err.println("There was an error with this query: " + error);
                }
            });


        } else {
            System.out.print("no auth");
        }


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void callSearchActivity(View view) {
        Intent intent = new Intent(GeoActivity.this, SearchActivity.class);
        Bundle extras = new Bundle();
        String topic = null, question = null;
        if (searchSubjects.getText().toString().equals(null))
            return;
        if (editTextTopic.getText().toString().equals(null))
            topic = "Not available";
        else
            topic = editTextTopic.getText().toString();
        if (editTextQuestion.getText().toString().equals(null))
            question = "Not available ";
        else
            question = editTextQuestion.getText().toString();
        intent.putExtra("subject", searchSubjects.getText().toString());
        intent.putExtra("topic", topic);
        intent.putExtra("question", question);
        intent.putExtra("uid", uid);
        startActivity(intent);

    }

    public void updateInterest() {
        Intent intent = new Intent(GeoActivity.this, UpdateInterestActivity.class);
        intent.putExtra("uid", current_user);
        startActivity(intent);
    }

    public void logout() {
        Firebase ref = new Firebase(Constants.FIREBASE_URL + "/activeUsers/");
        ref.unauth();
        Intent intent = new Intent(GeoActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
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
    public void callHelpOutActivity(View view)
    {
        Intent intent = new Intent(GeoActivity.this, HelpOutActivity.class);
        startActivity(intent);
    }
}


