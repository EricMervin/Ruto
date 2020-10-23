package com.quarantino.ruto.HelperClasses.NearbyRouteAdapter;

import android.graphics.drawable.Drawable;

public class NearbyPlacesRouteHelperClass {

    String categoryType;
    Drawable categoryIcon;

    public NearbyPlacesRouteHelperClass(String categoryType, Drawable categoryIcon) {
        this.categoryType = categoryType;
        this.categoryIcon = categoryIcon;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public Drawable getCategoryIcon() {
        return categoryIcon;
    }
}
