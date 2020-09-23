package com.quarantino.ruto.HelperClasses.NearbyAdapter;

import android.graphics.Bitmap;

public class NearbyPlacesHelperClass {

    int imageOfPlace;
    float rating;
    String nameOfPlace;

    public NearbyPlacesHelperClass(int imageOfPlace, String nameOfPlace, float rating) {
        this.imageOfPlace = imageOfPlace;
        this.nameOfPlace = nameOfPlace;
        this.rating = rating;
    }

    public int getImageOfPlace() {
        return imageOfPlace;
    }

    public String getNameOfPlace() {
        return nameOfPlace;
    }

    public float getRating() {
        return rating;
    }
}
