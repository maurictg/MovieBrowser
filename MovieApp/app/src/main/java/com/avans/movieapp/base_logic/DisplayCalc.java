package com.avans.movieapp.base_logic;

import android.content.Context;
import android.util.DisplayMetrics;

public class DisplayCalc {
    // Resize GridManager of the RecycleViews
    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return (int) (dpWidth / 180);
    }
}
