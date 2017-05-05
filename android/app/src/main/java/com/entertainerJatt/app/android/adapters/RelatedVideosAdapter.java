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

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Imbibian on 3/25/2017.
 */

public class RelatedVideosAdapter extends RecyclerView.Adapter {
    private ArrayList<HashMap<String, String>> relatedSongsList;
    OnItemClickListener clickListener;

    public RelatedVideosAdapter(Context context, ArrayList<HashMap<String, String>> relatedSongsList) {
        this.context = context;
        this.relatedSongsList = relatedSongsList;
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
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.row_related_songs, viewGroup, false);
            return new RelatedVideosAdapter.ViewItem(view);
        }
        return null;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolderValue, final int position) {
        if (viewHolderValue instanceof RelatedVideosAdapter.ViewItem) {
            final RelatedVideosAdapter.ViewItem viewHolder = (RelatedVideosAdapter.ViewItem) viewHolderValue;
            if (relatedSongsList.get(position).get("type").equalsIgnoreCase("album")) {
                viewHolder.descriptionTextView.setText("By : " + relatedSongsList.get(position).get("title").split("-")[0]);
            } else {
                //viewHolder.descriptionTextView.setText("By : Unknown");
            }
            viewHolder.namTextView.setText(relatedSongsList.get(position).get("title"));
            Glide.with(context)
                    .load(relatedSongsList.get(position).get("image"))
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
        return relatedSongsList.size();
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }


}
