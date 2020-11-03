package com.quarantino.ruto.HelperClasses.Preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class sharedPrefs {
    Context context;
    SharedPreferences sharedPref;

    public sharedPrefs(Context context) {
        this.context = context;
        sharedPref = context.getSharedPreferences("User Authentication", 0);
    }

    public boolean getIsFirstTime() {
        return sharedPref.getBoolean("First Time", true);
    }

    public void setIsFirstTime(boolean state) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("First Time", state);
        editor.commit();
    }
    public boolean getIsLoggedIn() {
        return sharedPref.getBoolean("User Logged In", false);
    }

    public void setIsLoggedIn(boolean state) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("User Logged In", state);
        editor.commit();
    }

    public boolean getIsLoggedOut() {
        return sharedPref.getBoolean("User Logged Out", false);
    }

    public void setIsLoggedOut(boolean state) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("User Logged Out", state);
        editor.commit();
    }

    public boolean getProfilePhotoToken() {
        return sharedPref.getBoolean("Profile Photo", false);
    }

    public void setProfilePhotoToken(boolean state) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("Profile Photo", state);
        editor.commit();
    }

    public boolean getMDFirstTime() {
        return sharedPref.getBoolean("Main Dashboard First Time", true);
    }

    public void setMDFirstTime(boolean bool){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("Main Dashboard First Time", bool);
        editor.commit();
    }

    public boolean getPermission(){
        return sharedPref.getBoolean("Permission Granted", true);
    }

    public void setPermission(boolean bool){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("Permission Granted", bool);
        editor.commit();
    }

    public int getCounter() {
        return sharedPref.getInt("Counter", 0);
    }

    public void setCounter(int counter){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("Counter", ++counter);
        editor.commit();
    }
}
