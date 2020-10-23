package com.quarantino.ruto.HelperClasses.CategoriesAdapter;

import android.graphics.drawable.Drawable;

public class CategoriesHelperClass {

    String categoryType;
    Drawable categoryIcon;

    public CategoriesHelperClass(String categoryType, Drawable categoryIcon) {
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
