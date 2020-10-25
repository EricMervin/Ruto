package com.quarantino.ruto.HelperClasses.SelectedPlacesAdapter;

import android.graphics.Bitmap;

public class SelectedPlacesHelperClass {

    String idOfSelectedPlace, nameOfSelectedPlace;
    Bitmap photoOfSelectedPlace;

    public SelectedPlacesHelperClass(String idOfSelectedPlace, String nameOfSelectedPlace, Bitmap photoOfSelectedPlace) {
        this.idOfSelectedPlace = idOfSelectedPlace;
        this.nameOfSelectedPlace = nameOfSelectedPlace;
        this.photoOfSelectedPlace = photoOfSelectedPlace;
    }

    public String getIdOfSelectedPlace() {
        return idOfSelectedPlace;
    }

    public String getNameOfSelectedPlace() {
        return nameOfSelectedPlace;
    }

    public Bitmap getPhotoOfSelectedPlace() {
        return photoOfSelectedPlace;
    }

    public void remove(int i){

    }
}
