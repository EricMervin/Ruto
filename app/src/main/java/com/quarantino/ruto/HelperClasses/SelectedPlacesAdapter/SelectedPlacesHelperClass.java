package com.quarantino.ruto.HelperClasses.SelectedPlacesAdapter;

import android.graphics.Bitmap;

public class SelectedPlacesHelperClass {

    String nameOfSelectedPlace;
    Bitmap photoOfSelectedPlace;

    public SelectedPlacesHelperClass(String nameOfSelectedPlace, Bitmap photoOfSelectedPlace) {
        this.nameOfSelectedPlace = nameOfSelectedPlace;
        this.photoOfSelectedPlace = photoOfSelectedPlace;
    }

    public String getNameOfSelectedPlace() {
        return nameOfSelectedPlace;
    }

    public Bitmap getPhotoOfSelectedPlace() {
        return photoOfSelectedPlace;
    }
}
