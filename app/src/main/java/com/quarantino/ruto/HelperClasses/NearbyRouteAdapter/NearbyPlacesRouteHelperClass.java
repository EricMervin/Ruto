package com.quarantino.ruto.HelperClasses.NearbyRouteAdapter;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class NearbyPlacesRouteHelperClass {

    String placeName;
    Bitmap placePhoto;

    public NearbyPlacesRouteHelperClass(String placeName, Bitmap placePhoto) {
        this.placeName = placeName;
        this.placePhoto = placePhoto;
    }

    public String getPlaceName() {
        return placeName;
    }

    public Bitmap getPlacePhoto() {
        return placePhoto;
    }
}
