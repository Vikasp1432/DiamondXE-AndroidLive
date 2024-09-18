package com.diamondxe.Utils;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.diamondxe.R;

public class CurvedBottomNavigationView extends View {
    private Path path;
    private Paint paint;

    private Paint shadowPaint;

    public CurvedBottomNavigationView(Context context) {
        super(context);
        init();
    }

    public CurvedBottomNavigationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CurvedBottomNavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        path = new Path();
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(getResources().getColor(R.color.white)); // Set your desired color

        shadowPaint = new Paint();
        shadowPaint.setStyle(Paint.Style.FILL);
        shadowPaint.setColor(getResources().getColor(R.color.shadow_color)); // Shadow color should match the view color
        shadowPaint.setShadowLayer(10, 0, 5, getResources().getColor(R.color.shadow_color)); // Adjust shadow parameters
        setLayerType(LAYER_TYPE_SOFTWARE, shadowPaint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();

        path.reset();
        path.moveTo(0, 0);
        path.lineTo(width, 0);
        path.lineTo(width, height - 1300); // 50 is the depth of the curve, adjust as needed

        path.quadTo(
                width / 2, height + 1300, // Control point
                0, height - 1300); // End point
        path.lineTo(0, 0);
        path.close();

        canvas.drawPath(path, paint);
    }
}

