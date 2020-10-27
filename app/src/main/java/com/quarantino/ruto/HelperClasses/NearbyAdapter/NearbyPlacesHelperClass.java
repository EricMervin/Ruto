package com.quarantino.ruto.HelperClasses.NearbyAdapter;

import android.graphics.Bitmap;

public class NearbyPlacesHelperClass {

    Bitmap imageOfPlace;
    float rating;
    double placeLat, placeLong;
    String nameOfPlace, idOfPlace, openStatus;

    public NearbyPlacesHelperClass(Bitmap imageOfPlace, String nameOfPlace, float rating, String idOfPlace, double placeLat, double placeLong) {
        this.idOfPlace = idOfPlace;
        this.imageOfPlace = imageOfPlace;
        this.nameOfPlace = nameOfPlace;
        this.rating = rating;
        this.placeLat = placeLat;
        this.placeLong = placeLong;
    }

    public NearbyPlacesHelperClass(Bitmap imageOfPlace, String nameOfPlace, String openStatus ,float rating, String idOfPlace, double placeLat, double placeLong) {
        this.idOfPlace = idOfPlace;
        this.imageOfPlace = imageOfPlace;
        this.nameOfPlace = nameOfPlace;
        this.rating = rating;
        this.placeLat = placeLat;
        this.placeLong = placeLong;
        this.openStatus = openStatus;
    }

    public String getOpenStatus() {
        return openStatus;
    }

    public double getPlaceLat() {
        return placeLat;
    }

    public double getPlaceLong() {
        return placeLong;
    }

    public String getIdOfPlace() {
        return idOfPlace;
    }

    public Bitmap getImageOfPlace() {
        return imageOfPlace;
    }

    public String getNameOfPlace() {
        return nameOfPlace;
    }

    public float getRating() {
        return rating;
    }
}