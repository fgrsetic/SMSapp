package com.franjo.smsapp.util;

import android.content.res.Resources;
import android.util.TypedValue;

import com.franjo.smsapp.app.App;


public class DpAndPxConverter {

    private DpAndPxConverter() {
    }

    // Convert dp to pixels
    public static int convertDpToPx(int dp) {
        Resources r = App.getAppContext().getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    // Convert pixels to dp
    public static int convertPxToDp(int px) {
        float scale = App.getAppContext().getResources().getDisplayMetrics().density;
        return (int) (px * scale + 0.5f);
    }
}
