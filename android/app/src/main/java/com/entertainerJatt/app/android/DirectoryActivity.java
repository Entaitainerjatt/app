package com.entertainerJatt.app.android;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.entertainerJatt.app.android.adapters.DirectoryAdapter;
import com.entertainerJatt.app.android.asyncTask.GetDirectoryAsyncTask;
import com.entertainerJatt.app.android.entity.UserInfo;
import com.entertainerJatt.app.android.interfaces.IAsyncTask;
import com.entertainerJatt.app.android.models.AlphabetItem;
import com.entertainerJatt.app.android.util.Global;
import com.entertainerJatt.app.android.util.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sony on 3/31/2017.
 */

public class DirectoryActivity extends Fragment implements IAsyncTask {
    private Context context;
    private List<AlphabetItem> mAlphabetItems;
    private static int count;
    private RecyclerView mRecyclerView;
    private DirectoryAdapter mAdapter;
    private boolean isFirstTimeHitOccur = false;
    private boolean isFirstTimeAdapter = true;
    private Global global;
    private int start = 0;
    private int end = 5000 ;
    private static ArrayList<HashMap<String, String>> DirectoryList = new ArrayList<>();
    private LinearLayoutManager manager;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 1;
    private EditText serachEditText;
    private LinearLayout progressLayout;
    private String main_list_array[];
    private RecyclerViewFastScroller recyclerViewFastScroller;
    private SwipeRefreshLayout swipeRefreshLayout;
    private boolean isEnable = false;

