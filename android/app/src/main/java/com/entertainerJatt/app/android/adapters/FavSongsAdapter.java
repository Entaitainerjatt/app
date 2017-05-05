package com.entertainerJatt.app.android.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.entertainerJatt.app.android.FavoritesActivity;
import com.entertainerJatt.app.android.R;
import com.entertainerJatt.app.android.database.DatabaseHelper;
import com.entertainerJatt.app.android.entity.UserInfo;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sony on 3/28/2017.
 */

public class FavSongsAdapter extends RecyclerView.Adapter {

    private OnItemClickListener clickListener;
    private List<UserInfo> LocalsongsList = new ArrayList<>();
    private ArrayList<HashMap<String, String>> songList;
    private static final int LOADING = 1;
    private final int VIEW_ITEM = 1;
    private RecyclerView recyclerView;
    private TextView noDataText;
    private boolean isLoadingAdded = false;


    public FavSongsAdapter(Context context, ArrayList<HashMap<String, String>> songList, RecyclerView recyclerView, TextView noDataText) {
        this.context = context;
        this.songList = songList;
        this.noDataText = noDataText;
        this.recyclerView = recyclerView;
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

            vh = new FavSongsAdapter.ViewItem(view);
        } else {
            View v2 = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.progress, viewGroup, false);
            vh = new LoadingVH(v2);

        }


        return vh;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolderValue, final int position) {
        if (viewHolderValue instanceof FavSongsAdapter.ViewItem) {
            final FavSongsAdapter.ViewItem viewHolder = (FavSongsAdapter.ViewItem) viewHolderValue;
            viewHolder.namTextView.setText(songList.get(position).get("title"));
            viewHolder.addFavImageView.setImageResource(R.mipmap.add_to_fav_red);
            final String id = songList.get(position).get("id");
            viewHolder.addFavImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseHelper databaseHelper = new DatabaseHelper(context);
                    LocalsongsList = databaseHelper.getAllFav();
                    for (int i = 0; i < LocalsongsList.size(); i++) {
                        String videoId = LocalsongsList.get(i).getVideoId();
                        if (id.equalsIgnoreCase(videoId)) {
                            UserInfo userInfo = LocalsongsList.get(i);
                            userInfo.setFav("false");
                            databaseHelper.updateFav(userInfo);
                            songList.remove(position);
                            Toast.makeText(context, "Removed from Favourites", Toast.LENGTH_SHORT).show();
                            notifyDataSetChanged();
                        }
                    }
                    if (songList.size() == 0) {
                        recyclerView.setVisibility(View.GONE);
                        noDataText.setVisibility(View.VISIBLE);

                    }

                }
            });
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
            Log.i("Object", songList.get(position).get("object"));
            Glide.with(context)
                    .load(songList.get(position).get("image"))
                    .error(R.mipmap.default_small)
                    .placeholder(R.mipmap.default_small)
                    .into(viewHolder.songImage);

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