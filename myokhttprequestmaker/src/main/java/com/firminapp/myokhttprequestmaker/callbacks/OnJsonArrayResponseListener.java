package com.firminapp.myokhttprequestmaker.callbacks;

import org.json.JSONArray;

/**
 * Created by firmin on 02/02/18.
 */

public interface OnJsonArrayResponseListener {

    void onJonArrayResponse(JSONArray arry);
    public void onUnsuccefull(String response, String code);

}
