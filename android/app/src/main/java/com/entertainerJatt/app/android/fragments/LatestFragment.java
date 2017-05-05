package com.entertainerJatt.app.android.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.entertainerJatt.app.android.DetailPageActivity;
import com.entertainerJatt.app.android.HomeActivity;
import com.entertainerJatt.app.android.R;
import com.entertainerJatt.app.android.ViewAllActivity;
import com.entertainerJatt.app.android.adapters.LatestAdapter;
import com.entertainerJatt.app.android.asyncTask.LatestSongsAsynTask;
import com.entertainerJatt.app.android.database.DatabaseHelper;
import com.entertainerJatt.app.android.entity.UserInfo;
import com.entertainerJatt.app.android.interfaces.IAsyncTask;
import com.entertainerJatt.app.android.util.Global;
import com.entertainerJatt.app.android.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Imbibian on 3/22/2017.
 */

public class LatestFragment extends Fragment implements View.OnClickListener, IAsyncTask {
    private RecyclerView recyclerView;
    private Context context;
    private ImageView viewCHangeImageView;
    private boolean isGridViewOpen = false;
    private SwipeRefreshLayout swipeRefreshLayout;

    private TextView notAvailableTextView;
    private Global global;
    DatabaseHelper databaseHelper;
    private LinearLayout progressLayout;
    private int start = 0;
    private int end = 10;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 1;
    private int TOTAL_PAGES_ = 0;
    private boolean isFirstTimeAdapter = true;
    private GridLayoutManager manager;
    private LatestAdapter adapter = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_latest, container, false);
        assignId(view);
        listners();

        return view;
    }

    private void listners() {
        viewCHangeImageView.setOnClickListener(this);
    }

    private void assignId(View view) {
        context = getActivity();
        databaseHelper = new DatabaseHelper(context);
        viewCHangeImageView = (ImageView) getActivity().findViewById(R.id.viewCHangeImageView);
        global = (Global) getActivity().getApplicationContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        manager = new GridLayoutManager(context, 1);
        recyclerView.setLayoutManager(manager);
        notAvailableTextView = (TextView) view.findViewById(R.id.notAvailableTextView);
        progressLayout = (LinearLayout) view.findViewById(R.id.progressLayout);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    getSongs(start, end);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        try {
            getSongs(start, end);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.viewCHangeImageView:
                if (isGridViewOpen) {
                    isGridViewOpen = false;
                    viewCHangeImageView.setImageResource(R.mipmap.list);
                } else {
                    isGridViewOpen = true;
                    viewCHangeImageView.setImageResource(R.mipmap.grid);
                }
                break;
        }
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
        swipeRefreshLayout.setRefreshing(false);
        try {
            ViewAllActivity.isFirstTimeHitOccur = true;
            final ArrayList<HashMap<String, String>> AlbumssongArrayList = new ArrayList<>();
            String acount = jsonObject.getString("count");
            final int count = Integer.parseInt(acount);
            if (count <= 10) {
                TOTAL_PAGES = 0;
            } else {
                TOTAL_PAGES_ = 1;
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
                if (object.has("artist_name")) {
                    JSONArray artist_name = object.getJSONArray("artist_name");
                    String s = null;
                    for (int p = 0; p < artist_name.length(); p++) {
                        if (artist_name.length() > 1) {
                            if (s != null) {
                                s = s + " , " + artist_name.getString(p);
                            } else {
                                s = artist_name.getString(p);
                            }
                        } else {
                            s = artist_name.getString(p);
                        }
                    }
                    songsHashMap.put("artist_name", s);
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
                    adapter = new LatestAdapter(context, AlbumssongArrayList);
                    recyclerView.setAdapter(adapter);
                } else {
                    adapter.updateAdapter(AlbumssongArrayList);
                }
                adapter.SetOnItemClickListener(new LatestAdapter.OnItemClickListener() {
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

                        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                        UserInfo userInfo = new UserInfo();
                        userInfo.setNode_type(AlbumssongArrayList.get(position).get("type"));
                        userInfo.setTime(currentDateTimeString);
                        userInfo.setFav("false");
                        userInfo.setJsonString(AlbumssongArrayList.get(position).get("object"));
                        userInfo.setVideoId(AlbumssongArrayList.get(position).get("id"));

                        try {
                            boolean isAdded = false;
                            List<UserInfo> songsList = databaseHelper.getAllSongs();
                            if (songsList.size() == 0) {
                                databaseHelper.addSONGS(userInfo);
                            } else {
                                for (int pos = 0; pos < songsList.size(); pos++) {
                                    String jsonString = songsList.get(pos).getVideoId();
                                    String compareString = AlbumssongArrayList.get(position).get("id");
                                    if (compareString.equals(jsonString)) {
                                        databaseHelper.updateContact(userInfo);
                                        return;
                                    } else {
                                        isAdded = true;
                                    }
                                }
                                if (isAdded) {
                                    databaseHelper.addSONGS(userInfo);


                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                });
                notAvailableTextView.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

                recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);

                        if (newState == 0) {
                            if (count != AlbumssongArrayList.size()) {
                                try {
                                    if (end - count <= 9) {
                                        progressLayout.setVisibility(View.VISIBLE);
                                        getSongs(start, end);
                                        end = end + 10;
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    }

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                    }
                });


//                recyclerView.addOnScrollListener(new PaginationScrollListener(manager) {
//                    @Override
//                    protected void loadMoreItems() {
//                        if (count != AlbumssongArrayList.size()) {
//                            end = end + 10;
//                            if(TOTAL_PAGES_ == TOTAL_PAGES){
//                                return;
//                            }else{
//                                TOTAL_PAGES_++;
//
//                                try {
//                                    progressLayout.setVisibility(View.VISIBLE);
//                                    getSongs(start, end);
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            }
//
//                        } else {
//                            isLastPage = true;
//                        }
//
//                    }
//
//                    @Override
//                    public int getTotalPageCount() {
//                        Log.i("TOTAL : ", TOTAL_PAGES + "");
//                        return TOTAL_PAGES_;
//                    }
//
//                    @Override
//                    public boolean isLastPage() {
//                        return isLastPage;
//                    }
//
//                    @Override
//                    public boolean isLoading() {
//                        return false;
//                    }
//                });

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


    private void getSongs(int start, int end) throws Exception {

        UserInfo userInfo = new UserInfo();
        userInfo.setCallbackToken(Util.getUserToken(context));
        userInfo.setUsername(Util.getUserName(context));
        userInfo.setStart(String.valueOf(start));
        userInfo.setNode_type(global.getNodeType());
        userInfo.setLimit(String.valueOf(end));
        LatestSongsAsynTask latestSongsAsynTask = new LatestSongsAsynTask(userInfo, LatestFragment.this);
        latestSongsAsynTask.execute();
    }
}

