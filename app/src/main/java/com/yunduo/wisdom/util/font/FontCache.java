package com.yunduo.wisdom.util.font;

import android.content.Context;
import android.graphics.Typeface;

import java.util.HashMap;

/**
 * Created by parade岁月 on 2019/3/27 9:45
 */
public class FontCache {
    private static HashMap<String, Typeface> fontCache = new HashMap<>();
    public static Typeface getTypeface(String fontname, Context context) {
        Typeface typeface = fontCache.get(fontname);
        if (typeface == null) {
            try {
                typeface = Typeface.createFromAsset(context.getAssets(), fontname);
            } catch (Exception e) {
                return null;
            }
            fontCache.put(fontname, typeface);
        }
        return typeface;
    }
}
