package com.entertainerJatt.app.android.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.entertainerJatt.app.android.R;

/**
 * Created by Imbibian on 3/22/2017.
 */

public class TabFragmentsViewAll extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_viewall, container, false);


        return view;
    }

}
