package com.entertainerJatt.app.android.util;

import org.json.JSONObject;

/**
 * Created by Imbibian on 9/23/2016.
 */

public class ResultItem {

    String Message;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    int HttpErrorCode;
    JSONObject jsonObject;

    public int getHttpErrorCode() {
        return HttpErrorCode;
    }

    public void setHttpErrorCode(int httpErrorCode) {
        HttpErrorCode = httpErrorCode;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }
}
