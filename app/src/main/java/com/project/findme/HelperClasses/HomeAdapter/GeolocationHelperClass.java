package com.project.findme.HelperClasses.HomeAdapter;


import android.graphics.drawable.GradientDrawable;

public class GeolocationHelperClass {

    int image;
    String title;
    GradientDrawable gradientDrawable;


    public GeolocationHelperClass(int image, String title, GradientDrawable gradientDrawable) {
        this.image = image;
        this.title = title;
        this.gradientDrawable = gradientDrawable;
    }

    public int getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public GradientDrawable getGradientDrawable() {
        return gradientDrawable;
    }
}
