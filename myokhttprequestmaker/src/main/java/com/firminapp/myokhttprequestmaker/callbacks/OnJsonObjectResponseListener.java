package com.firminapp.myokhttprequestmaker.callbacks;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by firmin on 02/02/18.
 */

public interface OnJsonObjectResponseListener {
    void onJsonObjetResponse(JSONObject object) throws JSONException;
    public void onUnsuccefull(String response, String code);
}
