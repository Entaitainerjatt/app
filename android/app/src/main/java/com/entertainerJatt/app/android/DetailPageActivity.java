package com.entertainerJatt.app.android;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.entertainerJatt.app.android.fragments.InformationFragment;
import com.entertainerJatt.app.android.fragments.RelatedVideosFragment;
import com.entertainerJatt.app.android.util.ApplicationConstants;
import com.entertainerJatt.app.android.util.Global;
import com.entertainerJatt.app.android.util.Util;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubePlayerView;

/**
 * Created by Imbibian on 3/25/2017.
 */

public class DetailPageActivity extends AppCompatActivity implements YouTubePlayer.OnInitializedListener {
    private Context context;
    private YouTubePlayerView youTubeView;
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private YouTubePlayer youTubePlayer;
    private Global global;
    private int height;
    private ImageView artistImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_page);
        assignIds();
    }


    private void assignIds() {
        context = this;
        height = Util.getHeight(context);
        height = height / 3;

        global = (Global) getApplicationContext();
        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        youTubeView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
        youTubeView.initialize(ApplicationConstants.DEVELOPER_KEY, this);
        artistImage = (ImageView) findViewById(R.id.artistImage);
        youTubeView.setVisibility(View.GONE);
        viewPager = (ViewPager) findViewById(R.id.viewpager);


        tabLayout = (TabLayout) findViewById(R.id.tabs);

        TabsAdapter adapter = new TabsAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        YouTubePlayerSupportFragment youTubePlayerFragment = (YouTubePlayerSupportFragment) getSupportFragmentManager()
                .findFragmentById(R.id.youtube_view);
        youTubePlayerFragment.initialize(ApplicationConstants.DEVELOPER_KEY, this);

    }


    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                        YouTubePlayer player, boolean wasRestored) {
        if (!wasRestored) {
            // loadVideo() will auto play video
            // Use cueVideo() method, if you don't want to play it automatically
            youTubePlayer = player;
            // Hiding player controls
            player.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {

            Toast.makeText(this, errorReason.toString(), Toast.LENGTH_LONG).show();
        }
    }


    class TabsAdapter extends FragmentStatePagerAdapter {

        public TabsAdapter(FragmentManager fm) {
            super(fm);

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = null;

            switch (i) {
                case 0:
                    fragment = new InformationFragment();
                    break;
                case 1:
                    fragment = new RelatedVideosFragment();
                    break;
            }
            return fragment;


        }

        @Override
        public CharSequence getPageTitle(int position) {
            String returnString = null;
            switch (position) {
                case 0:
                    returnString = "INFORMATION";
                    break;
                case 1:
                    returnString = "RELATED VIDEOS";
                    break;
            }
            return returnString;
        }
    }

    public void plaVideo() {
        youTubeView.setVisibility(View.VISIBLE);
        artistImage.setVisibility(View.GONE);
        String Url = global.getURL();
        if (youTubePlayer != null) {
            if (youTubePlayer.isPlaying()) {
                youTubePlayer.pause();
            }
            youTubePlayer.loadVideo(Url);
        }
    }


    public void setImage(String ImageUrl) {
        youTubeView.setVisibility(View.GONE);
        artistImage.setVisibility(View.VISIBLE);
        artistImage.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
        Glide.with(context)
                .load(ImageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)

                .error(R.mipmap.default_small)
                .into(artistImage);
    }

    @Override //reconfigure display properties on screen rotation
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        //Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (youTubePlayer != null) {
                youTubePlayer.setFullscreen(false);
            }
            // handle change here
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (youTubePlayer != null) {
                youTubePlayer.setFullscreen(true);

            }

            // or here
        }
    }

}
