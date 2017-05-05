package com.entertainerJatt.app.android.request;

import com.entertainerJatt.app.android.entity.UserInfo;
import com.entertainerJatt.app.android.util.ApplicationConstants;
import com.entertainerJatt.app.android.util.PrepareUrl;
import com.entertainerJatt.app.android.util.ResultItem;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


/**
 * Created by Imbibian on 2/16/2017.
 */

public class RegisterRequest {
    PrepareUrl prepareUrl;
    private static String REQUEST = null;

    public ResultItem getJSONFromUrl(UserInfo usersInfo) throws UnsupportedEncodingException {
        String data = URLEncoder.encode("device_type", "UTF-8")
                + "=" + URLEncoder.encode(usersInfo.getDeviceType(), "UTF-8");

        data += "&" + URLEncoder.encode("callback_token", "UTF-8") + "="
                + URLEncoder.encode(usersInfo.getCallbackToken(), "UTF-8");

        data += "&" + URLEncoder.encode("mobile_number", "UTF-8") + "="
                + URLEncoder.encode(usersInfo.getMobileNumber(), "UTF-8");

        REQUEST = data;
        prepareUrl = new PrepareUrl();
        String url = ApplicationConstants.BaseURL + "" + ApplicationConstants.REGISTER;
        return prepareUrl.makeServiceCall(url, REQUEST);
    }
}
