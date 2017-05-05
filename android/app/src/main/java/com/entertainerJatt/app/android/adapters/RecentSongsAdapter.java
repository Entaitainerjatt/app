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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.entertainerJatt.app.android.R;
import com.entertainerJatt.app.android.database.DatabaseHelper;
import com.entertainerJatt.app.android.entity.UserInfo;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sony on 3/28/2017.
 */

public class RecentSongsAdapter  extends RecyclerView.Adapter<RecentSongsAdapter.SingleItemRowHolder> {

    DatabaseHelper databaseHelper;
    private Context mContext;
    ArrayList<HashMap<String, String>> songArrayList = new ArrayList<>();

    public RecentSongsAdapter(Context context, ArrayList<HashMap<String, String>> songArrayList) {
        this.songArrayList = songArrayList;
        this.mContext = context;
        databaseHelper = new DatabaseHelper(mContext);
    }

    @Override
    public RecentSongsAdapter.SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_single_card, null);
        RecentSongsAdapter.SingleItemRowHolder mh = new RecentSongsAdapter.SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(RecentSongsAdapter.SingleItemRowHolder holder, final int i) {


        holder.tvTitle.setText(songArrayList.get(i).get("title"));


        holder.itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                Toast.makeText(mContext, currentDateTimeString, Toast.LENGTH_SHORT).show();
                UserInfo userInfo = new UserInfo();
                userInfo.setNode_type(songArrayList.get(i).get("type"));
                userInfo.setTime(currentDateTimeString);
                userInfo.setJsonString(songArrayList.get(i).toString());
                userInfo.setVideoId(songArrayList.get(i).get("id"));
                try {
                    boolean isAdded = false;

                    List<UserInfo> songsList = databaseHelper.getAllSongs();
                    for (int pos = 0; pos < songsList.size(); pos++) {
                        String jsonString = songsList.get(pos).getJsonString();
                        String compareString  = songArrayList.get(i).toString();
                        if (compareString.equals(jsonString)) {
                            databaseHelper.updateContact(userInfo);
                            return;
                        } else {
                            isAdded = true;
                        }
                    }
                    if (isAdded) {
                        databaseHelper.addSONGS(userInfo);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });
        Glide.with(mContext)
                .load(songArrayList.get(i).get("image"))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
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


        }

    }

}