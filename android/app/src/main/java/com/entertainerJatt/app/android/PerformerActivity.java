package com.entertainerJatt.app.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.entertainerJatt.app.android.adapters.PerformerAdapter;
import com.entertainerJatt.app.android.asyncTask.LatestSongsAsynTask;
import com.entertainerJatt.app.android.entity.UserInfo;
import com.entertainerJatt.app.android.interfaces.IAsyncTask;
import com.entertainerJatt.app.android.util.ApplicationConstants;
import com.entertainerJatt.app.android.util.Global;
import com.entertainerJatt.app.android.util.PaginationScrollListener;
import com.entertainerJatt.app.android.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sony on 4/5/2017.
 */

public class PerformerActivity extends AppCompatActivity implements IAsyncTask {
    private Context context;
    Global global;
    private TextView notAvailableTextView;
    private RecyclerView recyclerView;
    private TextView HeaderTextView;
    private LinearLayout progressLayout;
    private int start = 0;
    private int end = 10;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 1;
    private boolean isFirstTimeAdapter = true;
    private GridLayoutManager manager;
    private PerformerAdapter adapter = null;

    private ImageView searchImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performer);
        context = this;
        global = (Global) getApplicationContext();
        String type = getIntent().getStringExtra("type");

        HeaderTextView = (TextView) findViewById(R.id.HeaderTextView);
        if (type.equalsIgnoreCase("performer")) {
            global.setNodeType("artist");
            HeaderTextView.setText("Performers       ");
            ApplicationConstants.GET_MIX_DATA = "Node.GetArtists";
        } else {
            HeaderTextView.setText("Lyrists         ");
            global.setNodeType("lyrist");
            ApplicationConstants.GET_MIX_DATA = "Node.GetLyrists";
        }
        searchImageView = (ImageView) findViewById(R.id.searchImageView);
        searchImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, SearchActivity.class));
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        progressLayout = (LinearLayout) findViewById(R.id.progressLayout);
        notAvailableTextView = (TextView) findViewById(R.id.notAvailableTextView);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        manager = new GridLayoutManager(context, 1);
        recyclerView.setLayoutManager(manager);
        try {
            getSongs(start, end);
        } catch (Exception e) {
            e.printStackTrace();
        }

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

    private void getSongs(int start, int end) throws Exception {

        UserInfo userInfo = new UserInfo();
        userInfo.setCallbackToken(Util.getUserToken(context));
        userInfo.setUsername(Util.getUserName(context));
        userInfo.setStart(String.valueOf(start));
        userInfo.setNode_type(global.getNodeType());
        userInfo.setLimit(String.valueOf(end));
        LatestSongsAsynTask latestSongsAsynTask = new LatestSongsAsynTask(userInfo, PerformerActivity.this);
        latestSongsAsynTask.execute();
    }

    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    public void OnPreExecute() {
        if (isFirstTimeAdapter) {
            Util.showDialog(context);
        }
    }

    @Override
    public void OnPostExecute(String URL, JSONObject jsonObject) {
        Util.dismissDialog();
        HashMap<String, String> songsHashMap;
        progressLayout.setVisibility(View.GONE);
        try {
            ViewAllActivity.isFirstTimeHitOccur = true;
            final ArrayList<HashMap<String, String>> AlbumssongArrayList = new ArrayList<>();
            String acount = jsonObject.getString("count");
            final int count = Integer.parseInt(acount);
            if (count <= 10) {
                TOTAL_PAGES = 0;
            } else {
                String totalPages = String.valueOf(count);
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
            JSONArray jsonArray = jsonObject.getJSONArray("nodes");
            for (int i = 0; i < jsonArray.length(); i++) {
                songsHashMap = new HashMap<>();
                JSONObject object = jsonArray.getJSONObject(i);
                String id = object.getString("id");
                String type = object.getString("type");
                String title = object.getString("title");
                String image = object.getString("image");
                if (object.has("teaser_link")) {
                    String teaser_link = object.getString("teaser_link");
                    songsHashMap.put("teaser_link", teaser_link);
                } else {
                    songsHashMap.put("teaser_link", null);
                }
                if (object.has("video_link")) {
                    String teaser_link = object.getString("video_link");
                    songsHashMap.put("teaser_link", teaser_link);
                } else {
                    songsHashMap.put("teaser_link", null);
                }
                if (object.has("release_date")) {
                    String release_date = object.getString("release_date");
                    songsHashMap.put("release_date", release_date);
                } else {
                    songsHashMap.put("release_date", null);
                }
                if (object.has("body")) {
                    String body = object.getString("body");
                    body = Html.fromHtml(body).toString();
                    songsHashMap.put("body", body);
                } else {
                    songsHashMap.put("body", null);
                }
                if (object.has("artist_name")) {
                    String artist_name = object.getString("artist_name");
                    songsHashMap.put("artist_name", artist_name);
                } else {
                    songsHashMap.put("artist_name", null);
                }
                songsHashMap.put("fav", "false");
                songsHashMap.put("id", id);
                songsHashMap.put("type", type);
                songsHashMap.put("title", title);
                songsHashMap.put("image", image);
                songsHashMap.put("object", object.toString());
                AlbumssongArrayList.add(songsHashMap);

            }
            if (AlbumssongArrayList.size() > 0) {
                if (isFirstTimeAdapter) {
                    isFirstTimeAdapter = false;
                    adapter = new PerformerAdapter(context, AlbumssongArrayList);
                    recyclerView.setAdapter(adapter);
                } else {
                    adapter.updateAdapter(AlbumssongArrayList);
                }
                adapter.SetOnItemClickListener(new PerformerAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        String id = AlbumssongArrayList.get(position).get("id");
                        global.setVideoId(id);
                        String Url = AlbumssongArrayList.get(position).get("teaser_link");
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
                notAvailableTextView.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.addOnScrollListener(new PaginationScrollListener(manager) {
                    @Override
                    protected void loadMoreItems() {
                        if (count != AlbumssongArrayList.size()) {
                            end = end + 10;
                            try {
                                progressLayout.setVisibility(View.VISIBLE);
                                getSongs(start, end);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            isLastPage = true;
                        }

                    }

                    @Override
                    public int getTotalPageCount() {
                        Log.i("TOTAL : ", TOTAL_PAGES + "");
                        return TOTAL_PAGES;
                    }

                    @Override
                    public boolean isLastPage() {
                        return isLastPage;
                    }

                    @Override
                    public boolean isLoading() {
                        return false;
                    }
                });

            } else {
                notAvailableTextView.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void OnErrorMessage(String Message) {
        Util.dismissDialog();
        Toast.makeText(context, Message, Toast.LENGTH_SHORT).show();
    }
}
