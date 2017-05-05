package com.entertainerJatt.app.android.notification;

/**
 * Created by sony on 4/6/2017.
 */

import android.util.Log;

import com.entertainerJatt.app.android.util.Global;
import com.entertainerJatt.app.android.util.Util;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by francesco on 13/09/16.
 */
public class FireIDService extends FirebaseInstanceIdService {

    Global global;

    @Override
    public void onTokenRefresh() {
        global=(Global)getApplicationContext();
        String tkn = FirebaseInstanceId.getInstance().getToken();
        global.setUniqueId(tkn);
        try {
            Util.setUserRToken(getApplicationContext() , tkn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("Not","Token ["+tkn+"]");

    }
}
