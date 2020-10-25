package com.quarantino.ruto.HelperClasses.NearbyRouteAdapter;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class NearbyPlacesRouteHelperClass {

    Bitmap imageOfPlace;
    float rating;
    double placeLat, placeLong;
    String nameOfPlace, idOfPlace;

    public NearbyPlacesRouteHelperClass(Bitmap imageOfPlace, String nameOfPlace, float rating, String idOfPlace, double placeLat, double placeLong) {
        this.idOfPlace = idOfPlace;
        this.imageOfPlace = imageOfPlace;
        this.nameOfPlace = nameOfPlace;
        this.rating = rating;
        this.placeLat = placeLat;
        this.placeLong = placeLong;
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
