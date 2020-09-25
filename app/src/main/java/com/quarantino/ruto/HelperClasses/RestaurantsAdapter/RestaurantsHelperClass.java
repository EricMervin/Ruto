package com.quarantino.ruto.HelperClasses.RestaurantsAdapter;

import android.graphics.Bitmap;

public class RestaurantsHelperClass {

    Bitmap imageOfPlace;
    float rating;
    String nameOfPlace;

    public RestaurantsHelperClass(Bitmap imageOfPlace, String nameOfPlace, float rating) {
        this.imageOfPlace = imageOfPlace;
        this.rating = rating;
        this.nameOfPlace = nameOfPlace;
    }

    public Bitmap getImageOfPlace() {
        return imageOfPlace;
    }

    public float getRating() {
        return rating;
    }

    public String getNameOfPlace() {
        return nameOfPlace;
    }
}
