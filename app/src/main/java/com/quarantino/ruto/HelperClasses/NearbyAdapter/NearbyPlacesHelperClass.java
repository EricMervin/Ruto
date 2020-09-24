package com.quarantino.ruto.HelperClasses.NearbyAdapter;

import android.graphics.Bitmap;

public class NearbyPlacesHelperClass {

    Bitmap imageOfPlace;
    float rating;
    String nameOfPlace;

    public NearbyPlacesHelperClass(Bitmap imageOfPlace, String nameOfPlace, float rating) {
        this.imageOfPlace = imageOfPlace;
        this.nameOfPlace = nameOfPlace;
        this.rating = rating;
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
