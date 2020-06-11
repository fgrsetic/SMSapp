package com.franjo.smsapp.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.franjo.smsapp.R;
import com.franjo.smsapp.app.App;

public class ImageUtil {

    private static final Resources res = App.getAppContext().getResources();

    public static Drawable getRandomImage(String name) {
        final int tileSize = res.getDimensionPixelSize(R.dimen.letter_tile_size);
        final LetterTileProvider tileProvider = new LetterTileProvider(App.getAppContext());
        final Bitmap letterTile = tileProvider.getLetterTile(name, getColorKey(name), tileSize, tileSize);
        return new BitmapDrawable(res, letterTile);
    }

    private static String getColorKey(String name) {
        return name.substring(0, 1);
    }
}
