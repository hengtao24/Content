package com.sht.content.support;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.graphics.Palette;

/**
 * Created by sht on 2017/2/20.
 */

public class ImageUtil {
    public static int getImageColor(Bitmap bitmap){
        Palette palette = Palette.from(bitmap).generate();
        if (palette == null || palette.getDarkMutedSwatch() == null){
            return Color.LTGRAY;
        }
        return palette.getDarkMutedSwatch().getRgb();
    }
}
