package com.entertainerJatt.app.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

import com.entertainerJatt.app.android.util.Global;
import com.entertainerJatt.app.android.util.Util;

/**
 * Created by Imbibian on 3/20/2017.
 */

public class SplashActivity extends AppCompatActivity {
    private Context context;
private Global global;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        context = this;
        global = (Global)getApplicationContext();
        getScreenSize();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
                if (Util.getUserName(context) != null) {

                    startActivity(new Intent(context, MainActivity.class));
                } else {
                    startActivity(new Intent(context, MobileVerfificationActivity.class));

                }
            }
        }, 500);

    }


    private void getScreenSize() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        Util.setHeight(context, height);


       String unique_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);


//
    }
}
