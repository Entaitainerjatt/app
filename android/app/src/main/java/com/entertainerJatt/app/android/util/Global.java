package com.entertainerJatt.app.android.util;

import android.app.Application;

/**
 * Created by sony on 3/30/2017.
 */

public class Global extends Application {

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    private String videoId;

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    private String URL;

    public String getNodeType() {
        return nodeType;
    }

    private String nodeType;

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    private String uniqueId;

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }
}
