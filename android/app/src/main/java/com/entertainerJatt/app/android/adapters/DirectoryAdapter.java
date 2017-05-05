package com.entertainerJatt.app.android.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.entertainerJatt.app.android.R;
import com.entertainerJatt.app.android.RecyclerViewFastScroller;
import com.entertainerJatt.app.android.util.CircularImageView;
import com.entertainerJatt.app.android.util.PredicateLayout;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sony on 3/30/2017.
 */

public class DirectoryAdapter extends RecyclerView.Adapter implements RecyclerViewFastScroller.BubbleTextGetter {

    OnItemClickListener clickListener;

   static ArrayList<HashMap<String, String>> DirectoryList;

    public static ArrayList<HashMap<String, String>> getList() {
        return DirectoryList;
    }

    public DirectoryAdapter(Context context, ArrayList<HashMap<String, String>> DirectoryList) {
        this.context = context;
        this.DirectoryList = DirectoryList;
    }

    private static final int TYPE_ITEM = 0;
    private Context context;

    public void updateList(ArrayList<HashMap<String, String>> reataurantListfilter) {
        DirectoryList = reataurantListfilter;
        notifyDataSetChanged();
    }

    @Override
    public String getTextToShowInBubble(int pos) {
        if (pos < 0 || pos >= DirectoryList.size())
            return null;

        String name = DirectoryList.get(pos).get("title");
        if (name == null || name.length() < 1)
            return null;
        String aa = DirectoryList.get(pos).get("title").substring(0, 1);
        return aa;
    }

    public class ViewItem extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvType;
        TextView nameText;
        TextView emailText;
        TextView mobileText;
        CircularImageView userPic;

        public ViewItem(View holderView) {
            super(holderView);
            nameText = (TextView) holderView.findViewById(R.id.textName);
            emailText = (TextView) holderView.findViewById(R.id.emailText);
            mobileText = (TextView) holderView.findViewById(R.id.phoneText);
            userPic = (CircularImageView) holderView.findViewById(R.id.userPic);
            tvType = (TextView) holderView.findViewById(R.id.tvType);
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
                .inflate(R.layout.row_directory, viewGroup, false);


        return new DirectoryAdapter.ViewItem(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolderValue, final int position) {
        if (viewHolderValue instanceof DirectoryAdapter.ViewItem) {
            final DirectoryAdapter.ViewItem viewHolder = (DirectoryAdapter.ViewItem) viewHolderValue;
            viewHolder.nameText.setText(DirectoryList.get(position).get("title"));
            viewHolder.emailText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //senddMail(DirectoryList.get(position).get("email"));
                }
            });
            viewHolder.emailText.setText(DirectoryList.get(position).get("email"));
            viewHolder.mobileText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //callToSinger(DirectoryList.get(position).get("phoneNumber"));
                }
            });
            viewHolder.mobileText.setText(DirectoryList.get(position).get("phoneNumber"));


            String array = DirectoryList.get(position).get("type");
            viewHolder.tvType.setText(array);
//            try {
//                JSONArray jsonArray = new JSONArray(array);
//                PredicateLayout linearlayout = (PredicateLayout) viewHolder.itemView.findViewById(R.id.linearlayout);
//                linearlayout.removeAllViews();
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    View rootView = LayoutInflater.from(context).inflate(R.layout.artisttype, null, false);
//                    TextView texttype = (TextView) rootView.findViewById(R.id.texttype);
//                    texttype.setText(jsonArray.getString(i));
//                    linearlayout.addView(rootView);
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }


            Glide.with(context)
                    .load(DirectoryList.get(position).get("image"))
                    .placeholder(R.mipmap.defaut_directory)
                    .error(R.mipmap.defaut_directory)
                    .into(viewHolder.userPic);
        }
    }


//    private void callToSinger(String phoneNumber) {
//        String number = "tel:" + phoneNumber.toString().trim();
//        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(number));
//        context.startActivity(callIntent);
//    }

    private void senddMail(String email) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/html");
        intent.putExtra(Intent.EXTRA_EMAIL, email);
        intent.putExtra(Intent.EXTRA_SUBJECT, "");
        intent.putExtra(Intent.EXTRA_TEXT, "");

        intent.setType("text/plain");
        context.startActivity(Intent.createChooser(intent, "Send Email"));
    }

    @Override
    public int getItemViewType(int position) {

        return TYPE_ITEM;
    }


    @Override
    public int getItemCount() {
        return DirectoryList.size();
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }
}
