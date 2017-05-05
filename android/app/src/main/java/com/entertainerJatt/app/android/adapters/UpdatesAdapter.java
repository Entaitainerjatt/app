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
import com.entertainerJatt.app.android.entity.UserInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sony on 4/8/2017.
 */

public class UpdatesAdapter extends RecyclerView.Adapter {

    OnItemClickListener clickListener;

    private ArrayList<HashMap<String, String>> songList;
    private List<UserInfo> LocalsongsList;

    public UpdatesAdapter(Context context, ArrayList<HashMap<String, String>> songList) {
        this.context = context;
        this.songList = songList;
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

        public ViewItem(View holderView) {
            super(holderView);
            dateTextView = (TextView) holderView.findViewById(R.id.dateTextView);
            namTextView = (TextView) holderView.findViewById(R.id.namTextView);
            descriptionTextView = (TextView) holderView.findViewById(R.id.descriptionTextView);
            songImage = (ImageView) holderView.findViewById(R.id.songImage);
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
                .inflate(R.layout.row_updates, viewGroup, false);
        return new UpdatesAdapter.ViewItem(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolderValue, final int position) {
        if (viewHolderValue instanceof UpdatesAdapter.ViewItem) {
            final UpdatesAdapter.ViewItem viewHolder = (UpdatesAdapter.ViewItem) viewHolderValue;

            String name = songList.get(position).get("artist_name");
            if (name != null && !name.equalsIgnoreCase("null")) {
                viewHolder.descriptionTextView.setText("By : " + name);
                viewHolder.descriptionTextView.setVisibility(View.VISIBLE);
            } else {
                viewHolder.descriptionTextView.setText("By : " + "Unknown");
                viewHolder.descriptionTextView.setVisibility(View.GONE);
            }
            viewHolder.namTextView.setText(songList.get(position).get("title"));


            String type = songList.get(position).get("type");
            if (type.equalsIgnoreCase("album") || type.equalsIgnoreCase("movie")) {
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
            } else {
                if (songList.get(position).get("body") != null && !songList.get(position).get("body").equalsIgnoreCase("null")) {
                    viewHolder.dateTextView.setText(songList.get(position).get("body"));
                    viewHolder.dateTextView.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.dateTextView.setVisibility(View.GONE);
                }
            }


            Glide.with(context)
                    .load(songList.get(position).get("image"))
                    .placeholder(R.mipmap.default_small)
                    .error(R.mipmap.default_small)
                    .into(viewHolder.songImage);


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

    public void SetOnItemClickListener(final UpdatesAdapter.OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }


}
