package com.example.android.studymate;

/**
 * Created by Harsh Khandelwal on 20-02-2016.
 */
public class User {

    public String email;
    public String name;

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public User(String email, String fullName){
        this.email = email;
        this.name = fullName;
    }


}
