package com.firminapp.easyRequestMaker.callbacks;


import com.firminapp.easyRequestMaker.EasyRequestMaker;

/**
 * Created by firmin on 03/02/18.
 */

public interface OncacheListener {
    void onCache(boolean serverStatut, EasyRequestMaker requestMaker);
}
