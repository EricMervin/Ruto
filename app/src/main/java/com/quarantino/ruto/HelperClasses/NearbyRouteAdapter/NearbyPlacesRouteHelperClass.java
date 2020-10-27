package com.quarantino.ruto.HelperClasses.NearbyRouteAdapter;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class NearbyPlacesRouteHelperClass {

    Bitmap imageOfPlace;
    float rating;
    double placeLat, placeLong;
    String nameOfPlace, idOfPlace, openNowStatus;
    boolean placeAddedStatus;

    public NearbyPlacesRouteHelperClass(Bitmap imageOfPlace, String nameOfPlace, String openNowStatus,float rating, String idOfPlace, double placeLat, double placeLong) {
        this.idOfPlace = idOfPlace;
        this.imageOfPlace = imageOfPlace;
        this.nameOfPlace = nameOfPlace;
        this.rating = rating;
        this.placeLat = placeLat;
        this.placeLong = placeLong;
        this.openNowStatus = openNowStatus;
//        this.placeAddedStatus = placeAddedStatus;
    }

    public boolean getPlaceAddedStatus(){
        return placeAddedStatus;
    }
//
    public void setPlaceAddedStatus(boolean placeAddedStatus) {
        this.placeAddedStatus = placeAddedStatus;
    }

    public String getOpenNowStatus() {
        return openNowStatus;
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
