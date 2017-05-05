package com.entertainerJatt.app.android;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.entertainerJatt.app.android.adapters.HomeListAdapter;
import com.entertainerJatt.app.android.asyncTask.GetNodeAsyncTask;
import com.entertainerJatt.app.android.entity.UserInfo;
import com.entertainerJatt.app.android.interfaces.IAsyncTask;
import com.entertainerJatt.app.android.util.ApplicationConstants;
import com.entertainerJatt.app.android.util.CustomGridLayoutManager;
import com.entertainerJatt.app.android.util.LockableScrollView;
import com.entertainerJatt.app.android.util.Util;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.entertainerJatt.app.android.R.id.image;


public class HomeActivity extends Fragment implements View.OnClickListener, IAsyncTask {

    private static RelativeLayout reletiveLayout;
    private ImageView playImageView;
    public static boolean isFirstTimelayed = false;
    private static CustomGridLayoutManager linearLayoutManager;
    private static ArrayList<HashMap<String, String>> ArtistsongArrayList = new ArrayList<>();
    private static ArrayList<HashMap<String, String>> TitleArrayList = new ArrayList<>();
    private static ArrayList<HashMap<String, String>> AlbumssongArrayList = new ArrayList<>();
    private static ArrayList<HashMap<String, String>> MoviessongArrayList = new ArrayList<>();
    private static RecyclerView my_recycler_view;
    private static String url;
    private static Context context;
    private static FrameLayout linearLayout;
    public static YouTubePlayer player;
    private static ImageView headerLayout;
    private static LockableScrollView scrollView;
    private HomeListAdapter adapter;
    private boolean isResumeEnable = false;
    private FloatingActionButton floatingActionSearch;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        scrollView = (LockableScrollView) view.findViewById(R.id.scrollView);
        scrollView.smoothScrollTo(0, 0);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        initToolbar(view);
        initRecyclerView(view);
        listners();
        return view;
    }

    private void listners() {
        playImageView.setOnClickListener(this);
    }

    private void initToolbar(View view) {
        context = getActivity();
        // TODO will remove after testing
        Log.i("Token", Util.getUserRToken(context));
        reletiveLayout = (RelativeLayout) view.findViewById(R.id.reletiveLayout);
        int height = Util.getHeight(context);
        height = height / 3;
        linearLayout = (FrameLayout) view.findViewById(R.id.linearLayout);
        reletiveLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));

        floatingActionSearch = (FloatingActionButton) getActivity().findViewById(R.id.floatingActionSearch);
        floatingActionSearch.setVisibility(View.VISIBLE);
        floatingActionSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, SearchActivity.class));
            }
        });

        playImageView = (ImageView) view.findViewById(R.id.playImageView);
        headerLayout = (ImageView) view.findViewById(R.id.headerLayout);


    }

    private void initRecyclerView(View view) {
        my_recycler_view = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        my_recycler_view.invalidate();
        if (TitleArrayList.size() > 0 && AlbumssongArrayList.size() > 0) {
            PlayerYouTubeFrag myFragment = PlayerYouTubeFrag.newInstance("u7waKjf5XPg");
            getFragmentManager().beginTransaction().remove(myFragment).commit();

            getFragmentManager().beginTransaction().replace(R.id.linearLayout, myFragment).commit();

            try {
                Util.setsaveUrl(context, Util.getSavedUrl(context));
            } catch (Exception e) {
                e.printStackTrace();
            }
            Glide.with(context)
                    .load(Util.getSavedImageUrl(context))
                    .placeholder(R.mipmap.default_small)
                    .error(R.mipmap.default_small)
                    .into(headerLayout);

            adapter = new HomeListAdapter(getActivity(), TitleArrayList, ArtistsongArrayList, AlbumssongArrayList, MoviessongArrayList);
            linearLayoutManager = new CustomGridLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            isResumeEnable = true;
            linearLayoutManager.setScrollEnabled(false);
            linearLayoutManager.setAutoMeasureEnabled(true);
            my_recycler_view.setLayoutManager(linearLayoutManager);
            my_recycler_view.setAdapter(adapter);
            my_recycler_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    if (dy > 0) {
                        floatingActionSearch.hide();
                    } else {
                        floatingActionSearch.show();
                    }

                    super.onScrolled(recyclerView, dx, dy);
                }
            });
        } else {
            PlayerYouTubeFrag myFragment = PlayerYouTubeFrag.newInstance("u7waKjf5XPg");
            getFragmentManager().beginTransaction().replace(R.id.linearLayout, myFragment).commit();

            UserInfo userInfo = new UserInfo();
            userInfo.setCallbackToken(Util.getUserToken(context));
            userInfo.setUsername(Util.getUserName(context));
            userInfo.setStart("0");
            userInfo.setLimit("10");
            GetNodeAsyncTask getNodeAsyncTask = new GetNodeAsyncTask(userInfo, HomeActivity.this);
            getNodeAsyncTask.execute();
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.playImageView:
                linearLayout.setVisibility(View.VISIBLE);
                reletiveLayout.setVisibility(View.GONE);
                plaVideo(Util.getSavedUrl(context), isFirstTimelayed);
                break;

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
        String teaser_link = null;
        try {
            HashMap<String, String> songsHashMap;
            HashMap<String, String> TitleHashMap;
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int j = 0; j < jsonArray.length(); j++) {
                TitleHashMap = new HashMap<>();

                JSONObject outerobject = jsonArray.getJSONObject(j);
                String type = outerobject.getString("type");
                String title = outerobject.getString("title");
                String weight = outerobject.getString("weight");

                TitleHashMap.put("type", type);
                TitleHashMap.put("title", title);
                TitleHashMap.put("weight", weight);
                TitleArrayList.add(TitleHashMap);

                String list = outerobject.getString("list");
                if (!list.equalsIgnoreCase("null")) {
                    JSONArray innerJsonArray = outerobject.getJSONArray("list");

                    for (int k = 0; k < innerJsonArray.length(); k++) {
                        songsHashMap = new HashMap<>();
                        JSONObject object = innerJsonArray.getJSONObject(k);
                        String id = object.getString("id");
                        String type_ = object.getString("type");
                        String title_ = object.getString("title");
                        String image = object.getString("image");
                        if (object.has("video_link")) {
                            teaser_link = object.getString("video_link");
                            songsHashMap.put("teaser_link", teaser_link);
                        } else {
                            songsHashMap.put("teaser_link", null);
                        }
                        songsHashMap.put("id", id);
                        if (type.equalsIgnoreCase("lyrist")) {

                            songsHashMap.put("type", "lyrist");
                        } else {

                            songsHashMap.put("type", type_);
                        }
                        songsHashMap.put("weight", weight);
                        songsHashMap.put("title", title_);
                        songsHashMap.put("datacoming", "Server");
                        songsHashMap.put("image", image);
                        songsHashMap.put("object", object.toString());
                        AlbumssongArrayList.add(songsHashMap);


                        if (weight.equalsIgnoreCase("0") && k == 0) {
                            Util.setsaveUrl(context, teaser_link);
                            Util.setsaveImageUrl(context, image);
                            Glide.with(context)
                                    .load(image)
                                    .placeholder(R.mipmap.default_small)
                                    .error(R.mipmap.default_small)
                                    .into(headerLayout);
                        }

                    }

                }
            }
            adapter = new HomeListAdapter(getActivity(), TitleArrayList, ArtistsongArrayList, AlbumssongArrayList, MoviessongArrayList);
            linearLayoutManager = new CustomGridLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            isResumeEnable = true;
            linearLayoutManager.setScrollEnabled(false);
            linearLayoutManager.setAutoMeasureEnabled(true);
            my_recycler_view.setLayoutManager(linearLayoutManager);
            my_recycler_view.setAdapter(adapter);
            my_recycler_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    if (dy > 0) {
                        floatingActionSearch.hide();
                    } else {
                        floatingActionSearch.show();
                    }

                    super.onScrolled(recyclerView, dx, dy);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnErrorMessage(String Message) {
        Util.dismissDialog();
        Toast.makeText(context, Message, Toast.LENGTH_SHORT).show();

    }

    public static void setAdapterAgain() {
        HomeListAdapter adapter = new HomeListAdapter(context, TitleArrayList, ArtistsongArrayList, AlbumssongArrayList, MoviessongArrayList);
        linearLayoutManager = new CustomGridLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setScrollEnabled(false);
        my_recycler_view.setLayoutManager(linearLayoutManager);
        my_recycler_view.setAdapter(adapter);
    }


    public static void plaVideo(String urll, boolean value) {
        linearLayout.setVisibility(View.VISIBLE);
        reletiveLayout.setVisibility(View.GONE);
        if (player != null) {
            if (value) {
                player.play();
            } else {
                url = urll.split("=")[1];
                url = url.split("&")[0];
                player.loadVideo(url);
            }
        }

    }

    public static void setImage(String image) {
        Glide.with(context)
                .load(image)
                .placeholder(R.mipmap.default_small)
                .error(R.mipmap.default_small)
                .into(headerLayout);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (isResumeEnable) {
            adapter = new HomeListAdapter(getActivity(), TitleArrayList, ArtistsongArrayList, AlbumssongArrayList, MoviessongArrayList);
            linearLayoutManager = new CustomGridLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            my_recycler_view.setLayoutManager(linearLayoutManager);
            linearLayoutManager.setScrollEnabled(false);

            my_recycler_view.setAdapter(adapter);
            my_recycler_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    if (dy > 0) {
                        floatingActionSearch.hide();
                    } else {
                        floatingActionSearch.show();
                    }

                    super.onScrolled(recyclerView, dx, dy);
                }
            });

        }
    }

    public static class PlayerYouTubeFrag extends YouTubePlayerSupportFragment {


        public static PlayerYouTubeFrag newInstance(String url) {

            PlayerYouTubeFrag playerYouTubeFrag = new PlayerYouTubeFrag();

            Bundle bundle = new Bundle();
            bundle.putString("url", url);

            playerYouTubeFrag.setArguments(bundle);

            return playerYouTubeFrag;
        }

        @Override
        public void onCreate(Bundle bundle) {
            super.onCreate(bundle);
            init();
        }

        private void init() {

            initialize(ApplicationConstants.DEVELOPER_KEY, new YouTubePlayer.OnInitializedListener() {

                @Override
                public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1) {
                }

                @Override
                public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player1, boolean wasRestored) {
                    player = player1;
                    player.setPlaybackEventListener(new YouTubePlayer.PlaybackEventListener() {
                        @Override
                        public void onPlaying() {
                        }

                        @Override
                        public void onPaused() {
                            linearLayout.setVisibility(View.GONE);
                            reletiveLayout.setVisibility(View.VISIBLE);
                            isFirstTimelayed = true;
                        }

                        @Override
                        public void onStopped() {
                        }

                        @Override
                        public void onBuffering(boolean b) {

                        }

                        @Override
                        public void onSeekTo(int i) {
                        }
                    });


                    player.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                }


            });
        }


        @Override //reconfigure display properties on screen rotation
        public void onConfigurationChanged(Configuration newConfig) {
            super.onConfigurationChanged(newConfig);

            //Checks the orientation of the screen
            if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
                if (player != null) {
                    player.setFullscreen(false);
                }
                // handle change here
            } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                if (player != null) {
                    player.setFullscreen(true);

                }

                // or here
            }
        }


    }


    public  static void scrollToTop(){
        scrollView.smoothScrollTo(0, 0);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        player = null;
    }
}

