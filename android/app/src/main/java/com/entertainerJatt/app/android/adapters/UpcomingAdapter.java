package com.entertainerJatt.app.android.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.entertainerJatt.app.android.R;
import com.entertainerJatt.app.android.database.DatabaseHelper;
import com.entertainerJatt.app.android.entity.UserInfo;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sony on 3/30/2017.
 */

public class UpcomingAdapter extends RecyclerView.Adapter {

    OnItemClickListener clickListener;

    private ArrayList<HashMap<String, String>> songList;
    private List<UserInfo> LocalsongsList;
    private DatabaseHelper databaseHelper;

    public UpcomingAdapter(Context context, ArrayList<HashMap<String, String>> songList) {
        this.context = context;
        this.songList = songList;
        databaseHelper = new DatabaseHelper(context);
        LocalsongsList = databaseHelper.getAllFav();
    }

    private static final int TYPE_ITEM = 0;
    private Context context;


    public void updateAdapter(ArrayList<HashMap<String, String>> songList) {
        this.songList = songList;
        notifyDataSetChanged();

    }

    public class ViewItem extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected TextView descriptionTextView;
        protected TextView dateTextView;
        protected TextView namTextView;
        protected ImageView songImage;
        protected ImageView addFavImageView;

        public ViewItem(View holderView) {
            super(holderView);
            dateTextView = (TextView) holderView.findViewById(R.id.dateTextView);
            namTextView = (TextView) holderView.findViewById(R.id.namTextView);
            descriptionTextView = (TextView) holderView.findViewById(R.id.descriptionTextView);
            songImage = (ImageView) holderView.findViewById(R.id.songImage);
            addFavImageView = (ImageView) holderView.findViewById(R.id.addFavImageView);
            holderView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(view, getPosition());
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_latest_songs, viewGroup, false);
        return new UpcomingAdapter.ViewItem(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolderValue, final int position) {
        if (viewHolderValue instanceof UpcomingAdapter.ViewItem) {
            final UpcomingAdapter.ViewItem viewHolder = (UpcomingAdapter.ViewItem) viewHolderValue;

            String name = songList.get(position).get("artist_name");
            if (name != null && !name.equalsIgnoreCase("null")) {
                viewHolder.descriptionTextView.setText("By : " + name);
            } else {
                //   viewHolder.descriptionTextView.setText("By : " + "Unknown");
            }
            viewHolder.namTextView.setText(songList.get(position).get("title"));
            if (songList.get(position).get("release_date") != null && !songList.get(position).get("release_date").equalsIgnoreCase("null")) {
                String date = songList.get(position).get("release_date").split("T")[0];
                String[] splitDate = date.split("-");
                String year = splitDate[0];
                String month = splitDate[1];
                String spliDate = splitDate[2];
                String finaldate = spliDate + "/" + month + "/" + year;
                viewHolder.dateTextView.setText("Release date :" + finaldate);
                viewHolder.dateTextView.setVisibility(View.VISIBLE);
            } else {
                viewHolder.dateTextView.setVisibility(View.GONE);
            }

            viewHolder.addFavImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean isAdded = false;
                    String videoId = songList.get(position).get("id");
                    String toast = null;

                    String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                    UserInfo userInfo = new UserInfo();
                    userInfo.setNode_type(songList.get(position).get("type"));
                    userInfo.setTime(currentDateTimeString);
                    userInfo.setJsonString(songList.get(position).get("object"));
                    userInfo.setVideoId(songList.get(position).get("id"));

                    if (LocalsongsList.size() == 0) {
                        userInfo.setFav("true");
                        databaseHelper.addFav(userInfo);
                        LocalsongsList.clear();
                        LocalsongsList = databaseHelper.getAllFav();
                        Toast.makeText(context, "Added to Favourites", Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();
                    } else {
                        for (int i = 0; i < LocalsongsList.size(); i++) {
                            String localVideoId = LocalsongsList.get(i).getVideoId();
                            String fav = LocalsongsList.get(i).getFav();
                            if (videoId.equalsIgnoreCase(localVideoId)) {
                                if (fav.equalsIgnoreCase("true")) {
                                    userInfo.setFav("false");
                                    toast = "Removed from Favourites";
                                } else {
                                    toast = "Added to Favourites";
                                    userInfo.setFav("true");
                                }
                                databaseHelper.updateFav(userInfo);
                                LocalsongsList.clear();
                                LocalsongsList = databaseHelper.getAllFav();
                                notifyDataSetChanged();
                                Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();

                                return;
                            } else {

                                isAdded = false;
                            }
                        }
                        if (!isAdded) {
                            userInfo.setFav("true");
                            databaseHelper.addFav(userInfo);
                            Toast.makeText(context, "Added to Favourites", Toast.LENGTH_SHORT).show();
                            LocalsongsList.clear();
                            LocalsongsList = databaseHelper.getAllFav();
                            notifyDataSetChanged();
                        }
                    }


                }
            });
            Glide.with(context)
                    .load(songList.get(position).get("image"))
                    .placeholder(R.mipmap.default_small)
                    .error(R.mipmap.default_small)
                    .into(viewHolder.songImage);

            for (int k = 0; k < LocalsongsList.size(); k++) {
                String vId = LocalsongsList.get(k).getVideoId();
                String id = songList.get(position).get("id");
                if (vId.equalsIgnoreCase(id)) {

                    String LocalFav = LocalsongsList.get(k).getFav();
                    if (LocalFav.equalsIgnoreCase("true")) {

                        viewHolder.addFavImageView.setImageResource(R.mipmap.add_to_fav_red);
                        return;
                    } else {
                        viewHolder.addFavImageView.setImageResource(R.mipmap.add_to_fav_grey);
                    }

                } else {
                    viewHolder.addFavImageView.setImageResource(R.mipmap.add_to_fav_grey);

                }


            }
        }


    }

    @Override
    public int getItemViewType(int position) {

        return TYPE_ITEM;
    }


    @Override
    public int getItemCount() {
        return songList.size();
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }


}
