package com.entertainerJatt.app.android.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.entertainerJatt.app.android.LocalSongsActivity;
import com.entertainerJatt.app.android.PerformerActivity;
import com.entertainerJatt.app.android.R;
import com.entertainerJatt.app.android.ViewAllActivity;
import com.entertainerJatt.app.android.database.DatabaseHelper;
import com.entertainerJatt.app.android.entity.UserInfo;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class HomeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<HashMap<String, String>> ArtistsongArrayList = new ArrayList<>();
    ArrayList<HashMap<String, String>> AlbumssongArrayList = new ArrayList<>();
    ArrayList<HashMap<String, String>> MoviessongArrayList = new ArrayList<>();
    private Context mContext;
    private static final int TYPE_ITEM = 1;
    ArrayList<HashMap<String, String>> songArrayList = new ArrayList<>();
    ArrayList<HashMap<String, String>> TitleArrayList = new ArrayList<>();

    DatabaseHelper databaseHelper;

    public HomeListAdapter(Context context, ArrayList<HashMap<String, String>> TitleArrayList, ArrayList<HashMap<String, String>> ArtistsongArrayList, ArrayList<HashMap<String, String>> AlbumssongArrayList, ArrayList<HashMap<String, String>> MoviessongArrayList) {
        this.ArtistsongArrayList = ArtistsongArrayList;
        this.TitleArrayList = TitleArrayList;
        this.AlbumssongArrayList = AlbumssongArrayList;
        this.MoviessongArrayList = MoviessongArrayList;
        this.mContext = context;
        databaseHelper = new DatabaseHelper(context);
    }

    @Override
    public int getItemViewType(int position) {

        return TYPE_ITEM;
    }

    public Comparator<UserInfo> CartComparator = new Comparator<UserInfo>() {
        public int compare(UserInfo m1, UserInfo m2) {

            return m1.getTime().compareTo(m2.getTime());
        }
    };

    public Comparator<HashMap<String, String>> TitleCartComparator = new Comparator<HashMap<String, String>>() {
        public int compare(HashMap<String, String> m1, HashMap<String, String> m2) {

            return m1.get("weight").compareTo(m2.get("weight"));
        }
    };

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {


        if (i == TYPE_ITEM) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, null);
            ItemRowHolder mh = new ItemRowHolder(v);
            return mh;
        }

        throw new RuntimeException("there is no type that matches the type " + i + " + make sure your using types correctly");

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder itemRowHolder, final int i) {


        if (itemRowHolder instanceof ItemRowHolder) {
            ItemRowHolder rowHolder = (ItemRowHolder) itemRowHolder;
            Collections.sort(TitleArrayList, TitleCartComparator);
            Collections.sort(AlbumssongArrayList, TitleCartComparator);
            SectionListDataAdapter itemListDataAdapter = null;
            if (i == 4) {

                rowHolder.recycler_view_list.setVisibility(View.VISIBLE);
                rowHolder.itemTitle.setText("Recent Played");

                List<UserInfo> songsList = databaseHelper.getAllSongs();
                Collections.sort(songsList, CartComparator);
                HashMap<String, String> songsHashMap;

                if (songsList.size() > 0) {
                    rowHolder.textNoVideos.setVisibility(View.VISIBLE);
                    try {
                        rowHolder.linearLayout.setVisibility(View.VISIBLE);
                        for (int pos = songsList.size() - 1; pos >= 0; pos--) {
                            songsHashMap = new HashMap<>();
                            JSONObject object = new JSONObject(songsList.get(pos).getJsonString());
                            String id = object.getString("id");
                            String type = object.getString("type");
                            String title = object.getString("title");
                            String image = object.getString("image");
                            if (object.has("teaser_link")) {
                                String teaser_link = object.getString("teaser_link");
                                songsHashMap.put("video_link", teaser_link);
                            } else {
                                songsHashMap.put("video_link", null);
                            }
                            songsHashMap.put("id", id);
                            songsHashMap.put("time", songsList.get(pos).getTime());
                            songsHashMap.put("object", songsList.get(pos).getJsonString());
                            songsHashMap.put("datacoming", "Local");
                            songsHashMap.put("type", type);
                            songsHashMap.put("title", title);
                            songsHashMap.put("image", image);
                            if (songsList.size() - pos <= 10) {
                                songArrayList.add(songsHashMap);
                            }
                        }

                        itemListDataAdapter = new SectionListDataAdapter(mContext, songArrayList);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    rowHolder.textNoVideos.setVisibility(View.GONE);
                    rowHolder.textNoVideos.setVisibility(View.VISIBLE);
                    rowHolder.recycler_view_list.setVisibility(View.GONE);
                }
            } else {
                rowHolder.btnMore.setVisibility(View.VISIBLE);
                rowHolder.itemTitle.setText(TitleArrayList.get(i).get("title"));
                ArrayList<HashMap<String, String>> localList = new ArrayList<>();

                for (int k = 0; k < AlbumssongArrayList.size(); k++) {
                    String type = TitleArrayList.get(i).get("type");
                    if (type.contains(AlbumssongArrayList.get(k).get("type"))) {
                        HashMap<String, String> localInnerMap = AlbumssongArrayList.get(k);
                        localList.add(localInnerMap);
                    }
                }
                itemListDataAdapter = new SectionListDataAdapter(mContext, localList);

            }
            rowHolder.recycler_view_list.setHasFixedSize(true);
            rowHolder.recycler_view_list.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
            rowHolder.recycler_view_list.setAdapter(itemListDataAdapter);


            rowHolder.btnMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String type = null;
                    if (i == 4) {
                        List<UserInfo> songsList = databaseHelper.getAllSongs();
                        mContext.startActivity(new Intent(mContext, LocalSongsActivity.class));

                        return;
                    } else if (i == 0) {
                        type = "movie";
                        mContext.startActivity(new Intent(mContext, ViewAllActivity.class).putExtra("type", type));

                    } else if (i == 1) {
                        type = "album";
                        mContext.startActivity(new Intent(mContext, ViewAllActivity.class).putExtra("type", type));

                    } else if (i == 2) {
                        type = "performer";
                        mContext.startActivity(new Intent(mContext, PerformerActivity.class).putExtra("type", type));

                    } else if (i == 3) {
                        type = "lyricist";
                        mContext.startActivity(new Intent(mContext, PerformerActivity.class).putExtra("type", type));

                    }
                }
            });
        }
//        else {
//            VHHeader rowHolder = (VHHeader) itemRowHolder;
//            int height = Util.getHeight(mContext);
//            height = height / 2 - 50;
//            rowHolder.headerLayout.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
//
//        }




       /* Glide.with(mContext)
                .load(feedItem.getImageURL())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .error(R.drawable.bg)
                .into(feedListRowHolder.thumbView);*/
    }

    @Override
    public int getItemCount() {

        return TitleArrayList.size() + 1;
    }


    public class ItemRowHolder extends RecyclerView.ViewHolder {

        protected TextView itemTitle;
        protected TextView textNoVideos;

        protected RecyclerView recycler_view_list;

        protected TextView btnMore;
        protected LinearLayout linearLayout;


        public ItemRowHolder(View view) {
            super(view);
            this.linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);
            this.itemTitle = (TextView) view.findViewById(R.id.itemTitle);
            this.textNoVideos = (TextView) view.findViewById(R.id.textNoVideos);
            this.recycler_view_list = (RecyclerView) view.findViewById(R.id.recycler_view_list);
            this.btnMore = (TextView) view.findViewById(R.id.btnMore);


        }

    }


}