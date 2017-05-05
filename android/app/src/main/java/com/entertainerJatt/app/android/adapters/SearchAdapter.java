package com.entertainerJatt.app.android.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.entertainerJatt.app.android.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sony on 4/4/2017.
 */

public class SearchAdapter extends RecyclerView.Adapter {

    OnItemClickListener clickListener;
    private ArrayList<String> titles;
    private ArrayList<HashMap<String, String>> serachArrayList1;
    private ArrayList<HashMap<String, String>> serachArrayList2;
    private ArrayList<HashMap<String, String>> serachArrayList3;
    SearchDetailDataAdapter itemListDataAdapter;

    public SearchAdapter(Context context, ArrayList<String> titles, ArrayList<HashMap<String, String>> serachArrayList1, ArrayList<HashMap<String, String>> serachArrayList2, ArrayList<HashMap<String, String>> serachArrayList3) {
        this.context = context;
        this.titles = titles;
        this.serachArrayList1 = serachArrayList1;
        this.serachArrayList2 = serachArrayList2;
        this.serachArrayList3 = serachArrayList3;

    }

    private static final int TYPE_ITEM = 0;
    private Context context;


    public class ViewItem extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected TextView itemTitle;
        protected TextView btnMore;
        protected TextView textoops;
        protected RecyclerView recycler_view_list;

        public ViewItem(View holderView) {
            super(holderView);

            itemTitle = (TextView) holderView.findViewById(R.id.itemTitle);
            btnMore = (TextView) holderView.findViewById(R.id.btnMore);
            textoops = (TextView) holderView.findViewById(R.id.textNoVideos);
            this.recycler_view_list = (RecyclerView) holderView.findViewById(R.id.recycler_view_list);
            holderView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            //clickListener.onItemClick(view, getPosition());
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.list_item, viewGroup, false);
            return new SearchAdapter.ViewItem(view);
        }
        return null;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolderValue, final int position) {
        if (viewHolderValue instanceof SearchAdapter.ViewItem) {
            final SearchAdapter.ViewItem viewHolder = (SearchAdapter.ViewItem) viewHolderValue;
            viewHolder.itemTitle.setText(titles.get(position));
            viewHolder.btnMore.setVisibility(View.GONE);

            if (position == 0) {
                itemListDataAdapter = new SearchDetailDataAdapter(context, serachArrayList1);

            }
            if (position == 1) {
                itemListDataAdapter = new SearchDetailDataAdapter(context, serachArrayList2);

            }
            if (position == 2) {
                itemListDataAdapter = new SearchDetailDataAdapter(context, serachArrayList3);

            }


            if (itemListDataAdapter.getItemCount() > 0) {
                viewHolder.recycler_view_list.setVisibility(View.VISIBLE);
                viewHolder.textoops.setVisibility(View.GONE);
                viewHolder.recycler_view_list.setHasFixedSize(true);
                viewHolder.recycler_view_list.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                viewHolder.recycler_view_list.setAdapter(itemListDataAdapter);
            } else {
                viewHolder.recycler_view_list.setVisibility(View.GONE);
                viewHolder.textoops.setText("No Result available");
                viewHolder.textoops.setVisibility(View.VISIBLE);
            }
        }


    }

    @Override
    public int getItemViewType(int position) {

        return TYPE_ITEM;
    }


    @Override
    public int getItemCount() {
        return titles.size();
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

}