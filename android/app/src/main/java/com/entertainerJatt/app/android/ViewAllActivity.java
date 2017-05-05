package com.entertainerJatt.app.android;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.entertainerJatt.app.android.fragments.LatestFragment;
import com.entertainerJatt.app.android.fragments.UpcominingFragment;
import com.entertainerJatt.app.android.util.ApplicationConstants;
import com.entertainerJatt.app.android.util.Global;

/**
 * Created by Imbibian on 3/22/2017.
 */

public class ViewAllActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private TabsAdapter adapter;
    private ViewPager viewPager;
    String[] data;
    String type;
    Spinner spinner;
    private Context context;
    Global global;
    private ImageView searchImageView;

    public static boolean isFirstTimeHitOccur = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewall);
        assignIds();
    }

    private void assignIds() {
        context = this;
        isFirstTimeHitOccur = false;
        global = (Global) getApplicationContext();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tabLayout = (TabLayout) findViewById(R.id.materialup_tabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        adapter = new TabsAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        searchImageView = (ImageView) findViewById(R.id.searchImageView);
        searchImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, SearchActivity.class));
            }
        });
        type = getIntent().getStringExtra("type");
        global.setNodeType(type);

        data = new String[4];
        data[0] = "Albums";
        data[1] = "Artists";
        data[2] = "Movies";
        data[3] = "Lyrists";

        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.getBackground().setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,
                R.layout.row_spinner, data);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);

        if (type.equalsIgnoreCase("album")) {
            spinner.setSelection(0);
            ApplicationConstants.GET_MIX_DATA = "Node.GetAlbums";
        } else if (type.equalsIgnoreCase("movie")) {
            ApplicationConstants.GET_MIX_DATA = "Node.GetMovies";
            spinner.setSelection(2);
        } else if (type.equalsIgnoreCase("artist")) {
            ApplicationConstants.GET_MIX_DATA = "Node.GetArtists";
            spinner.setSelection(1);
        } else if (type.equalsIgnoreCase("Lyrists")) {
            ApplicationConstants.GET_MIX_DATA = "Node.GetLyrists";
            spinner.setSelection(3);
        }
        DisplayMetrics d = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(d);
        int wdt = d.widthPixels;
        spinner.setLayoutParams(new RelativeLayout.LayoutParams(wdt / 3, ViewGroup.LayoutParams.WRAP_CONTENT));


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    String node_type = (String) spinner.getItemAtPosition(i);


                    if (isFirstTimeHitOccur) {
                        if (node_type.equalsIgnoreCase("Albums")) {
                            global.setNodeType("album");
                            ApplicationConstants.GET_MIX_DATA = "Node.GetAlbums";
                            viewPager.setAdapter(adapter);
                        } else if (node_type.equalsIgnoreCase("Movies")) {
                            ApplicationConstants.GET_MIX_DATA = "Node.GetMovies";
                            global.setNodeType("movie");
                            viewPager.setAdapter(adapter);
                        }
                    }
                    if (node_type.equalsIgnoreCase("Artists")) {
                        startActivity(new Intent(context, PerformerActivity.class).putExtra("type", "performer"));
                        global.setNodeType("artist");

                    } else if (node_type.equalsIgnoreCase("Lyrists")) {
                        global.setNodeType("lyrist");
                        startActivity(new Intent(context, PerformerActivity.class).putExtra("type", "lyricist"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    class TabsAdapter extends FragmentStatePagerAdapter {

        public TabsAdapter(FragmentManager fm) {
            super(fm);

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);

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
                    fragment = new LatestFragment();
                    break;
                case 1:
                    fragment = new UpcominingFragment();

                    break;
            }
            return fragment;


        }


        @Override
        public CharSequence getPageTitle(int position) {
            String returnString = null;
            switch (position) {
                case 0:
                    returnString = "LATEST";
                    break;
                case 1:
                    returnString = "UPCOMING";
                    break;
            }
            return returnString;
        }
    }

}
