package com.quarantino.ruto.HelperClasses.NearbyAdapter;

public class NearbyPlacesHelperClass {

    int image;
    String title, rating;

    public NearbyPlacesHelperClass(int image, String title) {
        this.image = image;
        this.title = title;
        this.rating = rating;
    }

    public int getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getRating() {
        return rating;
    }
}
