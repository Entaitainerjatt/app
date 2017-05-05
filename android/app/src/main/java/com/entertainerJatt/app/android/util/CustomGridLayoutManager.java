package com.entertainerJatt.app.android.util;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

/**
 * Created by Imbibian on 3/22/2017.
 */

public class CustomGridLayoutManager extends LinearLayoutManager {
    private boolean isScrollEnabled = true;

    public CustomGridLayoutManager(Context context, int bb, boolean aa) {
        super(context, bb, aa);
    }

    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollVertically() {
        //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
        return isScrollEnabled && super.canScrollVertically();
    }
}