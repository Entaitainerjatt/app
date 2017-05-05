package com.entertainerJatt.app.android.util;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;


/**
 * Created by desk1 on 16/12/15.
 */
public class PrepareUrl {

    public ResultItem makeServiceCall(String url, String params) {
        Log.d(PrepareUrl.class.getName(), url + "\n" + params);
        ResultItem resultItem = new ResultItem();
        URL url1 = null;
        try {
            url1 = new URL(url);
            HttpURLConnection httpURLConnection = null;
            httpURLConnection = (HttpURLConnection) url1.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setConnectTimeout(9000);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(params.getBytes());
            outputStream.flush();
            outputStream.close();
            resultItem.setHttpErrorCode(httpURLConnection.getResponseCode());
            if (Util.CheckHttpConnection200Series(resultItem)) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String inputLine;
                StringBuffer stringBuffer = new StringBuffer();
                while ((inputLine = bufferedReader.readLine()) != null) {
                    stringBuffer.append(inputLine);
                }
                bufferedReader.close();
                if (!TextUtils.isEmpty(stringBuffer.toString())) {
                    try {
                        resultItem.setJsonObject(new JSONObject(stringBuffer.toString()));
                        Log.i("resultItem", "::" + resultItem.getJsonObject().toString());
                    } catch (JSONException e) {
                        resultItem.setMessage(stringBuffer.toString());
                        resultItem.setHttpErrorCode(100);
                        e.printStackTrace();
                    }
                }
                httpURLConnection.disconnect();
                return resultItem;
            } else {
                resultItem.setMessage(httpURLConnection.getResponseMessage());
                httpURLConnection.disconnect();
                resultItem.setJsonObject(new JSONObject());
            }
        } catch (ConnectException e) {
            e.printStackTrace();
            resultItem.setMessage( e.getLocalizedMessage());
            resultItem.setHttpErrorCode(HttpURLConnection.HTTP_USE_PROXY);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            resultItem.setMessage( e.getLocalizedMessage());
            resultItem.setHttpErrorCode(HttpURLConnection.HTTP_UNAUTHORIZED);
        } catch (ProtocolException e) {
            e.printStackTrace();
            resultItem.setMessage( e.getLocalizedMessage());
            resultItem.setHttpErrorCode(HttpURLConnection.HTTP_UNAUTHORIZED);
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            resultItem.setMessage( e.getLocalizedMessage());
            resultItem.setHttpErrorCode(ApplicationConstants.HTTP_INTER_NET_SLOW_VERSION);
        } catch (IOException e) {
            e.printStackTrace();
            resultItem.setMessage( e.getLocalizedMessage());
            resultItem.setHttpErrorCode(HttpURLConnection.HTTP_UNAUTHORIZED);
        }
        return resultItem;
    }
}