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

public class LatestSongsRequest {
    PrepareUrl prepareUrl;
    private static String REQUEST = null;

    public ResultItem getJSONFromUrl(UserInfo usersInfo) throws UnsupportedEncodingException {
        String data = URLEncoder.encode("username", "UTF-8")
                + "=" + URLEncoder.encode(usersInfo.getUsername(), "UTF-8");

        data += "&" + URLEncoder.encode("token", "UTF-8") + "="
                + URLEncoder.encode(usersInfo.getCallbackToken(), "UTF-8");
        data += "&" + URLEncoder.encode("node_type", "UTF-8") + "="
                + URLEncoder.encode(usersInfo.getNode_type(), "UTF-8");

        data += "&" + URLEncoder.encode("start", "UTF-8") + "="
                + URLEncoder.encode(usersInfo.getStart(), "UTF-8");

        data += "&" + URLEncoder.encode("limit", "UTF-8") + "="
                + URLEncoder.encode(usersInfo.getLimit(), "UTF-8");

        data += "&" + URLEncoder.encode("upcoming_only", "UTF-8") + "="
                + URLEncoder.encode("0", "UTF-8");

        REQUEST = data;
        prepareUrl = new PrepareUrl();
        String url = ApplicationConstants.BaseURL + "" + ApplicationConstants.GET_MIX_DATA;
        return prepareUrl.makeServiceCall(url, REQUEST);
    }
}