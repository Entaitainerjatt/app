package com.entertainerJatt.app.android;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.entertainerJatt.app.android.adapters.SearchAdapter;
import com.entertainerJatt.app.android.asyncTask.SearchAsynTask;
import com.entertainerJatt.app.android.entity.UserInfo;
import com.entertainerJatt.app.android.interfaces.IAsyncTask;
import com.entertainerJatt.app.android.util.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sony on 4/4/2017.
 */

public class SearchActivity extends AppCompatActivity implements IAsyncTask, View.OnClickListener {
    private RecyclerView mRecyclerView;
    private SearchAdapter searchAdapter;
    private Context context;
    private EditText etSearchText;
    private ImageView imageCross;
    private TextView notAvailableTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        assignIds();
        listners();
    }

    private void listners() {
        imageCross.setOnClickListener(this);
    }

    private void assignIds() {
        context = this;
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        GridLayoutManager manager = new GridLayoutManager(context, 1);
        mRecyclerView.setLayoutManager(manager);

        imageCross = (ImageView) findViewById(R.id.imageCross);

        etSearchText = (EditText) findViewById(R.id.etSearchText);

        notAvailableTextView = (TextView) findViewById(R.id.notAvailableTextView);

        etSearchText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    etSearchText.setHint("");
                } else {
                    etSearchText.setHint("Search");
                }
            }
        });


        etSearchText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView vm, int actionId, KeyEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(etSearchText.getWindowToken(), 0);
                String text = etSearchText.getText().toString().trim();
                if (!TextUtils.isEmpty(text)) {

                    searchSongs(text);
                } else {

                    Toast.makeText(SearchActivity.this, "Please enter value ", Toast.LENGTH_SHORT).show();
                }


                return false;
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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


    private void searchSongs(String text) {
        if (!TextUtils.isEmpty(text)) {
            UserInfo userInfo = new UserInfo();
            userInfo.setSearch_text(text);
            userInfo.setUsername(Util.getUserName(context));
            userInfo.setCallbackToken(Util.getUserToken(context));
            SearchAsynTask searchAsynTask = new SearchAsynTask(userInfo, SearchActivity.this);
            searchAsynTask.execute();
        }
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
        Util.showDialog(context);
    }

    @Override
    public void OnPostExecute(String URL, JSONObject jsonObject) {
        Util.dismissDialog();
        Log.i("Data : ", jsonObject.toString());
        ArrayList<HashMap<String, String>> searchList1 = new ArrayList<>();
        ArrayList<HashMap<String, String>> searchList2 = new ArrayList<>();
        ArrayList<HashMap<String, String>> searchList3 = new ArrayList<>();
        ArrayList<String> TitleArrayList = new ArrayList<>();
        HashMap<String, String> searchMap;
        try {
            for (int i = 0; i < jsonObject.length(); i++) {
                JSONObject object = null;

                if (i == 0) {
                    object = jsonObject.getJSONObject("artists");
                    TitleArrayList.add("Artists");
                } else if (i == 1) {
                    object = jsonObject.getJSONObject("movies");
                    TitleArrayList.add("Movies");

                } else if (i == 2) {
                    object = jsonObject.getJSONObject("albums");
                    TitleArrayList.add("Albums");

                }
                JSONArray jsonArray = object.getJSONArray("nodes");
                for (int j = 0; j < jsonArray.length(); j++) {
                    searchMap = new HashMap<>();
                    JSONObject innerObject = jsonArray.getJSONObject(j);
                    String id = innerObject.getString("id");
                    String type = innerObject.getString("type");
                    String title = innerObject.getString("title");
                    String release_date = innerObject.getString("release_date");
                    String image = innerObject.getString("image");

                    searchMap.put("id", id);
                    searchMap.put("type", type);
                    searchMap.put("title", title);
                    searchMap.put("release_date", release_date);
                    searchMap.put("image", image);

                    if (innerObject.has("video_link")) {

                        String video_link = innerObject.getString("video_link");
                        searchMap.put("teaser_link", video_link);
                    } else {
                        searchMap.put("teaser_link", "null");
                    }
                    if (innerObject.has("artist_name")) {
                        String artist_name = innerObject.getString("artist_name");
                        searchMap.put("artist_name", artist_name);
                    } else {
                        searchMap.put("artist_name", "null");
                    }


                    if (i == 0) {
                        searchList1.add(searchMap);
                    } else if (i == 1) {
                        searchList2.add(searchMap);
                    } else if (i == 2) {
                        searchList3.add(searchMap);
                    }
                }
                if (searchList1.size() > 0 || searchList2.size() > 0 || searchList2.size() > 0) {
                    notAvailableTextView.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    searchAdapter = new SearchAdapter(context, TitleArrayList, searchList1, searchList2, searchList3);
                    mRecyclerView.setAdapter(searchAdapter);
                } else {
                    notAvailableTextView.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnErrorMessage(String Message) {
        Util.dismissDialog();
        Toast.makeText(context, Message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageCross:
                String text = etSearchText.getText().toString().trim();
                if (!TextUtils.isEmpty(text)) {
                    searchSongs(text);
                } else {
                    Toast.makeText(SearchActivity.this, "Please enter value ", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
