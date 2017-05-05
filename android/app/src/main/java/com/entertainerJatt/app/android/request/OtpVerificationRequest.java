package com.entertainerJatt.app.android.request;

import com.entertainerJatt.app.android.entity.UserInfo;
import com.entertainerJatt.app.android.util.ApplicationConstants;
import com.entertainerJatt.app.android.util.PrepareUrl;
import com.entertainerJatt.app.android.util.ResultItem;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Imbibian on 3/20/2017.
 */

public class OtpVerificationRequest {
    PrepareUrl prepareUrl;
    private static String REQUEST = null;

    public ResultItem getJSONFromUrl(UserInfo usersInfo) throws UnsupportedEncodingException {
        String data = URLEncoder.encode("rid", "UTF-8")
                + "=" + URLEncoder.encode(usersInfo.getRid(), "UTF-8");



        data += "&" + URLEncoder.encode("confirmation_code", "UTF-8") + "="
                + URLEncoder.encode(usersInfo.getConfirmation_code(), "UTF-8");

        REQUEST = data;
        prepareUrl = new PrepareUrl();
        String url = ApplicationConstants.BaseURL + "" + ApplicationConstants.OTP_VERIFICATION;
        return prepareUrl.makeServiceCall(url, REQUEST);
    }
}
