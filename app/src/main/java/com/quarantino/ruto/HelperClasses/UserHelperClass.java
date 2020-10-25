package com.quarantino.ruto.HelperClasses;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.maps.model.LatLng;

public class UserHelperClass {
    Context context;
    SharedPreferences sharedPref;

    public UserHelperClass(Context context) {
        this.context = context;
        sharedPref = context.getSharedPreferences("User Details", 0);
    }

    public String getName() {
        return sharedPref.getString("Name", "");
    }

    public void setName(String name) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("Name", name);
        editor.commit();
    }

    public String getUsername() {
        return sharedPref.getString("Username", "");
    }

    public void setUsername(String username) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("Username", username);
        editor.commit();
    }

    public String getEmail() {
        return sharedPref.getString("Email", "");
    }

    public void setEmail(String phoneNo) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("Email", phoneNo);
        editor.commit();
    }

    public String getPassword() {
        return sharedPref.getString("Password", "");
    }

    public void setPassword(String password) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("Password", password);
        editor.commit();
    }

    public String getProfilePhoto() {
        return sharedPref.getString("Profile Photo", "");
    }

    public void setProfilePhoto(String filePath) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("Profile Photo", filePath);
        editor.commit();
    }
}
