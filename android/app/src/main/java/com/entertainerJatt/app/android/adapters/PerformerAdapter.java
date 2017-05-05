package com.entertainerJatt.app.android.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.entertainerJatt.app.android.R;
import com.entertainerJatt.app.android.database.DatabaseHelper;
import com.entertainerJatt.app.android.entity.UserInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Imbibian on 3/22/2017.
 */


/**
 * Created by Imbibian on 2/13/2017.
 */

public class PerformerAdapter extends RecyclerView.Adapter {
    OnItemClickListener clickListener;
    private ArrayList<HashMap<String, String>> songList;
    DatabaseHelper databaseHelper;
    private List<UserInfo> LocalsongsList = new ArrayList<>();

    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }


    private static final int LOADING = 1;
    private final int VIEW_ITEM = 1;

    private boolean isLoadingAdded = false;

    public PerformerAdapter(Context context, ArrayList<HashMap<String, String>> songList) {
        this.context = context;
        this.songList = songList;
        databaseHelper = new DatabaseHelper(context);
        LocalsongsList = databaseHelper.getAllSongs();
    }

    private static final int TYPE_ITEM = 0;
    private Context context;


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
                    .inflate(R.layout.row_performers_songs, viewGroup, false);

            vh = new PerformerAdapter.ViewItem(view);
        } else {
            View v2 = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.progress, viewGroup, false);
            vh = new PerformerAdapter.LoadingVH(v2);

        }

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolderValue, final int position) {
        if (viewHolderValue instanceof PerformerAdapter.ViewItem) {
            final PerformerAdapter.ViewItem viewHolder = (PerformerAdapter.ViewItem) viewHolderValue;

            String name = songList.get(position).get("body");

            if (songList.get(position).get("body") != null && !songList.get(position).get("body").equalsIgnoreCase("null")) {
                viewHolder.descriptionTextView.setText(name);

            } else {
                viewHolder.descriptionTextView.setVisibility(View.GONE);
            }

            viewHolder.namTextView.setText(songList.get(position).get("title"));
            if (songList.get(position).get("release_date") != null && !songList.get(position).get("release_date").equalsIgnoreCase("null")) {
                viewHolder.dateTextView.setText("");
                viewHolder.dateTextView.setVisibility(View.VISIBLE);
            } else {
                viewHolder.dateTextView.setVisibility(View.GONE);
            }
            viewHolder.addFavImageView.setVisibility(View.GONE);
            Glide.with(context)
                    .load(songList.get(position).get("image"))
                    .placeholder(R.mipmap.default_small)
                    .error(R.mipmap.default_small)
                    .into(viewHolder.songImage);
        }
    }

    @Override
    public int getItemViewType(int position) {

        return (position == songList.size() && isLoadingAdded) ? LOADING : VIEW_ITEM;
    }


    @Override
    public int getItemCount() {
        return songList == null ? 0 : songList.size();
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public void updateAdapter(ArrayList<HashMap<String, String>> songList) {
        this.songList = songList;
        notifyDataSetChanged();

    }
}
