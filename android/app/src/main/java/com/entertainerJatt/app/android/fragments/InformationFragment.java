package com.entertainerJatt.app.android.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.entertainerJatt.app.android.DetailPageActivity;
import com.entertainerJatt.app.android.R;
import com.entertainerJatt.app.android.asyncTask.GetDetailsAsyncTask;
import com.entertainerJatt.app.android.entity.UserInfo;
import com.entertainerJatt.app.android.interfaces.IAsyncTask;
import com.entertainerJatt.app.android.util.Global;
import com.entertainerJatt.app.android.util.PredicateLayout;
import com.entertainerJatt.app.android.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by Imbibian on 3/25/2017.
 */

public class InformationFragment extends Fragment implements IAsyncTask {
    private Context context;
    String videoId;
    Global global;
    private TextView textdob;
    private ImageView imageFacebook;
    private ImageView imageGoogle;
    private ImageView imagelinked;
    private ImageView imageTwitter;
    private TextView nameTextView;
    private TextView singerTextView;
    private TextView uploadedTextView;
    private WebView wv1;
    private LinearLayout linearlayoutbelow;
    private PredicateLayout linearlayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_information, container, false);
        assignId(view);
        listners();

        return view;
    }

    private void listners() {
    }

    private void assignId(View view) {

        context = getActivity();


        global = (Global) getActivity().getApplicationContext();


        videoId = global.getVideoId();


        linearlayoutbelow = (LinearLayout) view.findViewById(R.id.linearlayoutbelow);
        linearlayout = (PredicateLayout) view.findViewById(R.id.linearlayout);


        nameTextView = (TextView) view.findViewById(R.id.namTextView);


        uploadedTextView = (TextView) view.findViewById(R.id.uploadedTextView);
        singerTextView = (TextView) view.findViewById(R.id.singerTextView);
        wv1 = (WebView) view.findViewById(R.id.webView);
        WebSettings settings = wv1.getSettings();
        settings.setDefaultTextEncodingName("utf-8");
        wv1.setWebViewClient(new MyBrowser());


        textdob = (TextView) view.findViewById(R.id.textdob);

        imageFacebook = (ImageView) view.findViewById(R.id.imageFacebook);
        imagelinked = (ImageView) view.findViewById(R.id.imageLinkedin);
        imageGoogle = (ImageView) view.findViewById(R.id.imageGoogle);
        imageTwitter = (ImageView) view.findViewById(R.id.imageTwitter);

        getDetails();
    }

    private void getDetails() {
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(Util.getUserName(context));
        userInfo.setCallbackToken(Util.getUserToken(context));
        userInfo.setVideoId(videoId);
        GetDetailsAsyncTask getDetailsAsyncTask = new GetDetailsAsyncTask(userInfo, InformationFragment.this);
        getDetailsAsyncTask.execute();
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    public void OnPreExecute() {
        Util.showDialog(context);
    }

    @Override
    public void OnPostExecute(String URL, JSONObject jsonObject) {
        Util.dismissDialog();

        linearlayoutbelow.setVisibility(View.VISIBLE);
        linearlayout.setVisibility(View.VISIBLE);

        String image = null;
        try {
            JSONObject object = jsonObject.getJSONObject("data");
            String title = object.getString("title");
            image = object.getString("image");
            String body = object.getString("body");
            if (object.has("date_of_birth")) {
                String dob = object.getString("date_of_birth");
                if (dob != null && !dob.equalsIgnoreCase("null")) {
                    textdob.setText("Date of Birth : " + dob);
                } else {

                    textdob.setText("Date of Birth : NOT AVAILABLE");
                }
            } else {
                textdob.setVisibility(View.GONE);
            }
            final String facebook_link = object.getString("facebook_link");
            final String twitter_link = object.getString("twitter_link");
            final String youtube_channel_link = object.getString("youtube_channel_link");
            final String instagram_link = object.getString("instagram_link");


            if (facebook_link == null || facebook_link.equalsIgnoreCase("null")) {
                imageFacebook.setVisibility(View.GONE);
            }
            if (twitter_link == null || twitter_link.equalsIgnoreCase("null")) {
                imageTwitter.setVisibility(View.GONE);
            }
            if (youtube_channel_link == null || youtube_channel_link.equalsIgnoreCase("null")) {
                imageGoogle.setVisibility(View.GONE);
            }
            if (instagram_link == null || instagram_link.equalsIgnoreCase("null")) {
                imagelinked.setVisibility(View.GONE);
            }
            imageFacebook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {

//                        if (facebook_link != null && !facebook_link.equalsIgnoreCase("null")) {
//                            context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
//                            new Intent(Intent.ACTION_VIEW, Uri.parse(facebook_link));
//                        } else {
//                            Toast.makeText(context, "Link not available", Toast.LENGTH_SHORT).show();
//                        }
                        String url = facebook_link;
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    } catch (Exception e) {
                        e.printStackTrace();
                        String url = facebook_link;
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                }
            });
            imageGoogle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        String url = youtube_channel_link;
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);

                    } catch (Exception e) {
                        Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }

                }
            });

            imagelinked.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        String url = instagram_link;
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    } catch (Exception e) {
                        e.printStackTrace();
                        String url = instagram_link;
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }

                }
            });
            imageTwitter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {

                        if (instagram_link != null && !instagram_link.equalsIgnoreCase("null")) {
                            context.getPackageManager().getPackageInfo("com.twitter.android", 0);
                            new Intent(Intent.ACTION_VIEW, Uri.parse(instagram_link));
                        } else {
                            Toast.makeText(context, "Link not available", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        String url = twitter_link;
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }

                }
            });

            if (object.has("artists_types")) {
                JSONArray jsonArray = object.getJSONArray("artists_types");

                try {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        View rootView = LayoutInflater.from(context).inflate(R.layout.artisttype, null, false);
                        TextView texttype = (TextView) rootView.findViewById(R.id.texttype);
                        texttype.setText(jsonArray.getString(i));

                        linearlayout.addView(rootView);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                linearlayout.setVisibility(View.GONE);
            }

            if (object.has("video_link")) {

                String video_link = object.getString("video_link");
            }
            if (object.has("release_date")) {
                String release_date = object.getString("release_date");
                String date = release_date.split("T")[0];
                uploadedTextView.setText("Uploaded on : " + date);
            }

            wv1.getSettings().setLoadsImagesAutomatically(true);

            wv1.getSettings().setJavaScriptEnabled(true);
            wv1.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            wv1.loadData(body, "text/html; charset=utf-8", "utf-8");

            nameTextView.setText(title);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (global.getURL() != null && !global.getURL().equalsIgnoreCase("null")) {
            ((DetailPageActivity) context).plaVideo();
        } else {
            ((DetailPageActivity) context).setImage(image);
        }
    }

    @Override
    public void OnErrorMessage(String Message) {
        Util.dismissDialog();
        Toast.makeText(context, Message, Toast.LENGTH_SHORT).show();
    }
}
