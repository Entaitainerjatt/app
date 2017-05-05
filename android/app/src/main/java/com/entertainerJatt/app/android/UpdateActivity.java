package com.entertainerJatt.app.android;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.entertainerJatt.app.android.adapters.UpdatesAdapter;
import com.entertainerJatt.app.android.asyncTask.GetRecentAsycTask;
import com.entertainerJatt.app.android.entity.UserInfo;
import com.entertainerJatt.app.android.interfaces.IAsyncTask;
import com.entertainerJatt.app.android.util.Global;
import com.entertainerJatt.app.android.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.entertainerJatt.app.android.R.id.end;
import static com.entertainerJatt.app.android.R.id.recyclerView;

/**
 * Created by sony on 4/6/2017.
 */

public class UpdateActivity extends Fragment implements IAsyncTask {

    private SwipeRefreshLayout swipeRefreshLayout;
    private Context context;
    private int start = 0;
    private int limit = 10;
    private UpdatesAdapter updatesAdapter;
    private RecyclerView mRecyclerView;
    private GridLayoutManager manager;
    private Global global;
    private LinearLayout progressLayout;
    private boolean isProgressFirstTime = true;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 1;
    private int TOTAL_PAGES_ = 0;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_update, container, false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        assignIds(view);
        return view;
    }


    private void assignIds(View view) {
        context = getActivity();
        global = (Global) getActivity().getApplicationContext();
        progressLayout = (LinearLayout) view.findViewById(R.id.progressLayout);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getUpdates();
            }
        });
        mRecyclerView = (RecyclerView) view.findViewById(recyclerView);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        manager = new GridLayoutManager(context, 1);
        mRecyclerView.setLayoutManager(manager);

        FloatingActionButton floatingActionSearch = (FloatingActionButton) getActivity().findViewById(R.id.floatingActionSearch);
        floatingActionSearch.setVisibility(View.VISIBLE);
        floatingActionSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, SearchActivity.class));
            }
        });
        getUpdates();

    }


    private void getUpdates() {
        UserInfo userInfo = new UserInfo();
        userInfo.setStart(String.valueOf(start));
        userInfo.setLimit(String.valueOf(end));
        userInfo.setUsername(Util.getUserName(context));
        userInfo.setCallbackToken(Util.getUserToken(context));
        GetRecentAsycTask getRecentAsycTask = new GetRecentAsycTask(userInfo, UpdateActivity.this);
        getRecentAsycTask.execute();
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
        if (isProgressFirstTime) {
            Util.showDialog(context);
        }
    }

    @Override
    public void OnPostExecute(String URL, JSONObject jsonObject) {
        Util.dismissDialog();
        progressLayout.setVisibility(View.GONE);
        Log.i("Json Object : ", jsonObject.toString());
        swipeRefreshLayout.setRefreshing(false);
        try {
            String acount = jsonObject.getString("count");
            final int count = Integer.parseInt(acount);
            if (count <= 10) {
                TOTAL_PAGES = 0;
                TOTAL_PAGES_ = 0;
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
            final ArrayList<HashMap<String, String>> songsList = new ArrayList<>();

            JSONArray jsonArray = jsonObject.getJSONArray("nodes");
            HashMap<String, String> songsMap;
            for (int i = 0; i < jsonArray.length(); i++) {
                songsMap = new HashMap<>();
                JSONObject object = jsonArray.getJSONObject(i);
                String id = object.getString("id");
                String type = object.getString("type");
                String title = object.getString("title");
                String release_date = object.getString("release_date");
                String image = object.getString("image");
                if (object.has("body")) {
                    String body = object.getString("body");
                    body = Html.fromHtml(body).toString();
                    songsMap.put("body", body);
                } else {
                    songsMap.put("body", null);
                }
                if (object.has("artist_name")) {
                    JSONArray artist_name = object.getJSONArray("artist_name");
                    String s = null;
                    for (int p = 0; p < artist_name.length(); p++) {
                        if (artist_name.length() > 1) {
                            if (s == null) {
                                s = artist_name.getString(p);
                            } else {
                                s = s + " , " + artist_name.getString(p);
                            }
                        } else {
                            s = artist_name.getString(p);
                        }
                    }
                    songsMap.put("artist_name", s);
                } else {
                    songsMap.put("artist_name", null);
                }
                if (object.has("video_link")) {
                    String video_link = object.getString("video_link");
                    songsMap.put("teaser_link", video_link);

                } else {

                    songsMap.put("teaser_link", "null");
                }
                songsMap.put("id", id);
                songsMap.put("type", type);
                songsMap.put("title", title);
                songsMap.put("release_date", release_date);
                songsMap.put("image", image);
                songsList.add(songsMap);
            }
            if (songsList.size() > 0) {
                if (isProgressFirstTime) {
                    isProgressFirstTime = false;
                    updatesAdapter = new UpdatesAdapter(context, songsList);
                    mRecyclerView.setAdapter(updatesAdapter);
                } else {
                    updatesAdapter.updateAdapter(songsList);
                }
                updatesAdapter.SetOnItemClickListener(new UpdatesAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        String id = songsList.get(position).get("id");
                        global.setVideoId(id);
                        String Url = songsList.get(position).get("teaser_link");
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
                mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);

                        if (newState == 0) {
                            if (count != songsList.size()) {
                                try {
                                    if (limit - count <= 9) {
                                        progressLayout.setVisibility(View.VISIBLE);
                                        getUpdates();
                                        limit = limit + 10;
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

//                mRecyclerView.addOnScrollListener(new PaginationScrollListener(manager) {
//                    @Override
//                    protected void loadMoreItems() {
//                        if (count != songsList.size()) {
//                            limit = limit + 10;
//                            if(TOTAL_PAGES_ == TOTAL_PAGES){
//                                return;
//                            }else{
//                                TOTAL_PAGES_++;
//                                try {
//                                    progressLayout.setVisibility(View.VISIBLE);
//                                    getUpdates();
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        } else {
//                            isLastPage = true;
//                        }
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