    @android.support.annotation.Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @android.support.annotation.Nullable ViewGroup container, @android.support.annotation.Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_directory, container, false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        assignIDS(view);
        return view;
    }


    private void assignIDS(View view) {
        global = (Global) getActivity().getApplicationContext();
        context = getActivity();


        recyclerViewFastScroller = (RecyclerViewFastScroller) view.findViewById(R.id.recyclerViewFastScroller);

        main_list_array = context.getResources().getStringArray(R.array.alphabtes_array);
        mAlphabetItems = new ArrayList<>();

        for (int i = 0; i < main_list_array.length; i++) {
            String name = main_list_array[i];
            if (i == 0) {
                mAlphabetItems.add(new AlphabetItem(i, name, true));
            } else {
                mAlphabetItems.add(new AlphabetItem(i, name, false));
            }
        }


        FloatingActionButton floatingActionSearch = (FloatingActionButton) getActivity().findViewById(R.id.floatingActionSearch);
        floatingActionSearch.setVisibility(View.VISIBLE);
        floatingActionSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, SearchActivity.class));
            }
        });


        // use a linear layout manager
        manager = new LinearLayoutManager(context);


        progressLayout = (LinearLayout) view.findViewById(R.id.progressLayout);


        serachEditText = (EditText) view.findViewById(R.id.serachEditText);
        serachEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                setSearchedData(editable.toString());

            }
        });
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        manager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(manager);

        if (DirectoryList.size() > 0) {
            if (isFirstTimeAdapter) {
                isFirstTimeAdapter = false;
                mAdapter = new DirectoryAdapter(context, DirectoryList);
                mRecyclerView.setAdapter(mAdapter);

                recyclerViewFastScroller.setRecyclerView(mRecyclerView);
                recyclerViewFastScroller.setUpAlphabet(mAlphabetItems);
            } else {
                mAdapter.updateList(DirectoryList);
            }
            mAdapter.SetOnItemClickListener(new DirectoryAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    String id = DirectoryList.get(position).get("id");
                    global.setVideoId(id);
                    global.setURL(null);
                    startActivity(new Intent(context, DetailPageActivity.class));

                }
            });


            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (newState == 0) {
                        if (count != DirectoryList.size()) {
                            try {
                                if (end - count <= 9) {
                                    progressLayout.setVisibility(View.VISIBLE);
                                    getDirectory();
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
        } else {
            try {
                getDirectory();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    isEnable = true;
                    getDirectory();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void setSearchedData(String chare) {
        if (TextUtils.isEmpty(chare)) {
            mAdapter.updateList(DirectoryList);
            return;
        }
        ArrayList<HashMap<String, String>> searchedArrayList = new ArrayList<>();
        for (int i = 0; i < DirectoryList.size(); i++) {
            if (DirectoryList.get(i).get("title").toLowerCase().contains(chare.toLowerCase())) {
                HashMap<String, String> searchedMap = DirectoryList.get(i);
                searchedArrayList.add(searchedMap);
            }
        }
        mAdapter.updateList(searchedArrayList);
    }

    private void getDirectory() throws Exception {
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(Util.getUserName(context));
        userInfo.setCallbackToken(Util.getUserToken(context));
        userInfo.setStart(String.valueOf(start));
        userInfo.setLimit(String.valueOf(end));

        GetDirectoryAsyncTask getDirectoryAsyncTask = new GetDirectoryAsyncTask(userInfo, DirectoryActivity.this);
        getDirectoryAsyncTask.execute();
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
        progressLayout.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);
        isFirstTimeHitOccur = true;
        HashMap<String, String> directoryMap;
        try {
            String acount = jsonObject.getString("count");
            count = Integer.parseInt(acount);
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
                directoryMap = new HashMap<>();
                JSONObject object = jsonArray.getJSONObject(i);
                String id = object.getString("id");
                String type = object.getString("type");
                String title = object.getString("title");
                String image = object.getString("image");
                String phoneNumber = object.getString("phone_number");
                String email = object.getString("email");
                if (object.has("artist_types")) {
                    JSONArray jsonArray1 = object.getJSONArray("artist_types");
                    String s = null;
                    for (int p = 0; p < jsonArray1.length(); p++) {

                        if (jsonArray1.length() > 1) {
                            if (s != null) {
                                s = s + " , " + jsonArray1.getString(p);
                            } else {
                                s = jsonArray1.getString(p);
                            }
                        } else {
                            s = jsonArray1.getString(p);
                        }
                    }
                    directoryMap.put("type", s);

                } else {
                    directoryMap.put("type", null);

                }

                directoryMap.put("id", id);

                directoryMap.put("type_", type);
                directoryMap.put("title", title);
                directoryMap.put("image", image);
                directoryMap.put("phoneNumber", phoneNumber);
                directoryMap.put("email", email);
                DirectoryList.add(directoryMap);
            }
            if (DirectoryList.size() > 0) {

                if (isFirstTimeAdapter) {
                    isFirstTimeAdapter = false;
                    mAdapter = new DirectoryAdapter(context, DirectoryList);
                    mRecyclerView.setAdapter(mAdapter);

                    recyclerViewFastScroller.setRecyclerView(mRecyclerView);
                    recyclerViewFastScroller.setUpAlphabet(mAlphabetItems);
                } else {
                    mAdapter.updateList(DirectoryList);
                }
                mAdapter.SetOnItemClickListener(new DirectoryAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        String id = DirectoryList.get(position).get("id");
                        global.setVideoId(id);
                        global.setURL(null);
                        startActivity(new Intent(context, DetailPageActivity.class));

                    }
                });


                mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);

                        if (newState == 0) {
                            if (count != DirectoryList.size()) {
                                try {
                                    if (end - count <= 9) {
                                        progressLayout.setVisibility(View.VISIBLE);
                                        getDirectory();
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

//                mRecyclerView.addOnScrollListener(new PaginationScrollListener(manager) {
//                    @Override
//                    protected void loadMoreItems() {
//                        if (count != DirectoryList.size()) {
//                            end = end + 10;
//                            progressLayout.setVisibility(View.VISIBLE);
//                            try {
//                                getDirectory();
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        } else {
//                            isLastPage = true;
//                        }
//                    }
//
//                    @Override
//                    public int getTotalPageCount() {
//                        return TOTAL_PAGES;
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnErrorMessage(String Message) {
        Util.dismissDialog();
        Toast.makeText(context, Message, Toast.LENGTH_SHORT).show();
    }


    public void setData(String chare) {
        for (int i = 0; i < DirectoryList.size(); i++) {
            if (chare.equalsIgnoreCase("#")) {
                for (int k = 0; k < 10; k++) {
                    if (DirectoryList.get(i).get("title").equalsIgnoreCase("" + k)) {
                        mRecyclerView.getLayoutManager().smoothScrollToPosition(mRecyclerView, null, i);
                    }
                }

            } else {
                if (DirectoryList.get(i).get("title").startsWith(chare)) {
                    mRecyclerView.getLayoutManager().smoothScrollToPosition(mRecyclerView, null, i);
                }
            }


        }

    }
}
