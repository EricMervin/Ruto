package com.quarantino.ruto.HelperClasses.NearbyAdapter;

import android.graphics.Bitmap;

public class NearbyPlacesHelperClass {

    Bitmap imageOfPlace;
    float rating;
    double placeLat, placeLng;
    String nameOfPlace, idOfPlace;

    public NearbyPlacesHelperClass(Bitmap imageOfPlace, String nameOfPlace, float rating, String idOfPlace, double placeLat, double placeLng) {
        this.idOfPlace = idOfPlace;
        this.imageOfPlace = imageOfPlace;
        this.nameOfPlace = nameOfPlace;
        this.rating = rating;
        this.placeLat = placeLat;
        this.placeLng = placeLng;
    }

    public double getPlaceLat() {
        return placeLat;
    }

    public double getPlaceLng() {
        return placeLng;
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