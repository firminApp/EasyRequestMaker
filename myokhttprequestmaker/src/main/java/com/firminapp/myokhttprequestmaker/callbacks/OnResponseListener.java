package com.firminapp.myokhttprequestmaker.callbacks;

import okhttp3.Response;

/**
 * Created by firmin on 02/02/18.
 */

public interface OnResponseListener {
    void onResponse(Response response);
}
