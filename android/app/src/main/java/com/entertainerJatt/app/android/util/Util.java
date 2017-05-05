package com.entertainerJatt.app.android.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import com.entertainerJatt.app.android.R;
import com.entertainerJatt.app.android.interfaces.IAsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

/**
 * Created by Imbibian on 2/9/2017.
 */

public class Util {

    public static Dialog dialog;
    private static String USER_ID = "USER_ID";
    private static String UPDATE_COUNT = "UPDATE_COUNT";
    private static String HEIGHT = "HEIGHT";
    private static String USER_NAME = "USER_NAME";
    private static String SAVE_URL = "SAVE_URL";
    private static String SAVE_URL_ = "SAVE_URL_";
    private static String USER_TOKEN = "USER_TOKEN";
    private static String USER_RTOKEN = "USER_RTOKEN";


    public static String getRId(Context activity) {
        if (activity != null) {
            SharedPreferences mPrefs = activity.getSharedPreferences(USER_ID, Activity.MODE_PRIVATE);
            String right = mPrefs.getString(USER_ID, null);
            if (right != null) {
                return right;
            }
        }

        return null;
    }

    public static void setRId(Context activity, String userId) throws Exception {
        SharedPreferences mPrefs = activity.getSharedPreferences(USER_ID, Activity.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        prefsEditor.putString(USER_ID, userId);
        prefsEditor.commit();
    }

    public static String getSavedUrl(Context activity) {
        if (activity != null) {
            SharedPreferences mPrefs = activity.getSharedPreferences(SAVE_URL, Activity.MODE_PRIVATE);
            String right = mPrefs.getString(SAVE_URL, null);
            if (right != null) {
                return right;
            }
        }

        return null;
    }

    public static void setUpdateCount(Context activity, int count) throws Exception {
        SharedPreferences mPrefs = activity.getSharedPreferences(USER_ID, Activity.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        prefsEditor.putInt(UPDATE_COUNT, count);
        prefsEditor.commit();
    }

    public static int getUpdateCount(Context activity) {
        if (activity != null) {
            SharedPreferences mPrefs = activity.getSharedPreferences(SAVE_URL, Activity.MODE_PRIVATE);
            int count = mPrefs.getInt(UPDATE_COUNT, 0);
            return count;
        }

        return 0;
    }

    public static void setsaveUrl(Context activity, String userId) throws Exception {
        SharedPreferences mPrefs = activity.getSharedPreferences(SAVE_URL, Activity.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        prefsEditor.putString(SAVE_URL, userId);
        prefsEditor.commit();
    }

    public static String getSavedImageUrl(Context activity) {
        if (activity != null) {
            SharedPreferences mPrefs = activity.getSharedPreferences(SAVE_URL_, Activity.MODE_PRIVATE);
            String right = mPrefs.getString(SAVE_URL_, null);
            if (right != null) {
                return right;
            }
        }

        return null;
    }

    public static void setsaveImageUrl(Context activity, String userId) throws Exception {
        SharedPreferences mPrefs = activity.getSharedPreferences(SAVE_URL_, Activity.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        prefsEditor.putString(SAVE_URL_, userId);
        prefsEditor.commit();
    }

    public static String getUserName(Context activity) {
        if (activity != null) {
            SharedPreferences mPrefs = activity.getSharedPreferences(USER_NAME, Activity.MODE_PRIVATE);
            String right = mPrefs.getString(USER_NAME, null);
            if (right != null) {
                return right;
            }
        }

        return null;
    }

    public static void setUserName(Context activity, String userId) throws Exception {
        SharedPreferences mPrefs = activity.getSharedPreferences(USER_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        prefsEditor.putString(USER_NAME, userId);
        prefsEditor.commit();
    }

    public static String getUserToken(Context activity) {
        if (activity != null) {
            SharedPreferences mPrefs = activity.getSharedPreferences(USER_TOKEN, Activity.MODE_PRIVATE);
            String right = mPrefs.getString(USER_TOKEN, null);
            if (right != null) {
                return right;
            }
        }

        return null;
    }

    public static void setUserToken(Context activity, String userId) throws Exception {
        SharedPreferences mPrefs = activity.getSharedPreferences(USER_TOKEN, Activity.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        prefsEditor.putString(USER_TOKEN, userId);
        prefsEditor.commit();
    }


    public static String getUserRToken(Context activity) {
        if (activity != null) {
            SharedPreferences mPrefs = activity.getSharedPreferences(USER_RTOKEN, Activity.MODE_PRIVATE);
            String right = mPrefs.getString(USER_RTOKEN, null);
            if (right != null) {
                return right;
            }
        }

        return null;
    }

    public static void setUserRToken(Context activity, String userId) throws Exception {
        SharedPreferences mPrefs = activity.getSharedPreferences(USER_RTOKEN, Activity.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        prefsEditor.putString(USER_RTOKEN, userId);
        prefsEditor.commit();
    }

    public static void setHeight(Context context, int height) {
        SharedPreferences mPrefs = context.getSharedPreferences(HEIGHT, Activity.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        prefsEditor.putInt(HEIGHT, height);
        prefsEditor.commit();
    }

    public static int getHeight(Context activity) {
        if (activity != null) {
            SharedPreferences mPrefs = activity.getSharedPreferences(HEIGHT, Activity.MODE_PRIVATE);
            int height = mPrefs.getInt(HEIGHT, 0);
            if (height != 0) {
                return height;
            }
        }

        return 0;
    }


    public static boolean CheckHttpConnection200Series(ResultItem resultItem) {
        if (resultItem.getHttpErrorCode() >= HttpURLConnection.HTTP_OK && resultItem.getHttpErrorCode() <= HttpURLConnection.HTTP_PARTIAL)
            return true;
        else
            return false;
    }


    public static boolean haveNetworkConnection(Context context) {
        try {
            boolean haveConnectedWifi = false;
            boolean haveConnectedMobile = false;

            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo[] netInfo = cm.getAllNetworkInfo();
            for (NetworkInfo ni : netInfo) {
                if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                    if (ni.isConnected())
                        haveConnectedWifi = true;
                if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                    if (ni.isConnected())
                        haveConnectedMobile = true;
            }
            return haveConnectedWifi || haveConnectedMobile;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public static void CheckHttpConnectionResponse(IAsyncTask iAsyncTask, ResultItem resultItem, String BaseUrlPathUser) {
        if (Util.CheckHttpConnection200Series(resultItem)) {
            if (resultItem.getJsonObject() != null && !TextUtils.isEmpty(resultItem.getJsonObject().toString())) {
                try {
                    JSONObject jsonObject = new JSONObject(resultItem.getJsonObject().toString());
                    if (jsonObject.has("success")) {
                        if (jsonObject.getString("success").equalsIgnoreCase("true"))
                            iAsyncTask.OnPostExecute(BaseUrlPathUser, resultItem.getJsonObject());
                        else if (jsonObject.getString("success").equalsIgnoreCase("false") && (jsonObject.has("errorMessage") && !TextUtils.isEmpty(jsonObject.getString("errorMessage")))) {
                            iAsyncTask.OnErrorMessage(jsonObject.getString("errorMessage"));
                        } else if (jsonObject.getString("success").equalsIgnoreCase("false") && jsonObject.has("error")) {
                            JSONObject ErrorJSONObject = jsonObject.getJSONObject("error");
                            if (ErrorJSONObject.has("errorMessage") && !TextUtils.isEmpty(ErrorJSONObject.getString("errorMessage"))) {
                                iAsyncTask.OnErrorMessage(ErrorJSONObject.getString("errorMessage"));
                            } else {
                                iAsyncTask.OnErrorMessage(iAsyncTask.getContext().getResources().getString(CustomMessageApplication(BaseUrlPathUser)));
                            }
                        } else {
                            iAsyncTask.OnErrorMessage(iAsyncTask.getContext().getResources().getString(CustomMessageApplication(BaseUrlPathUser)));
                        }
                    } else {
                        iAsyncTask.OnPostExecute(BaseUrlPathUser, resultItem.getJsonObject());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    iAsyncTask.OnErrorMessage(iAsyncTask.getContext().getResources().getString(CustomMessageApplication(BaseUrlPathUser)));
                }
            } else
                iAsyncTask.OnErrorMessage(iAsyncTask.getContext().getResources().getString(R.string.error_message_unknown));
        } else if (resultItem.getHttpErrorCode() == ApplicationConstants.HTTP_INTER_NET_SLOW_VERSION)
            iAsyncTask.OnErrorMessage(iAsyncTask.getContext().getResources().getString(R.string.error_message_internet_slow));
        else if (resultItem.getHttpErrorCode() == HttpURLConnection.HTTP_USE_PROXY)
            iAsyncTask.OnErrorMessage(iAsyncTask.getContext().getResources().getString(R.string.error_message_unknown));
        else if (resultItem.getHttpErrorCode() == HttpURLConnection.HTTP_INTERNAL_ERROR)
            iAsyncTask.getContext().getResources().getString(R.string.error_message_internet_slow);
        else if (resultItem.getHttpErrorCode() == ApplicationConstants.HTTP_NO_INTERNET_VERSION)
            iAsyncTask.OnErrorMessage(iAsyncTask.getContext().getResources().getString(R.string.error_message_network_to_connect));
        else
            iAsyncTask.OnErrorMessage(iAsyncTask.getContext().getResources().getString(R.string.error_message_unknown));
    }

    private static int CustomMessageApplication(String baseUrlPathUser) {

        return 0;
    }

    public static void showDialog(Context context) {
        if (dialog == null) {
            dialog = new Dialog(context);
            dialog.setContentView(R.layout.progressbar);
            dialog.setCancelable(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        }

    }


    public static void dismissDialog() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }


}
