package com.entertainerJatt.app.android.asyncTask;

import android.os.AsyncTask;

import com.entertainerJatt.app.android.entity.UserInfo;
import com.entertainerJatt.app.android.interfaces.IAsyncTask;
import com.entertainerJatt.app.android.request.OtpVerificationRequest;
import com.entertainerJatt.app.android.util.ApplicationConstants;
import com.entertainerJatt.app.android.util.ResultItem;
import com.entertainerJatt.app.android.util.Util;

import java.io.UnsupportedEncodingException;

/**
 * Created by Imbibian on 3/20/2017.
 */


public class OtpVerificationAsyncTask extends AsyncTask<Object, String, ResultItem> {

    UserInfo userInfo;
    IAsyncTask iAsyncTask;

    public OtpVerificationAsyncTask(UserInfo usersInfo, IAsyncTask iAsyncTask) {
        this.userInfo = usersInfo;
        this.iAsyncTask = iAsyncTask;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        iAsyncTask.OnPreExecute();
    }

    @Override
    protected ResultItem doInBackground(Object[] params) {
        if (Util.haveNetworkConnection(iAsyncTask.getContext()))
            try {
                return new OtpVerificationRequest().getJSONFromUrl(userInfo);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        else {
            ResultItem resultItem = new ResultItem();
            resultItem.setHttpErrorCode(ApplicationConstants.HTTP_NO_INTERNET_VERSION);
            resultItem.setMessage("Please Check your internet connection");

            return resultItem;
        }
        return null;
    }

    @Override
    protected void onPostExecute(ResultItem resultItem) {
        super.onPostExecute(resultItem);
        if (isCancelled())
            return;
        if (resultItem.getHttpErrorCode() == 200) {
            iAsyncTask.OnPostExecute(ApplicationConstants.OTP_VERIFICATION, resultItem.getJsonObject());
        } else {
            if (resultItem.getMessage() != null) {

                iAsyncTask.OnErrorMessage(resultItem.getMessage());
            } else {
                iAsyncTask.OnErrorMessage("" + resultItem.getHttpErrorCode());

            }
        }

    }
}

