package com.entertainerJatt.app.android.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.entertainerJatt.app.android.DetailPageActivity;
import com.entertainerJatt.app.android.R;
import com.entertainerJatt.app.android.adapters.RelatedVideosAdapter;
import com.entertainerJatt.app.android.asyncTask.GetDetailsAsyncTask;
import com.entertainerJatt.app.android.entity.UserInfo;
import com.entertainerJatt.app.android.interfaces.IAsyncTask;
import com.entertainerJatt.app.android.util.Global;
import com.entertainerJatt.app.android.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Imbibian on 3/25/2017.
 */

public class RelatedVideosFragment extends Fragment implements IAsyncTask {

    private TextView notAvailableTextView;

    private RecyclerView recyclerView;
    private Context context;
    private String videoId;
    private Global global;
    String image = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_related_videos, container, false);
        assignId(view);
        listners();

        return view;
    }

    private void listners() {
    }

    private void assignId(View view) {
        context = getActivity();
        global = (Global) getActivity().getApplicationContext();
        videoId = global.getVideoId();
        notAvailableTextView = (TextView) view.findViewById(R.id.notAvailableTextView);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        GridLayoutManager manager = new GridLayoutManager(context, 1);
        recyclerView.setLayoutManager(manager);
        getDetails();

    }

    private void getDetails() {

        UserInfo userInfo = new UserInfo();

        userInfo.setUsername(Util.getUserName(context));
        userInfo.setCallbackToken(Util.getUserToken(context));
        userInfo.setVideoId(videoId);

        GetDetailsAsyncTask getDetailsAsyncTask = new GetDetailsAsyncTask(userInfo, RelatedVideosFragment.this);
        getDetailsAsyncTask.execute();
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


        try {
            final ArrayList<HashMap<String, String>> relatedSongsList = new ArrayList<>();
            HashMap<String, String> relatedSongsMap;
            JSONObject object = jsonObject.getJSONObject("data");
            String list = object.getString("related_nodes");
            if (!list.equalsIgnoreCase("null")) {
                JSONObject object1 = object.getJSONObject("related_nodes");
                String count = object1.getString("count");
                JSONArray jsonArray = object1.getJSONArray("nodes");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject innerjsonObject = jsonArray.getJSONObject(i);

                    relatedSongsMap = new HashMap<>();
                    String id = innerjsonObject.getString("id");
                    String type = innerjsonObject.getString("type");
                    String title = innerjsonObject.getString("title");
                    String image = innerjsonObject.getString("image");


                    if(innerjsonObject.has("video_link")){
                        String video_link = innerjsonObject.getString("video_link");
                        relatedSongsMap.put("video_link", video_link);

                    }else{
                        relatedSongsMap.put("video_link", "null");

                    }
                    relatedSongsMap.put("id", id);
                    relatedSongsMap.put("type", type);
                    relatedSongsMap.put("title", title);
                    relatedSongsMap.put("image", image);
                    relatedSongsList.add(relatedSongsMap);
                }
            }

            if (relatedSongsList.size() > 0) {

                RelatedVideosAdapter adapter = new RelatedVideosAdapter(context, relatedSongsList);
                recyclerView.setAdapter(adapter);
                notAvailableTextView.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                adapter.SetOnItemClickListener(new RelatedVideosAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        String image = relatedSongsList.get(position).get("image");
                        String video_link = relatedSongsList.get(position).get("video_link");
                        if (video_link != null && !video_link.equalsIgnoreCase("null")) {
                            video_link = video_link.split("=")[1];
                            video_link = video_link.split("&")[0];
                            global.setURL(video_link);
                            ((DetailPageActivity) context).plaVideo();
                        } else {
                            ((DetailPageActivity) context).setImage(image);
                        }
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
