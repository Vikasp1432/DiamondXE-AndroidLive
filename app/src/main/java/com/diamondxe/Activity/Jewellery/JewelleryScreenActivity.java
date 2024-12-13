package com.diamondxe.Activity.Jewellery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.diamondxe.Interface.RecyclerInterface;
import com.diamondxe.Network.SuperActivity;
import com.diamondxe.R;
import com.diamondxe.Utils.Utils;

import org.json.JSONObject;

public class JewelleryScreenActivity extends SuperActivity implements RecyclerInterface {

    private ImageView back_img;
    TextView title_tv;
    private Activity activity;
    private Context context;
    private String intenttype="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jewellery_screen);

        context = activity = this;
        title_tv=findViewById(R.id.title_tv);
        Intent intent = getIntent();
        String intentValue = null;

        if (intent != null) {
            intentValue = intent.getStringExtra("intentvalue");
        }
        if (intentValue != null) {
            intenttype=intentValue;
            Log.e("Search result", "Received intent value: " + intentValue);
        } else {
            Log.e("IntentValue", "No intent value received.");
        }
        if(intenttype.equals("jewellery"))
        {
            title_tv.setText("Jewellery");
        }
        else
        {
            title_tv.setText("Gemstones");
        }


        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        Intent intent;
        int id = view.getId();

        if(id == R.id.back_img)
        {
            Utils.hideKeyboard(activity);
            finish();
        }
    }


    @Override
    public void getSuccessResponce(JSONObject jsonObject, int service_ID) {

    }

    @Override
    public void getErrorResponce(String error, int service_ID) {

    }

    @Override
    public void itemClick(int position, String action) {

    }
}