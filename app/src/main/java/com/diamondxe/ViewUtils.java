package com.diamondxe;

import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.core.content.ContextCompat;

public class ViewUtils
{
    public static Drawable generateBackgroundWithShadow(View view, @ColorRes int backgroundColor,
                                                        @DimenRes int cornerRadius,
                                                        @ColorRes int shadowColor,
                                                        @DimenRes int elevation,
                                                        int shadowGravity) {
        // Retrieve dimensions and colors from resources
        float cornerRadiusValue = view.getContext().getResources().getDimension(cornerRadius);
        int elevationValue = (int) view.getContext().getResources().getDimension(elevation);
        int shadowColorValue = ContextCompat.getColor(view.getContext(), shadowColor);
        int backgroundColorValue = ContextCompat.getColor(view.getContext(), backgroundColor);

        // Define the outer radius for the rounded corners
        float[] outerRadius = {cornerRadiusValue, cornerRadiusValue, cornerRadiusValue,
                cornerRadiusValue, cornerRadiusValue, cornerRadiusValue, cornerRadiusValue,
                cornerRadiusValue};

        // Create a new Paint object for background and shadow
        Paint backgroundPaint = new Paint();
        backgroundPaint.setStyle(Paint.Style.FILL);
        backgroundPaint.setShadowLayer(cornerRadiusValue, 0, 0, 0);

        // Define padding for the ShapeDrawable
        Rect shapeDrawablePadding = new Rect();
        shapeDrawablePadding.left = elevationValue;
        shapeDrawablePadding.right = elevationValue;

        int DY;
        switch (shadowGravity) {
            case Gravity.CENTER:
                shapeDrawablePadding.top = elevationValue;
                shapeDrawablePadding.bottom = elevationValue;
                DY = 0;
                break;
            case Gravity.TOP:
                shapeDrawablePadding.top = elevationValue * 2;
                shapeDrawablePadding.bottom = elevationValue;
                DY = -1 * elevationValue / 3;
                break;
            default:
            case Gravity.BOTTOM:
                shapeDrawablePadding.top = elevationValue;
                shapeDrawablePadding.bottom = elevationValue * 2;
                DY = elevationValue / 3;
                break;
        }

        // Create a new ShapeDrawable with specified padding and shape (rounded rectangle)
        ShapeDrawable shapeDrawable = new ShapeDrawable();
        shapeDrawable.setPadding(shapeDrawablePadding);

        // Set the background color and apply shadow layer to the ShapeDrawable
        shapeDrawable.getPaint().setColor(backgroundColorValue);
        shapeDrawable.getPaint().setShadowLayer(cornerRadiusValue / 3, 0, DY, shadowColorValue);

        // Set the shape of the ShapeDrawable (rounded rectangle)
        shapeDrawable.setShape(new RoundRectShape(outerRadius, null, null));

        // Create a LayerDrawable with the ShapeDrawable as the content
        LayerDrawable drawable = new LayerDrawable(new Drawable[]{shapeDrawable});
        drawable.setLayerInset(0, elevationValue * 0, elevationValue * 0, elevationValue * 0, elevationValue * 2);

        // Set software layer type to ensure shadow layer is rendered properly
        view.setLayerType(View.LAYER_TYPE_SOFTWARE, shapeDrawable.getPaint());

        // Return the LayerDrawable containing the shadowed background
        return drawable;
    }

}

/*public class ViewUtils {
    public static Drawable generateBackgroundWithBottomShadow(View view, @ColorRes int backgroundColor,
                                                              @DimenRes int cornerRadius,
                                                              @ColorRes int shadowColor,
                                                              @DimenRes int elevation) {
        float cornerRadiusValue = view.getContext().getResources().getDimension(cornerRadius);
        int elevationValue = (int) view.getContext().getResources().getDimension(elevation);
        int shadowColorValue = ContextCompat.getColor(view.getContext(), shadowColor);
        int backgroundColorValue = ContextCompat.getColor(view.getContext(), backgroundColor);

        float[] outerRadius = {cornerRadiusValue, cornerRadiusValue, cornerRadiusValue,
                cornerRadiusValue, cornerRadiusValue, cornerRadiusValue, cornerRadiusValue,
                cornerRadiusValue};

        Paint backgroundPaint = new Paint();
        backgroundPaint.setStyle(Paint.Style.FILL);
        backgroundPaint.setShadowLayer(cornerRadiusValue, 0, 0, 0);

        Rect shapeDrawablePadding = new Rect();
        shapeDrawablePadding.left = 0;
        shapeDrawablePadding.right = 0;
        shapeDrawablePadding.top = 0;
        shapeDrawablePadding.bottom = elevationValue;

        ShapeDrawable shapeDrawable = new ShapeDrawable();
        shapeDrawable.setPadding(shapeDrawablePadding);

        shapeDrawable.getPaint().setColor(backgroundColorValue);
        shapeDrawable.getPaint().setShadowLayer(cornerRadiusValue / 3, 0, elevationValue / 3, shadowColorValue);

        view.setLayerType(View.LAYER_TYPE_SOFTWARE, shapeDrawable.getPaint());

        shapeDrawable.setShape(new RoundRectShape(outerRadius, null, null));

        LayerDrawable drawable = new LayerDrawable(new Drawable[]{shapeDrawable});
        drawable.setLayerInset(0, 0, 0, 0, elevationValue);

        return drawable;
    }
}*/
