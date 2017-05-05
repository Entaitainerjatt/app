package com.entertainerJatt.app.android;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.entertainerJatt.app.android.asyncTask.AboutUsAsynTask;
import com.entertainerJatt.app.android.interfaces.IAsyncTask;
import com.entertainerJatt.app.android.util.Util;

import org.json.JSONObject;

/**
 * Created by sony on 4/5/2017.
 */

public class AboutUsActivity extends Fragment implements IAsyncTask {
    private Context context;
    private TextView useridText;
    View view;

    private static JSONObject object1 = new JSONObject();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_aboutus, container, false);
        context = getActivity();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        assignWidgetIds(view);
        return view;
    }

    private void assignWidgetIds(View view) {
        try {
            PackageInfo packageInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            ((TextView) view.findViewById(R.id.versionTextView)).setText("Version " + packageInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        FloatingActionButton floatingActionSearch = (FloatingActionButton) getActivity().findViewById(R.id.floatingActionSearch);
        floatingActionSearch.setVisibility(View.GONE);

        useridText = (TextView) view.findViewById(R.id.useridText);
        useridText.setText("User name : " + Util.getRId(context));


        if (object1.length() > 0) {
            try {
                JSONObject object = object1.getJSONObject("data");
                String comapny_name = object.getString("company_name");
                final String comapny_number = object.getString("contact_number");
                final String email = object.getString("email");
                String office_address = object.getString("office_address");


                View phoneNumberTextView = view.findViewById(R.id.phoneNumberTextView);
                phoneNumberTextView.setVisibility(View.VISIBLE);
                phoneNumberTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + comapny_number));
                        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        startActivity(callIntent);
                    }
                });
                TextView contactTextView = (TextView) view.findViewById(R.id.contactTextView);
                TextView address = (TextView) view.findViewById(R.id.address);
                address.setText(office_address);
                contactTextView.setText(email);
                contactTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        StringBuilder content = new StringBuilder();
                        content.append("Please enter your text here");
                        content.append('\n');
                        content.append("--------------------");
                        content.append('\n');
                        String Subject = "Enter Subject";
                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", email, null));
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, Subject);
                        emailIntent.putExtra(Intent.EXTRA_TEXT, content.toString());
                        startActivity(Intent.createChooser(emailIntent, "Send email..."));
                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            getAboutUsData();

        }

    }


    @Override
    public Context getContext() {
        return context;
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
        object1 = jsonObject;
        Util.dismissDialog();
        try {
            JSONObject object = jsonObject.getJSONObject("data");
            String comapny_name = object.getString("company_name");
            final String comapny_number = object.getString("contact_number");
            final String email = object.getString("email");
            String office_address = object.getString("office_address");


            View phoneNumberTextView = view.findViewById(R.id.phoneNumberTextView);
            phoneNumberTextView.setVisibility(View.VISIBLE);
            phoneNumberTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + comapny_number));
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    startActivity(callIntent);
                }
            });
            TextView contactTextView = (TextView) view.findViewById(R.id.contactTextView);
            TextView address = (TextView) view.findViewById(R.id.address);
            address.setText(office_address);
            contactTextView.setText(email);
            contactTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StringBuilder content = new StringBuilder();
                    content.append("Please enter your text here");
                    content.append('\n');
                    content.append("--------------------");
                    content.append('\n');
                    String Subject = "Enter Subject";
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", email, null));
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, Subject);
                    emailIntent.putExtra(Intent.EXTRA_TEXT, content.toString());
                    startActivity(Intent.createChooser(emailIntent, "Send email..."));
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnErrorMessage(String Message) {
        Util.dismissDialog();
        Toast.makeText(context, Message, Toast.LENGTH_SHORT).show();
    }


    private void getAboutUsData() {
        AboutUsAsynTask aboutUsAsynTask = new AboutUsAsynTask(AboutUsActivity.this);
        aboutUsAsynTask.execute();
    }
}
