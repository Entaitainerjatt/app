package com.entertainerJatt.app.android.request;

import com.entertainerJatt.app.android.util.ApplicationConstants;
import com.entertainerJatt.app.android.util.PrepareUrl;
import com.entertainerJatt.app.android.util.ResultItem;

import java.io.UnsupportedEncodingException;

/**
 * Created by Imbibian on 3/20/2017.
 */

public class AboutUsRequest {
    PrepareUrl prepareUrl;
    private static String REQUEST = null;

    public ResultItem getJSONFromUrl() throws UnsupportedEncodingException {
        String data = "";

        REQUEST = data;
        prepareUrl = new PrepareUrl();
        String url = ApplicationConstants.BaseURL + "" + ApplicationConstants.ABOUTUS;
        return prepareUrl.makeServiceCall(url, REQUEST);
    }
}
