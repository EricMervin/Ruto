package com.quarantino.ruto.HelperClasses.NearbyAdapter;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class NearbyPlacesHelperClass implements Parcelable {

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

    public NearbyPlacesHelperClass(String nameOfPlace, String openStatus ,float rating, String idOfPlace, double placeLat, double placeLong) {
        this.idOfPlace = idOfPlace;
        this.nameOfPlace = nameOfPlace;
        this.rating = rating;
        this.placeLat = placeLat;
        this.placeLong = placeLong;
        this.openStatus = openStatus;
    }


    protected NearbyPlacesHelperClass(Parcel in) {
        rating = in.readFloat();
        placeLat = in.readDouble();
        placeLong = in.readDouble();
        nameOfPlace = in.readString();
        idOfPlace = in.readString();
        openStatus = in.readString();
    }

    public static final Creator<NearbyPlacesHelperClass> CREATOR = new Creator<NearbyPlacesHelperClass>() {
        @Override
        public NearbyPlacesHelperClass createFromParcel(Parcel in) {
            return new NearbyPlacesHelperClass(in);
        }

        @Override
        public NearbyPlacesHelperClass[] newArray(int size) {
            return new NearbyPlacesHelperClass[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeFloat(rating);
        parcel.writeDouble(placeLat);
        parcel.writeDouble(placeLong);
        parcel.writeString(nameOfPlace);
        parcel.writeString(idOfPlace);
        parcel.writeString(openStatus);
    }
}