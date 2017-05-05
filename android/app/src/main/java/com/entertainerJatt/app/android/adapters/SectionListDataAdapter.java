package com.entertainerJatt.app.android.adapters;

/**
 * Created by Imbibian on 3/21/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.entertainerJatt.app.android.DetailPageActivity;
import com.entertainerJatt.app.android.HomeActivity;
import com.entertainerJatt.app.android.R;
import com.entertainerJatt.app.android.database.DatabaseHelper;
import com.entertainerJatt.app.android.entity.UserInfo;
import com.entertainerJatt.app.android.util.Global;
import com.entertainerJatt.app.android.util.Util;

import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class SectionListDataAdapter extends RecyclerView.Adapter<SectionListDataAdapter.SingleItemRowHolder> {

    DatabaseHelper databaseHelper;
    private Context mContext;
    ArrayList<HashMap<String, String>> songArrayList = new ArrayList<>();

    int height;
    Global globa;

    public SectionListDataAdapter(Context context, ArrayList<HashMap<String, String>> songArrayList) {
        this.songArrayList = songArrayList;
        this.mContext = context;
        globa = (Global) mContext.getApplicationContext();
        height = Util.getHeight(mContext);
        databaseHelper = new DatabaseHelper(mContext);
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_single_card, null);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, final int i) {
        holder.tvTitle.setText(songArrayList.get(i).get("title"));
        holder.itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    JSONObject object = new JSONObject(songArrayList.get(i).get("object"));
                    String link = object.getString("video_link");
                    String image = object.getString("image");
                    if (Util.getSavedUrl(mContext) != null && Util.getSavedUrl(mContext).equalsIgnoreCase(link)) {

                        HomeActivity.plaVideo(link, false);
                        return;
                    } else {
                        HomeActivity.plaVideo(link, false);
                    }
                    Util.setsaveUrl(mContext, link);
                    Util.setsaveImageUrl(mContext, image);
                    HomeActivity.setImage(image);
                    String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                    UserInfo userInfo = new UserInfo();
                    userInfo.setNode_type(songArrayList.get(i).get("type"));
                    userInfo.setTime(currentDateTimeString);
                    userInfo.setFav("false");
                    userInfo.setJsonString(songArrayList.get(i).get("object"));
                    userInfo.setVideoId(songArrayList.get(i).get("id"));
                    if (songArrayList.get(i).get("datacoming").equalsIgnoreCase("Server")) {
                        try {
                            boolean isAdded = false;
                            List<UserInfo> songsList = databaseHelper.getAllSongs();
                            if (songsList.size() == 0) {
                                databaseHelper.addSONGS(userInfo);
                                HomeActivity.setAdapterAgain();
                            } else {
                                for (int pos = 0; pos < songsList.size(); pos++) {
                                    String jsonString = songsList.get(pos).getVideoId();
                                    String compareString = songArrayList.get(i).get("id");
                                    if (compareString.equals(jsonString)) {
                                        databaseHelper.updateContact(userInfo);

                                        HomeActivity.setAdapterAgain();
                                        return;
                                    } else {
                                        isAdded = true;
                                    }
                                }
                                if (isAdded) {
                                    databaseHelper.addSONGS(userInfo);

                                    HomeActivity.setAdapterAgain();

                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        databaseHelper.updateContact(userInfo);


                    }

                } catch (Exception e) {
                    if (e.getLocalizedMessage().equalsIgnoreCase("No value for video_link")) {
                        String id = songArrayList.get(i).get("id");
                        globa.setVideoId(id);
                        globa.setURL(null);
                        mContext.startActivity(new Intent(mContext, DetailPageActivity.class));

                    }
                    e.printStackTrace();
                }
            }
        });
//        holder.itemImage.setLayoutParams(new LinearLayout.LayoutParams(height / 5, ViewGroup.LayoutParams.MATCH_PARENT -100));
        Glide.with(mContext)
                .load(songArrayList.get(i).get("image"))
                .placeholder(R.mipmap.default_small)
                .error(R.mipmap.default_small)
                .into(holder.itemImage);


    }

    @Override
    public int getItemCount() {
        return songArrayList.size();
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        protected TextView tvTitle;

        protected ImageView itemImage;


        public SingleItemRowHolder(View view) {
            super(view);

            this.tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            this.itemImage = (ImageView) view.findViewById(R.id.itemImage);

//
//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//
//                    Toast.makeText(v.getContext(), tvTitle.getText(), Toast.LENGTH_SHORT).show();
//
//                }
//            });


        }

    }

}