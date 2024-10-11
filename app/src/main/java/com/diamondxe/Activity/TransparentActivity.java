package com.diamondxe.Activity;

import static com.diamondxe.Activity.HomeScreenActivity.context;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.diamondxe.R;


public class TransparentActivity extends Activity {

    private ImageView loader_image_View;
    public static Activity ctx;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transparent);

        loader_image_View = findViewById(R.id.loader_image_View);

        ctx = TransparentActivity.this;

        String path = "android.resource://com.diamondxe/"+R.raw.diamond_logo;

        // Set static dimensions for debugging
        loader_image_View.getLayoutParams().width = 500; // Set a static width
        loader_image_View.getLayoutParams().height = 500; // Set a static height
        loader_image_View.requestLayout(); // Apply the changes

        loader_image_View.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                // Remove listener to avoid multiple calls
                loader_image_View.removeOnLayoutChangeListener(this);

                int width = loader_image_View.getWidth();
                int height = loader_image_View.getHeight();

                // Log dimensions
                Log.e("TransparentActivity", "ImageView dimensions: Width: " + width + ", Height: " + height);
            }
        });

        Glide.with(ctx)
                .asGif()
                .load(Uri.parse(path))
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC) // or .diskCacheStrategy(DiskCacheStrategy.NONE) if needed
                .skipMemoryCache(true) // optional, depending on your caching strategy
                .into(loader_image_View);
    }

    public static boolean active = false;

    @Override
    public void onStart() {
        super.onStart();
        active = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        active = false;
    }

    public static void terminateTransparentActivity() {
        if (ctx != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ctx.finish();
                }
            }, 300);
        }
    }

    @Override
    public void onBackPressed() {
//        if(ctx==null)
//        super.onBackPressed();
    }
}