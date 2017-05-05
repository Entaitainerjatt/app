package com.entertainerJatt.app.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.entertainerJatt.app.android.adapters.LocalSongsAdapter;
import com.entertainerJatt.app.android.database.DatabaseHelper;
import com.entertainerJatt.app.android.entity.UserInfo;
import com.entertainerJatt.app.android.util.Global;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sony on 3/28/2017.
 */

public class LocalSongsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DatabaseHelper databaseHelper;
    private Context context;
    private ArrayList<HashMap<String, String>> songsLists = new ArrayList<>();
    private ArrayList<HashMap<String, String>> songsHiddenList = new ArrayList<>();
    private List<UserInfo> songsList;
    private Global global;
    private TextView oppsText;

    ProgressBar progressBar;

    private static final int PAGE_START = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 1;
    private int currentPage = PAGE_START;

    private LocalSongsAdapter localSongsAdapter;
    int i;
    int j = 10;
    public Comparator<UserInfo> CartComparator = new Comparator<UserInfo>() {
        public int compare(UserInfo m1, UserInfo m2) {

            return m1.getTime().compareTo(m2.getTime());
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localsongs);
        asssignIds();
    }

    private void asssignIds() {
        context = this;
        global = (Global) getApplicationContext();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        oppsText = (TextView)findViewById(R.id.oppsText);
        databaseHelper = new DatabaseHelper(context);
        songsList = databaseHelper.getAllSongs();


        Collections.sort(songsList, CartComparator);
        if (songsList.size() <= 10) {
            TOTAL_PAGES = 0;
        } else {
            String totalPages = String.valueOf(songsList.size());
            if (totalPages.length() > 1) {
                String lastChar = totalPages.substring(totalPages.length() - 1);
                totalPages = totalPages.substring(0, totalPages.length() - 1);
                if (lastChar.equalsIgnoreCase("0")) {
                    int finalv = Integer.parseInt(totalPages);
                    TOTAL_PAGES = finalv;
                } else {
                    int finalv = Integer.parseInt(totalPages) + 1;
                    TOTAL_PAGES = finalv;
                }
            }
            TOTAL_PAGES = TOTAL_PAGES - 1;

        }


        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
//        recyclerView.getLayoutManager().scrollToPosition(0);
        try {
            HashMap<String, String> songsHashMap;
            for (i = songsList.size() - 1; i >= 0; i--) {
                songsHashMap = new HashMap<>();
                String fav = songsList.get(i).getFav();
                JSONObject object = new JSONObject(songsList.get(i).getJsonString());
                String id = object.getString("id");
                String time = songsList.get(i).getTime();
                String type = object.getString("type");
                String title = object.getString("title");
                String image = object.getString("image");
                if (object.has("teaser_link")) {
                    String teaser_link = object.getString("teaser_link");
                    songsHashMap.put("teaser_link", teaser_link);
                } else {
                    songsHashMap.put("teaser_link", null);
                }

                songsHashMap.put("id", id);
                songsHashMap.put("fav", fav);
                songsHashMap.put("object", songsList.get(i).getJsonString());
                songsHashMap.put("time", time.split(" ")[0]);
                songsHashMap.put("type", type);
                songsHashMap.put("title", title);
                songsHashMap.put("image", image);
                songsHiddenList.add(songsHashMap);

                //  if (songsList.size() - i <= 10) {
                songsLists.add(songsHashMap);
                //}


            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        if(songsLists.size()>0){
            oppsText.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            localSongsAdapter = new LocalSongsAdapter(context, songsLists, recyclerView);

            GridLayoutManager manager = new GridLayoutManager(context, 1);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(localSongsAdapter);
            localSongsAdapter.SetOnItemClickListener(new LocalSongsAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    String id = songsLists.get(position).get("id");
                    global.setVideoId(id);
                    String Url = songsLists.get(position).get("teaser_link");
                    if (Url != null && !Url.equalsIgnoreCase("null")) {
                        Url = Url.split("=")[1];
                        Url = Url.split("&")[0];
                        global.setURL(Url);
                        startActivity(new Intent(context, DetailPageActivity.class));
                    } else {
                        global.setURL(null);
                        startActivity(new Intent(context, DetailPageActivity.class));
                    }
                }
            });
        }else{
            oppsText.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }




//        recyclerView.addOnScrollListener(new PaginationScrollListener(manager) {
//            @Override
//            protected void loadMoreItems() {
//                isLoading = true;
//                currentPage += 1;
//
//                // mocking network delay for API call
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (TOTAL_PAGES != 0) {
//                            loadNextPage();
//                        }
//                    }
//                }, 1000);
//            }
//
//            @Override
//            public int getTotalPageCount() {
//                return TOTAL_PAGES;
//            }
//
//            @Override
//            public boolean isLastPage() {
//                return isLastPage;
//            }
//
//            @Override
//            public boolean isLoading() {
//                return isLoading;
//            }
//        });
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


    private void loadNextPage() {
        try {
            HashMap<String, String> songsHashMap;
            for (int i = 0; i < songsHiddenList.size(); i++) {
                songsHashMap = new HashMap<>();
                if (!songsLists.contains(songsHiddenList.get(i))) {
                    JSONObject object = new JSONObject(songsHiddenList.get(i).get("object"));
                    String id = object.getString("id");
                    String time = songsHiddenList.get(i).get("time");
                    String type = object.getString("type");
                    String title = object.getString("title");
                    String image = object.getString("image");
                    if (object.has("teaser_link")) {
                        String teaser_link = object.getString("teaser_link");
                        songsHashMap.put("teaser_link", teaser_link);
                    } else {
                        songsHashMap.put("teaser_link", null);
                    }
                    songsHashMap.put("id", id);
                    songsHashMap.put("time", time.split(" ")[0]);
                    songsHashMap.put("type", type);
                    songsHashMap.put("title", title);
                    songsHashMap.put("image", image);
                    songsLists.add(songsHashMap);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        isLoading = false;
        Toast.makeText(context, "" + localSongsAdapter.getItemCount(), Toast.LENGTH_SHORT).show();

        localSongsAdapter.addAll(songsLists);
        if (currentPage != TOTAL_PAGES) localSongsAdapter.addLoadingFooter();
        else isLastPage = true;
    }

}
