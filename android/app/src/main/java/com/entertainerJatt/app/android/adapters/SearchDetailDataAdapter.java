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
import com.entertainerJatt.app.android.R;
import com.entertainerJatt.app.android.util.Global;
import com.entertainerJatt.app.android.util.Util;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchDetailDataAdapter extends RecyclerView.Adapter<SearchDetailDataAdapter.SingleItemRowHolder> {

    private Context mContext;
    ArrayList<HashMap<String, String>> songArrayList = new ArrayList<>();
    private Global global;
    int height;

    public SearchDetailDataAdapter(Context context, ArrayList<HashMap<String, String>> songArrayList) {
        this.songArrayList = songArrayList;
        this.mContext = context;

        height = Util.getHeight(mContext);
        global = (Global) mContext.getApplicationContext();
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_single_card, null);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, final int position) {
        holder.tvTitle.setText(songArrayList.get(position).get("title"));
        holder.itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = songArrayList.get(position).get("id");
                global.setVideoId(id);
                String Url = songArrayList.get(position).get("teaser_link");
                if (Url != null && !Url.equalsIgnoreCase("null")) {
                    Url = Url.split("=")[1];
                    Url = Url.split("&")[0];
                    global.setURL(Url);
                    mContext.startActivity(new Intent(mContext, DetailPageActivity.class));
                } else {
                    global.setURL(null);
                    mContext.startActivity(new Intent(mContext, DetailPageActivity.class));
                }
            }
        });
        Glide.with(mContext)
                .load(songArrayList.get(position).get("image"))
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