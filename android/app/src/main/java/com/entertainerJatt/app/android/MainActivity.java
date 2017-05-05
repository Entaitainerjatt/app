package com.entertainerJatt.app.android;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.entertainerJatt.app.android.models.Country;
import com.entertainerJatt.app.android.util.ApplicationConstants;
import com.entertainerJatt.app.android.util.Util;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

/**
 * Created by Imbibian on 4/11/2017.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private ImageView imageMore;
    private LinearLayout layoutHome;
    private TextView textMore;
    private TextView textUpdate;
    private TextView textFav;
    private Context context;
    private TextView textHome;
    private TextView textDir;
    private ImageView imageDir;
    private ImageView imageUpdate;
    private ImageView imageHome;
    private ImageView imageFav;
    private LinearLayout layoutDirectory;
    private LinearLayout layoutFav;
    private LinearLayout layoutMore;
    private RelativeLayout layoutUpdates;


    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        context = this;
        layoutDirectory = (LinearLayout) findViewById(R.id.layoutDirectory);
        layoutMore = (LinearLayout) findViewById(R.id.layoutMore);
        layoutUpdates = (RelativeLayout) findViewById(R.id.layoutUpdates);
        layoutHome = (LinearLayout) findViewById(R.id.layoutHome);
        layoutFav = (LinearLayout) findViewById(R.id.layoutFav);
        imageMore = (ImageView) findViewById(R.id.imageMore);
        imageUpdate = (ImageView) findViewById(R.id.imageUpdate);
        imageFav = (ImageView) findViewById(R.id.imageFav);
        imageHome = (ImageView) findViewById(R.id.imageHome);
        imageDir = (ImageView) findViewById(R.id.imageDir);
        if (Build.VERSION.SDK_INT >= 21) {
            layoutMore.setBackgroundResource(R.drawable.ripple);
            layoutUpdates.setBackgroundResource(R.drawable.ripple);
            layoutDirectory.setBackgroundResource(R.drawable.ripple);
            layoutFav.setBackgroundResource(R.drawable.ripple);
            layoutHome.setBackgroundResource(R.drawable.ripple);
        }
        textUpdate = (TextView) findViewById(R.id.textUpdate);
        textFav = (TextView) findViewById(R.id.textFav);
        textMore = (TextView) findViewById(R.id.textMore);
        textHome = (TextView) findViewById(R.id.textHome);
        textDir = (TextView) findViewById(R.id.textDir);
        listners();

        Fragment fragment = new HomeActivity();
        replaceFragment(fragment);

    }

    private void listners() {
        layoutDirectory.setOnClickListener(this);
        layoutFav.setOnClickListener(this);
        layoutMore.setOnClickListener(this);
        layoutUpdates.setOnClickListener(this);
        layoutHome.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Fragment fragment = null;
        switch (view.getId()) {

            case R.id.layoutDirectory:

                if (HomeActivity.player != null) {
                    if (HomeActivity.player.isPlaying()) {
                        HomeActivity.player.pause();
                    }
                }
                ApplicationConstants.isHomeFragment = false;
                imageDir.setImageResource(R.mipmap.directory_white);
                textDir.setTextColor(Color.parseColor("#FFFFFF"));

                imageMore.setImageResource(R.mipmap.more_red);
                textMore.setTextColor(Color.parseColor("#E6767E"));

                imageHome.setImageResource(R.mipmap.home_red);
                textHome.setTextColor(Color.parseColor("#E6767E"));

                textUpdate.setTextColor(Color.parseColor("#E6767E"));
                imageUpdate.setImageResource(R.mipmap.updates);

                textFav.setTextColor(Color.parseColor("#E6767E"));
                imageFav.setImageResource(R.mipmap.fav_red);
                fragment = new DirectoryActivity();
                replaceFragment(fragment);

                break;
            case R.id.layoutHome:
                if (HomeActivity.player == null) {
//                    if (!HomeActivity.player.isPlaying()) {
//                    }
                    imageHome.setImageResource(R.mipmap.home_white);
                    textHome.setTextColor(Color.parseColor("#FFFFFF"));

                    imageMore.setImageResource(R.mipmap.more_red);
                    textMore.setTextColor(Color.parseColor("#E6767E"));

                    imageUpdate.setImageResource(R.mipmap.updates);
                    textUpdate.setTextColor(Color.parseColor("#E6767E"));

                    textDir.setTextColor(Color.parseColor("#E6767E"));
                    imageDir.setImageResource(R.mipmap.directory_red);

                    textFav.setTextColor(Color.parseColor("#E6767E"));
                    imageFav.setImageResource(R.mipmap.fav_red);
                    Fragment fragment1 = new HomeActivity();
                    replaceFragment(fragment1);
                } else {
                    HomeActivity.scrollToTop();
                }

                break;
            case R.id.layoutFav:
                if (HomeActivity.player != null) {
                    if (HomeActivity.player.isPlaying()) {
                        HomeActivity.player.pause();
                    }
                }
                ApplicationConstants.isHomeFragment = false;

                imageFav.setImageResource(R.mipmap.fav_white);
                textFav.setTextColor(Color.parseColor("#FFFFFF"));

                imageMore.setImageResource(R.mipmap.more_red);
                textMore.setTextColor(Color.parseColor("#E6767E"));

                imageHome.setImageResource(R.mipmap.home_red);
                textHome.setTextColor(Color.parseColor("#E6767E"));

                textDir.setTextColor(Color.parseColor("#E6767E"));
                imageDir.setImageResource(R.mipmap.directory_red);

                textUpdate.setTextColor(Color.parseColor("#E6767E"));
                imageUpdate.setImageResource(R.mipmap.updates);
                fragment = new FavoritesActivity();
                replaceFragment(fragment);

                break;
            case R.id.layoutMore:
                if (HomeActivity.player != null) {
                    if (HomeActivity.player.isPlaying()) {
                        HomeActivity.player.pause();
                    }
                }
                ApplicationConstants.isHomeFragment = false;
                imageMore.setImageResource(R.mipmap.more_white);
                textMore.setTextColor(Color.parseColor("#FFFFFF"));

                imageUpdate.setImageResource(R.mipmap.updates);
                textUpdate.setTextColor(Color.parseColor("#E6767E"));

                imageHome.setImageResource(R.mipmap.home_red);
                textHome.setTextColor(Color.parseColor("#E6767E"));

                textDir.setTextColor(Color.parseColor("#E6767E"));
                imageDir.setImageResource(R.mipmap.directory_red);

                textFav.setTextColor(Color.parseColor("#E6767E"));
                imageFav.setImageResource(R.mipmap.fav_red);

                fragment = new AboutUsActivity();
                replaceFragment(fragment);

                break;
            case R.id.layoutUpdates:
                try {
                    Util.setUpdateCount(context , 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (HomeActivity.player != null) {
                    if (HomeActivity.player.isPlaying()) {
                        HomeActivity.player.pause();
                    }
                }
                ApplicationConstants.isHomeFragment = false;

                imageUpdate.setImageResource(R.mipmap.updates_white);
                textUpdate.setTextColor(Color.parseColor("#FFFFFF"));

                imageMore.setImageResource(R.mipmap.more_red);
                textMore.setTextColor(Color.parseColor("#E6767E"));

                imageHome.setImageResource(R.mipmap.home_red);
                textHome.setTextColor(Color.parseColor("#E6767E"));

                textDir.setTextColor(Color.parseColor("#E6767E"));
                imageDir.setImageResource(R.mipmap.directory_red);

                textFav.setTextColor(Color.parseColor("#E6767E"));
                imageFav.setImageResource(R.mipmap.fav_red);
                fragment = new UpdateActivity();
                replaceFragment(fragment);

                break;
        }

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                        System.exit(0);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void replaceFragment(Fragment fragment) {
//        String backStateName = fragment.getClass().getName();
        FragmentManager manager = getSupportFragmentManager();
//        manager.popBackStack();
//        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);
        FragmentTransaction ft = manager.beginTransaction();
//        //   ft.show()
//
//        if (!fragmentPopped) {
        ft.replace(R.id.openFragmentLayout, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//            ft.addToBackStack(backStateName);
        ft.commit();
//        }
    }


}
