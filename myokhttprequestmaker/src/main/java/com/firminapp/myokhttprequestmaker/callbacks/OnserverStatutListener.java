package com.firminapp.myokhttprequestmaker.callbacks;

import okhttp3.Response;

/**
 * Created by firmin on 04/02/18.
 */

public interface OnserverStatutListener {
    void onStatutResponse(Response response);
}
