package com.entertainerJatt.app.android.request;

import com.entertainerJatt.app.android.entity.UserInfo;
import com.entertainerJatt.app.android.util.ApplicationConstants;
import com.entertainerJatt.app.android.util.PrepareUrl;
import com.entertainerJatt.app.android.util.ResultItem;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by sony on 3/30/2017.
 */

public class GetDetailsRequest {
    PrepareUrl prepareUrl;
    private static String REQUEST = null;

    public ResultItem getJSONFromUrl(UserInfo usersInfo) throws UnsupportedEncodingException {
        String data = URLEncoder.encode("username", "UTF-8")
                + "=" + URLEncoder.encode(usersInfo.getUsername(), "UTF-8");

        data += "&" + URLEncoder.encode("token", "UTF-8") + "="
                + URLEncoder.encode(usersInfo.getCallbackToken(), "UTF-8");
        data += "&" + URLEncoder.encode("nid", "UTF-8") + "="
                + URLEncoder.encode(usersInfo.getVideoId(), "UTF-8");


        REQUEST = data;
        prepareUrl = new PrepareUrl();
        String url = ApplicationConstants.BaseURL + "" + ApplicationConstants.GET_DETAIL;
        return prepareUrl.makeServiceCall(url, REQUEST);
    }
}
