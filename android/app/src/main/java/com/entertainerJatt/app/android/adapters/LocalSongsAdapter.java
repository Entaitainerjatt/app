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

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sony on 3/28/2017.
 */

public class LocalSongsAdapter extends RecyclerView.Adapter {

    OnItemClickListener clickListener;

    private ArrayList<HashMap<String, String>> songList;
    private static final int LOADING = 1;

    private final int VIEW_ITEM = 1;

    RecyclerView recyclerView;
    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private boolean isLoadingAdded = false;
private  DatabaseHelper databaseHelper;

    private  List<UserInfo> LocalsongsList;

    public LocalSongsAdapter(Context context, ArrayList<HashMap<String, String>> songList, RecyclerView recyclerView) {
        this.context = context;
        this.songList = songList;
        this.recyclerView = recyclerView;
        databaseHelper = new DatabaseHelper(context);
        LocalsongsList = databaseHelper.getAllFav();

    }


    private Context context;

    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
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

        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.row_latest_songs, viewGroup, false);

            vh = new LocalSongsAdapter.ViewItem(view);
        } else {
            View v2 = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.progress, viewGroup, false);
            vh = new LoadingVH(v2);

        }


        return vh;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolderValue, final int position) {
        if (viewHolderValue instanceof LocalSongsAdapter.ViewItem) {
            final LocalSongsAdapter.ViewItem viewHolder = (LocalSongsAdapter.ViewItem) viewHolderValue;
            viewHolder.namTextView.setText(songList.get(position).get("title"));


//            if (songList.get(position).get("fav").equalsIgnoreCase("true")) {
//                viewHolder.addFavImageView.setImageResource(R.mipmap.add_to_fav_red);
//            } else {
//                viewHolder.addFavImageView.setImageResource(R.mipmap.add_to_fav_grey);
//
//            }


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

//            viewHolder.addFavImageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    String toast = null;
//                    DatabaseHelper databaseHelper = new DatabaseHelper(context);
//                    HashMap<String, String> map = songList.get(position);
//                    List<UserInfo> userInfos = databaseHelper.getAllSongs();
//                    for (int i = 0; i < userInfos.size(); i++) {
//                        if (userInfos.get(i).getVideoId().equalsIgnoreCase(map.get("id"))) {
//                            UserInfo userInfo = userInfos.get(i);
//                            if (userInfo.getFav().equalsIgnoreCase("true")) {
//                                userInfo.setFav("false");
//                            } else {
//                                userInfo.setFav("true");
//                            }
//                            databaseHelper.updateContact(userInfo);
//                        }
//                    }
//                    if (map.get("fav").equalsIgnoreCase("true")) {
//                        map.put("fav", "false");
//                        toast = "Removed to Favourites";
//                    } else {
//                        map.put("fav", "true");
//                        toast = "Added to Favourites";
//                    }
//
//                    songList.remove(position);
//                    songList.add(position, map);
//                    notifyDataSetChanged();
//
//                    Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
//
//
//                }
//            });


            try {
                JSONObject object = new JSONObject(songList.get(position).get("object"));
                if (object.has("artist_name")) {
                    JSONArray artist_name = object.getJSONArray("artist_name");
                    String s = null;
                    for (int p = 0; p < artist_name.length(); p++) {
                        if (artist_name.length() > 1) {
                            s = s + " , " + artist_name.getString(p);
                        } else {
                            s = artist_name.getString(p);
                        }
                    }
                    viewHolder.descriptionTextView.setText("By : " + s);
                } else {
                    //songsHashMap.put("artist_name", null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            Glide.with(context)
                    .load(songList.get(position).get("image"))
                    .error(R.mipmap.default_small)
                    .placeholder(R.mipmap.default_small)
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



        } else {

        }
    }

    @Override
    public int getItemCount() {
        return songList == null ? 0 : songList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == songList.size() && isLoadingAdded) ? LOADING : VIEW_ITEM;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new HashMap<String, String>());
    }

    public void add(HashMap<String, String> mc) {
        songList.add(mc);
        notifyItemInserted(songList.size() - 1);
    }


    public void addAll(ArrayList<HashMap<String, String>> mcList) {
        songList = mcList;
        notifyDataSetChanged();
    }
}