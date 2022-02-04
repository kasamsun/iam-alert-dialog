package com.kswafx.iamalertdialog;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.StateListDrawable;
import android.view.View;

public class ViewUtils {

    static Drawable[] getDrawable(View view) {
        StateListDrawable drawable = (StateListDrawable) view.getBackground();
        DrawableContainer.DrawableContainerState dcs = (DrawableContainer.DrawableContainerState) drawable.getConstantState();
        if (dcs != null) {
            return dcs.getChildren();
        }
        return null;
    }

    public static int dpToPx(float dp) {
        float density = Resources.getSystem().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    public static float pxToDp(float px) {
        float densityDpi = Resources.getSystem().getDisplayMetrics().densityDpi;
        return px / (densityDpi / 160f);
    }

}