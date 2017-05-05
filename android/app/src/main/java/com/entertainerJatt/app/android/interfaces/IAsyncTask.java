package com.entertainerJatt.app.android.interfaces;

import android.content.Context;
import android.support.v4.app.Fragment;

import org.json.JSONObject;

/**
 * Created by desk52 on 27/7/16.
 */
public interface IAsyncTask {
   Context getContext();
   Fragment getFragment();
   void OnPreExecute();
   void OnPostExecute(String URL, JSONObject jsonObject);
   void OnErrorMessage(String Message);

}
